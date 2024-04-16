package com.chw.application.widget.edittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import com.chw.application.util.keyboard.MInputConnection;

@SuppressLint("AppCompatCustomView")
public class KeyBoardTextEdit extends EditText {

    private MInputConnection inputConnection;

    public KeyBoardTextEdit(Context context) {
        super(context);
        defaultSet();
    }

    public KeyBoardTextEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        defaultSet();
    }

    public KeyBoardTextEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        defaultSet();
    }

    public KeyBoardTextEdit(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        defaultSet();
    }

    private void defaultSet() {
        inputConnection = new MInputConnection(null, true);
    }

    /**
     * 当输入法和EditText建立连接的时候会通过这个方法返回一个InputConnection。
     * 我们需要代理这个方法的父类方法生成的InputConnection并返回我们自己的代理类。
     */
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        inputConnection.setTarget(super.onCreateInputConnection(outAttrs));
        return inputConnection;
    }

    public void setOnBackSpaceListener(MInputConnection.BackSpaceListener backSpaceListener) {
        this.inputConnection.setBackspaceListener(backSpaceListener);
    }

    public void setEnterListener(MInputConnection.EnterListener enterListener) {
        this.inputConnection.setBackspaceListener(enterListener);
    }

}
