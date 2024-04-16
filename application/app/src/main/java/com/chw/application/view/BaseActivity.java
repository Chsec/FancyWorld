package com.chw.application.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.viewbinding.ViewBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chw.application.*;
import com.chw.application.model.bean.Result;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.xuexiang.xui.XUI;

import java.util.List;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity implements View.OnClickListener {

    /**
     * 布局
     */
    public T viewBinding;

    /**
     * 应用数据中心ViewModel对象
     */
    public AppConfigCenter ACC;

    /**
     * 应用操作中心ViewModel对象
     */
    public AppOperationCenter AOC;

    /**
     * 应用数据中心ViewModel对象
     */
    public AppDataCenter ADC;

    /**
     * 线程控制桥,子线程控制主线程执行方法
     */
    protected MutableLiveData<Result> TCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        viewBinding = viewBinding();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(viewBinding.getRoot());
        ACC = App.getInstance().getACC();
        AOC = App.getInstance().getAOC();
        ADC = App.getInstance().getADC();
        configPermission();
        initPrepareFirst();
        initViewSecond();
        initListenerThird();
        initObserveFourth();
    }

    /**
     * 视图绑定工作
     *
     * @return ViewBinding对象
     */
    protected abstract T viewBinding();

    /**
     * 初始化准备工作
     */
    protected abstract void initPrepareFirst();

    /**
     * 初始化视图工作
     */
    protected abstract void initViewSecond();

    /**
     * 初始化监听设置工作
     */
    protected abstract void initListenerThird();

    /**
     * 初始化数据观察者工作
     */
    protected abstract void initObserveFourth();

    /**
     * Activity页面控件监听事件处理
     *
     * @param view Activity页面发生点击事件的控件
     */
    @Override
    public abstract void onClick(View view);

    private void configPermission() {
        XXPermissions.with(this)
                // 申请单个权限
                .permission(Permission.READ_MEDIA_AUDIO, Permission.READ_MEDIA_IMAGES, Permission.READ_MEDIA_VIDEO).request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            System.out.println("获取录音和日历权限成功");
                        } else {
                            System.out.println("获取部分权限成功，但部分权限未正常授予");
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            System.out.println("被永久拒绝授权，请手动授予录音和日历权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(BaseActivity.this, permissions);
                        } else {
                            System.out.println("获取录音和日历权限失败");
                        }
                    }
                });
    }

    /**
     * 设置图片
     *
     * @param view 需要设置图片的控件
     */
    public void useGlide(ImageView view) {
        RequestOptions options = RequestOptions
                .circleCropTransform();
        Glide.with(this)
                .load(R.mipmap.ic_launcher)
                .apply(options)
                .into(view);
    }

    protected abstract class TextWatcherAdapter implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public abstract void afterTextChanged(Editable editable);
    }

}

