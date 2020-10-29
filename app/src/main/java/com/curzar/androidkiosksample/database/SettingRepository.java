package com.curzar.androidkiosksample.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.curzar.androidkiosksample.model.Setting;
import com.curzar.androidkiosksample.model.SettingDao;

import java.util.List;

public class SettingRepository {
    private SettingDao mSettingDao;
    private LiveData<List<Setting>> mAllSettings;

    SettingRepository(Application application){
        SettingRoomDatabase db = SettingRoomDatabase.getDatabase(application);
        mSettingDao = db.settingDao();
        mAllSettings = mSettingDao.getAlphabetizedSettings();
    }
    LiveData<List<Setting>> getAllSettings() {
        return mAllSettings;
    }
    void insert(Setting setting) {
        SettingRoomDatabase.databaseWriteExecutor.execute(() -> {
            mSettingDao.insert(setting);
        });
    }

    public void update(Setting row) {
        SettingRoomDatabase.databaseWriteExecutor.execute(() -> {
            mSettingDao.update(row);
        });
    }
}
