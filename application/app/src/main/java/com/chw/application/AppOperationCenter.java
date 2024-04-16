package com.chw.application;

import android.app.Application;
import android.os.Environment;
import androidx.lifecycle.AndroidViewModel;
import com.elvishew.xlog.XLog;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class AppOperationCenter extends AndroidViewModel {



    private final String LOG_TAG = "AppOperationCenter";

    /**
     * 外部存储文件对象
     */
    private final File externalShareStorage;

    private final File extPrivateDirFile;

    private final File extPrivateDirCache;

    private File userHome;

    private File bookHome;

    private File profileDir;

    public AppOperationCenter(@NotNull Application application) {
        super(application);
        externalShareStorage = Environment.getExternalStorageDirectory();
        extPrivateDirFile = getApplication().getApplicationContext().getExternalFilesDir(null);
        extPrivateDirCache = getApplication().getApplicationContext().getExternalCacheDir();
    }

    /**
     * 初始化应用目录
     */
    public void createUserDir(long userId, long bookId) {
        userHome = new File(extPrivateDirFile.getAbsolutePath(), String.valueOf(userId));
        bookHome = new File(userHome.getAbsolutePath(), String.valueOf(bookId));
        profileDir = new File(bookHome.getAbsolutePath(), "profile");

        if (!userHome.exists()) {
            if (userHome.mkdir()) {
                XLog.tag(LOG_TAG).i("userHome目录创建成功：" + userHome.getAbsolutePath());
            } else {
                XLog.tag(LOG_TAG).e("userHome目录创建失败：" + userHome.getAbsolutePath());
            }
        }

        if (!bookHome.exists()) {
            if (bookHome.mkdir()) {
                XLog.tag(LOG_TAG).i("bookHome目录创建成功：" + bookHome.getAbsolutePath());
            } else {
                XLog.tag(LOG_TAG).e("bookHome目录创建失败：" + bookHome.getAbsolutePath());
            }
        }

        if (!profileDir.exists()) {
            if (profileDir.mkdir()) {
                XLog.tag(LOG_TAG).i("profileDir目录创建成功：" + profileDir.getAbsolutePath());
            } else {
                XLog.tag(LOG_TAG).e("profileDir目录创建失败：" + profileDir.getAbsolutePath());
            }
        }
    }

    /**
     * 获取用户主目录
     */
    public File getUserHome() {
        if (userHome == null) {
            throw new NullPointerException("用户主目录获取失败");
        }
        return userHome;
    }

    /**
     * 获取书籍主目录
     */
    public File getBookHome() {
        if (bookHome == null) {
            throw new NullPointerException("书籍主目录获取失败");
        }
        return bookHome;
    }

    /**
     * 获取头像目录
     */
    public File getProfileDir() {
        if (profileDir == null) {
            throw new NullPointerException("头像目录获取失败");
        }
        return profileDir;
    }
}
