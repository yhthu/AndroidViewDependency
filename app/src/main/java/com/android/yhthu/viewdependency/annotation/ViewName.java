package com.android.yhthu.viewdependency.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控件业务名称
 * Created by yanghao1 on 2016/12/19.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ViewName {

    /**
     * 控件业务名称
     *
     * @return
     */
    String value() default "";
}
