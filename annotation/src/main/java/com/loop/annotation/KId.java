package com.loop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 唯一标识，用做标识本段数据的作用。
 * 在同一个类中默认只有第一个标记有效。
 *
 * @author Kinovi
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.SOURCE)
public @interface KId {
    /*指令编号，每条指令唯一编号*/
    int cmd() default 0;

    String value() default "";
}
