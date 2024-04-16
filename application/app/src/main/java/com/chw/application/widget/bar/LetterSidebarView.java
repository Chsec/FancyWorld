package com.chw.application.widget.bar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import com.xuexiang.xui.utils.DensityUtils;

public class LetterSidebarView extends View {

    private static final String[] LETTER_INDEX = new String[]{"↑", "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private Paint mPaint;
    private int selectedColor;
    private Rect bounds;
    private float viewWidth, cellHeight;
    private OnLetterUpdateListener listener;
    private int touchIndex = -1;

    public LetterSidebarView(Context context) {
        this(context, null);
    }

    public LetterSidebarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSidebarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setTextSize(DensityUtils.dp2px(context, 14));
        mPaint.setAntiAlias(true);
        bounds = new Rect();
        // 获取文字被选中的颜色
        // selectedColor = ContextCompat.getColor(context, );
        selectedColor = Color.parseColor("#999DA1");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < LETTER_INDEX.length; i++) {
            String text = LETTER_INDEX[i];
            //计算绘制字符的X方向起点
            int x = (int) (viewWidth / 2.0f - mPaint.measureText(text) / 2.0f);
            mPaint.getTextBounds(text, 0, text.length(), bounds);
            int textHeight = bounds.height();
            //计算绘制字符的Y方向起点
            int y = (int) (cellHeight / 2.0f + textHeight / 2.0f + i
                    * cellHeight);
            mPaint.setColor(selectedColor);
            canvas.drawText(text, x, y, mPaint);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 计算当前触摸的字符索引
        int index = (int) (event.getY() / cellHeight);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (index >= 0 && index < LETTER_INDEX.length) {
                    if (listener != null) {
                        listener.onDownListener(LETTER_INDEX[index]);
                    }
                    touchIndex = index;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算当前触摸的字符索引
                index = (int) (event.getY() / cellHeight);
                if (index >= 0 && index < LETTER_INDEX.length) {
                    if (index != touchIndex) {
                        if (listener != null) {
                            listener.onMoveListener(LETTER_INDEX[index]);
                        }
                        touchIndex = index;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (listener != null) {
                    listener.onUpListener(LETTER_INDEX[index]);
                }
                break;

        }
        invalidate();
        return true;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 得到当前控件的宽度
        viewWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();
        // 获取单个字符能够拥有的高度
        cellHeight = mHeight * 1.0f / LETTER_INDEX.length;
    }

    public void setListener(OnLetterUpdateListener listener) {
        this.listener = listener;
    }

    public interface OnLetterUpdateListener {
        void onDownListener(String letter);

        void onMoveListener(String letter);

        void onUpListener(String letter);
    }

}

