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

import org.apache.commons.lang3.ArrayUtils;

public class ZalgoMiniCodec extends CodecImpl {

    public static final char[] ZALGO_UP =
            {
                    '\u030d', /*     Ì?     */'\u030e', /*     ÌŽ     */'\u0304', /*     Ì„     */'\u0305', /*     Ì…     */
                    '\u033f', /*     Ì¿     */'\u0311', /*     Ì‘     */'\u0306', /*     Ì†     */'\u0310', /*     Ì?     */
                    '\u0352', /*     Í’     */'\u0357', /*     Í—    */'\u0351', /*     Í‘     */'\u0307', /*     Ì‡     */
                    '\u0308', /*     Ìˆ     */'\u030a', /*     ÌŠ     */'\u0342', /*     Í‚     */'\u0343', /*     Ì“     */
                    '\u0344', /*     ÌˆÌ?   */'\u034a', /*     ÍŠ     */'\u034b', /*     Í‹     */'\u034c', /*     ÍŒ     */
                    '\u0303', /*     Ìƒ     */'\u0302', /*     Ì‚     */'\u030c', /*     ÌŒ     */'\u0350', /*     Í?     */
                    '\u0300', /*     Ì€     */'\u0301', /*     Ì?     */'\u030b', /*     Ì‹     */'\u030f', /*     Ì?     */
                    '\u0312', /*     Ì’     */'\u0313', /*     Ì“     */'\u0314', /*     Ì”     */'\u033d', /*     Ì½     */
                    '\u0309', /*     Ì‰    */'\u0363', /*     Í£     */'\u0364', /*     Í¤     */'\u0365', /*     Í¥     */
                    '\u0366', /*     Í¦     */'\u0367', /*     Í§     */'\u0368', /*     Í¨     */'\u0369', /*     Í©     */
                    '\u036a', /*     Íª     */'\u036b', /*     Í«     */'\u036c', /*     Í¬     */'\u036d', /*     Í­     */
                    '\u036e', /*     Í®     */'\u036f', /*     Í¯     */'\u033e', /*     Ì¾     */'\u035b', /*     Í›     */
                    '\u0346', /*     Í†     */'\u031a'  /*     Ìš     */
            };

    public static final char[] ZALGO_DOWN =
            {'\u0316', /*     Ì–     */'\u0317', /*     Ì—     */'\u0318', /*     Ì˜     */'\u0319', /*     Ì™     */
                    '\u031c', /*     Ìœ     */'\u031d', /*     Ì?     */'\u031e', /*     Ìž     */'\u031f', /*     ÌŸ     */
                    '\u0320', /*     Ì      */'\u0324', /*     Ì¤     */'\u0325', /*     Ì¥     */'\u0326', /*     Ì¦     */
                    '\u0329', /*     Ì©     */'\u032a', /*     Ìª     */'\u032b', /*     Ì«     */'\u032c', /*     Ì¬     */
                    '\u032d', /*     Ì­     */'\u032e', /*     Ì®     */'\u032f', /*     Ì¯     */'\u0330', /*     Ì°     */
                    '\u0331', /*     Ì±     */'\u0332', /*     Ì²     */'\u0333', /*     Ì³     */'\u0339', /*     Ì¹     */
                    '\u033a', /*     Ìº     */'\u033b', /*     Ì»     */'\u033c', /*     Ì¼     */'\u0345', /*     Í…     */
                    '\u0347', /*     Í‡     */'\u0348', /*     Íˆ     */'\u0349', /*     Í‰     */'\u034d', /*     Í?     */
                    '\u034e', /*     ÍŽ     */'\u0353', /*     Í“     */'\u0354', /*     Í”     */'\u0355', /*     Í•     */
                    '\u0356', /*     Í–     */'\u0359', /*     Í™     */'\u035a', /*     Íš     */'\u0323' /*     Ì£     */
            };

    //those always stay in the middle
    public static final char[] ZALGO_MID =
            {'\u0315', /*     Ì•     */'\u031b', /*     Ì›     */'\u0340', /*     Ì€     */'\u0341', /*     Ì?     */
                    '\u0358', /*     Í˜     */'\u0321', /*     Ì¡     */'\u0322', /*     Ì¢     */'\u0327', /*     Ì§     */
                    '\u0328', /*     Ì¨     */'\u0334', /*     Ì´     */'\u0335', /*     Ìµ     */'\u0336', /*     Ì¶     */
                    '\u034f', /*     Í?     */'\u035c', /*     Íœ     */'\u035d', /*     Í?     */'\u035e', /*     Íž     */
                    '\u035f', /*     ÍŸ     */'\u0360', /*     Í      */'\u0362', /*     Í¢     */'\u0338', /*     Ì¸     */
                    '\u0337', /*     Ì·     */'\u0361', /*     Í¡     */'\u0489' /*     Ò‰_     */
            };


    // rand funcs
    //---------------------------------------------------

    //gets an int between 0 and max

    private static int rand(int max) {
        return (int) Math.floor(Math.random() * max);
    }

    //gets a random char from a zalgo char table

    private static char randomCharFrom(char[] array) {
        int ind = (int) Math.floor(Math.random() * array.length);
        return array[ind];
    }

    //hide show element
    //lookup char to know if its a zalgo char or not

    private static boolean isZalgoChar(char c) {
        for (char aZalgo_up : ZALGO_UP) {
            if (c == aZalgo_up) {
                return true;
            }
        }
        for (char aZalgo_down : ZALGO_DOWN) {
            if (c == aZalgo_down) {
                return true;
            }
        }
        for (char aZalgo_mid : ZALGO_MID) {
            if (c == aZalgo_mid) {
                return true;
            }
        }
        return false;
    }

    public static String convert(final String text, boolean isMini, boolean isNormal,
                                 boolean up, boolean down, boolean mid) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            if (isZalgoChar(text.charAt(i)))
                continue;

            int num_up;
            int num_mid;
            int num_down;

            //add the normal character
            result.append(text.charAt(i));

            if (isMini) {
                num_up = rand(8);
                num_mid = rand(2);
                num_down = rand(8);
            } else if (isNormal) {
                num_up = rand(16) / 2 + 1;
                num_mid = rand(6) / 2;
                num_down = rand(16) / 2 + 1;
            } else {
                num_up = rand(64) / 4 + 3;
                num_mid = rand(16) / 4 + 1;
                num_down = rand(64) / 4 + 3;
            }

            if (up) {
                for (int j = 0; j < num_up; j++) {
                    result.append(randomCharFrom(ZALGO_UP));
                }
            }
            if (mid) {
                for (int j = 0; j < num_mid; j++) {
                    result.append(randomCharFrom(ZALGO_MID));
                }
            }
            if (down) {
                for (int j = 0; j < num_down; j++) {
                    result.append(randomCharFrom(ZALGO_DOWN));
                }
            }
        }


        return result.toString();
    }


    @NonNull
    @Override
    public String encode(@NonNull String text) {
        return convert(text, true, false, true, true, true);
    }

    @NonNull
    @Override
    public String decode(@NonNull String text) {
        StringBuilder decoded = new StringBuilder();
        char[] chars = text.toCharArray();
        for (char aChar : chars) {
            if (ArrayUtils.contains(ZALGO_DOWN, aChar)
                    || ArrayUtils.contains(ZALGO_MID, aChar)
                    || ArrayUtils.contains(ZALGO_UP, aChar)) {
                continue;
            }
            decoded.append(aChar);
        }
        return decoded.toString();
    }


    @NonNull
    @Override
    public String getName(Context context) {
        return "Zalgo Mini";
    }
}
