package com.ryanheise.audioservice;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ryanheise.audioservice.AudioService.ServiceListener;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;

public class AudioServiceActivity extends FlutterActivity {
    @Override
    public FlutterEngine provideFlutterEngine(@NonNull Context context) {
        return AudioServicePlugin.getFlutterEngine(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        final ServiceListener listener = AudioService.listener;

        if (intent.getAction().compareTo(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH) == 0) {
            String mediaFocus = intent.getStringExtra(MediaStore.EXTRA_MEDIA_FOCUS);
            String query = intent.getStringExtra(SearchManager.QUERY);

            if (mediaFocus == null) {
                // 'Unstructured' search mode (backward compatible)
                listener.onPlayFromSearch(query, intent.getExtras());
            } else {
                if (!query.isEmpty()) {
                    // 'Unstructured' search mode
                    listener.onPlayFromSearch(query, intent.getExtras());
                } else {
                    listener.onPlay();
                }
            }
        }
    }
}

