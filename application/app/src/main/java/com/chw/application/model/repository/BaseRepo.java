package com.chw.application.model.repository;

import com.chw.application.App;
import com.yc.ycthreadpoollib.PoolThread;

public class BaseRepo {
    protected static final PoolThread executor = App.getInstance().getExecutor();

}
