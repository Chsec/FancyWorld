package com.chw.application.widget.toast;

import android.content.Context;
import android.widget.Toast;

public class ToastTipWrapper {

    public static Toast toastDialog(Context context, String tip) {
        return Toast.makeText(context, tip, Toast.LENGTH_SHORT);
    }

}
