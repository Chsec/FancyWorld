package com.chw.application.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import com.chw.application.App;
import com.chw.application.AppConfigCenter;
import com.chw.application.R;
import com.chw.application.model.table.User;
import com.chw.application.model.repository.UserRepo;

public class SplashActivity extends AppCompatActivity {

    /**
     * 默认启动页过渡时间
     */
    private static final int DEFAULT_SPLASH_DURATION_MILLIS = 500;

    private LinearLayout page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPage();
        startSplashAnim(new AlphaAnimation(0.1f, 0.2f));
    }

    /**
     * 初始化页面
     */
    private void initPage() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        page = new LinearLayout(this);
        page.setLayoutParams(params);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setBackground(AppCompatResources.getDrawable(this, R.mipmap.guide));
        setContentView(page);
    }

    /**
     * 开启引导过渡动画
     */
    private void startSplashAnim(Animation anim) {
        anim.setDuration(DEFAULT_SPLASH_DURATION_MILLIS);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AppConfigCenter adc = App.getInstance().getACC();
                User logined = UserRepo.getLogined();
                Intent intent;
                if (logined == null) {
                    adc.createUser("admin", "admin");
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else if (logined.getUserState()) {
                    adc.initUser(logined);
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, EntranceActivity.class);
                }
                startActivity(intent);
            }
        });
        page.startAnimation(anim);
    }

    @Override
    protected void onDestroy() {
        Drawable d = page.getBackground();
        page.setBackgroundResource(0);
        if (d instanceof BitmapDrawable) {
            Bitmap bmp = ((BitmapDrawable) d).getBitmap();
            if (bmp != null && !bmp.isRecycled()) {
                bmp.recycle();
            }
        }
        if (d != null) {
            d.setCallback(null);
        }
        super.onDestroy();
    }

}
