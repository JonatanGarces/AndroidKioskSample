package com.curzar.androidkiosksample.model;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;

import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.curzar.androidkiosksample.model.Setting;

import java.util.List;

@Dao
public interface SettingDao {

    @Query("SELECT * FROM setting ORDER BY ID")
    List<Setting> loadAllPersons();

    @Query("SELECT * FROM setting ORDER BY name ASC")
    LiveData<List<Setting>> getAlphabetizedSettings();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Setting... setting);

    @Update
    void update(Setting setting);

    @Delete
    void delete(Setting setting);

    @Query("SELECT * FROM setting WHERE id = :id")
    Setting loadSettingById(int id);

    @Query("UPDATE setting  set value =:value WHERE name = :name")
    void updateByName(String name,String value);

    @Query("SELECT * FROM setting WHERE name = :name")
    Setting loadSettingByName(String name);

    @Query("DELETE FROM setting")
    void deleteAll();

}
