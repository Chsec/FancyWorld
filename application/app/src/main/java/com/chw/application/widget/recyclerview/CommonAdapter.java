package com.chw.application.widget.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {

    /**
     * 空ViewHolder
     */
    private final static int EMPTY_VIEW_HOLDER = 0;

    /**
     * ViewHolder数据绑定回调接口对象
     */
    private BindDataImpl<T> bindData;

    /**
     * 支持ViewBinding的单类型ViewHolder数据绑定回调接口对象
     */
    private BindDataVBImpl<T> bindDataVB;

    private int holderPosition;

    /**
     * 数据存放容器
     */
    private List<T> mDatas;

    private ItemClickListener listener;

    /**
     * 普通构造，通过资源id绑定资源
     */
    public CommonAdapter(List<T> mList, BindDataImpl<T> bindData) {
        this.mDatas = mList;
        this.bindData = bindData;
    }

    /**
     * ViewBinding构造
     */
    public CommonAdapter(List<T> mList, BindDataVBImpl<T> bindDataVB) {
        this.mDatas = mList;
        this.bindDataVB = bindDataVB;
    }

    /**
     * @param position 查询位置
     * @return 表示位置项所需的ViewHolder类型
     */
    @Override
    public int getItemViewType(int position) {
        holderPosition = position;
        if (mDatas == null) {
            return EMPTY_VIEW_HOLDER;
        } else if (bindDataVB != null) {
            return bindDataVB.getItemViewBinding(mDatas.get(position), position).getRoot().getId();
        } else if (bindData != null) {
            return bindData.getItemViewLayoutId(mDatas.get(position), position);
        } else {
            return EMPTY_VIEW_HOLDER;
        }
    }

    /**
     * 根据不同视图类型创建不同的ViewHolder
     *
     * @param parent   RecyclerView
     * @param viewType ViewHolder的视图类型，由getItemViewType返回值确定；
     * @return 通用视图控制器
     */
    @NotNull
    @Override
    public CommonViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        // 如果数据为空则创建默认空视图
        if (viewType == EMPTY_VIEW_HOLDER) {
            LinearLayout empty = new LinearLayout(parent.getContext());
            empty.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return CommonViewHolder.getCommonViewHolder(parent.getContext(), empty);
        } else if (bindDataVB != null) {
            ViewBinding layoutView = bindDataVB.getItemViewBinding(mDatas.get(holderPosition), holderPosition);
            layoutView.getRoot().setOnClickListener(new ListenerAdapter());
            layoutView.getRoot().setOnLongClickListener(new ListenerAdapter());
            return CommonViewHolder.getCommonViewHolder(parent.getContext(), layoutView);
        } else if (bindData != null) {
            int layoutId = bindData.getItemViewLayoutId(mDatas.get(holderPosition), holderPosition);
            // item使用liner布局时不能占满RecyclerView
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            view.setOnClickListener(new ListenerAdapter());
            view.setOnLongClickListener(new ListenerAdapter());
            return CommonViewHolder.getCommonViewHolder(parent.getContext(), view);
        } else {
            throw new RuntimeException("RecyclerView#onCreateViewHolder异常");
        }
    }

    /**
     * 适配器数据集项数据与该位置视图的绑定
     *
     * @param holder   适配器数据集项的ViewHolder
     * @param position 适配器数据集项的Position
     */
    @Override
    public void onBindViewHolder(@NotNull CommonViewHolder holder, int position) {
        int itemViewType;
        if (bindDataVB != null) {
            itemViewType = getItemViewType(position);
            bindDataVB.onBindViewHolder(this, holder, mDatas.get(holderPosition), itemViewType);
        } else {
            itemViewType = getItemViewType(position);
            bindData.onBindViewHolder(mDatas.get(holderPosition), holder, itemViewType, position);
        }
    }

    /**
     * ViewHolder视图局部刷新执行方法，通知数据更改是payload参数有任意值时执行
     */
    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            if (bindDataVB != null) {
                bindDataVB.onBindViewHolder(holder, position, payloads);
            } else {
                bindData.onBindViewHolder(holder, position, payloads);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setItemListener(ItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * @return 获取适配器中数据集合
     */
    public List<T> getMDatas() {
        return mDatas;
    }

    /**
     * 更新适配器数据，自动刷新RecyclerView
     *
     * @param mList 待更新数据
     */
    public void updateData(List<T> mList) {
        if (this.mDatas != null) {
            int size = this.mDatas.size();
            this.mDatas.clear();
            notifyItemRangeRemoved(0, size);
            this.mDatas.addAll(mList);
            refresh();
        } else {
            this.mDatas = mList;
            refresh();
        }
    }

    /**
     * 更新适配器数据，自动刷新RecyclerView
     *
     * @param mList 待更新数据
     */
    public void updateData(List<T> mList, Object payload) {
        if (this.mDatas != null) {
            int size = this.mDatas.size();
            this.mDatas.clear();
            notifyItemRangeRemoved(0, size);
            this.mDatas.addAll(mList);
            refresh();
        } else {
            this.mDatas = mList;
            refresh(payload);
        }
    }

    /**
     * 插入一项数据
     *
     * @param item     数据项
     * @param position 数据项插入位置
     */
    public void insert(T item, int position) {
        mDatas.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * 删除position数据项
     *
     * @param position 数据项位置
     */
    public void remove(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 数据项添加置数据容器尾部
     *
     * @param item 添加的数据项
     */
    public void add(T item) {
        mDatas.add(item);
        int itemCount = getItemCount();
        notifyItemInserted(itemCount - 1);
    }

    /**
     * 替换position位置的对象为item
     */
    public void replace(T item, int position) {
        mDatas.set(position, item);
        notifyItemChanged(position);
    }

    /**
     * 不改变数据刷新RecyclerView条目视图
     */
    public void refresh() {
        notifyItemRangeChanged(0, getItemCount());
    }

    /**
     * 不改变数据刷新RecyclerView条目内视图(需要实现BindDataVB接口)
     */
    public void refresh(Object payload) {
        notifyItemRangeChanged(0, getItemCount(), payload);
    }

    public interface ItemClickListener {
        void OnItemClickListener(View view, int viewId);

        void OnItemLongClickListener(View view, int viewId);
    }

    public class ListenerAdapter implements View.OnClickListener, View.OnLongClickListener {
        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.OnItemClickListener(view, view.getId());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (listener != null) {
                listener.OnItemLongClickListener(view, view.getId());
            }
            return false;
        }
    }

}
