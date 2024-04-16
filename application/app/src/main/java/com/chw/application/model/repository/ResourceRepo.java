package com.chw.application.model.repository;

import androidx.lifecycle.LiveData;
import com.chw.application.App;
import com.chw.application.model.ApplicationDataBase;
import com.chw.application.model.dao.ResourceDao;
import com.chw.application.model.table.Resource;
import com.chw.application.model.table.ResourceData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ResourceRepo extends BaseRepo {

    private static final ResourceDao resourceDao = ApplicationDataBase.getInstance().resourceDao();

    /**
     * 创建多条Resource数据,主键重复则覆盖。
     */
    public static void createReso(ArrayList<Resource> datas) {
        executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ApplicationDataBase.getInstance().runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        resourceDao.insertReso(datas);
                    }
                });
                return true;
            }
        });
    }

    /**
     * 创建单条Resource数据,主键重复则覆盖(返回创建数据的id)。
     */
    public static Long createReso(Resource data) {
        try {
            return executor.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return resourceDao.insertReso(data);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建多条ResourceData数据,主键重复则覆盖。
     */
    public static void createData(ArrayList<ResourceData> datas) {
        executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ApplicationDataBase.getInstance().runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        resourceDao.insertData(datas);
                    }
                });
                return true;
            }
        });
    }

    /**
     * 创建单条ResourceData数据,主键重复则覆盖(返回创建数据的id)。
     */
    public static Long createData(ResourceData data) {
        try {
            return executor.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return resourceDao.insertData(data);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Resource数据集合
     */
    public static LiveData<List<Resource>> getResourceLD(long tableFlag) {
        try {
            return executor.submit(() -> resourceDao.findResoByTableFlag(tableFlag, App.PRE_BOOK_ID)).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取所有Resource数据集合
     */
    public static LiveData<List<Resource>> getAllResourceLD() {
        try {
            return executor.submit(() -> resourceDao.findAllResoByTableFlag(App.PRE_BOOK_ID)).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
