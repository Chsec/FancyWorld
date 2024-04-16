package com.chw.application.widget.recyclerview;

import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public class CommonViewHolder extends RecyclerView.ViewHolder {

    /**
     * ViewHolder内组件的可见性开关，默认不可见
     */
    public static int visible = android.view.View.INVISIBLE;

    /**
     * ViewHolder内组件的可用性开关，默认不可用
     */
    public static boolean available = false;

    /**
     * RecyclerView的上下文
     */
    private final Context context;

    public ViewBinding binding;

    /**
     * ViewHolder视图
     */
    public View mViews;

    public CommonViewHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        mViews = itemView;
    }

    public CommonViewHolder(Context context, ViewBinding itemView) {
        super(itemView.getRoot());
        this.context = context;
        binding = itemView;
    }

    /**
     * 获取普通视图控制器
     *
     * @param context  上下文
     * @param itemView 子视图
     * @return CommonViewHolder
     */
    public static CommonViewHolder getCommonViewHolder(Context context, View itemView) {
        return new CommonViewHolder(context, itemView);
    }

    /**
     * 获取普通视图控制器
     *
     * @param context  上下文
     * @param itemView ViewBinding子视图
     * @return CommonViewHolder
     */
    public static CommonViewHolder getCommonViewHolder(Context context, ViewBinding itemView) {
        return new CommonViewHolder(context, itemView);
    }

    public static void setVisible(int visible) {
        CommonViewHolder.visible = visible;
    }

    public static void setAvailable(boolean available) {
        CommonViewHolder.available = available;
    }

}
