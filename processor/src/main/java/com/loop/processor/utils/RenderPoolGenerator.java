package com.loop.processor.utils;

import com.loop.processor.e.RenderEntity;
import com.loop.render.IRender;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

/**
 * @author Kinovi
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class RenderPoolGenerator {

    public static TypeSpec generator(Map<Integer, String> renderFullNameList) {
//        String className = proxyInfo.fullClazzName + "_render";

//        FieldSpec type;
        return TypeSpec
                .classBuilder("RenderPool")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                .addField(createRenderEntityField())//实体类信息，用于javapoet生成代码
                .addField(FieldSpec.builder(
                                ParameterizedTypeName.get(Map.class, Integer.class, IRender.class),
                                "mRenderPoolMap", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                        .initializer("new $T<>()", LinkedHashMap.class)
                        .build())//
                .addStaticBlock(createStaticBlock(renderFullNameList))
                .addMethod(createGetRender())
//                .addMethod(createConstructorMethod(renderFullNameList))//构造方法
//                .addMethod(createInitRender())
                .build();
    }

    private static CodeBlock createStaticBlock(Map<Integer, String> renderFullNameList) {
        CodeBlock.Builder builder = CodeBlock.builder();
        for (int cmd : renderFullNameList.keySet()) {
            builder.addStatement("mRenderPoolMap.put($L,new $T())", cmd, ClassName.bestGuess(renderFullNameList.get(cmd)));
        }
        return builder.build();
    }

    private static MethodSpec createConstructorMethod(Map<Integer, String> renderFullNameList) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder();
        for (int cmd : renderFullNameList.keySet()) {
            builder.addStatement("mRenderPoolMap.put($L,new $T())", cmd, ClassName.bestGuess(renderFullNameList.get(cmd)));
        }
        return builder.build();
    }

    private static MethodSpec createInitRender() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("onInit");
        return builder.build();
    }

    private static MethodSpec createGetRender() {
        MethodSpec.Builder builder = MethodSpec
                .methodBuilder("get")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(IRender.class)
                .addParameter(int.class, "cmd")
                .addStatement("return mRenderPoolMap.get(cmd)");

        return builder.build();
    }
}
