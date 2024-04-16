package com.chw.application.widget;

import com.chw.application.model.bean.Result;
import com.elvishew.xlog.XLog;
import com.yc.ycthreadpoollib.callback.AsyncCallback;

public class AsyncThreadCallback implements AsyncCallback<Result> {
    @Override
    public void onSuccess(Result s) {
        XLog.i(s.getResultTip());
    }

    @Override
    public void onFailed(Throwable t) {
        XLog.e(t.getMessage());
    }

    @Override
    public void onStart(String threadName) {
        XLog.i(threadName);
    }
}
