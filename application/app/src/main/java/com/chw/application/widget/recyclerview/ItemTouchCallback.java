package com.chw.application.widget.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ItemTouchCallback<T> extends ItemTouchHelper.Callback {

    private static boolean DRAG_ENABLE = false;

    private static boolean SWIPE_ENABLE = false;

    public static void setDRAG_ENABLE(boolean DRAG_ENABLE) {
        ItemTouchCallback.DRAG_ENABLE = DRAG_ENABLE;
    }

    public static void setSWIPE_ENABLE(boolean SWIPE_ENABLE) {
        ItemTouchCallback.SWIPE_ENABLE = SWIPE_ENABLE;
    }

    /**
     * 设置上下左右动作
     * 只有在此处打开了指定方向的设置 , 才可以应用具体方向的拖动
     *
     * @param recyclerView 需要设置动作的RecyclerView视图
     * @param viewHolder   改视图的ViewHolder
     * @return 动作方向复合标志
     */
    @Override
    public int getMovementFlags(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
        // 设置拖动方向, 此处设置上下拖动事件
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // 设置滑动方向, 此处设置左右侧滑事件
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * 拖拽结束后（手指抬起）会回调此方法
     *
     * @param recyclerView recyclerView
     * @param viewHolder   手指拖拽的item
     * @param target       移动到的item
     * @return 是否执行
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder target) {
        // 我们在该方法中实现item数据在数据集合中的位置交换，并调用Adapter的notifyItemMoved完成item界面刷新
        recyclerView.getParent().requestDisallowInterceptTouchEvent(true);
        CommonAdapter<T> adapter = (CommonAdapter<T>) recyclerView.getAdapter();
        if (adapter == null || adapter.getMDatas() == null) {
            return false;
        }
        List<T> mDatas = adapter.getMDatas();
        //得到当前拖拽的viewHolder的Position
        int fromPosition = viewHolder.getAbsoluteAdapterPosition();
        //拿到当前拖拽到的item的viewHolder
        int toPosition = target.getAbsoluteAdapterPosition();
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                //这里暂时注释，用外面的数据来进行交换
                Collections.swap(mDatas, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                //这里暂时注释，用外面的数据来进行交换
                Collections.swap(mDatas, i, i - 1);
            }
        }
        int movementFlags = getMovementFlags(recyclerView, viewHolder);
        //通知适配器改变位置
        adapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /**
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    /**
     * item长按滑动动作开关
     *
     * @return true开启，false禁用
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return DRAG_ENABLE;
    }

    /**
     * item任意位置触发滑动操作开关
     *
     * @return rue开启，false禁用
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return SWIPE_ENABLE;
    }

}
