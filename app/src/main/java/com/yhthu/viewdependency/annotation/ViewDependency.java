package com.yhthu.viewdependency.annotation;

import com.yhthu.viewdependency.annotation.ViewName;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控件状态依赖
 * Created by yanghao1 on 2016/12/19.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ViewDependency {

    /**
     * 控件名称（嵌套注解）
     *
     * @return
     */
    ViewName name() default @ViewName;

    /**
     * 控件状态依赖
     *
     * @return
     */
    String[] dependency() default {};
}
