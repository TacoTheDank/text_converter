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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.text.converter.R;
import com.duy.text.converter.adapters.DecodeResultAdapter;
import com.duy.text.converter.core.codec.interfaces.Codec;
import com.duy.text.converter.core.codec.interfaces.CodecMethod;
import com.duy.text.converter.pro.menu.DecodeAllProcessTextActivity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Duy on 11/13/2017.
 */

public class DecodeAllFragment extends Fragment {
    private static final String KEY_INPUT = "KEY_INPUT";
    private static final String KEY_PROCESS_TEXT = "KEY_PROCESS_TEXT";
    @Nullable
    private DecodeTask mDecodeTask;
    private ArrayList<Codec> mDecoders;
    private DecodeResultAdapter mDecodeResultAdapter;

    /**
     * @param input       - input data
     * @param processText - start from {@link DecodeAllProcessTextActivity}
     * @return DecodeAllFragment
     */
    public static DecodeAllFragment newInstance(String input, boolean processText) {

        Bundle args = new Bundle();
        args.putString(KEY_INPUT, input);
        args.putBoolean(KEY_PROCESS_TEXT, processText);
        DecodeAllFragment fragment = new DecodeAllFragment();
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
        mDecodeResultAdapter = new DecodeResultAdapter(getContext(), processText);
        if (processText && getActivity() instanceof OnTextSelectedListener) {
            mDecodeResultAdapter.setListener((OnTextSelectedListener) getActivity());
        }
        recyclerView.setAdapter(mDecodeResultAdapter);

        initCodec();
        generateResult(input);
    }

    private void generateResult(String input) {
        if (mDecodeTask != null) mDecodeTask.cancel(true);
        mDecodeTask = new DecodeTask();
        mDecodeTask.execute(input);
    }

    @Override
    public void onDestroyView() {
        if (mDecodeTask != null) mDecodeTask.cancel(true);
        super.onDestroyView();
    }

    private void initCodec() {
        mDecoders = new ArrayList<>();
        CodecMethod[] values = CodecMethod.values();
        for (CodecMethod value : values) mDecoders.add(value.getCodec());
    }


    private class DecodeTask extends AsyncTask<String, Object, Void> {
        private static final String TAG = "EncodeTask";

        DecodeTask() {
        }

        @Override
        protected Void doInBackground(String... strings) {
            String input = strings[0];
            for (int i = 0, mDecodersSize = mDecoders.size(); i < mDecodersSize; i++) {
                Codec codec = mDecoders.get(i);
                if (isCancelled()) return null;
                String decode = codec.decode(input);
                publishProgress(decode, codec.getName(getContext()), codec.getMax(), codec.getConfident());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate() called with: values = [" + Arrays.toString(values) + "]");
            String result = (String) values[0];
            String name = (String) values[1];
            int max = (int) values[2];
            int confident = (int) values[3];
            addToRecycleView(result, name, max, confident);
        }

        private void addToRecycleView(String result, String name, int max, int confident) {
            if (isCancelled()) return;
            DecodeResultAdapter.DecodeItem item = new DecodeResultAdapter.DecodeItem(name, result);
            item.setConfident(confident);
            item.setMax(max);
            mDecodeResultAdapter.add(item);
        }
    }
}
