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

package com.duy.text.converter.fragments;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.duy.text.converter.R;
import com.duy.text.converter.activities.MainActivity;
import com.duy.text.converter.core.hashfunction.IHash;
import com.duy.text.converter.core.hashfunction.Md5Hash;
import com.duy.text.converter.core.hashfunction.Sha1Hash;
import com.duy.text.converter.core.hashfunction.Sha256Hash;
import com.duy.text.converter.core.hashfunction.Sha384Hash;
import com.duy.text.converter.core.hashfunction.Sha512Hash;
import com.duy.text.converter.core.hashfunction.UnixCryptHash;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by Duy on 2/10/2018.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HashFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> mRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void selectCodecFragment() {
        onView(withContentDescription(mRule.getActivity().getString(R.string.desc_open_drawer)))
                .perform(click());
        onView(withText(mRule.getActivity().getString(R.string.tab_title_hash_function)))
                .perform(click());
    }

    @Test
    public void Md5HashFunction_encode() throws InterruptedException {
        testEncode(new Md5Hash(), "Md5HashFunction");
    }

    @Test
    public void Sha1HashFunction_encode() throws InterruptedException {
        testEncode(new Sha1Hash(), "Sha1HashFunction");
    }

    @Test
    public void Sha256HashFunction_encode() throws InterruptedException {
        testEncode(new Sha256Hash(), "Sha256HashFunction");
    }

    @Test
    public void Sha384HashFunction_encode() throws InterruptedException {
        testEncode(new Sha384Hash(), "Sha384HashFunction");
    }

    @Test
    public void Sha512HashFunction_encode() throws InterruptedException {
        testEncode(new Sha512Hash(), "Sha512HashFunction");
    }

    @Test
    public void UnixCryptHashFunction_encode() throws InterruptedException {
        testEncode(new UnixCryptHash(), "UnixCryptHashFunction");
    }

    private void testEncode(IHash codec, String stringToBeType) throws InterruptedException {
        String selectionText = codec.getName();
        onView(allOf(withId(R.id.spinner_hash_methods), isDisplayed())).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(selectionText))).perform(click());
        onView(allOf(withId(R.id.spinner_hash_methods), isDisplayed())).check(matches(withSpinnerText(selectionText)));

        onView(allOf(withId(R.id.edit_input), isDisplayed()))
                .perform(click())
                .perform(clearText())
                .perform(typeText(stringToBeType))
                .perform(closeSoftKeyboard());

        onView(allOf(withId(R.id.edit_output), isDisplayed()))
                .check(matches(withText(codec.encode(stringToBeType))));
    }

}
