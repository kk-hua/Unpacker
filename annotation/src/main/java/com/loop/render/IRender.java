package com.loop.render;

/**
 * @author Kinovi
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public interface IRender<T> {

    /**
     * 解析数据
     *
     * @param src
     */
    T render(byte[] src);
}
