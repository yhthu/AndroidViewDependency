package com.yhthu.viewdependency.state;

import android.content.Context;

/**
 * 有输入，并且校验成功
 * Created by yanghao1 on 2016/12/15.
 */
public class Complete extends Operator {

    private static Complete complete;

    private Complete(Context context) {
        this.context = context;
    }

    public static Complete getInstance(Context context) {
        if (complete == null) {
            complete = new Complete(context);
        }
        return complete;
    }

    @Override
    public boolean operator(String operatorName, String viewName) {
        return true;
    }
}
