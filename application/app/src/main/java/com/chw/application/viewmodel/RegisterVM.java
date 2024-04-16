package com.chw.application.viewmodel;

import androidx.lifecycle.MutableLiveData;
import com.chw.application.model.ApplicationDataBase;
import com.chw.application.model.dao.UserDao;
import com.chw.application.model.table.User;

public class RegisterVM extends BaseVM {

    private final String REGISTER_NULL = "请输入账户密码！";
    private final String REGISTER_EXIST = "注册账户已存在，请重新注册！";
    private final String REGISTER_SUCCESS = "注册成功，自动跳转至登录页面！";


    private MutableLiveData<User> data;
    private ApplicationDataBase db;

    public RegisterVM() {
    }

    public boolean doRegister(User user) {
        UserDao userDao = ApplicationDataBase.getInstance().userDao();
        if (user.getUserName() != null || user.getUserPwd() != null) {
            String byName = userDao.findPasswordByUserName(user.getUserName());
            if (byName == null) {
//                userDao.insertUser(user);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


}
