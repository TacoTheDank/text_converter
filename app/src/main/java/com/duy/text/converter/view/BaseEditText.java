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

package com.duy.text.converter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.duy.text.converter.R;

/**
 * Created by Duy on 23-Mar-17.
 * full support unicode
 */
public class BaseEditText extends AppCompatEditText {
    public BaseEditText(Context context) {
        super(context);
        setup(context);

    }

    public BaseEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);

    }

    public BaseEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
    }

    private void setup(Context context) {
        setSaveEnabled(false);
//        AssetManager assetManager = context.getAssets();
//        Typeface typeface = Typeface.createFromAsset(assetManager, "fonts/Roboto-Regular.ttf");
//        setTypeface(typeface);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        try {
            super.setText(text, type);
        } catch (Throwable e) {
            e.printStackTrace();
            setError(getContext().getString(R.string.message_out_of_memory));
            Toast.makeText(getContext(), R.string.message_out_of_memory, Toast.LENGTH_SHORT).show();
        }
    }

}
