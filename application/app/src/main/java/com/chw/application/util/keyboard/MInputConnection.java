package com.chw.application.util.keyboard;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

/**
 * {@link InputConnection} 是输入法和View交互的纽带。
 * {@link InputConnectionWrapper} 是 InputConnection 的代理类,可以代理EditText的InputConnection，监听和拦截软键盘的各种输入事件。
 * 注：用 {@link View#setOnKeyListener(View.OnKeyListener)} 监听软键盘的按键点击事件对有些键盘无效(比如谷歌输入法)，
 * 最好用InputConnection去监听。
 */
public class MInputConnection extends InputConnectionWrapper {

    private BackSpaceListener backSpaceListener;

    private EnterListener enterListener;

    public MInputConnection(InputConnection target, boolean mutable) {
        super(target, mutable);
    }

    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        // 在此处记录用户的文本输入
        return super.commitText(text, newCursorPosition);
    }

    /**
     * 当软键盘删除文本之前，会调用这个方法通知输入框，我们可以重写这个方法并判断是否要拦截这个删除事件。
     * 在谷歌输入法上，点击退格键的时候不会调用{@link #sendKeyEvent(KeyEvent event)}，
     * 而是直接回调这个方法，所以也要在这个方法上做拦截；
     */
    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        if (backSpaceListener != null) {
            if (backSpaceListener.onBackspace()) {
                return true;
            }
        }
        return super.deleteSurroundingText(beforeLength, afterLength);
    }

    @Override
    public boolean sendKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
            if (backSpaceListener != null && backSpaceListener.onBackspace()) {
                return true;
            }
        }
        return super.sendKeyEvent(event);
    }

    public void setBackspaceListener(BackSpaceListener backspaceListener) {
        this.backSpaceListener = backspaceListener;
    }

    public void setBackspaceListener(EnterListener enterListener) {
        this.enterListener = enterListener;
    }

    /**
     * 删除事件监听接口
     */
    public interface BackSpaceListener {
        /**
         * @return true消费事件，false交给系统消费
         */
        boolean onBackspace();
    }

    public interface EnterListener {
        boolean onEnter();
    }

}
