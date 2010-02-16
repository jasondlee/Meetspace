package com.steeplesoft.meetspace.util;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * User: jasonlee
 * Date: Feb 15, 2010
 * Time: 10:22:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@InterceptorBinding
@Documented
@Inherited
public @interface Transactional {
}
