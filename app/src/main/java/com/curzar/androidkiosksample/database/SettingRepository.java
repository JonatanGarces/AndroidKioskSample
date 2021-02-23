package com.curzar.androidkiosksample.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.curzar.androidkiosksample.model.Setting;
import com.curzar.androidkiosksample.model.SettingDao;
import com.curzar.androidkiosksample.model.SettingRoomDatabase;

import java.util.List;

public class SettingRepository {
    private SettingDao mSettingDao;
    private LiveData<List<Setting>> mAllSettings;
    private Setting setting;

    public SettingRepository(Application application){
        SettingRoomDatabase db = SettingRoomDatabase.getDatabase(application);
        mSettingDao = db.settingDao();
        mAllSettings = mSettingDao.getAlphabetizedSettings();

    }
    public LiveData<List<Setting>> getAllSettings() {
        return mAllSettings;
    }
    void insert(Setting setting) {
        SettingRoomDatabase.databaseWriteExecutor.execute(() -> {
            mSettingDao.insert(setting);
        });
    }

    public void updateByName(String name,String value) {
        SettingRoomDatabase.databaseWriteExecutor.execute(() -> {
            mSettingDao.updateByName(name,value);
        });
    }

    Setting loadSettingByName(String name) {
        setting = mSettingDao.loadSettingByName(name);
        return setting;
    }

    public void update(Setting row) {
        SettingRoomDatabase.databaseWriteExecutor.execute(() -> {
            mSettingDao.update(row);
        });
    }

    public void updatebyname(Setting row) {
        SettingRoomDatabase.databaseWriteExecutor.execute(() -> {
            mSettingDao.update(row);
        });
    }
}
