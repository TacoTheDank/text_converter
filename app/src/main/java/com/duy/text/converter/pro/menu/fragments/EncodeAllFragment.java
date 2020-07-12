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

package com.duy.text.converter.pro.menu.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duy.text.converter.R;
import com.duy.text.converter.adapters.EncodeResultAdapter;
import com.duy.text.converter.core.codec.interfaces.Codec;
import com.duy.text.converter.core.codec.interfaces.CodecMethod;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Duy on 16-Dec-17.
 */

public class EncodeAllFragment extends Fragment {
    private static final String KEY_INPUT = "KEY_INPUT";
    private static final String KEY_PROCESS_TEXT = "KEY_PROCESS_TEXT";
    @Nullable
    private EncodeTask mEncodeTask;
    private ArrayList<Codec> mEncoders;
    private EncodeResultAdapter mEncodeResultAdapter;

    public static EncodeAllFragment newInstance(String input, boolean processText) {

        Bundle args = new Bundle();
        args.putString(KEY_INPUT, input);
        args.putBoolean(KEY_PROCESS_TEXT, processText);
        EncodeAllFragment fragment = new EncodeAllFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_decode_all, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String input = getArguments().getString(KEY_INPUT);
        boolean processText = getArguments().getBoolean(KEY_PROCESS_TEXT);


        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mEncodeResultAdapter = new EncodeResultAdapter(getContext(), processText);
        if (processText && getActivity() instanceof OnTextSelectedListener) {
            mEncodeResultAdapter.setListener((OnTextSelectedListener) getActivity());
        }
        recyclerView.setAdapter(mEncodeResultAdapter);

        initCodec();
        generateResult(input);
    }

    private void generateResult(String input) {
        if (mEncodeTask != null) mEncodeTask.cancel(true);
        mEncodeTask = new EncodeTask();
        mEncodeTask.execute(input);
    }

    @Override
    public void onDestroyView() {
        if (mEncodeTask != null) mEncodeTask.cancel(true);
        super.onDestroyView();
    }

    private void initCodec() {
        mEncoders = new ArrayList<>();
        CodecMethod[] values = CodecMethod.values();
        for (CodecMethod value : values) mEncoders.add(value.getCodec());
    }


    private class EncodeTask extends AsyncTask<String, Object, Void> {
        private static final String TAG = "EncodeTask";

        EncodeTask() {
        }

        @Override
        protected Void doInBackground(String... strings) {
            String input = strings[0];
            for (int i = 0, mDecodersSize = mEncoders.size(); i < mDecodersSize; i++) {
                Codec codec = mEncoders.get(i);
                if (isCancelled()) return null;
                String decode = codec.encode(input);
                publishProgress(decode, codec.getName(getContext()));
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate() called with: values = [" + Arrays.toString(values) + "]");
            String result = (String) values[0];
            String name = (String) values[1];
            addToRecycleView(result, name);
        }

        private void addToRecycleView(String result, String name) {
            if (isCancelled()) return;
            EncodeResultAdapter.EncodeItem item = new EncodeResultAdapter.EncodeItem(name, result);
            mEncodeResultAdapter.add(item);
        }
    }
}
