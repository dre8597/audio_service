package com.ryanheise.audioservice;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;

public class MediaButtonReceiver extends androidx.media.session.MediaButtonReceiver {
    public static final String ACTION_NOTIFICATION_DELETE = "com.ryanheise.audioservice.intent.action.ACTION_NOTIFICATION_DELETE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null
                && ACTION_NOTIFICATION_DELETE.equals(intent.getAction())
                && AudioService.instance != null) {
            AudioService.instance.handleDeleteNotification();
            return;
        }
        // Added as an attempt to enable searching by voice using Google Assistant.
        if (intent != null && intent.getAction().equals(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH)) {
            String query = intent.getStringExtra(MediaStore.EXTRA_MEDIA_FOCUS);
            AudioService.listener.onPlayFromSearch(query, intent.getExtras());
            return;
        }
        super.onReceive(context, intent);
    }
}
