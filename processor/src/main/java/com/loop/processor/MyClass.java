package com.loop.processor;

import com.loop.annotation.KField;
import com.loop.annotation.KId;

public class MyClass {
    @KId("测试指令")
    @KField(index = 0)
    private String head;
    @KField(index = 1, byteLen = 2)
    private int length;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}