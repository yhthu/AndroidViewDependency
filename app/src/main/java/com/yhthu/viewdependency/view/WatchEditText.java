package com.yhthu.viewdependency.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.yhthu.viewdependency.state.Complete;
import com.yhthu.viewdependency.state.Enter;
import com.yhthu.viewdependency.state.Operator;
import com.yhthu.viewdependency.state.Verify;

/**
 * 被依赖EditText（输入）
 * Created by yanghao1 on 2016/12/19.
 */
public class WatchEditText extends EditText implements ViewState {

    protected Context context;
    protected Operator operator;

    public WatchEditText(Context context) {
        super(context);
        init(context);
    }

    public WatchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WatchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        // 初始化为待输入状态
        enter();
        // 添加输入内容改变监听
        addTextListener();
    }

    @Override
    public void enter() {
        setOperator(Enter.getInstance(context));
    }

    @Override
    public void verify() {
        setOperator(Verify.getInstance(context));
    }

    @Override
    public void complete() {
        setOperator(Complete.getInstance(context));
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Operator getOperator() {
        return operator;
    }

    /**
     * 监听输入变化
     */
    private void addTextListener() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    enter();
                } else {
                    verify();
                }
            }
        });
    }
}
