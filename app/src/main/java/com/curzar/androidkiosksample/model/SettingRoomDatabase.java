package com.curzar.androidkiosksample.model;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Setting.class}, version = 1, exportSchema = false)

public abstract class SettingRoomDatabase extends RoomDatabase {

    public abstract SettingDao settingDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile SettingRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static SettingRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SettingRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SettingRoomDatabase.class, "setting_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onCreate method to populate the database.
     * For this sample, we clear the database every time it is created.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                SettingDao dao = INSTANCE.settingDao();
                dao.deleteAll();

                Setting setting = new Setting("email","");
                dao.insert(setting);
                setting = new Setting("email","");
                dao.insert(setting);
                setting = new Setting("password","");
                dao.insert(setting);
                setting = new Setting("currency","");
                dao.insert(setting);
                setting = new Setting("device","");
                dao.insert(setting);
                setting = new Setting("timeperunitofcurrency","");
                dao.insert(setting);
            });
        }
    };


}
