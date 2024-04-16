package com.chw.application.widget.recyclerview;

import androidx.annotation.NonNull;

import java.util.List;

public interface BindData<T> {

    /**
     * 普通视图与数据绑定实现
     *
     * @param model      ViewHolder数据项
     * @param viewHolder ViewHolder布局
     * @param type       ViewHolder布局类型
     * @param position   ViewHolder在适配器中的位置
     */
    void onBindViewHolder(T model, CommonViewHolder viewHolder, int type, int position);

    /**
     * ViewHolder视图局部刷新方法
     *
     * @param holder   ViewHolder视图
     * @param position ViewHolder位置
     * @param payloads 调用数据刷新时传递的payload
     */
    void onBindViewHolder(@NonNull CommonViewHolder holder, int position, @NonNull List<Object> payloads);

    /**
     * @param model    数据
     * @param position 位置
     * @return 返回ViewHolder的布局id
     */
    int getItemViewLayoutId(T model, int position);

}
