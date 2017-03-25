package com.yhthu.viewdependency.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.yhthu.viewdependency.annotation.ViewDependency;
import com.yhthu.viewdependency.annotation.ViewName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 依赖触发控件（Button）
 * Created by yanghao1 on 2016/12/19.
 */
public class WatchButton extends Button {

    private Context context;
    // 该控件对应的Activity类
    private Class aClass;
    // 该控件名称
    private String operatorName;
    // 该控件依赖的控件ID
    private String[] dependency;

    public WatchButton(Context context) {
        super(context);
        init(context);
    }

    public WatchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WatchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        aClass = context.getClass();
        getAnnotation((String) getTag());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // 如果不允许继续分发，则拦截事件
            if (dispatchAnnotation()) {
                return true;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                && event.getAction() == KeyEvent.ACTION_UP) {
            // 如果不允许继续分发，则拦截事件
            if (dispatchAnnotation()) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 根据FieldName获取Field
     *
     * @param fieldName
     */
    private void getAnnotation(String fieldName) {
        try {
            Field field = aClass.getDeclaredField(fieldName);
            Annotation watchButtonAnnotation = field.getAnnotation(ViewDependency.class);
            if (watchButtonAnnotation != null) {
                operatorName = ((ViewDependency) watchButtonAnnotation).name().value();
                dependency = ((ViewDependency) watchButtonAnnotation).dependency();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理注解
     *
     * @return
     */
    private boolean dispatchAnnotation() {
        for (String tagStr : dependency) {
            View contentView = (((Activity) context).findViewById(android.R.id.content));
            WatchEditText watchEditText = (WatchEditText) contentView.findViewWithTag(tagStr);
            try {
                Field watchEditTextField = aClass.getDeclaredField(((String) watchEditText.getTag()));
                Annotation watchButtonAnnotation = watchEditTextField.getAnnotation(ViewName.class);
                if (watchButtonAnnotation != null) {
                    // 可以执行操作：事件继续分发；不可以执行操作：事件拦截（返回true）
                    if (!watchEditText.getOperator().operator(operatorName,
                            ((ViewName) watchButtonAnnotation).value())) {
                        return true;
                    }
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
