package com.chw.application.widget.recyclerview;

import androidx.viewbinding.ViewBinding;

import java.util.List;

public interface BindDataVB<T> {

    /**
     * ViewBinding视图与数据绑定实现
     *
     * @param adapter    适配器引用
     * @param model      ViewHolder数据项
     * @param viewHolder ViewHolder布局
     * @param type       ViewHolder布局类型
     */
    void onBindViewHolder(CommonAdapter<T> adapter, CommonViewHolder viewHolder, T model, int type);

    /**
     * ViewHolder视图局部刷新方法
     *
     * @param holder   ViewHolder视图
     * @param position ViewHolder位置
     * @param payloads 调用数据刷新时传递的payload
     */
    void onBindViewHolder(CommonViewHolder holder, int position, List<Object> payloads);

    /**
     * 获取ViewHolder的ViewBinding对象
     *
     * @param model    数据
     * @param position 位置
     * @return ViewBinding类型对象，需将布局与视图进行绑定。
     */
    ViewBinding getItemViewBinding(T model, int position);
}
