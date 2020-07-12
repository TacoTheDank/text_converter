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

package com.duy.text.converter.barcode;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.duy.text.converter.R;
import com.duy.text.converter.activities.base.BaseActivity;
import com.google.android.material.tabs.TabLayout;

/**
 * Created by Duy on 14-Aug-17.
 */
public class BarcodeEncodedActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.tab_title_barcode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String text = getIntent().getStringExtra("data");
        if (text == null) text = "";
        toolbar.setSubtitle(text);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new BarcodeEncodedPagerAdapter(getSupportFragmentManager(), text));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(8); //barcode
        ((TabLayout) findViewById(R.id.tab_layout)).setupWithViewPager(viewPager);
    }


}
