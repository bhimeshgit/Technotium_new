package com.technotium.technotiumapp.config;

import android.content.Context;
import android.content.SharedPreferences;

public class SyncSharePref {
    private static final String KEY_SYNC_SHARE_PREF = "SyncSharePref";
    private static final int PRIVATE_MODE = 0;

    public static final int VALUE_NOT_FIRST_TIME_SYNC_FLAG = 1;
    private static final String KEY_SYNC_FIRST_TIME_DOCS_FLAG = "SyncFirstTimeDocsFlag";
    private static final String KEY_LAST_MODIFIED_DATE_TIME_DOCS = "ModifiedDateTimeDocs";

    public static long getModifiedDateTimeDocs(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(KEY_SYNC_SHARE_PREF, PRIVATE_MODE);
        long epoch = sharedPref.getLong(KEY_LAST_MODIFIED_DATE_TIME_DOCS, 0);
        return  epoch;
    }

    public static void setModifiedDateTimeDocs(Context context, long epoch) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SYNC_SHARE_PREF, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_LAST_MODIFIED_DATE_TIME_DOCS, epoch);
        editor.commit();
    }

    public static int getSyncFirstTimeDocs(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(KEY_SYNC_SHARE_PREF, PRIVATE_MODE);
        int flag = sharedPref.getInt(KEY_SYNC_FIRST_TIME_DOCS_FLAG, 0);
        return  flag;
    }

    public static void setSyncFirstTimeDocs(Context context, int flag) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SYNC_SHARE_PREF, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_SYNC_FIRST_TIME_DOCS_FLAG, flag);
        editor.commit();
    }
}
