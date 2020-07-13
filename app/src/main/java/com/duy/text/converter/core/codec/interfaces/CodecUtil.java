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

package com.duy.text.converter.core.codec.interfaces;

import android.content.Context;

import androidx.annotation.NonNull;

import com.duy.text.converter.R;
import com.duy.text.converter.core.codec.AsciiCodec;
import com.duy.text.converter.core.codec.AtbashCodec;
import com.duy.text.converter.core.codec.Base32Codec;
import com.duy.text.converter.core.codec.Base64Codec;
import com.duy.text.converter.core.codec.BinaryCodec;
import com.duy.text.converter.core.codec.CaesarCodec;
import com.duy.text.converter.core.codec.HexCodec;
import com.duy.text.converter.core.codec.LowerCaseCodec;
import com.duy.text.converter.core.codec.MorseCodec;
import com.duy.text.converter.core.codec.NatoCodec;
import com.duy.text.converter.core.codec.OctalCodec;
import com.duy.text.converter.core.codec.RandomCaseCodec;
import com.duy.text.converter.core.codec.ReverseCodec;
import com.duy.text.converter.core.codec.Rot13Codec;
import com.duy.text.converter.core.codec.SubscriptCodec;
import com.duy.text.converter.core.codec.SuperscriptCodec;
import com.duy.text.converter.core.codec.URLCodec;
import com.duy.text.converter.core.codec.UnicodeCodec;
import com.duy.text.converter.core.codec.UpperCaseCodec;
import com.duy.text.converter.core.codec.UpsideDownCodec;
import com.duy.text.converter.core.codec.WingdingsCodec;
import com.duy.text.converter.core.codec.ZalgoBigCodec;
import com.duy.text.converter.core.codec.ZalgoMiniCodec;
import com.duy.text.converter.core.codec.ZalgoNormalCodec;

import java.util.ArrayList;

/**
 * Created by Duy on 29-Jul-17.
 */

public class CodecUtil {
    public static String decode(String method, Context context, String inp) {
        String[] array = context.getResources().getStringArray(R.array.codec_methods);
        int pos;
        for (pos = 0; pos < array.length; pos++) {
            String s = array[pos];
            if (s.equals(method)) {
                break;
            }
        }
        CodecMethod decodeMethod = CodecMethod.values()[pos];
        return decode(decodeMethod, inp);
    }

    public static String decode(CodecMethod decodeMethod, String inp) {
        switch (decodeMethod) {
            case ASCII:
                return new AsciiCodec().decode(inp);
            case ATBASH:
                return new AtbashCodec().decode(inp);
            case BASE_32:
                return new Base32Codec().decode(inp);
            case BASE_64:
                return new Base64Codec().decode(inp);
            case BINARY:
                return new BinaryCodec().decode(inp);
            case CAESAR:
                return new CaesarCodec().decode(inp);
            case HEX:
                return new HexCodec().decode(inp);
            case LOWER_CASE:
                return new LowerCaseCodec().decode(inp);
            case MORSE_CODE:
                return new MorseCodec().decode(inp);
            case NATO:
                return new NatoCodec().decode(inp);
            case OCTAL:
                return new OctalCodec().decode(inp);
            case RANDOM_CASE:
                return new RandomCaseCodec().decode(inp);
            case REVERSE:
                return new ReverseCodec().decode(inp);
            case ROT_13:
                return new Rot13Codec().decode(inp);
            case SUB_SCRIPT:
                return new SubscriptCodec().decode(inp);
            case SUPER_SCRIPT:
                return new SuperscriptCodec().decode(inp);
            case UNICODE:
                return new UnicodeCodec().decode(inp);
            case UPPER_CASE:
                return new UpperCaseCodec().decode(inp);
            case UPSIDE_DOWN:
                return new UpsideDownCodec().decode(inp);
            case URL:
                return new URLCodec().decode(inp);
            case WINGDINGS:
                return new WingdingsCodec().decode(inp);
            case ZALGO_BIG:
                return new ZalgoBigCodec().decode(inp);
            case ZALGO_MINI:
                return new ZalgoMiniCodec().decode(inp);
            case ZALGO_NORMAL:
                return new ZalgoNormalCodec().decode(inp);
            default:
                return decodeMethod.getCodec().decode(inp);
        }
    }

    public static String encode(String name, Context context, String inp) {
        String[] array = context.getResources().getStringArray(R.array.codec_methods);
        int pos;
        for (pos = 0; pos < array.length; pos++) {
            String s = array[pos];
            if (s.equals(name)) {
                break;
            }
        }
        CodecMethod encodeMethod = CodecMethod.values()[pos];
        return encode(encodeMethod, inp);
    }

    public static String encode(CodecMethod encodeMethod, String inp) {
        switch (encodeMethod) {
            case ASCII:
                return new AsciiCodec().encode(inp);
            case ATBASH:
                return new AtbashCodec().encode(inp);
            case BASE_32:
                return new Base32Codec().encode(inp);
            case BASE_64:
                return new Base64Codec().encode(inp);
            case BINARY:
                return new BinaryCodec().encode(inp);
            case CAESAR:
                return new CaesarCodec().encode(inp);
            case HEX:
                return new HexCodec().encode(inp);
            case LOWER_CASE:
                return new LowerCaseCodec().encode(inp);
            case MORSE_CODE:
                return new MorseCodec().encode(inp);
            case NATO:
                return new NatoCodec().encode(inp);
            case OCTAL:
                return new OctalCodec().encode(inp);
            case RANDOM_CASE:
                return new RandomCaseCodec().encode(inp);
            case REVERSE:
                return new ReverseCodec().encode(inp);
            case ROT_13:
                return new Rot13Codec().encode(inp);
            case SUB_SCRIPT:
                return new SubscriptCodec().encode(inp);
            case SUPER_SCRIPT:
                return new SuperscriptCodec().encode(inp);
            case UNICODE:
                return new UnicodeCodec().encode(inp);
            case UPPER_CASE:
                return new UpperCaseCodec().encode(inp);
            case UPSIDE_DOWN:
                return new UpsideDownCodec().encode(inp);
            case URL:
                return new URLCodec().encode(inp);
            case WINGDINGS:
                return new WingdingsCodec().encode(inp);
            case ZALGO_BIG:
                return new ZalgoBigCodec().encode(inp);
            case ZALGO_MINI:
                return new ZalgoMiniCodec().encode(inp);
            case ZALGO_NORMAL:
                return new ZalgoNormalCodec().encode(inp);
            default:
                return encodeMethod.getCodec().encode(inp);
        }
    }

    @NonNull
    public static ArrayList<String> getAllCodecName(@NonNull Context context) {
        ArrayList<String> names = new ArrayList<>();
        for (CodecMethod codec : CodecMethod.values()) {
            names.add(codec.getCodec().getName(context));
        }
        return names;
    }

}
