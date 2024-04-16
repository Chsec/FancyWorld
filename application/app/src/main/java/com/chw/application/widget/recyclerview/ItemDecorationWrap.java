package com.chw.application.widget.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.chw.application.model.table.TCharacter;
import com.chw.application.widget.recyclerview.CommonAdapter;
import com.xuexiang.xui.utils.DensityUtils;
import org.jetbrains.annotations.NotNull;

public class ItemDecorationWrap {

    public LetterBarItem getLetterBarItem(Context context) {
        return new LetterBarItem(context);
    }

    public class LetterBarItem extends RecyclerView.ItemDecoration {

        private final Context context;

        private final int screenWithPixel;

        /**
         * 字母分组栏画笔
         */
        private final Paint groupBarPaint;

        /**
         * 字母分组栏颜色
         */
        private final int groupBarColor = 0xFFf4f4f4;

        /**
         * 字母分组栏高度
         */
        private final int groupBarHeight;

        /**
         * 字母分组栏文字颜色
         */
        private final int textPaintColor = 0xFF999999;

        /**
         * 绘制文字的矩形边框
         */
        private final Rect textRect = new Rect();

        /**
         * 悬停item的画笔
         */
        private final Paint itemHoverPaint;

        /**
         * 分组item的颜色
         */
        private final int itemHoverPaintColor = 0xFFf4f4f4;

        /**
         * 分割线高度
         */
        private final int divideHeight;

        /**
         * 字母分组栏文本距离左边距
         */
        private final int textPadLeft;

        /**
         * 字母分组栏文字画笔
         */
        private final Paint textPaint;

        public LetterBarItem(Context context) {
            this.context = context;
            // 屏幕宽度
            this.screenWithPixel = context.getResources().getDisplayMetrics().widthPixels;

            // 字母分组栏变量初始化
            this.groupBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            this.groupBarHeight = DensityUtils.dp2px(context, 15);

            // 字母分组栏文本变量初始化
            this.textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            this.textPaint.setColor(textPaintColor);
            this.textPaint.setTextSize(DensityUtils.dp2px(context, 15));
            this.textPadLeft = DensityUtils.dp2px(context, 20);

            // 分割线变量初始化
            this.divideHeight = DensityUtils.dp2px(context, 1);
            this.itemHoverPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            // 粘性栏
            // this.itemHoverPaint.setColor(itemHoverPaintColor);
        }

        @Override
        public void onDraw(@NonNull Canvas c, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
            super.onDraw(c, parent, state);
            int count = parent.getChildCount();
            CommonAdapter<TCharacter> adapter = (CommonAdapter<TCharacter>) parent.getAdapter();
            if (adapter != null && adapter.getMDatas() != null && !adapter.getMDatas().isEmpty()) {
                for (int i = 0; i < count; i++) {
                    View view = parent.getChildAt(i);
                    //字母分组栏的顶部和底部
                    int itemTop = view.getTop() - groupBarHeight;
                    int itemBottom = view.getTop();

                    // 可见item在adapter中真实的位置,根据位置获取适配器数据项字母
                    int position = parent.getChildAdapterPosition(view);
                    String text = getFirstChar(position, adapter);
                    //如果是一组中第一个的话绘制出分组的item和文字，否则绘制分割线
                    if (isFirstInGroup(position, adapter)) {
                        groupBarPaint.setColor(groupBarColor);
                        c.drawRect(0, itemTop, screenWithPixel, itemBottom, groupBarPaint);
                        drawText(c, itemTop, itemBottom, text);
                    } else {
                        c.drawRect(0, view.getTop() - divideHeight, screenWithPixel, view.getTop(), itemHoverPaint);
                    }
                }
            }
        }

        private void drawText(Canvas canvas, int itemTop, int itemBottom, String text) {
            textRect.left = textPadLeft;
            textRect.top = itemTop;
            textRect.right = text.length();
            textRect.bottom = itemBottom;

            Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
            int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
            // 文字绘制到整个布局的中心位置
            canvas.drawText(text, textRect.left, baseline, textPaint);
        }

        /**
         * 该ItemDecoration仅支持CommonAdapter
         *
         * @param outRect item的上下左右所撑开的距离,默认是0。
         * @param view    当前Item的View对象
         * @param parent  指item所依附的RecyclerView
         * @param state   通过State可以获取当前RecyclerView的状态
         */
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NotNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int adapterPosition = parent.getChildAdapterPosition(view);
            CommonAdapter<TCharacter> adapter = (CommonAdapter<TCharacter>) parent.getAdapter();
            // 如果是分组第一个，则在item上面撑起绘图空间
            if (isFirstInGroup(adapterPosition, adapter)) {
                outRect.top = groupBarHeight;
            } else {
                outRect.top = divideHeight;
            }
        }

        private boolean isFirstInGroup(int position, CommonAdapter<TCharacter> adapter) {
            if (position == 0) {
                return true;
            } else {
                String prevItemText = getFirstChar(position - 1, adapter);
                String currentItemText = getFirstChar(position, adapter);
                //上一个和当前位置的值一样说明是同一个组的否则就是新的一组
                return !prevItemText.equals(currentItemText);
            }
        }

        private String getFirstChar(int position, CommonAdapter<TCharacter> adapter) {
            return adapter.getMDatas().get(position).getCharLetter().substring(0, 1);
        }

    }
}
