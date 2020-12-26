/*
 * Copyright (C)  2017-2018 Tran Le Duy
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.duy.text.converter.pro.notification;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.duy.text.converter.R;
import com.duy.text.converter.pro.floating.codec.FloatingCodecOpenShortcutActivity;
import com.duy.text.converter.pro.floating.stylish.FloatingStylishOpenShortcutActivity;

import static com.duy.common.preferences.PreferencesUtil.bindPreferenceSummaryToValue;

/**
 * This fragment shows notification preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 */
public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences
        // to their values. When their values change, their summaries are
        // updated to reflect the new value, per the Android Design
        // guidelines.
        bindPreferenceSummaryToValue(findPreference("pref_key_encode_menu"));

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_theme)));
        addEvent();
    }

    private void addEvent() {
        findPreference("open_floating_codec").setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(getActivity(), FloatingCodecOpenShortcutActivity.class));
            return false;
        });

        findPreference("open_floating_stylish").setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(getActivity(), FloatingStylishOpenShortcutActivity.class));
            return false;
        });
    }

}
