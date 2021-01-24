package com.curzar.androidkiosksample.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.curzar.androidkiosksample.model.Setting;

import java.util.List;

public class SettingViewModel extends AndroidViewModel {
    private SettingRepository mRepository;
    private final LiveData<List<Setting>> mAllSettings;

    // private final Setting setting;
//    public SettingViewModel(Application application, LiveData<com.curzar.androidkiosksample.model.Setting> setting){
    public SettingViewModel(Application application){
       super(application);
       mRepository = new SettingRepository(application);
        mAllSettings =  mRepository.getAllSettings();
    }

   public Setting loadSettingByName(String name) {
        return mRepository.loadSettingByName(name);
    }

    public LiveData<List<Setting>> getAllSettings() {
        return mAllSettings;
    }
    void insert(Setting setting) {
        mRepository.insert(setting);
    }
    public   void update(Setting setting) {
        mRepository.insert(setting);
    }
    public   void updateByName(String name,String value) {
        mRepository.updateByName(name,value);
    }

}
