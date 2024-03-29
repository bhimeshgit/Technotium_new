package com.technotium.technotiumapp.config.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

import com.technotium.technotiumapp.R;
import com.technotium.technotiumapp.config.ApplicationGlobal;
import com.technotium.technotiumapp.config.DateUtil;
import com.technotium.technotiumapp.config.SyncSharePref;
import com.technotium.technotiumapp.config.SyncingCallbacks;
import com.technotium.technotiumapp.config.Utils;
import com.technotium.technotiumapp.config.syncingEntities.PostDocsModule;
import com.technotium.technotiumapp.db.dao.DocsDao;
import com.technotium.technotiumapp.db.database.TechnotiumDatabase;
import com.technotium.technotiumapp.db.pojo.ModifiedMaxDate;

import java.util.Date;
import java.util.Locale;

import static com.technotium.technotiumapp.config.SyncSharePref.VALUE_NOT_FIRST_TIME_SYNC_FLAG;

public class SyncingService extends JobIntentService implements SyncingCallbacks {
    private static final int JOB_ID = 1;
    private static TechnotiumDatabase technotiumDatabase;
    private static DocsDao docsDao;
    private static Context context;
    private boolean isSyncFailed=false;
    private NotificationManager notificationManager;
    NotificationCompat.Builder notification;
    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static void enqueueWork(Context context, Intent intent) {
        technotiumDatabase=TechnotiumDatabase.getInstance(context);
        docsDao=technotiumDatabase.getDocsDAO();
        SyncingService.context=context;
        enqueueWork(context, SyncingService.class, JOB_ID, intent);
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        setLastModifiedDateTimeDocs();

        if(!isSyncFailed) {
            showNotification();
 //           notification.setProgress(100, 50, false);
            // Displays the progress bar for the first time.
            notificationManager.notify(0, notification.build());
            postDocsModule();
            notificationManager.cancel(0);
        }

    }

    private void setLastModifiedDateTimeDocs(){
        if (SyncSharePref.getSyncFirstTimeDocs(getApplicationContext()) == VALUE_NOT_FIRST_TIME_SYNC_FLAG) {
            if (SyncSharePref.getModifiedDateTimeDocs(getApplicationContext()) == 0) {
                 ModifiedMaxDate modifiedMaxDate = docsDao.getMaxModifiedDateTime();
                if (modifiedMaxDate.maxModifiedDate!=null) {
                    Date date = DateUtil.convertStringToDateTime(modifiedMaxDate.maxModifiedDate,
                            DateUtil.DATE_FORMAT_yyyy_MM_dd_HH_mm_ss_SSS, "UTC", Locale.ENGLISH);
                    long docsEpoch = date.getTime();
                    SyncSharePref.setModifiedDateTimeDocs(getApplicationContext(), docsEpoch);
                }
            }
        }
    }

    public static void startSyncing(Context context,Intent intent) {
        if(ApplicationGlobal.checkInternetConenction(context)){
            SyncingService.enqueueWork(context,intent);
        }
        else{
            Utils.showToastLongMsg(context,"Internet connection not available");
        }
    }

    private void postDocsModule() {
        PostDocsModule postDocsModule = new PostDocsModule(getApplicationContext(),docsDao,
                this);
        postDocsModule.getProductListForPost();
    }

    @Override
    public void onServerResponse(int serverStatus) {
        if(SyncingCallbacks.SYNC_FAILED==serverStatus){
            isSyncFailed=true;
        }
    }

    @Override
    public void uniqueIdsForPullingData(Object[] uniqueKeyUpdates) {

    }

    // Creates and displays a notification
    private void showNotification() {
        String channelId = "myNotificationChannel"; // Store channel ID as String or String resource
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(channelId , "Notify", NotificationManager.IMPORTANCE_HIGH);

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }else{
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        notification = new NotificationCompat.Builder(this, channelId) // Use  the same channelId String while creating notification
                .setContentTitle("Document upload is in progress")
                .setSmallIcon(R.mipmap.ic_launcher);



    }

}
