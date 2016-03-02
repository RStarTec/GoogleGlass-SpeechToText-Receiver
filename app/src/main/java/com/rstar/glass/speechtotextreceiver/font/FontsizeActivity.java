

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

package com.rstar.glass.speechtotextreceiver.font;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.rstar.glass.speechtotextreceiver.AppSettings;
import com.rstar.glass.speechtotextreceiver.R;
import com.rstar.glass.speechtotextreceiver.utils.Savelog;

public class FontsizeActivity extends Activity {

    private static final String TAG = FontsizeActivity.class.getSimpleName();
    private static final boolean debug = AppSettings.defaultDebug;

    private static final String Text = "Tap if this is the text size of the transcript you want. Scroll forward or backward for more options. Scroll down to cancel selection.";
    private static final int[] fontlist = Fontsize.list;

    private CardScrollAdapter mAdapter;
    private CardScrollView mCardScroller;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mAdapter = new FontCardAdapter(this);
        mCardScroller = new CardScrollView(this);
        mCardScroller.setAdapter(mAdapter);
        setContentView(mCardScroller);
        setCardScrollerListener(this);
    }

    private class FontCardAdapter extends CardScrollAdapter {
        Context context;
        public FontCardAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return fontlist.length;
        }

        @Override
        public Object getItem(int i) {
            return fontlist[i];
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            CardBuilder card = new CardBuilder(context, CardBuilder.Layout.EMBED_INSIDE)
                    .setEmbeddedLayout(R.layout.card_transcript);
            View view = card.getView(convertView, parent);
            TextView transcriptView = (TextView) view.findViewById(R.id.transcript);
            transcriptView.setText(Text);
            transcriptView.setTextSize((float)fontlist[i]);
            Savelog.d(TAG, debug, "Set position " + i + " to size " + fontlist[i]);
            return view;
        }

        @Override
        public int getPosition(Object o) {
            return AdapterView.INVALID_POSITION;
        }
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

    private void setCardScrollerListener(final Context context) {
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Savelog.d(TAG, debug, "Clicked view at position " + position + ", row-id " + id);
                int soundEffect = Sounds.TAP;

                Fontsize.set(context, position);
                // Play sound.
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(soundEffect);

                finish();
            }
        });
    }

}
