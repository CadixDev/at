/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Minecrell (https://github.com/Minecrell)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.minecrell.at;

import java.lang.reflect.Modifier;

public enum AccessChange {
    NONE(-1),
    PRIVATE(Modifier.PRIVATE),
    PACKAGE_PRIVATE(0),
    PROTECTED(Modifier.PROTECTED),
    PUBLIC(Modifier.PUBLIC);

    private final int modifier;

    AccessChange(int modifier) {
        this.modifier = modifier;
    }

    public int getModifier() {
        return modifier;
    }

    public AccessChange merge(AccessChange other) {
        if (this == other) {
            return this;
        } else if (this == NONE) {
            return other;
        } else if (other == NONE) {
            return this;
        }

        int compare = compareTo(other);
        if (compare == 0) {
            return this;
        } else if (compare < 0) {
            return other;
        } else {
            return this;
        }
    }

}
