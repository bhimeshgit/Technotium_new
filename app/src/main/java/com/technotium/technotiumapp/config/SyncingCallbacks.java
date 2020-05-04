package com.technotium.technotiumapp.config;

public interface SyncingCallbacks {
    int SYNC_SUCCESFULL = 1;
    int SYNC_FAILED = 2;

    void onServerResponse(int serverStatus);

    void uniqueIdsForPullingData(Object[] uniqueKeyUpdates);
}