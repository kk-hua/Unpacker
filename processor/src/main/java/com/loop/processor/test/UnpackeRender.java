package com.loop.processor.test;

import com.loop.processor.e.RenderEntity;
import com.loop.processor.e.RenderField;
import com.loop.render.IRender;
import com.loop.render.KRenderCallback;

import java.util.List;

/**
 * @author Kinovi
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class UnpackeRender implements IRender {

    /**/
    private RenderEntity mRenderEntity;
    /*指令标识*/
    private int cmd;

    /*解析回调*/
    private KRenderCallback mRenderCallback;

    private List<RenderField> renderFieldList;

    public UnpackeRender(RenderEntity renderEntity) {
        this.mRenderEntity = renderEntity;
        this.cmd = renderEntity.cmd;
        renderFieldList = renderEntity.renderFields;
    }

    @Override
    public void render(byte[] src) {
        int count = 0;
        //没有需要解析数据直接返回
        if (
//                mRenderCallback == null ||
                renderFieldList == null ||
                        (count = renderFieldList.size()) == 0
        ) {
            return;
        }

//        MyClass myClass = new MyClass();
        int index;
        int size;
        byte[] value;
        RenderField tempElement;

        Class fieldType;
//        myClass.setHead(Arrays.copyOfRange(src,index, len));
//        myClass.setLength(Arrays.copyOfRange(src,index, len));

        /*try {
            Class<?> aClass = Class.forName(mRenderEntity.fullClazzName);
            Object obj = aClass.newInstance();

            for (int i = 0; i < count; i++) {
                tempElement = renderFieldList.get(i);
                index = tempElement.index;
                size = tempElement.byteLen;
                //没有足够的数据源，或者是一个错误的拆包
                if (src.length < (index + size)) {
                    continue;
                }
                value = Arrays.copyOfRange(src, index, index + size);
                String methodName = StringUtil.getMethodName(tempElement.name);
                Method method = aClass.getMethod("set" + methodName, tempElement.attrType);
                method.invoke(obj, ByteArrayConvertUtil.convert(tempElement.attrType, value));
            }
            if (mRenderCallback != null) {
                mRenderCallback.onKRenderFrame(this.cmd, obj);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }*/
//        myClass.setHead();
    }

//    private boolean

}
