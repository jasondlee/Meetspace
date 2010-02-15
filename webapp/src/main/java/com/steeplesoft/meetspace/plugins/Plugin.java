package com.steeplesoft.meetspace.plugins;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 11, 2010
 * Time: 2:42:11 PM
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Plugin {
    String name();
    String type();
    String description() default "";
}
