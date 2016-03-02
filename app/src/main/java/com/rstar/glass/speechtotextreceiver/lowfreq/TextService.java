/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rstar.glass.speechtotextreceiver.lowfreq;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.google.android.glass.timeline.LiveCard;
import com.rstar.glass.speechtotextreceiver.AppSettings;
import com.rstar.glass.speechtotextreceiver.font.Fontsize;
import com.rstar.glass.speechtotextreceiver.R;
import com.rstar.glass.speechtotextreceiver.watson.Credentials;
import com.rstar.glass.speechtotextreceiver.watson.WatsonConnector;

/**
 * Service owning the LiveCard living in the timeline.
 */
public class TextService extends Service implements WatsonConnector.Frontend {

    private static final String LIVE_CARD_TAG = "LectureTranscriber";

    private WatsonConnector connector = null;

    private LiveCard mLiveCard;
    RemoteViews mLiveCardView;
    String data = "Waiting for data...";
    int fontsize = Fontsize.list[Fontsize.DefaultChoice];
    private int charLimit = Fontsize.limit[Fontsize.DefaultChoice];

    private Handler mHandler = new Handler();

    private final Runnable mUpdateLiveCardRunnable = new Runnable() {
        @Override
        public void run() {
            if (mLiveCard!=null) {
                // Update the remote view with the new data.
                mLiveCardView = getRemoteViews(data, fontsize);
                // Always call setViews() to update the live card's RemoteViews.
                mLiveCard.setViews(mLiveCardView);
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        fontsize = Fontsize.get(this);
        charLimit = Fontsize.getCharLimit(fontsize);
        Credentials credentials = new Credentials(this.getApplicationContext());
        try {
            connector = new WatsonConnector(AppSettings.host, false, this, credentials.toJSON());
            connector.prepare();
        } catch (Exception e) {
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLiveCard == null) {
            // Get an instance of a live card
            mLiveCard = new LiveCard(this, LIVE_CARD_TAG);

            mLiveCardView = getRemoteViews(data, fontsize);

            // Set up the live card's action with a pending intent
            // to show a menu when tapped
            Intent menuIntent = new Intent(this, TranscribeMenuActivity.class);
            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mLiveCard.setAction(PendingIntent.getActivity(
                    this, 0, menuIntent, 0));

            // Publish the live card
            mLiveCard.publish(LiveCard.PublishMode.REVEAL);

            // Queue the update text runnable
            mHandler.post(mUpdateLiveCardRunnable);
        }

        // Return START_NOT_STICKY to prevent the system from restarting the service if it is killed
        // (e.g., due to an error). It doesn't make sense to restart automatically because the
        // transcriber state will have been lost.
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
            mLiveCard.unpublish();
            mLiveCard = null;
        }
        if (connector!=null) {
            connector.stop();
            connector.close();
        }
        super.onDestroy();
    }


    private RemoteViews getRemoteViews(String data, int fontsize) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.card_transcript);
        remoteViews.setTextViewText(R.id.transcript, data);
        remoteViews.setTextViewTextSize(R.id.transcript, TypedValue.COMPLEX_UNIT_SP, fontsize);
        return remoteViews;
    }



    @Override
    public void showData(String data) {
        // Restart immediately to get a refreshed score
        if (data.length()>charLimit) {
            int trimAt = data.substring(0, data.length()-charLimit).lastIndexOf(' ');
            this.data = data.substring(trimAt+1);
        }
        else {
            this.data = data;
        }
        mHandler.postDelayed(mUpdateLiveCardRunnable, 0);
    }

    @Override
    public void showStatus(String message) {
        // Choose not to show status message on screen
    }

    @Override
    public void showConnection(boolean connected) {
        // Choose not to show connection status now.
    }
}
