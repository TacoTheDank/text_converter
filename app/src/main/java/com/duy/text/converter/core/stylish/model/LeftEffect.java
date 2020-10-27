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

package com.duy.text.converter.core.stylish.model;


import androidx.annotation.NonNull;

/**
 * Created by Duy on 13-Jul-17.
 */

public class LeftEffect implements Style {

    private final String left;

    public LeftEffect(String left) {
        this.left = left;
    }

    @NonNull
    @Override
    public String generate(@NonNull String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                result.append(left).append(" ");
            } else {
                result.append(left).append(text.charAt(i));
            }
        }
        result.append(left);
        return result.toString();
    }

    @Override
    public int hashCode() {
        return left.hashCode();
    }
}
