package com.android.yhthu.viewdependency.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.android.yhthu.viewdependency.annotation.ViewDependency;
import com.android.yhthu.viewdependency.annotation.ViewName;

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

    /**
     * 根据依赖控件状态判定是否拦截OnClick事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            for (String tagStr : dependency) {
                View contentView = (((Activity) context).findViewById(android.R.id.content));
                WatchEditText watchEditText = (WatchEditText) contentView.findViewWithTag(tagStr);
                try {
                    Field watchEditTextField = aClass.getDeclaredField(((String) watchEditText.getTag()));
                    Annotation watchButtonAnnotation = watchEditTextField.getAnnotation(ViewName.class);
                    if (watchButtonAnnotation != null) {
                        if (!watchEditText.getOperator().operator(operatorName,
                                ((ViewName) watchButtonAnnotation).value())) {
                            return true;
                        }
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onTouchEvent(event);
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
}
