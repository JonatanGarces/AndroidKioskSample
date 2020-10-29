package com.curzar.androidkiosksample.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.curzar.androidkiosksample.model.Setting;

import java.util.List;

public class SettingViewModel extends AndroidViewModel {
    private SettingRepository mRepository;

    private final LiveData<List<Setting>> mAllSettings;

    private SettingViewModel(Application application){
       super(application);
       mRepository = new SettingRepository(application);
       mAllSettings =  mRepository.getAllSettings();
    }
    LiveData<List<Setting>> getAllSettings() {
        return mAllSettings;
    }

    void insert(Setting setting) {
        mRepository.insert(setting);
    }
}
