# ViewDependency
Android控件状态依赖

**使用场景：**
---------
该Demo主要针对生产型Android客户端软件，界面存在多个输入和多个操作，且操作依赖于输入状态。
![Demo图][1]
设定图中

 - 确认操作依赖于商品编码和储位的状态
 - 跳过操作不依赖于输入状态
 - 登记差异操作依赖于储位和数量的状态

输入框有三种状态：1）待输入；2）待校验；3）校验成功。操作需要当其依赖的输入数据校验成功，才能执行。
如果在Activity中去判断输入框状态，那么实际需要调用（3个输入框） * （3种状态） * （3个按钮） = 27个 **if** 判断，对于状态的维护将使得整个程序可维护性极差，并随着输入和操作的增加，维护的状态呈指数增长。

**使用方法：**
---------

由于目前未上传jcenter，仅供参考代码。

 1. 布局文件引用WatchEditText和WatchButton
```
<com.android.yhthu.viewdependency.view.WatchEditText
    android:id="@+id/edit_query_1"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:tag="editQuery1"
    android:imeOptions="actionNext"
    android:hint="商品编码"
    android:inputType="number"/>
```
```
<com.android.yhthu.viewdependency.view.WatchButton
    android:id="@+id/search_button_1"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:tag="buttonSearch1"
    android:text="确认" />
```
由于Library Module中的控件id不是常量（可参考ButterKnife对Library Module的支持采用R2的原因），这里采用了tag的方式。
 2. 在Activity中通过注解申明依赖

```
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
```
ViewName定义控件名称，ViewDependency中dependency指定其依赖的控件tag。
 3. 直接执行onClick和onEditorAction（修改状态）
```
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
```
可以看出，这里并没有通过if判断各个输入控件的状态。
```
@Override
public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    if (actionId == EditorInfo.IME_ACTION_NEXT && v == editQuery1
            && (query1Str = editQuery1.getText().toString()).isEmpty()) {
        if (query1Str.equals("12345")) {
            editQuery1.complete();
            return true;
        }
    } 
    // 省略代码
    return false;
}
```
onEditorAction模拟调用软件的Enter进行校验，这里需要注意通过editQuery1.complete()修改该EidtText的状态。

**实现原理**
--------
整个框架分为三个package：annotation、state和view。

 - 在annotation中定义ViewName和ViewDependency注解，分别用于WatchEditText和WatchButton。ViewName指定WatchEditText控件在业务中的名称，ViewDependency指定WatchButton依赖的WatchEditText控件；

```
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
```

 - 在state中通过**状态模式**定义Enter、Verify、Complete，其基类为抽象类Operator，定义方法operator；
```
/**
 - 操作抽象接口
 - Created by yanghao1 on 2016/12/15.
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
```
```
/**
 - 待输入状态（初始状态）
 - Created by yanghao1 on 2016/12/19.
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
```

 - WatchEditText和WatchButton定义控件的依赖关系。WatchEditText实现ViewState接口，其包含三种状态的转换方法。
```
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
```
 
  [1]: http://oifei75bf.bkt.clouddn.com/device-2016-12-19-172130.png