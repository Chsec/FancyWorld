package com.chw.application.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import com.chw.application.R;
import com.chw.application.databinding.ActivityEntranceBinding;
import com.chw.application.model.repository.UserRepo;
import com.chw.application.util.MD5Utils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;

import java.util.Objects;

public class EntranceActivity extends BaseActivity<ActivityEntranceBinding> {

    private final String loginText = getString(R.string.login);

    private final String registerText = getString(R.string.register);

    private Button login, register, done;

    private MaterialEditText account, password, repeat;

    private LinearLayout frame;

    private Drawable green, white;

    private TextView forget;

    @Override
    protected ActivityEntranceBinding viewBinding() {
        return ActivityEntranceBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initPrepareFirst() {
        login = viewBinding.login;
        register = viewBinding.register;
        done = viewBinding.done;

        frame = viewBinding.line;
        account = viewBinding.account;
        password = viewBinding.password;
        repeat = viewBinding.repeat;
        forget = viewBinding.forget;

        green = AppCompatResources.getDrawable(this, R.color.green1);
        white = AppCompatResources.getDrawable(this, R.color.white);

    }

    @Override
    protected void initViewSecond() {
        login.setBackground(green);
        register.setBackground(white);

        repeat.setHint("请重新输入密码");
        frame.removeView(repeat);

        account.setHint("请输入登录账户");
        password.setHint("请输入登录账号密码");

    }

    @Override
    protected void initListenerThird() {
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        done.setOnClickListener(this);
        forget.setOnClickListener(this);
    }

    @Override
    protected void initObserveFourth() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == login.getId()) {
            done.setText(loginText);
            login.setBackground(green);
            register.setBackground(white);
            frame.removeView(repeat);
            frame.addView(forget);
        } else if (id == register.getId()) {
            done.setText(registerText);
            login.setBackground(white);
            register.setBackground(green);
            frame.removeView(forget);
            frame.addView(repeat, 2);
        } else if (id == done.getId()) {
            String string = done.getText().toString();
            if (Objects.equals(string, loginText)) {
                Editable accountText = account.getText();
                Editable passwdText = password.getText();
                login(accountText != null ? accountText.toString() : "", passwdText != null ? passwdText.toString() : "");
            } else if (Objects.equals(string, registerText)) {
                Editable accountText = account.getText();
                Editable passwdText = password.getText();
                Editable repeatText = repeat.getText();
                register(accountText != null ? accountText.toString() : "", passwdText != null ? passwdText.toString() : "", repeatText != null ? repeatText.toString() : "");
            }
        }
    }

    private void login(String userName, String inputPasswd) {
        String passwd = UserRepo.getUserPasswd(userName);
        String encrypt = MD5Utils.md5Password(inputPasswd);
        if (Objects.equals(passwd, encrypt)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            XToastUtils.error("密码错误");
        }
    }

    private void register(String userName, String userPassword, String repeatPassword) {
        String name = UserRepo.getUserName(userName);
        if (Objects.equals(name, userName)) {
            XToastUtils.error("账户名已存在");
        } else if (Objects.equals(userName, "")) {
            XToastUtils.error("用户名为空");
        } else if (Objects.equals(userPassword, repeatPassword)) {
            XToastUtils.error("重读密码错误");
        } else {
            ACC.createUser(userName, userPassword);
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
