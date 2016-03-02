

/*
 * Copyright (c) 2015,2016 Annie Hui @ RStar Technology Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rstar.glass.speechtotextreceiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.rstar.glass.speechtotextreceiver.font.FontsizeActivity;
import com.rstar.glass.speechtotextreceiver.lowfreq.TextService;
import com.rstar.glass.speechtotextreceiver.utils.Savelog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final boolean debug = AppSettings.defaultDebug;

    private static final int Fn_transcribe = 0;
    private static final int Fn_fontsize = 1;

    private CardScrollAdapter mAdapter;
    private CardScrollView mCardScroller;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();
        cards.add(Fn_transcribe, new CardBuilder(this, CardBuilder.Layout.MENU).setText(R.string.start));
        cards.add(Fn_fontsize, new CardBuilder(this, CardBuilder.Layout.MENU).setText(R.string.fontsize));

        mAdapter = new MenuCardAdapter(cards);
        mCardScroller = new CardScrollView(this);
        mCardScroller.setAdapter(mAdapter);
        setContentView(mCardScroller);
        setCardScrollerListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();
    }

    @Override
    protected void onPause() {
        mCardScroller.deactivate();
        super.onPause();
    }

    private void setCardScrollerListener() {
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Savelog.d(TAG, debug, "Clicked view at position " + position + ", row-id " + id);
                int soundEffect = Sounds.TAP;
                switch (position) {
                    case Fn_transcribe:
                        startService(new Intent(MainActivity.this, TextService.class));
                        break;
                    case Fn_fontsize:
                        startActivity(new Intent(MainActivity.this, FontsizeActivity.class));
                        break;
                    default:
                        soundEffect = Sounds.ERROR;
                        Savelog.d(TAG, debug, "Don't show anything");
                }

                // Play sound.
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(soundEffect);
            }
        });
    }

    private class MenuCardAdapter extends CardScrollAdapter {

        final List<CardBuilder> mCards;

        public MenuCardAdapter(List<CardBuilder> cards) {
            mCards = cards;
        }

        @Override
        public int getCount() {
            return mCards.size();
        }

        @Override
        public Object getItem(int position) {
            return mCards.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return mCards.get(position).getView(convertView, parent);
        }

        @Override
        public int getViewTypeCount() {
            return CardBuilder.getViewTypeCount();
        }

        @Override
        public int getItemViewType(int position){
            return mCards.get(position).getItemViewType();
        }

        @Override
        public int getPosition(Object item) {
            for (int i = 0; i < mCards.size(); i++) {
                if (getItem(i).equals(item)) {
                    return i;
                }
            }
            return AdapterView.INVALID_POSITION;
        }
    }
}
