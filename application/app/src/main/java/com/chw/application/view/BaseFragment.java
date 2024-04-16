package com.chw.application.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;
import com.chw.application.App;
import com.chw.application.AppConfigCenter;
import com.chw.application.AppDataCenter;
import com.chw.application.AppOperationCenter;
import org.jetbrains.annotations.NotNull;

public abstract class BaseFragment<T extends ViewBinding> extends Fragment implements View.OnClickListener {

    public T viewBinding;

    /**
     * 应用数据中心ViewModel对象应用数据中心ViewModel对象
     */
    public AppConfigCenter ACC;

    public AppOperationCenter AOC;

    public AppDataCenter ADC;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewBinding = viewBinding();
        ACC = App.getInstance().getACC();
        AOC = App.getInstance().getAOC();
        ADC = App.getInstance().getADC();
        initPrepareFirst();
        initViewSecond();
        initListenerThird();
        initObserveFourth();
        return viewBinding.getRoot();
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

    @Override
    public void onPause() {
        super.onPause();
    }
}