package com.loop.processor.render;

/**
 * 解析器回调
 * @author Kinovi
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public interface KRenderCallback {

    /**
     *
     * @param cmd
     * @param obj
     */
    void onKRenderFrame(int cmd,Object obj);
}
