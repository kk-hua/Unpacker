package com.loop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Kinovi
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.SOURCE)
public @interface KField {

    /*字段开始位置下表*/
    int index();

    /*字段占用字节长度*/
    int byteLen() default 1;
}
