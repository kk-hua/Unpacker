package com.loop.unpacker;

import com.loop.annotation.KField;
import com.loop.annotation.KId;

/**
 * @author Kinovi
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class Test {

    @KId(cmd = 1, value = "测试指令")
    @KField(index = 0)
    private String head;
    @KField(index = 1, byteLen = 2)
    private int length;
    @KField(index = 3, byteLen = 4)
    private int bi;
    @KField(index = 7,byteLen = 3)
    private long bo;

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

    public int getBi() {
        return bi;
    }

    public void setBi(int bi) {
        this.bi = bi;
    }

    public long getBo() {
        return bo;
    }

    public void setBo(long bo) {
        this.bo = bo;
    }

    @Override
    public String toString() {
        return "Test{" +
                "head='" + head + '\'' +
                ", length=" + length +
                ", bi=" + bi +
                ", bo=" + bo +
                '}';
    }
}
