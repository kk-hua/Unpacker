package com.loop.processor;

import com.google.auto.service.AutoService;
import com.loop.annotation.KField;
import com.loop.annotation.KId;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
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

        //所有的@KField注解
        Set<? extends Element> kFieldElements = roundEnv.getElementsAnnotatedWith(KField.class);

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

        mMessage.printMessage(Diagnostic.Kind.NOTE, "process finish ...");
        return true;
    }
}
