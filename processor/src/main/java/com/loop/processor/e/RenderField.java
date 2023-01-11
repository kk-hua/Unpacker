package com.loop.processor.e;

public class RenderField {
        /*属性名称*/
        public final String name;
        /*属性类型*/
        public final String attrType;

        /*属性在数据中开始有效数据下标*/
        public final int index;

        /*属性在数据中字节长度*/
        public final int byteLen;

        public RenderField(String name, String attrType, int index, int byteLen) {
            this.name = name;
            this.attrType = attrType;
            this.index = index;
            this.byteLen = byteLen;
        }
    }