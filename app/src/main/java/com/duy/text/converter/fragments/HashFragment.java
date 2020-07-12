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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.duy.common.utils.ShareUtil;
import com.duy.text.converter.R;
import com.duy.text.converter.clipboard.ClipboardUtil;
import com.duy.text.converter.core.hashfunction.IHash;
import com.duy.text.converter.core.hashfunction.Md5Hash;
import com.duy.text.converter.core.hashfunction.Sha1Hash;
import com.duy.text.converter.core.hashfunction.Sha256Hash;
import com.duy.text.converter.core.hashfunction.Sha384Hash;
import com.duy.text.converter.core.hashfunction.Sha512Hash;
import com.duy.text.converter.core.hashfunction.UnixCryptHash;
import com.duy.text.converter.view.BaseEditText;
import com.duy.text.converter.view.RoundedBackgroundEditText;

import java.util.ArrayList;

/**
 * Created by Duy on 08-Aug-17.
 */

public class HashFragment extends Fragment {
    private static final String TAG = "HashFragment";
    private final Handler mHandler = new Handler();
    private ArrayList<IHash> mHashFunctions = new ArrayList<>();
    private BaseEditText mInput, mOutput;
    private Spinner mMethodSpinner;
    private final Runnable convertRunnable = this::convert;
    private TextWatcher mInputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mHandler.removeCallbacks(convertRunnable);
            mHandler.postDelayed(convertRunnable, 300);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public static HashFragment newInstance() {
        Bundle args = new Bundle();
        HashFragment fragment = new HashFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHashFunctions.clear();
        mHashFunctions.add(new Md5Hash());
        mHashFunctions.add(new Sha1Hash());
        mHashFunctions.add(new Sha256Hash());
        mHashFunctions.add(new Sha384Hash());
        mHashFunctions.add(new Sha512Hash());
        mHashFunctions.add(new UnixCryptHash());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHashFunctions.clear();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mInput = view.findViewById(R.id.edit_input);
        mOutput = view.findViewById(R.id.edit_output);
        mInput.addTextChangedListener(mInputWatcher);

        mMethodSpinner = view.findViewById(R.id.spinner_hash_methods);
        mMethodSpinner.setBackgroundDrawable(RoundedBackgroundEditText.createRoundedBackground(getContext()));
        view.findViewById(R.id.img_share_out).setOnClickListener(v ->
                ShareUtil.shareText(getContext(), mOutput.getText().toString()));
        view.findViewById(R.id.img_copy_out).setOnClickListener(v ->
                ClipboardUtil.setClipboard(getContext(), mOutput.getText().toString()));

        ArrayList<String> names = new ArrayList<>();
        for (IHash mHashFunction : mHashFunctions) {
            names.add(mHashFunction.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, names);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mMethodSpinner.setAdapter(adapter);
        mMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                convert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void convert() {
        int index = mMethodSpinner.getSelectedItemPosition();
        try {
            IHash hashFunction = mHashFunctions.get(index);
            String encoded = hashFunction.encode(mInput.getText().toString());
            mOutput.setText(encoded);
        } catch (Throwable e) { //out of memory
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        save();
    }

    @Override
    public void onResume() {
        super.onResume();
        restore();
    }

    public void save() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.edit().putString(TAG, mInput.getText().toString()).apply();
    }

    public void restore() {
        String text = getArguments().getString(Intent.EXTRA_TEXT, "");
        if (!text.isEmpty()) {
            mInput.setText(text);
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            mInput.setText(sharedPreferences.getString(TAG, ""));
        }
        convert();
    }


}
