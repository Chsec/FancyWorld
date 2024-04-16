package com.chw.application.model.repository;

import androidx.lifecycle.LiveData;
import com.chw.application.App;
import com.chw.application.model.ApplicationDataBase;
import com.chw.application.model.dao.CharacterDao;
import com.chw.application.model.table.TCharacter;
import com.chw.application.model.table.TCharacterData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class CharacterRepo extends BaseRepo {

    private static final CharacterDao characterDao = ApplicationDataBase.getInstance().characterDao();

    /**
     * 创建多条Character数据,主键重复则覆盖。
     */
    public static void createChar(List<TCharacter> datas) {
        executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                ApplicationDataBase.getInstance().runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        characterDao.insertChar(datas);
                    }
                });
                return null;
            }
        });
    }

    /**
     * 创建单条Character数据,主键重复则覆盖(返回创建数据的id)。
     */
    public static Long createChar(TCharacter data) {
        try {
            return executor.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return characterDao.insertChar(data);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建多条Character模板数据,主键重复则覆盖。
     */
    public static void createData(List<TCharacterData> charDatas) {
        executor.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                for (TCharacterData c : charDatas) {
                    c.setBookFlag(App.PRE_BOOK_ID);
                }
                characterDao.insertData(charDatas);
                return null;
            }
        });
    }

    /**
     * 创建单条Character模板数据,主键重复则覆盖(返回创建数据的id)。
     */
    public static Long createData(TCharacterData charData) {
        try {
            return executor.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    charData.setBookFlag(App.PRE_BOOK_ID);
                    return characterDao.insertData(charData);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除一条数据
     */
    public static boolean deleteChar(TCharacter data) {
        if (isDelete(data.getCharId())) {
            try {
                return executor.submit(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        characterDao.deleteCharById(data.getCharId());
                        return true;
                    }
                }).get();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            return false;
        }
    }

    /**
     * 获取character表中当前bookId所有数据
     */
    public static LiveData<List<TCharacter>> getCharacterLD(Long tableFlag) {
        try {
            return executor.submit(new Callable<LiveData<List<TCharacter>>>() {
                @Override
                public LiveData<List<TCharacter>> call() throws Exception {
                    return characterDao.findCharByTableFlagLD(tableFlag, App.PRE_BOOK_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取character表中当前bookId所有数据
     */
    public static LiveData<List<TCharacter>> getAllCharacterLD() {
        try {
            return executor.submit(new Callable<LiveData<List<TCharacter>>>() {
                @Override
                public LiveData<List<TCharacter>> call() throws Exception {
                    return characterDao.findAllCharByTableFlagLD(App.PRE_BOOK_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断是否能够删除
     */
    public static boolean isDelete(Integer id) {
        try {
            Integer i = executor.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return characterDao.findRefer(id);
                }
            }).get();
            return i == 0;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
