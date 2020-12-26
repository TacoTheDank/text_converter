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
import com.duy.text.converter.core.codec.UrlCodec;
import com.duy.text.converter.core.codec.UnicodeCodec;
import com.duy.text.converter.core.codec.UpperCaseCodec;
import com.duy.text.converter.core.codec.UpsideDownCodec;
import com.duy.text.converter.core.codec.WingdingsCodec;
import com.duy.text.converter.core.codec.ZalgoBigCodec;
import com.duy.text.converter.core.codec.ZalgoMiniCodec;
import com.duy.text.converter.core.codec.ZalgoNormalCodec;

public enum CodecMethod {
    ASCII {
        @Override
        public Codec getCodec() {
            return new AsciiCodec();
        }
    },
    ATBASH {
        @Override
        public Codec getCodec() {
            return new AtbashCodec();
        }
    },
    BASE_32 {
        @Override
        public Codec getCodec() {
            return new Base32Codec();
        }
    },
    BASE_64 {
        @Override
        public Codec getCodec() {
            return new Base64Codec();
        }
    },
    BINARY {
        @Override
        public Codec getCodec() {
            return new BinaryCodec();
        }
    },
    CAESAR {
        @Override
        public Codec getCodec() {
            return new CaesarCodec();
        }
    },
    HEX {
        @Override
        public Codec getCodec() {
            return new HexCodec();
        }
    },
    LOWER_CASE {
        @Override
        public Codec getCodec() {
            return new LowerCaseCodec();
        }
    },
    MORSE_CODE {
        @Override
        public Codec getCodec() {
            return new MorseCodec();
        }
    },
    NATO {
        @Override
        public Codec getCodec() {
            return new NatoCodec();
        }
    },
    OCTAL {
        @Override
        public Codec getCodec() {
            return new OctalCodec();
        }
    },
    RANDOM_CASE {
        @Override
        public Codec getCodec() {
            return new RandomCaseCodec();
        }
    },
    REVERSE {
        @Override
        public Codec getCodec() {
            return new ReverseCodec();
        }
    },
    ROT_13 {
        @Override
        public Codec getCodec() {
            return new Rot13Codec();
        }
    },
    SUB_SCRIPT {
        @Override
        public Codec getCodec() {
            return new SubscriptCodec();
        }
    },
    SUPER_SCRIPT {
        @Override
        public Codec getCodec() {
            return new SuperscriptCodec();
        }
    },
    UNICODE {
        @Override
        public Codec getCodec() {
            return new UnicodeCodec();
        }
    },
    UPPER_CASE {
        @Override
        public Codec getCodec() {
            return new UpperCaseCodec();
        }
    },
    UPSIDE_DOWN {
        @Override
        public Codec getCodec() {
            return new UpsideDownCodec();
        }
    },
    URL {
        @Override
        public Codec getCodec() {
            return new UrlCodec();
        }
    },
    WINGDINGS {
        @Override
        public Codec getCodec() {
            return new WingdingsCodec();
        }
    },
    ZALGO_BIG {
        @Override
        public Codec getCodec() {
            return new ZalgoBigCodec();
        }
    },
    ZALGO_MINI {
        @Override
        public Codec getCodec() {
            return new ZalgoMiniCodec();
        }
    },
    ZALGO_NORMAL {
        @Override
        public Codec getCodec() {
            return new ZalgoNormalCodec();
        }
    };

    public abstract Codec getCodec();
}
