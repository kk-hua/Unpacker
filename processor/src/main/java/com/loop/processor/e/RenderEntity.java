package com.loop.processor.e;


import java.util.LinkedList;
import java.util.List;

/**
 * @author Kinovi
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class RenderEntity {

    /*实体类全路径名称*/
    public final String fullClazzName;
    public final String simpleClassName;
    public final int cmd;
    public final List<RenderField> renderFields = new LinkedList<>();

    public RenderEntity(String fullClazzName, String simpleClassName, int cmd) {
        this.fullClazzName = fullClazzName;
        this.simpleClassName = simpleClassName;
        this.cmd = cmd;
    }


}
