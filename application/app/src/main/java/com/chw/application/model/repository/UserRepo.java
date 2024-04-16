package com.chw.application.model.repository;

import com.chw.application.model.ApplicationDataBase;
import com.chw.application.model.dao.UserDao;
import com.chw.application.model.table.User;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class UserRepo extends BaseRepo {

    final static UserDao userDao = ApplicationDataBase.getInstance().userDao();

    /**
     * 创建单条UserBean数据,并返回数据id（主键重复则覆盖）。
     */
    public static long create(User user) {
        try {
            return executor.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    return userDao.insertReId(user);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询已经登录了的用户
     */
    public static User getLogined() {
        try {
            return executor.submit(new Callable<User>() {
                @Override
                public User call() throws Exception {
                    ArrayList<User> a = new ArrayList<>();
                    return userDao.findUserByUserState(true);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过用户名获取用户密码
     */
    public static String getUserPasswd(String userName) {
        try {
            return executor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return userDao.findPasswordByUserName(userName);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过用户名获取用户名
     */
    public static String getUserName(String userName) {
        try {
            return executor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return userDao.findUserNameByUserName(userName);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过用户id获取用户名
     */
    public static User getUser(long userId) {
        try {
            return executor.submit(new Callable<User>() {
                @Override
                public User call() throws Exception {
                    return userDao.findUserByUserId(userId);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
