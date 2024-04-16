package com.chw.application.model.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.chw.application.App;
import com.chw.application.model.ApplicationDataBase;
import com.chw.application.model.dao.StateRecordDao;
import com.chw.application.model.table.StateRecord;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class StateRecordRepo extends BaseRepo {

    private final static StateRecordDao stateRecordDao = ApplicationDataBase.getInstance().appConfigDao();

    /**
     * 创建单条BookBean数据,主键重复则覆盖。
     */
    public static void create(StateRecord stateRecord) {
        executor.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                stateRecordDao.insert(stateRecord);
                return null;
            }
        });
    }

    /**
     * 获取配置
     */
    public static HashMap<String, String> getStateMap(boolean stateType, long flag) {
        try {
            String configStorage = executor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return stateRecordDao.findMapByStateAndFlag(stateType, flag);
                }
            }).get();
            return JSONObject.parseObject(JSON.parseObject(configStorage).toString(), new TypeReference<HashMap<String, String>>() {
            });
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 保存配置
     */
    public static void setStateMap(HashMap<String, String> data) {
        executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                stateRecordDao.insert(App.PRE_BOOK_ID, JSON.toJSONString(data));
                return false;
            }
        });
    }
}
