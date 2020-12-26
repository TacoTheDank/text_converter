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

package com.duy.text.converter.pro.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.duy.text.converter.R;
import com.duy.text.converter.core.codec.interfaces.CodecUtil;
import com.duy.text.converter.utils.FileUtil;
import com.duy.text.converter.view.RoundedBackgroundEditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Duy on 10-Aug-17.
 */

public class CodecFileFragment extends Fragment implements View.OnClickListener {
    private static final int REQUEST_SELECT_FILE = 1002;
    private static final String TAG = "CodecFileFragment";
    private static final int RED_PERCUSSION_READ_STORAGE = 13;
    private Uri mInputUri;
    private TextView mEditInputPath, mEditOutPath;
    private RadioButton mIsEncode;
    private Spinner mCodecMethodSpinner;
    private View mBtnProcess, mBtnOpenOutput;

    public static CodecFileFragment newInstance() {
        Bundle args = new Bundle();
        CodecFileFragment fragment = new CodecFileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_select_file).setOnClickListener(this);

        mBtnProcess = view.findViewById(R.id.btn_submit);
        mBtnProcess.setOnClickListener(this);
        mBtnOpenOutput = view.findViewById(R.id.btn_open_result);
        mBtnOpenOutput.setOnClickListener(this);

        mIsEncode = view.findViewById(R.id.rad_encode);
        mEditInputPath = view.findViewById(R.id.edit_input_path);
        mEditOutPath = view.findViewById(R.id.edit_output_path);
        if (savedInstanceState != null) {
            setInputUri(savedInstanceState.getParcelable("input_path"));
            setOutputPath(savedInstanceState.getString("output_path"));
        }

        ArrayList<String> data = CodecUtil.getAllCodecName(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, data);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        mCodecMethodSpinner = view.findViewById(R.id.spinner_codec_methods);
        mCodecMethodSpinner.setBackground(RoundedBackgroundEditText.createRoundedBackground(getContext()));
        mCodecMethodSpinner.setAdapter(adapter);
    }

    public void setOutputPath(final String outputPath) {
        mEditOutPath.post(() ->
                mEditOutPath.setText(outputPath));
        mBtnOpenOutput.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.btn_select_file:
                selectFile();

                break;
            case R.id.btn_submit:
                progressFile();

                break;
            case R.id.btn_open_result:
                openResult();
                break;
        }
    }

    private void openResult() {
        String path = mEditOutPath.getText().toString();
        if (!path.isEmpty()) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                File file = new File(path);
                Uri url = FileProvider.getUriForFile(getContext(),
                        getContext().getPackageName() + ".fileprovider", file);

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(url, "text/plain");
                Intent chooser = Intent.createChooser(intent, "Choose an application to open with:");
                startActivity(chooser);
            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("input_path", mInputUri);
        outState.putString("output_path", mEditOutPath.getText().toString());
    }

    private void progressFile() {
        if (mInputUri != null) {
            try {
                String methodName = mCodecMethodSpinner.getSelectedItem().toString();
                EncodeTask mEncodeTask = new EncodeTask(getContext(), mIsEncode.isChecked(), methodName);
                InputStream inputStream = getContext().getContentResolver().openInputStream(mInputUri);
                mEncodeTask.execute(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), R.string.message_select_text_file, Toast.LENGTH_SHORT).show();
        }
    }

    private void selectFile() {
        if (!isPermissionGranted()) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_SELECT_FILE);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/plain");
        startActivityForResult(intent, REQUEST_SELECT_FILE);
    }

    private boolean isPermissionGranted() {
        int i = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (i != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        i = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return i == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SELECT_FILE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectFile();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_FILE) {
            if (resultCode == RESULT_OK && data.getData() != null) {
                setInputUri(data.getData());
            }
        }
    }

    public void setInputUri(final Uri inputPath) {
        mInputUri = inputPath;
        if (mInputUri != null) {
            mEditInputPath.post(() ->
                    mEditInputPath.setText(inputPath.toString()));
            mBtnProcess.setEnabled(true);
        } else {
            mBtnProcess.setEnabled(false);
            mBtnOpenOutput.setEnabled(false);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class EncodeTask extends AsyncTask<InputStream, Integer, File> {

        private final Context context;
        private final boolean encode;
        private final String methodName;
        private ProgressDialog mProgressDialog;

        EncodeTask(Context context, boolean encode, String methodName) {
            this.context = context;
            this.encode = encode;
            this.methodName = methodName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setTitle("Progress...");
            mProgressDialog.setMax(3);
            mProgressDialog.show();
        }

        @Override
        protected File doInBackground(InputStream... params) {
            Log.d(TAG, "doInBackground() called with: params = [" + Arrays.toString(params) + "]");
            try {
                InputStream file = params[0];
                Log.d(TAG, "doInBackground file = " + file);
                publishProgress(0);
                String content = FileUtil.streamToString(file);
                Log.d(TAG, "doInBackground content = " + content);

                publishProgress(1);
                content = encode ? CodecUtil.encode(methodName, context, content) :
                        CodecUtil.decode(methodName, context, content);

                publishProgress(2);
                String fileName = String.format("%s_%s.txt", System.currentTimeMillis(), methodName);
                File out = new File(FileUtil.APP_PATH, fileName);
                if (!out.exists()) {
                    out.getParentFile().mkdirs();
                    out.createNewFile();
                }
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(out));
                bufferedWriter.write(content);
                bufferedWriter.close();
                return out;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            switch (values[0]) {
                case 0:
                    mProgressDialog.setProgress(0);
                    mProgressDialog.setMessage("Reading input");
                    break;
                case 1:
                    mProgressDialog.setProgress(1);
                    mProgressDialog.setMessage("Progressing text");
                    break;
                case 2:
                    mProgressDialog.setProgress(2);
                    mProgressDialog.setMessage("Write to output");
                    break;
            }
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if (file != null) {
                setOutputPath(file.getPath());
            } else {
                Toast.makeText(context, "Encode failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
