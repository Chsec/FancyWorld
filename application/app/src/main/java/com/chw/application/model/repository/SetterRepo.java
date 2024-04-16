package com.chw.application.model.repository;

import androidx.lifecycle.LiveData;
import com.chw.application.App;
import com.chw.application.model.ApplicationDataBase;
import com.chw.application.model.dao.SetterDao;
import com.chw.application.model.table.Setting;
import com.chw.application.model.table.SettingSort;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class SetterRepo extends BaseRepo {

    private final static SetterDao settingDao = ApplicationDataBase.getInstance().settingDao();

    /**
     * 创建单条Setting数据,主键重复则覆盖(返回创建数据的id)。
     */
    public static Long createSetting(Setting data) {
        try {
            return executor.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return settingDao.insert(data);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建多条Setting数据,主键重复则覆盖。
     */
    public static void createSetting(List<Setting> datas) {
        executor.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                settingDao.insert(datas);
                return null;
            }
        });
    }

    /**
     * 创建单条SettingSort数据,主键重复则覆盖(返回创建数据的id)。
     */
    public static Long createSort(SettingSort data) {
        try {
            return executor.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return settingDao.insertSort(data);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建多条Setting数据,主键重复则覆盖。
     */
    public static void createSort(List<SettingSort> datas) {
        executor.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                settingDao.insertSort(datas);
                return null;
            }
        });
    }

    /**
     * 获取设定器所有设定
     */
    public static LiveData<List<Setting>> getSetting(String settingOwner) {
        try {
            return executor.submit(new Callable<LiveData<List<Setting>>>() {
                @Override
                public LiveData<List<Setting>> call() throws Exception {
                    return settingDao.findSettingBySortAndOwnerLD(settingOwner, App.PRE_BOOK_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取设定器所有设定种类
     */
    public static LiveData<List<SettingSort>> getSettingSort(String settingOwner) {
        try {
            return executor.submit(new Callable<LiveData<List<SettingSort>>>() {
                @Override
                public LiveData<List<SettingSort>> call() throws Exception {
                    return settingDao.findSettingSortByOwnerLD(settingOwner, App.PRE_BOOK_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取所有设定器项
     */
    public static List<Setting> getSettersItem() {
        try {
            return executor.submit(new Callable<List<Setting>>() {
                @Override
                public List<Setting> call() throws Exception {
                    return settingDao.findSettingOwnersItemByOwner(App.PRE_BOOK_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取设定器所有设定项
     */
    public static List<Setting> getSettings(String setterOwner) {
        try {
            return executor.submit(new Callable<List<Setting>>() {
                @Override
                public List<Setting> call() throws Exception {
                    return settingDao.findSettingsByOwnerName(setterOwner, App.PRE_BOOK_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过设定名称获取设定flag
     */
    public static long getSettingFlag(String settingName) {
        try {
            return executor.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return settingDao.findSettingFlagBySettingName(settingName, App.PRE_BOOK_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
