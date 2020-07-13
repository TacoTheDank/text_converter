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

package com.duy.text.converter.core.codec;

import android.content.Context;

import androidx.annotation.NonNull;

import com.duy.text.converter.core.codec.interfaces.CodecImpl;
import com.duy.text.converter.utils.CodePointUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Duy on 2/13/2018.
 */

public class WingdingsCodec extends CodecImpl {
    static final HashMap<Character, Integer> WINGDINGS_MAP = new HashMap<>();

    static {
        WINGDINGS_MAP.put('!', 9999);
        WINGDINGS_MAP.put('"', 9986);
        WINGDINGS_MAP.put('#', 9985);
        WINGDINGS_MAP.put('$', 128083);
        WINGDINGS_MAP.put('%', 128365);
        WINGDINGS_MAP.put('&', 128366);
        WINGDINGS_MAP.put('\'', 128367);
        WINGDINGS_MAP.put('(', 9742);
        WINGDINGS_MAP.put(')', 9990);
        WINGDINGS_MAP.put('*', 128386);
        WINGDINGS_MAP.put('+', 128387);
        WINGDINGS_MAP.put(',', 128234);
        WINGDINGS_MAP.put('-', 128235);
        WINGDINGS_MAP.put('.', 128236);
        WINGDINGS_MAP.put('/', 128237);
        WINGDINGS_MAP.put('0', 128193);
        WINGDINGS_MAP.put('1', 128194);
        WINGDINGS_MAP.put('2', 128196);
        WINGDINGS_MAP.put('3', 128463);
        WINGDINGS_MAP.put('4', 128464);
        WINGDINGS_MAP.put('5', 128452);
        WINGDINGS_MAP.put('6', 8987);
        WINGDINGS_MAP.put('7', 128430);
        WINGDINGS_MAP.put('8', 128432);
        WINGDINGS_MAP.put('9', 128434);
        WINGDINGS_MAP.put(':', 128435);
        WINGDINGS_MAP.put(';', 128436);
        WINGDINGS_MAP.put('<', 128427);
        WINGDINGS_MAP.put('=', 128428);
        WINGDINGS_MAP.put('>', 9991);
        WINGDINGS_MAP.put('?', 9997);
        WINGDINGS_MAP.put('@', 128398);
        WINGDINGS_MAP.put('A', 9996);
        WINGDINGS_MAP.put('B', 128076);
        WINGDINGS_MAP.put('C', 128077);
        WINGDINGS_MAP.put('D', 128078);
        WINGDINGS_MAP.put('E', 9756);
        WINGDINGS_MAP.put('F', 9758);
        WINGDINGS_MAP.put('G', 9757);
        WINGDINGS_MAP.put('H', 9759);
        WINGDINGS_MAP.put('I', 9995);
        WINGDINGS_MAP.put('J', 9786);
        WINGDINGS_MAP.put('K', 128528);
        WINGDINGS_MAP.put('L', 9785);
        WINGDINGS_MAP.put('M', 128163);
        WINGDINGS_MAP.put('N', 9760);
        WINGDINGS_MAP.put('O', 9872);
        WINGDINGS_MAP.put('P', 127985);
        WINGDINGS_MAP.put('Q', 9992);
        WINGDINGS_MAP.put('R', 9788);
        WINGDINGS_MAP.put('S', 128167);
        WINGDINGS_MAP.put('T', 10052);
        WINGDINGS_MAP.put('U', 128326);
        WINGDINGS_MAP.put('V', 10014);
        WINGDINGS_MAP.put('W', 128328);
        WINGDINGS_MAP.put('X', 10016);
        WINGDINGS_MAP.put('Y', 10017);
        WINGDINGS_MAP.put('Z', 9770);
        WINGDINGS_MAP.put('[', 9775);
        WINGDINGS_MAP.put(']', 9784);
        WINGDINGS_MAP.put('^', 9800);
        WINGDINGS_MAP.put('_', 9801);
        WINGDINGS_MAP.put('`', 9802);
        WINGDINGS_MAP.put('{', 10048);
        WINGDINGS_MAP.put('|', 10047);
        WINGDINGS_MAP.put('}', 10077);
        WINGDINGS_MAP.put('~', 10078);
        WINGDINGS_MAP.put(' ', (int) ' ');
    }

    @NonNull
    @Override
    public String decode(@NonNull String text) {
        setMax(text.length());
        setConfident(0);

        StringBuilder decoded = new StringBuilder();
        loop:
        for (Integer codePoint : CodePointUtil.codePoints(text)) {
            for (Map.Entry<Character, Integer> entry : WINGDINGS_MAP.entrySet()) {
                if (codePoint.equals(entry.getValue())) {
                    decoded.append(entry.getKey());
                    incConfident();
                    continue loop;
                }
            }
            decoded.append(Character.toChars(codePoint));
        }
        return decoded.toString();
    }

    @NonNull
    @Override
    public String encode(@NonNull String text) {
        System.out.println("WingdingsCodec.encode");
        System.out.println("text = " + text);
        System.out.println(text.length());

        setMax(text.length());
        setConfident(0);
        StringBuilder encoded = new StringBuilder();
        text = text.toUpperCase();
        for (int i = 0; i < text.length(); i++) { //Iterating through string
            if (WINGDINGS_MAP.get(text.charAt(i)) != null) { //If that char of string can be found,
                int temp = WINGDINGS_MAP.get(text.charAt(i)); //get the number
                //and convert to real char, add to ArrayList //adding char[], because whole char[] needed for UTF-16 Unicode
                encoded.append((Character.toChars(temp)));
                incConfident();
            } else {
                encoded.append(text.charAt(i));
            }
        }
        return encoded.toString();
    }

    @NonNull
    @Override
    public String getName(Context context) {
        return "Wingdings";
    }

}
