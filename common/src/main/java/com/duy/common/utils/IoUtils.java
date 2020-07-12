/*
 * Copyright (c) 2018 by Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.common.utils;

import android.os.Build;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * Created by Duy on 12-Apr-18.
 */

public class IoUtils {

    public static String streamToString(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        } else {
            in = new InputStreamReader(inputStream, "UTF-8");
        }
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    public static void delete(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (file != null) {
                for (File f : files) {
                    delete(f);
                }
            }
        } else if (file.isFile()) {
            file.delete();
        }
    }

    public static void deleteChild(File toDelete, FileFilter fileFilter) {
        File[] files = toDelete.listFiles();
        if (files != null) {
            for (File file : files) {
                if (fileFilter.accept(file)) {
                    file.delete();
                }
            }
        }
    }
}
