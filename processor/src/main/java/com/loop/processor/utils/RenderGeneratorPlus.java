package com.loop.processor.utils;

import com.loop.processor.e.RenderEntity;
import com.loop.processor.e.RenderField;
import com.loop.render.KRenderCallback;
import com.loop.utils.ByteArrayConvertUtil;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.Modifier;

/**
 * @author Kinovi
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class RenderGeneratorPlus {

    public static TypeSpec generator(RenderEntity proxyInfo) {
//        String className = proxyInfo.fullClazzName + "_render";

//        FieldSpec type;
        return TypeSpec
                .classBuilder(proxyInfo.simpleClassName + "_render")
                .addSuperinterface(ClassName.get("com.loop.render", "IRender"))
                .addModifiers(Modifier.PUBLIC)
//                .addField(createRenderEntityField())//实体类信息，用于javapoet生成代码
                .addField(createCmdField(proxyInfo))//实体类编号
//                .addField(type = createFieldType())//实体类编号
                .addField(createKRenderCallbackField())//解析回调属性
//                .addField(createVariableElementsField())//解析属性字段
                .addMethod(createConstructorMethod())//构造方法
                .addMethod(createMethodRender2(proxyInfo))
                .build();
    }

    private static FieldSpec createRenderEntityField() {
        return FieldSpec.builder(RenderEntity.class, "mRenderEntity", Modifier.PRIVATE)
                .build();
    }

    private static FieldSpec createCmdField(RenderEntity proxyInfo) {
        return FieldSpec.builder(int.class, "cmd", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$L", proxyInfo.cmd)
                .build();
    }

    private static FieldSpec createFieldType() {
        return FieldSpec.builder(String.class, "mFiledMethodName", Modifier.PRIVATE)
                .initializer("$S", "Head")
                .build();
    }

    private static FieldSpec createKRenderCallbackField() {
        return FieldSpec.builder(KRenderCallback.class, "mRenderCallback", Modifier.PRIVATE)
                .build();
    }

    private static FieldSpec createVariableElementsField() {
        return FieldSpec.builder(ParameterizedTypeName.get(List.class, RenderField.class), "mRenderFieldList", Modifier.PRIVATE)
                .build();
    }

    private static MethodSpec createConstructorMethod() {
        return MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
//                .addParameter(RenderEntity.class, "renderEntity")
                .addParameter(KRenderCallback.class, "renderCallback")
//                .addStatement("this.mRenderEntity = renderEntity")
//                .addStatement("mRenderFieldList = renderEntity.renderFields")
                .addStatement("mRenderCallback = renderCallback")
                .build();
    }

    private static MethodSpec createMethodRender2(RenderEntity proxyInfo) {
        ClassName className = ClassName.bestGuess(proxyInfo.fullClazzName);
        MethodSpec.Builder builder = MethodSpec.methodBuilder("render")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ArrayTypeName.of(byte.class), "src")
                .addStatement("$T tempEntity = new $T()", className, className);

        int limitLength;
        for (RenderField renderField : proxyInfo.renderFields) {
            limitLength = renderField.index + renderField.byteLen;
            String methodName = MethodUtil.getMethodName(renderField.name);
            builder.beginControlFlow("if(src.length >= $L)", limitLength)
                    .addStatement("byte[] valueArr = $T.copyOfRange(src, $L, $L)", Arrays.class, renderField.index, limitLength)
                    .addStatement("tempEntity.set$N(($N)$T.convert($S, valueArr))", methodName, renderField.attrType, ByteArrayConvertUtil.class, renderField.attrType)
                    .endControlFlow();
        }

        builder.beginControlFlow("if(mRenderCallback!=null)")
                .addStatement("mRenderCallback.onKRenderFrame(cmd,tempEntity)")
                .endControlFlow();

        return builder.build();

    }

    private static MethodSpec createMethodRender(RenderEntity proxyInfo, FieldSpec type) {
        ClassName className = ClassName.bestGuess(proxyInfo.fullClazzName);
        return MethodSpec.methodBuilder("render")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ArrayTypeName.of(byte.class), "src")
                .addStatement("int count = 0")
                .beginControlFlow("if(mRenderFieldList == null || (count=mRenderFieldList.size()) ==0)")
                .addStatement("return")
                .endControlFlow()

//                .addStatement("$L tempEntity = new $L()", proxyInfo.simpleClassName, proxyInfo.simpleClassName)
                .addStatement("$T tempEntity = new $T()", className, className)
                .addStatement("int index")
                .addStatement("int size")
                .addStatement("byte[] valueArr")
                .addStatement("RenderField tempElement")
                .addStatement("Class fieldType")

                .beginControlFlow("try")
                .addStatement("$T<?> aClass = Class.forName(mRenderEntity.fullClazzName)", Class.class)
                .addStatement("Object obj = aClass.newInstance()")

                .beginControlFlow("for(int i=0; i<count;i++)")
                .addStatement("tempElement = mRenderFieldList.get(i)")
                .addStatement("index = tempElement.index")
                .addStatement("size = tempElement.byteLen")
                .beginControlFlow("if (src.length < (index + size))")
                .addStatement("continue")
                .endControlFlow()
                .addStatement(CodeBlock.of("valueArr = $T.copyOfRange(src, index, index + size)", TypeName.get(Arrays.class)))
                .addStatement(CodeBlock.of("String methodName = $T.getMethodName(tempElement.name)", TypeName.get(MethodUtil.class)))
                .addStatement("$T method = aClass.getMethod(\"set\" + methodName, tempElement.attrType)", Method.class)
                .addStatement("method.invoke(obj, $T.convert(tempElement.attrType, valueArr))", ByteArrayConvertUtil.class)
                .endControlFlow()
                .beginControlFlow("if (mRenderCallback != null)")
                .addStatement("mRenderCallback.onKRenderFrame(this.cmd, obj)")
                .endControlFlow()
                .nextControlFlow("catch ($T e)", Exception.class)
                .addStatement("e.printStackTrace()")
                .endControlFlow()

                .build();
    }
}
