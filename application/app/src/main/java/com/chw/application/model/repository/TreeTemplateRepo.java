package com.chw.application.model.repository;

import androidx.lifecycle.LiveData;
import com.chw.application.App;
import com.chw.application.model.ApplicationDataBase;
import com.chw.application.model.dao.TreeTemplateDao;
import com.chw.application.model.table.TreeTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class TreeTemplateRepo extends BaseRepo {

    private static final TreeTemplateDao treeTemplateDao = ApplicationDataBase.getInstance().treeTemplateDao();

    /**
     * 保存多条数据
     *
     * @param datas 树的节点数据集合
     */
    public static void create(List<TreeTemplate> datas) {
        executor.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ApplicationDataBase.getInstance().runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        for (TreeTemplate t : datas) {
                            treeTemplateDao.insertData(t);
                        }
                    }
                });
                return null;
            }
        });
    }

    /**
     * 保存单条数据
     *
     * @param datas 树的节点集合
     */
    public static void create(TreeTemplate datas) {

    }

    /**
     * 通过树组Flag获取树组节点组合字符串列表
     */
    public static ArrayList<String> getGroupList(Long groupFlag) {
        try {
            List<TreeTemplate> group = executor.submit(new Callable<List<TreeTemplate>>() {
                @Override
                public List<TreeTemplate> call() throws Exception {
                    return treeTemplateDao.findGroupByFlag(groupFlag);
                }
            }).get();
            Collections.sort(group);
            return treeExtractStrs((ArrayList<TreeTemplate>) group);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过树组名称获取树组节点组合字符串列表
     */
    public static ArrayList<String> getGroupList(String groupName) {
        try {
            List<TreeTemplate> group = executor.submit(new Callable<List<TreeTemplate>>() {
                @Override
                public List<TreeTemplate> call() throws Exception {
                    return treeTemplateDao.findGroupByName(groupName, App.PRE_BOOK_ID);
                }
            }).get();
            Collections.sort(group);
            return treeExtractStrs((ArrayList<TreeTemplate>) group);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过树组名称获取树组flag
     */
    public static long getGroupFlag(String groupName) {
        try {
            return executor.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return treeTemplateDao.findGroupFlagByGroupName(groupName, App.PRE_BOOK_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通说树组Flag获取树组名称
     */
    public static String getGroupName(long groupFlag) {
        try {
            return executor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return treeTemplateDao.findGroupNameByGroupFlag(groupFlag, App.PRE_BOOK_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从树平铺列表中提取每条线路所有节点值的字符串组合，返回字符串集合
     */
    private static ArrayList<String> treeExtractStrs(ArrayList<TreeTemplate> datas) {
        boolean flag = true;

        HashMap<Integer, String> cache = new HashMap<>();
        StringBuilder temp = new StringBuilder();
        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < datas.size(); i++) {
            TreeTemplate t = datas.get(i);
            cache.put(t.getNodeDepth(), t.getNodeValue());
            // 补齐头部缺失,只需要执行一次
            if (cache.containsKey(t.getNodeDepth())) {
                if (flag) {
                    for (int j = 0; j < t.getNodeDepth(); j++) {
                        temp.append(cache.get(j));
                    }
                    flag = false;
                }
            }
            temp.append(t.getNodeValue());
            //一条字符串结束
            if (t.getNodeDepth() >= datas.get(i + 1).getNodeDepth()) {
                result.add(temp.toString());
                temp.setLength(0);
                flag = true;
            }
        }

        return result;
    }

    /**
     * 返回TreeTemplate中所有组的其中一条数据集合
     */
    public static List<TreeTemplate> getGroupsItem() {
        try {
            return executor.submit(new Callable<List<TreeTemplate>>() {
                @Override
                public List<TreeTemplate> call() throws Exception {
                    return treeTemplateDao.findGroupsByName(App.TREE_OWNER[0], App.PRE_BOOK_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回树组中任意一条数据组成的集合Livedata对象
     */
    public static LiveData<List<TreeTemplate>> getGroupsItemLD() {
        try {
            return executor.submit(new Callable<LiveData<List<TreeTemplate>>>() {
                @Override
                public LiveData<List<TreeTemplate>> call() throws Exception {
                    return treeTemplateDao.findGroupsByNameLD(App.TREE_OWNER[0], App.PRE_BOOK_ID);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
