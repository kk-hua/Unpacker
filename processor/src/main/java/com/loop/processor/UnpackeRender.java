package com.loop.processor;

import com.loop.annotation.KField;
import com.loop.processor.render.IRender;
import com.loop.processor.render.KRenderCallback;
import com.loop.processor.utils.ByteArrayConvertUtil;
import com.loop.processor.utils.StringUtil;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.VariableElement;

/**
 * @author Kinovi
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class UnpackeRender implements IRender {

    /*指令标识*/
    private int cmd;

    /*解析回调*/
    private KRenderCallback mRenderCallback;

    private List<VariableElement> variableElements;

    @Override
    public void render(byte[] src) {
        int count = 0;
        //没有需要解析数据直接返回
        if (
//                mRenderCallback == null ||
                variableElements == null ||
                        (count = variableElements.size()) == 0
        ) {
            return;
        }

        MyClass myClass = new MyClass();
        KField kField;
        int index;
        int size;
        byte[] temp;
        VariableElement tempElement;

        String fieldName;
        Class fieldType;

        for (int i = 0; i < count; i++) {
            tempElement = variableElements.get(i);
            kField = tempElement.getAnnotation(KField.class);
            if (kField == null) {
                continue;
            }
            index = kField.index();
            size = kField.index();

            //没有足够的数据源，或者是一个错误的拆包
            if (src.length < (index + size)) {
                continue;
            }
            temp = Arrays.copyOfRange(src, index, index + size);
            fieldName = tempElement.getSimpleName().toString();
            StringUtil.getMethodName(fieldName);
            fieldType = tempElement.asType().getClass();

        }

//        myClass.setHead();
    }

//    private boolean

}
