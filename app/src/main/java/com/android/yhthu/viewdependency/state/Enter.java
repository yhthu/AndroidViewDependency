package com.android.yhthu.viewdependency.state;

import android.content.Context;
import android.widget.Toast;

/**
 * 待输入状态（初始状态）
 * Created by yanghao1 on 2016/12/19.
 */
public class Enter extends Operator {

    private static Enter enter;

    private Enter(Context context) {
        this.context = context;
    }

    public static Enter getInstance(Context context) {
        if (enter == null) {
            enter = new Enter(context);
        }
        return enter;
    }

    @Override
    public boolean operator(String operatorName, String viewName) {
        Toast.makeText(context, String.format("[%s]为空，不允许执行[%s]", viewName, operatorName),
                Toast.LENGTH_SHORT).show();
        return false;
    }
}
