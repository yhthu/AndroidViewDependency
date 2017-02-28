package com.yhthu.viewdependency.view;

/**
 * 控件状态
 * Created by yanghao1 on 2016/12/15.
 */
public interface ViewState {

    /**
     * 待输入状态（初始状态）
     */
    void enter();

    /**
     * 待校验状态（有输入（不为空），但未进行校验，或校验不成功）
     */
    void verify();

    /**
     * 有输入，并且校验成功
     */
    void complete();
}
