package com.loop.processor;

import com.google.auto.service.AutoService;
import com.loop.annotation.KField;
import com.loop.annotation.KId;
import com.loop.processor.e.RenderEntity;
import com.loop.processor.e.RenderField;
import com.loop.processor.utils.ClassCreatorProxy;
import com.loop.processor.utils.RenderGeneratorPlus;
import com.loop.processor.utils.RenderPoolGenerator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * 指令解析生成器
 *
 * @author Kinovi
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */

@AutoService(Processor.class)
public class RenderProcessor extends AbstractProcessor {
    private Messager mMessage;
    private Elements mElementUtils;
    private final Map<String, ClassCreatorProxy> mProxyMap = new HashMap<>();
    private final Map<String, RenderEntity> mProxyLinkedMap = new LinkedHashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessage = processingEnv.getMessager();
        mElementUtils = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(KField.class.getCanonicalName());
        annotations.add(KId.class.getCanonicalName());
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //所有的@KId注解
        Set<? extends Element> kIdElements = roundEnv.getElementsAnnotatedWith(KId.class);


        for (Element kIdElement : kIdElements) {
            VariableElement variableElement = (VariableElement) kIdElement;
            TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();
            //获取类名
            String fullClassName = classElement.getQualifiedName().toString();
            String simpleClassName = classElement.getSimpleName().toString();
            RenderEntity renderEntity = mProxyLinkedMap.get(fullClassName);

            //每个类中只有第一个@KId注解标记有效
            if (null == renderEntity) {
                KId kIdAnnotation = variableElement.getAnnotation(KId.class);
                renderEntity = new RenderEntity(fullClassName, simpleClassName, kIdAnnotation.cmd());
                mProxyLinkedMap.put(fullClassName, renderEntity);
            }
        }

        //所有的@KField注解
        Set<? extends Element> kFieldElements = roundEnv.getElementsAnnotatedWith(KField.class);

        RenderField tempRenderField;
        for (Element element : kFieldElements) {
            VariableElement variableElement = (VariableElement) element;
            TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();

            //获取类名
            String fullClassName = classElement.getQualifiedName().toString();

            //根据类名存放
            RenderEntity proxy = mProxyLinkedMap.get(fullClassName);

            //每个解析实体类都必须有一个 @KId
            if (proxy == null) {
                mMessage.printMessage(Diagnostic.Kind.ERROR, " --> create " + fullClassName + " class in not found annotation @KId");
                continue;
            }

            //获取@KField注解值
//            mMessage.printMessage(Diagnostic.Kind.ERROR, " --> create " + variableElement.asType().toString() + " @KId");
            KField bindAnnotation = variableElement.getAnnotation(KField.class);
            tempRenderField = new RenderField(
                    variableElement.getSimpleName().toString(),//属性名称
                    variableElement.asType().toString(),//属性类型
                    bindAnnotation.index(),//开始下标
                    bindAnnotation.byteLen()//属性字节长度
            );
            proxy.renderFields.add(tempRenderField);
        }
        String poolPackerName = "";
        Map<Integer, String> renderFullNameList = new LinkedHashMap();
        //通过遍历mProxyMap，创建java文件
        for (String key : mProxyLinkedMap.keySet()) {
            mMessage.printMessage(Diagnostic.Kind.NOTE, " --> create1 " + key + "error");
            RenderEntity proxyInfo = mProxyLinkedMap.get(key);
            poolPackerName = ClassName.bestGuess(proxyInfo.fullClazzName).packageName();
            renderFullNameList.put(proxyInfo.cmd, poolPackerName + ".render." + proxyInfo.simpleClassName + "_Render");
            JavaFile javaFile = JavaFile.builder(poolPackerName + ".render", RenderGeneratorPlus.generator(proxyInfo)).build();
            try {
                //生成Java文件
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                mMessage.printMessage(Diagnostic.Kind.NOTE, " --> create " + proxyInfo.fullClazzName + "error");
            }
        }

        //生成解析器缓存池代码
        if (!poolPackerName.isEmpty()) {
            JavaFile javaFile = JavaFile.builder(poolPackerName, RenderPoolGenerator.generator(renderFullNameList)).build();
            try {
                //生成Java文件
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                mMessage.printMessage(Diagnostic.Kind.NOTE, " --> create2 " + poolPackerName + "error");
            }
        }


        mMessage.printMessage(Diagnostic.Kind.NOTE, "process finish ...");


        /////////////////////////////////////////////////////

        //所有的@KField注解
       /* Set<? extends Element> kFieldElements = roundEnv.getElementsAnnotatedWith(KField.class);

        for (Element element : kFieldElements) {
            VariableElement variableElement = (VariableElement) element;
            TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();

            //获取类名
            String fullClassName = classElement.getQualifiedName().toString();

            //根据类名存放
            ClassCreatorProxy proxy = mProxyMap.get(fullClassName);

            if (proxy == null) {
                proxy = new ClassCreatorProxy(mElementUtils, classElement);
                mProxyMap.put(fullClassName, proxy);
            }
            KField bindAnnotation = variableElement.getAnnotation(KField.class);

            //获取@KField注解值
            int id = bindAnnotation.index();
            int len = bindAnnotation.byteLen();
            proxy.putElement(id, variableElement);
        }
        //通过遍历mProxyMap，创建java文件
        for (String key : mProxyMap.keySet()) {
            ClassCreatorProxy proxyInfo = mProxyMap.get(key);
            JavaFile javaFile = JavaFile.builder(proxyInfo.getPackageName(), proxyInfo.generateJavaCode2()).build();
            try {
                //生成Java文件
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                mMessage.printMessage(Diagnostic.Kind.NOTE, " --> create " + proxyInfo.getProxyClassFullName() + "error");
            }
        }

        mMessage.printMessage(Diagnostic.Kind.NOTE, "process finish ...");*/
        return true;
    }
}
