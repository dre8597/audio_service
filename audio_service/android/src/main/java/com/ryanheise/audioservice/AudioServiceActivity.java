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
            // Some of these extras may not be available depending on the search mode
            @Nullable String album = intent.getStringExtra(MediaStore.EXTRA_MEDIA_ALBUM);
            @Nullable String artist = intent.getStringExtra(MediaStore.EXTRA_MEDIA_ARTIST);
            @Nullable String genre = intent.getStringExtra("android.intent.extra.genre");
            @Nullable String playlist = intent.getStringExtra("android.intent.extra.playlist");
            @Nullable String title = intent.getStringExtra(MediaStore.EXTRA_MEDIA_TITLE);
            // Determine the search mode and use the corresponding extras


            if (mediaFocus == null) {
                // 'Unstructured' search mode (backward compatible)
                listener.onPlayFromSearch(query, savedInstanceState);
            } else if (mediaFocus.compareTo("vnd.android.cursor.item/*") == 0) {
                if (!query.isEmpty()) {
                    // 'Unstructured' search mode
                    listener.onPlayFromSearch(query, savedInstanceState);
                } else {
                    listener.onPlay();
                }
            } else if (
                    mediaFocus.compareTo(MediaStore.Audio.Genres.ENTRY_CONTENT_TYPE) == 0) {
                // 'Genre' search mode
                listener.onPlayFromSearch(query, savedInstanceState);
            } else if (mediaFocus.compareTo(MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE) == 0) {
                // 'Artist' search mode
                listener.onPlayFromSearch(query, savedInstanceState);
            } else if (mediaFocus.compareTo(MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE) == 0) {
                // 'Album' search mode
                listener.onPlayFromSearch(query, savedInstanceState);
            } else if (mediaFocus.compareTo("vnd.android.cursor.item/audio") == 0) {
                // 'Song' search mode
                listener.onPlayFromSearch(query, savedInstanceState);
            }
        }
    }
}

