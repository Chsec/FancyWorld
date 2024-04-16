package com.chw.application.widget.popwindow;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.chw.application.databinding.PopWindowFilterBinding;
import com.chw.application.databinding.PopWindowFilterItemBinding;
import com.chw.application.widget.recyclerview.BindDataVBImpl;
import com.chw.application.widget.recyclerview.CommonAdapter;
import com.chw.application.widget.recyclerview.CommonViewHolder;

import java.util.List;

public class FilterPopWindow extends PopupWindow {

    private final WindowManager.LayoutParams lp;

    private final Activity activity;

    /**
     * CommonPopup视图布局文件的ViewBinding对象
     */
    private PopWindowFilterBinding binding;

    /**
     * 显示数据存放池
     */
    private List<String> mDatas;

    /**
     * 选择条件的list
     */
    private RecyclerView rvView;

    private LinearLayoutManager rvLayoutManager;

    private CommonAdapter<String> rvAdapter;

    private RvDataBind bindData;

    public FilterPopWindow(Activity activity) {
        super(activity);
        this.activity = activity;
        // 点击窗体外部取消窗体
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        // 需要动画效果的话可以设置
        // this.setAnimationStyle(R.style.PopupWindowAnimation);
        lp = activity.getWindow().getAttributes();
    }

    /**
     * @param mDatas 弹出页面数据
     * @param alpha  页面弹出时背景透明度
     * @param width  弹出页面宽度
     * @param height 弹出页面高度
     */
    public void create(List<String> mDatas, float alpha, int width, int height) {
        this.setWidth(width);
        this.setHeight(height);
        this.mDatas = mDatas;

        binding = PopWindowFilterBinding.inflate(LayoutInflater.from(activity));
        this.setContentView(binding.getRoot());

        rvLayoutManager = new LinearLayoutManager(activity);
        rvLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvView = binding.popList;
        rvAdapter = new CommonAdapter<>(mDatas, new RvDataBind());
        rvView.setLayoutManager(rvLayoutManager);
        rvView.setAdapter(rvAdapter);

        //设置主页面透明度
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        lp.alpha = 1.0f;
        activity.getWindow().setAttributes(lp);
    }

    public CommonAdapter<String> getAdapter(){
        return rvAdapter;
    }

    public void setOnClickListener(ItemClickAdapter click) {
        rvAdapter.setItemListener(click);
    }

    public void setOnLongClickListener(ItemLongClickAdapter longClick) {
        rvAdapter.setItemListener(longClick);
    }

    public abstract static class ItemClickAdapter implements CommonAdapter.ItemClickListener {

        @Override
        public abstract void OnItemClickListener(View view, int viewId);

        @Override
        public void OnItemLongClickListener(View view, int viewId) {

        }
    }

    public abstract static class ItemLongClickAdapter implements CommonAdapter.ItemClickListener {

        @Override
        public void OnItemClickListener(View view, int viewId) {

        }

        @Override
        public abstract void OnItemLongClickListener(View view, int viewId);
    }

    public class RvDataBind extends BindDataVBImpl<String> {

        @Override
        public void onBindViewHolder(CommonAdapter<String> adapter, CommonViewHolder viewHolder, String model, int type) {
            PopWindowFilterItemBinding bind = (PopWindowFilterItemBinding) viewHolder.binding;
            bind.popItemTitle.setText(model);
        }

        @Override
        public ViewBinding getItemViewBinding(String model, int position) {
            return PopWindowFilterItemBinding.inflate(LayoutInflater.from(rvView.getContext()), rvView, false);
        }

    }

}
