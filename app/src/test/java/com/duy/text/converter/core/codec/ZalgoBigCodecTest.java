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

import com.duy.text.converter.core.codec.interfaces.Codec;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Duy on 13-Jul-17.
 */
public class ZalgoBigCodecTest {
    private static final String TO_BE_ENCODE = "ZalgoBigCodecTest";
    private final Codec codec = new ZalgoBigCodec();

    @Test
    public void encode() {
        String encode = codec.encode(TO_BE_ENCODE);
        System.out.println("encode = " + encode);
        assertNotNull(encode);
    }

    @Test
    public void decode() {
        String encode = codec.encode(TO_BE_ENCODE);
        String decode = codec.decode(encode);
        System.out.println("decode = " + decode);
        assertEquals(TO_BE_ENCODE, decode);
    }

}
