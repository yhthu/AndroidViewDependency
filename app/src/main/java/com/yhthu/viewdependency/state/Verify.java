package com.yhthu.viewdependency.state;

import android.content.Context;
import android.widget.Toast;

/**
 * 待校验状态（有输入（不为空），但未进行校验，或校验不成功）
 * Created by yanghao1 on 2016/12/19.
 */
public class Verify extends Operator {

    private static Verify verify;

    private Verify(Context context) {
        this.context = context;
    }

    public static Verify getInstance(Context context) {
        if (verify == null) {
            verify = new Verify(context);
        }
        return verify;
    }

    @Override
    public boolean operator(String operatorName, String viewName) {
        Toast.makeText(context, String.format("[%s]未校验，不允许执行[%s]", viewName, operatorName),
                Toast.LENGTH_SHORT).show();
        return false;
    }
}
