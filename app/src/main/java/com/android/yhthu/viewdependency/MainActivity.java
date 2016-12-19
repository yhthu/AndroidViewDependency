package com.android.yhthu.viewdependency;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yhthu.viewdependency.annotation.ViewDependency;
import com.android.yhthu.viewdependency.annotation.ViewName;
import com.android.yhthu.viewdependency.view.WatchButton;
import com.android.yhthu.viewdependency.view.WatchEditText;
import com.jd.mrd.viewdependency.R;

/**
 * 控件状态依赖Demo
 * @author yanghao1
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    @ViewName("商品编码")
    private WatchEditText editQuery1;
    @ViewName("储位")
    private WatchEditText editQuery2;
    @ViewName("数量")
    private WatchEditText editQuery3;

    @ViewDependency(name = @ViewName("确认"), dependency = {"editQuery1", "editQuery2"})
    private WatchButton buttonSearch1;
    @ViewDependency(name = @ViewName("跳过")/*不依赖输入*/)
    private WatchButton buttonSearch2;
    @ViewDependency(name = @ViewName("登记缺货"), dependency = {"editQuery2", "editQuery3"})
    private WatchButton buttonSearch3;

    // 输入框数值
    private String query1Str;
    private String query2Str;
    private String query3Str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        editQuery1 = (WatchEditText) findViewById(R.id.edit_query_1);
        editQuery2 = (WatchEditText) findViewById(R.id.edit_query_2);
        editQuery3 = (WatchEditText) findViewById(R.id.edit_query_3);
        buttonSearch1 = (WatchButton) findViewById(R.id.search_button_1);
        buttonSearch2 = (WatchButton) findViewById(R.id.search_button_2);
        buttonSearch3 = (WatchButton) findViewById(R.id.search_button_3);
    }

    private void initData() {
        editQuery1.setOnEditorActionListener(this);
        editQuery2.setOnEditorActionListener(this);
        editQuery3.setOnEditorActionListener(this);

        buttonSearch1.setOnClickListener(this);
        buttonSearch2.setOnClickListener(this);
        buttonSearch3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSearch1) {
            Toast.makeText(this, "调接口", Toast.LENGTH_SHORT).show();
        } else if (v == buttonSearch2) {
            Toast.makeText(this, "跳下一页", Toast.LENGTH_SHORT).show();
        } else if (v == buttonSearch3) {
            Toast.makeText(this, "登记缺货", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT && v == editQuery1
                && (query1Str = editQuery1.getText().toString()).isEmpty()) {
            if (query1Str.equals("12345")) {
                editQuery1.complete();
                return true;
            }
        } else if (actionId == EditorInfo.IME_ACTION_NEXT && v == editQuery2
                && (query2Str = editQuery1.getText().toString()).isEmpty()) {
            if (query2Str.equals("67890")) {
                editQuery2.complete();
                return true;
            }
        } else if (actionId == EditorInfo.IME_ACTION_NEXT && v == editQuery3
                && (query3Str = editQuery1.getText().toString()).isEmpty()) {
            if (Integer.parseInt(query3Str) < 10) {
                editQuery3.complete();
                return true;
            }
        }
        return false;
    }
}
