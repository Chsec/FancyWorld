package com.chw.application;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.lifecycle.ViewModelProvider;
import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.ConsolePrinter;
import com.elvishew.xlog.printer.Printer;
import com.yc.ycthreadpoollib.PoolThread;

public class App extends Application {

    /**
     * TableTemplate的拥有者支持数组
     */
    public static String[] TABLE_OWNER;

    /**
     * TreeTemplate的拥有者支持数组
     */
    public static String[] TREE_OWNER;

    /**
     * 当前用户id（保证最先初始化，仓库层依赖该字段）
     */
    public static long PRE_USER_ID;

    /**
     * 当前书籍id（保证最先初始化，仓库层依赖该字段）
     */
    public static long PRE_BOOK_ID;

    @SuppressLint("StaticFieldLeak")
    private static App instance = null;

    /**
     * 线程池对象
     */
    private PoolThread executor;

    /**
     * 应用配置中心
     */
    private AppConfigCenter ACC;

    /**
     * 应用操作中心
     */
    private AppOperationCenter AOC;

    /**
     * 应用数据中心
     */
    private AppDataCenter ADC;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        TABLE_OWNER = getResources().getStringArray(R.array.table_template_owner);
        TREE_OWNER = getResources().getStringArray(R.array.tree_template_owner);
        initThreadPool();
        initLog();

        ACC = new ViewModelProvider.AndroidViewModelFactory(this).create(AppConfigCenter.class);
        AOC = new ViewModelProvider.AndroidViewModelFactory(this).create(AppOperationCenter.class);
        ADC = new ViewModelProvider.AndroidViewModelFactory(this).create(AppDataCenter.class);
        AOC.createUserDir(PRE_USER_ID, PRE_BOOK_ID);
    }

    /**
     * 初始化线程池管理器
     */
    private void initThreadPool() {
        executor = PoolThread.ThreadBuilder
                .createFixed(5)
                .setPriority(Thread.MAX_PRIORITY)
                .build();
    }

    private void initLog() {
        LogConfiguration config = new LogConfiguration.Builder()
                .logLevel(LogLevel.ALL)       // 指定日志级别，低于该级别的日志将不会被打印，默认为 LogLevel.ALL
                .tag("APP_TAG")                                        // 指定 TAG，默认为 "X-LOG"
                .enableThreadInfo()                                    // 允许打印线程信息，默认禁止
                .enableStackTrace(2)                             // 允许打印深度为 2 的调用栈信息，默认禁止
                .enableBorder()                                        // 允许打印日志边框，默认禁止
                .build();
        // 日志打印器：android.util.Log打印日志到Logcat、通过 System.out 打印日志到控制台
        Printer androidPrinter = new AndroidPrinter(true);
        Printer consolePrinter = new ConsolePrinter();
        // 初始化 XLog
        // 指定日志配置，如果不指定，会默认使用 new LogConfiguration.Builder().build()
        // 添加任意多的打印器。如果没有添加任何打印器，会默认使用 AndroidPrinter(Android)/ConsolePrinter(java)
        XLog.init(
                config,
                androidPrinter,
                consolePrinter);

    }

    /**
     * 获取线程池管理器对象，统一的管理器维护所有的线程池
     *
     * @return executor对象
     */
    public PoolThread getExecutor() {
        return executor;
    }

    /**
     * 获取应用配置中心ViewModel对象
     */
    public AppConfigCenter getACC() {
        return ACC;
    }

    /**
     * 获取应用操作中心ViewModel对象
     */
    public AppOperationCenter getAOC() {
        return AOC;
    }

    /**
     * 获取应用数据中心ViewModel对象
     */
    public AppDataCenter getADC() {
        return ADC;
    }

}
