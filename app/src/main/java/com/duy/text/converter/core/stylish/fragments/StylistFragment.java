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

package com.duy.text.converter.core.stylish.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duy.text.converter.R;
import com.duy.text.converter.core.stylish.StylistGenerator;
import com.duy.text.converter.core.stylish.adapter.ItemTouchHelperCallback;
import com.duy.text.converter.core.stylish.adapter.OnSwapStyleListener;
import com.duy.text.converter.core.stylish.adapter.ResultAdapter;

import java.util.ArrayList;


/**
 * Created by DUy on 07-Feb-17.
 */

public class StylistFragment extends Fragment implements TextWatcher {
    public static final String KEY = "StylistFragment";
    private EditText mInput;
    private RecyclerView mListResult;
    private ResultAdapter mAdapter;
    private StylistGenerator mGenerator;

    public static StylistFragment newInstance() {
        StylistFragment fragment = new StylistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stylish, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGenerator = new StylistGenerator(getContext());

        mInput = view.findViewById(R.id.edit_input);


        mAdapter = new ResultAdapter(getActivity(), R.layout.list_item_style);
        mAdapter.setOnSwapStyleListener(new OnSwapStyleListener() {
            @Override
            public void onSwap(int fromPosition, int toPosition) {
                mGenerator.swapPosition(fromPosition, toPosition);
            }
        });

        mListResult = view.findViewById(R.id.recycler_view);
        mListResult.setLayoutManager(new LinearLayoutManager(getContext()));
        mListResult.setHasFixedSize(true);
        mListResult.setAdapter(mAdapter);
        mListResult.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mListResult);

        mInput.addTextChangedListener(this);

        restore();
    }

    public void convert(String inp) {
        if (inp.isEmpty()) inp = "Type something";
        ArrayList<String> translate = mGenerator.generate(inp);
        mAdapter.setData(translate);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        convert(mInput.getText().toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void restore() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mInput.setText(sharedPreferences.getString(KEY + 1, ""));
    }

    @Override
    public void onDestroyView() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.edit().putString(KEY + 1, mInput.getText().toString()).apply();
        super.onDestroyView();
    }


}
