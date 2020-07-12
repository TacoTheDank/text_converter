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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duy.text.converter.R;
import com.duy.text.converter.core.stylish.StylistGenerator;
import com.duy.text.converter.pro.menu.adapters.StylishProcessTextAdapter;

import java.util.ArrayList;


/**
 * Created by DUy on 07-Feb-17.
 */

public class StylistProcessTextFragment extends Fragment {
    public static final String KEY = "StylistFragment";
    private static final String KEY_INPUT = "KEY_INPUT";
    private StylishProcessTextAdapter mAdapter;

    public static StylistProcessTextFragment newInstance(String input) {
        StylistProcessTextFragment fragment = new StylistProcessTextFragment();
        Bundle args = new Bundle();
        args.putString(KEY_INPUT, input);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stylish_process_text, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String input = getArguments().getString(KEY_INPUT);

        RecyclerView mListResult = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mListResult.setLayoutManager(layoutManager);
        mListResult.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));

        mAdapter = new StylishProcessTextAdapter(getContext());
        if (getActivity() instanceof OnTextSelectedListener) {
            mAdapter.setListener((OnTextSelectedListener) getActivity());
        }
        mListResult.setAdapter(mAdapter);

        convert(input);
    }


    public void convert(String inp) {
        if (inp.isEmpty()) inp = "Type something";
        ArrayList<String> translate = new StylistGenerator(getContext()).generate(inp);
        mAdapter.addAll(translate);
    }


}
