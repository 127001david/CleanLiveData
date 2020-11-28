package com.rightpoint.lib_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/11/10 1:27 PM 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface LiveDataObserve {

}
