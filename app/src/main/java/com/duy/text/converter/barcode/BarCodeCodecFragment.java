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

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.duy.text.converter.R;
import com.duy.text.converter.ui.menu.EditMenuViewHolder;
import com.duy.text.converter.view.BaseEditText;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.android.Intents;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;

import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Duy on 14-Aug-17.
 */

public class BarCodeCodecFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "BarCodeFragment";
    private static final String KEY_TEXT = "KEY_TEXT";
    private static final int REQUEST_PICK_IMAGE = 1010;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1011;
    private BaseEditText mInput;
    private DecodeImageTask mDecodeImageTask;

    public static BarCodeCodecFragment newInstance() {

        Bundle args = new Bundle();

        BarCodeCodecFragment fragment = new BarCodeCodecFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_barcode, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInput = view.findViewById(R.id.edit_input);

        new EditMenuViewHolder(
                view.findViewById(R.id.edit_input),
                (EditText) view.findViewById(R.id.edit_input))
                .bind();

        String[] data = getResources().getStringArray(R.array.barcode_format);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, data);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        view.findViewById(R.id.btn_encode).setOnClickListener(this);
        view.findViewById(R.id.btn_decode_cam).setOnClickListener(this);
        view.findViewById(R.id.btn_decode_image).setOnClickListener(this);
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
        sharedPreferences.edit().putString(TAG + getArguments().getInt(KEY_TEXT), mInput.getText().toString()).apply();
    }

    public void restore() {
        String text = getArguments().getString(Intent.EXTRA_TEXT, "");
        if (!text.isEmpty()) {
            mInput.setText(text);
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            mInput.setText(sharedPreferences.getString(TAG + getArguments().getInt(KEY_TEXT), ""));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_encode:
                encode(mInput.getText().toString());
                break;
            case R.id.btn_decode_cam:
                decodeUseCamera();
                break;
            case R.id.btn_decode_image:
                decodeImage();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    decodeImage();
                } else {
                    Toast.makeText(getContext(), R.string.message_read_permission, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                final String contents = data.getStringExtra(Intents.Scan.RESULT);
                mInput.postDelayed(() -> {
                    mInput.setText(contents);
                    if (getContext() != null) {
                        Toast.makeText(getContext(), R.string.decoded, Toast.LENGTH_SHORT).show();
                    }
                }, 200);
            }

        } else if (requestCode == REQUEST_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (mDecodeImageTask != null && !mDecodeImageTask.isCancelled()) {
                    mDecodeImageTask.cancel(true);
                }
                mDecodeImageTask = new DecodeImageTask(getContext().getApplicationContext(), mInput);
                mDecodeImageTask.execute(data.getData());
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDecodeImageTask != null && !mDecodeImageTask.isCancelled()) {
            mDecodeImageTask.cancel(true);
        }
    }

    private void decodeUseCamera() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    private void encode(String s) {
        Intent intent = new Intent(getContext(), BarcodeEncodedActivity.class);
        intent.putExtra("data", s);
        startActivity(intent);
    }


    private void decodeImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int result = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            if (result != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), R.string.message_read_permission, Toast.LENGTH_SHORT).show();
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, REQUEST_READ_EXTERNAL_STORAGE);
                return;
            }
        }

        try {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.title_select_image));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, REQUEST_PICK_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private static class DecodeImageTask extends AsyncTask<Uri, Void, String> {
        private Context context;
        private TextView txtResult;

        private DecodeImageTask(@NonNull Context context, @NonNull TextView txtResult) {
            this.context = context;
            this.txtResult = txtResult;
        }

        @NonNull
        public Context getContext() {
            return context;
        }

        @Override
        protected String doInBackground(Uri... params) {
            Uri uri = params[0];
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.outWidth = 1024;
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, opts);
                if (bitmap == null) {
                    Log.e(TAG, "uri is not a bitmap," + uri.toString());
                    return null;
                }
                int width = bitmap.getWidth(), height = bitmap.getHeight();
                int[] pixels = new int[width * height];
                bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                bitmap.recycle();
                RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
                MultiFormatReader reader = new MultiFormatReader();
                try {
                    Result result = reader.decode(bBitmap);
                    return result.getText();
                } catch (NotFoundException e) {
                    Log.e(TAG, "decode exception", e);
                    return null;
                }
            } catch (Throwable e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (isCancelled()) {
                return;
            }
            if (s != null) {
                txtResult.setText(s);
                Toast.makeText(getContext(), R.string.decoded, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), R.string.message_cannot_decode, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
