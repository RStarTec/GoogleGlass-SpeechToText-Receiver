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


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Fontsize {
    private static final String PREF_fontsize = Fontsize.class.getSimpleName() + "_fontsize";
    private static final int Large = 64;
    private static final int Medium = 48;
    private static final int Small = 32;
    private static final int Large_char_limit = 30;
    private static final int Medium_char_limit = 60;
    private static final int Small_char_limit = 100;

    public static final int[] list = {Large, Medium, Small};
    public static final int[] limit = {Large_char_limit, Medium_char_limit, Small_char_limit};
    public static final int DefaultChoice = 0;


    public static int get(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(PREF_fontsize, list[DefaultChoice]);
    }

    public static void set(Context context, int listPosition) {
        if (listPosition<0 || listPosition>=list.length) return;
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(PREF_fontsize, list[listPosition]).commit();
    }

    public static int getCharLimit(int fontsize) {
        if (fontsize==Large) return Large_char_limit;
        else if (fontsize==Medium) return Medium_char_limit;
        else if (fontsize==Small) return Small_char_limit;
        return Large_char_limit;  // default limit
    }
}
