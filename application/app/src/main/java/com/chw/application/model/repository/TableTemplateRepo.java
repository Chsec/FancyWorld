package com.chw.application.model.repository;

import androidx.lifecycle.LiveData;
import com.chw.application.App;
import com.chw.application.model.ApplicationDataBase;
import com.chw.application.model.dao.TableTemplateDao;
import com.chw.application.model.table.TableTemplate;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class TableTemplateRepo extends BaseRepo {

    private static final TableTemplateDao tableTemplateDao = ApplicationDataBase.getInstance().tableTemplateDao();

    /**
     * 创建多条数据,主键重复则覆盖。
     */
    public static void create(List<TableTemplate> datas) {
        try {

            executor.submit((Callable<String>) () -> {
                for (TableTemplate t : datas) {
                    t.setBookFlag(App.PRE_BOOK_ID);
                }
                ApplicationDataBase.getInstance().runInTransaction(() -> tableTemplateDao.insert(datas));
                return null;
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建单条数据,主键重复则覆盖(返回创建数据的id)。
     */
    public static Long create(TableTemplate data) {
        try {
            return executor.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    data.setBookFlag(App.PRE_BOOK_ID);
                    return tableTemplateDao.insertReId(data);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取某类表模板名称集合
     */
    public static List<String> getTablesName(String tableOwner) {
        try {
            return executor.submit(new Callable<List<String>>() {
                @Override
                public List<String> call() throws Exception {
                    return tableTemplateDao.findTablesNameByOwner(tableOwner, App.PRE_BOOK_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取某类表模板名称集合的LiveData对象
     */
    public static LiveData<List<String>> getTablesNameLD(String tableOwner) {
        try {
            return executor.submit(new Callable<LiveData<List<String>>>() {
                @Override
                public LiveData<List<String>> call() throws Exception {
                    return tableTemplateDao.findTablesNameByOwnerLD(tableOwner, App.PRE_BOOK_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过模板名称获取表模板数据。
     */
    public static List<TableTemplate> getTableByName(String tempName, String tempOwner) {
        try {
            return executor.submit(new Callable<List<TableTemplate>>() {
                @Override
                public List<TableTemplate> call() throws Exception {
                    return tableTemplateDao.findTableByName(tempName, tempOwner, App.PRE_BOOK_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过模板标记获取表模板数据。
     */
    public static List<TableTemplate> getTableByFlag(long tableFlag) {
        try {
            return executor.submit(new Callable<List<TableTemplate>>() {
                @Override
                public List<TableTemplate> call() throws Exception {
                    return tableTemplateDao.findTableByFlag(tableFlag);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取某类模板任意一条数据集合的LiveData对象
     */
    public static LiveData<List<TableTemplate>> getTablesItemLD(String tempOwner) {
        try {
            return executor.submit(new Callable<LiveData<List<TableTemplate>>>() {
                @Override
                public LiveData<List<TableTemplate>> call() throws Exception {
                    return tableTemplateDao.findTablesItemByOwnerLD(tempOwner, App.PRE_BOOK_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * 重命名模板
     */
    public static void reNameTable(String oldName, String newName, String tableOwner) {
        executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                tableTemplateDao.renameTable(oldName, newName, tableOwner, App.PRE_BOOK_ID);
                return null;
            }
        });
    }

    /**
     * 判断模板是否能删除
     */
    public static boolean isDeleteTable(String tableName, String tableOwner) {
        try {
            int[] ints = executor.submit(new Callable<int[]>() {
                @Override
                public int[] call() throws Exception {
                    return tableTemplateDao.findTableRefer(tableName, tableOwner, App.PRE_BOOK_ID);
                }
            }).get();
            int temp = 0;
            for (int keyDelSwitch : ints) {
                temp += keyDelSwitch;
            }
            if (temp == 0) {
                return true;
            } else {
                return false;
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除模板
     */
    public static boolean cleanTable(String tableName, String tableOwner) {
        if (isDeleteTable(tableName, tableOwner)) {
            executor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    tableTemplateDao.deleteTable(tableName, tableOwner, App.PRE_BOOK_ID);
                    return null;
                }
            });
            return true;
        } else {
            return false;
        }
    }

    /**
     * 同步整个模板所有项的引用字段，true引用加一，false引用减一
     *
     * @param tempName  模板名字
     * @param tempOwner 模板拥有者
     * @param model     模板引用加减模式
     */
    public static void syncTableRefer(String tempName, String tempOwner, boolean model) {
        if (model) {
            executor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    tableTemplateDao.inTableRefer(tempName, tempOwner, App.PRE_BOOK_ID);
                    return null;
                }
            });
        } else {
            executor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    tableTemplateDao.deTableRefer(tempName, tempOwner, App.PRE_BOOK_ID);
                    return null;
                }
            });
        }
    }

    /**
     * 同步多条模板项的引用字段，true引用加一，false引用减一
     */
    public static void syncTableItemRefer(List<Long> itemIds, boolean model) {
        executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ApplicationDataBase.getInstance().runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        for (Long i : itemIds) {
                            if (model) {
                                tableTemplateDao.increaseTemplateItemRefer(i);
                            } else {
                                tableTemplateDao.decreaseTemplateItemRefer(i);
                            }
                        }
                    }
                });
                return true;
            }
        });
    }

    /**
     * 同步多条模板项的引用字段，true引用加一，false引用减一
     */
    public static void syncTableItemRefer(Long itemId, boolean model) {
        executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (model) {
                    tableTemplateDao.increaseTemplateItemRefer(itemId);
                } else {
                    tableTemplateDao.decreaseTemplateItemRefer(itemId);
                }
                return true;
            }
        });
    }

}
