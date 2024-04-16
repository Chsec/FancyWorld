package com.chw.application.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.chw.application.App;
import com.chw.application.model.dao.*;
import com.chw.application.model.table.*;

@Database(entities = {
        User.class,
        Book.class,
        TableTemplate.class,
        TCharacterData.class,
        TreeTemplate.class,
        TCharacter.class,
        Resource.class,
        ResourceData.class,
        Setting.class,
        SettingSort.class,
        StateRecord.class}, version = 2)
public abstract class ApplicationDataBase extends RoomDatabase {

    private static final String DA_NAME = "test.db";

    private static volatile ApplicationDataBase appDB;

    public static ApplicationDataBase getInstance() {
        if (appDB == null) {
            synchronized (ApplicationDataBase.class) {
                if (appDB == null) {
                    appDB = Room.databaseBuilder(App.getInstance().getApplicationContext(), ApplicationDataBase.class, DA_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return appDB;
    }

    public abstract UserDao userDao();

    public abstract BookDao bookDao();

    public abstract TableTemplateDao tableTemplateDao();

    public abstract TreeTemplateDao treeTemplateDao();

    public abstract CharacterDao characterDao();

    public abstract ResourceDao resourceDao();

    public abstract SetterDao settingDao();

    public abstract StateRecordDao appConfigDao();

}
