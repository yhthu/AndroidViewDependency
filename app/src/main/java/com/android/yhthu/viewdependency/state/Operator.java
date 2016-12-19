package com.android.yhthu.viewdependency.state;

import android.content.Context;

/**
 * 操作抽象接口
 * Created by yanghao1 on 2016/12/15.
 */
public abstract class Operator {

    // 操作对应的上下文
    protected Context context;

    /**
     * 操作
     *
     * @param operatorName 操作名称
     * @param viewName     控件名称
     * @return 是否可以执行操作
     */
    public abstract boolean operator(String operatorName, String viewName);
}
