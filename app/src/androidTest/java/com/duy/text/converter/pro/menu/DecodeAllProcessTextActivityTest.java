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

package com.duy.text.converter.pro.menu;

import android.content.Intent;

import androidx.test.rule.ActivityTestRule;

import com.duy.text.converter.R;
import com.duy.text.converter.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.duy.text.converter.pro.CodecAllFragmentTest.hasData;


/**
 * Created by Duy on 2/18/2018.
 */
public class DecodeAllProcessTextActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void decode() {
        Intent intent = new Intent(mRule.getActivity(), DecodeAllProcessTextActivity.class);
        intent.putExtra(Intent.EXTRA_PROCESS_TEXT, "Hello android");
        mRule.getActivity().startActivity(intent);

        onView(withId(R.id.recycler_view))
                .check(matches(isDisplayed()))
                .check(matches(hasData()));
    }
}
