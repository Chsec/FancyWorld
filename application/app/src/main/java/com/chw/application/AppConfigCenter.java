package com.chw.application;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.alibaba.fastjson.JSON;
import com.chw.application.model.repository.BookEntityRepo;
import com.chw.application.model.repository.StateRecordRepo;
import com.chw.application.model.repository.TableTemplateRepo;
import com.chw.application.model.repository.UserRepo;
import com.chw.application.model.table.Book;
import com.chw.application.model.table.StateRecord;
import com.chw.application.model.table.TableTemplate;
import com.chw.application.model.table.User;
import com.chw.application.util.bus.StickLiveDataBus;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AppConfigCenter extends AndroidViewModel {
    /**
     * 切换当前User一级事件标志
     */
    public final static String USER = "0";

    /**
     * 切换当前User二级事件标志
     */
    public final static String USER_CHANGE = "1";

    /**
     * 切换当前Book一级事件标志
     */
    public final static String BOOK = "2";

    /**
     * 切换当前Book二级事件标志
     */
    public final static String BOOK_CHANGE = "3";

    /**
     * 刷新TCharacter展示列表事件
     */
    public final static String CHARACTER_REFRESH = "4";

    /**
     * 刷新Resource展示列表事件
     */
    public final static String RESOURCE_REFRESH = "5";

    /**
     * 设定器集合
     */
    public static final List<String> setterList = Arrays.asList(App.getInstance().getResources().getStringArray(R.array.setter));

    /**
     * 用户状态记录容器key数组
     */
    private final String[] USER_RECORD_KEY;

    /**
     * 书籍状态记录容器key数组
     */
    private final String[] BOOK_RECORD_KEY;

    /**
     * 用户状态记录容器
     */
    private HashMap<String, String> userRecord;

    /**
     * 书籍状态记录容器
     */
    private HashMap<String, String> bookRecord;

    public AppConfigCenter(@NotNull Application application) {
        super(application);
        USER_RECORD_KEY = application.getResources().getStringArray(R.array.user_state_record_key);
        BOOK_RECORD_KEY = application.getResources().getStringArray(R.array.book_state_record_key);
        observe();
    }

    private void observe() {
        //更换书籍的事件观察者
        StickLiveDataBus.get().getChannel(BOOK, String.class).observeForever(s -> {
            // 获取当前书籍内容配置
            bookRecord = StateRecordRepo.getStateMap(false, App.PRE_BOOK_ID);
            setUserRecord(0, String.valueOf(App.PRE_BOOK_ID));
            StickLiveDataBus.get().getChannel(BOOK_CHANGE, String.class).setValue("");
            App.getInstance().getAOC().createUserDir(App.PRE_USER_ID, App.PRE_BOOK_ID);
        });
        //切换用户的事件观察者
        StickLiveDataBus.get().getChannel(USER, String.class).observeForever(s -> {
            User logined = UserRepo.getLogined();
            userRecord = StateRecordRepo.getStateMap(true, logined.getUserID());
        });
    }

    /**
     * 初始化应用已存在用户
     */
    public void initUser(User user) {
        App.PRE_USER_ID = user.getUserID();
        userRecord = StateRecordRepo.getStateMap(true, App.PRE_USER_ID);
        App.PRE_BOOK_ID = Long.parseLong(getUserRecord(0));
        bookRecord = StateRecordRepo.getStateMap(false, App.PRE_BOOK_ID);
        App.getInstance().getADC().initObserve();
    }

    /**
     * 创建新用户，不保证是否存在同名用户
     */
    public void createUser(String userName, String userPwd) {
        App.PRE_USER_ID = UserRepo.create(new User(userName, userPwd, false, true, ""));

        createBook("", App.PRE_USER_ID);

        userRecord = new HashMap<>();
        userRecord.put(USER_RECORD_KEY[0], String.valueOf(App.PRE_BOOK_ID));
        StateRecordRepo.create(new StateRecord(true, JSON.toJSONString(userRecord), App.PRE_USER_ID));
        App.getInstance().getADC().initObserve();
    }

    /**
     * 创建BookEntity
     */
    public void createBook(String bookName, long userId) {
        if (Objects.equals(bookName, "")) {
            bookName = App.getInstance().getResources().getString(R.string.default_book);
        }
        App.PRE_BOOK_ID = BookEntityRepo.create(new Book(bookName, userId));
        createBookData(App.PRE_BOOK_ID);
    }

    private void createBookData(long bookId) {
        ArrayList<TableTemplate> temp = new ArrayList<>();
        bookRecord = new HashMap<>();

        //创建BookEntity的状态记录容器
        bookRecord.put(BOOK_RECORD_KEY[0], "");
        bookRecord.put(BOOK_RECORD_KEY[1], "");
        bookRecord.put(BOOK_RECORD_KEY[2], "");
        bookRecord.put(BOOK_RECORD_KEY[3], "");
        bookRecord.put(BOOK_RECORD_KEY[4], "");
        bookRecord.put(BOOK_RECORD_KEY[5], "");
        StateRecordRepo.create(new StateRecord(false, JSON.toJSONString(bookRecord), bookId));

        //创建Character默认模板
        String charTempName = App.getInstance().getResources().getString(R.string.character_default_template_name);
        TableTemplate charEntity = new TableTemplate("字段一", 0, App.TABLE_OWNER[0], charTempName);
        Long charId = TableTemplateRepo.create(charEntity);
        charEntity.setItemId(charId);
        charEntity.setTableFlag(charId);

        temp.add(charEntity);
        temp.add(new TableTemplate("字段二", 1, App.TABLE_OWNER[0], charId, charTempName));
        temp.add(new TableTemplate("字段三", 2, App.TABLE_OWNER[0], charId, charTempName));
        temp.add(new TableTemplate("字段四", 3, App.TABLE_OWNER[0], charId, charTempName));
        temp.add(new TableTemplate("字段五", 4, App.TABLE_OWNER[0], charId, charTempName));

        //创建Resource默认模板
        String resoTempName = App.getInstance().getResources().getString(R.string.resource_default_template_name);
        TableTemplate resoEntity = new TableTemplate("字段一", 0, App.TABLE_OWNER[1], resoTempName);
        Long resoId = TableTemplateRepo.create(resoEntity);
        resoEntity.setItemId(resoId);
        resoEntity.setTableFlag(resoId);

        temp.add(resoEntity);
        temp.add(new TableTemplate("字段二", 1, App.TABLE_OWNER[1], resoId, resoTempName));
        temp.add(new TableTemplate("字段三", 2, App.TABLE_OWNER[1], resoId, resoTempName));
        temp.add(new TableTemplate("字段四", 3, App.TABLE_OWNER[1], resoId, resoTempName));
        temp.add(new TableTemplate("字段五", 4, App.TABLE_OWNER[1], resoId, resoTempName));
        TableTemplateRepo.create(temp);
    }

    /**
     * 获取BookEntity索引位置的配置值
     */
    public String getBookRecord(int index) {
        return bookRecord.get(BOOK_RECORD_KEY[index]);
    }

    /**
     * 获取UserEntity索引位置的配置值
     */
    public String getUserRecord(int index) {
        return userRecord.get(USER_RECORD_KEY[index]);
    }

    /**
     * 设置BookEntity索引位置的配置值
     */
    public synchronized void setBookRecord(int index, String value) {
        bookRecord.put(BOOK_RECORD_KEY[index], value);
    }

    /**
     * 设置UserEntity索引位置的配置值
     */
    public synchronized void setUserRecord(int index, String value) {
        userRecord.put(USER_RECORD_KEY[index], value);
    }

    /**
     * 保存BookEntity配置容器
     */
    public void saveBookRecord() {
        StateRecordRepo.setStateMap(bookRecord);
    }

    /**
     * 保存UserEntity配置容器
     */
    public void saveUserRecord() {
        StateRecordRepo.setStateMap(userRecord);
    }
}
