package com.chw.application.viewmodel;

import androidx.lifecycle.MutableLiveData;
import com.chw.application.model.ApplicationDataBase;
import com.chw.application.model.table.User;

public class LoginVM extends BaseVM {

    private MutableLiveData<User> data;

    public LoginVM() {
    }


    public String doLogin(String username, String password) {
        String pwd = ApplicationDataBase.getInstance().userDao().findPasswordByUserName(username);
        if (pwd == null) {
            return "register";
        } else if (pwd.equals(password)) {
            return "success";
        } else {
            return "error";
        }
    }

}
