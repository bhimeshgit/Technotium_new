package com.technotium.technotiumapp.db.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.technotium.technotiumapp.db.dao.DocsDao;
import com.technotium.technotiumapp.db.entities.Docs;

@Database(entities = {Docs.class},version  =1)
public abstract class TechnotiumDatabase extends RoomDatabase {

    public abstract DocsDao getDocsDAO();
    private static TechnotiumDatabase mDB;
    private static String DB_NAME="technotiumDB";
    private static final int newDbVersion=1;
    private static final int lastDbVersion=1;

    public static TechnotiumDatabase getInstance(Context context){
        if(mDB==null){
            synchronized(TechnotiumDatabase.class){
                mDB= Room.databaseBuilder(context,TechnotiumDatabase.class,DB_NAME).allowMainThreadQueries().addMigrations(migration).addCallback(callback).build();
            }
        }
        return mDB;
    }
    static Migration migration=new Migration(newDbVersion,lastDbVersion) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    static RoomDatabase.Callback callback=new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

}
