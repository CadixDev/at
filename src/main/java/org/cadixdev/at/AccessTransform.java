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

package org.cadixdev.at;

import java.util.Objects;
import java.util.StringJoiner;

public final class AccessTransform {

    public static final AccessTransform EMPTY = new AccessTransform(AccessChange.NONE, ModifierChange.NONE);
    public static final AccessTransform PUBLIC = of(AccessChange.PUBLIC);

    private final AccessChange accessChange;
    private final ModifierChange finalChange;

    private AccessTransform(AccessChange accessChange, ModifierChange finalChange) {
        this.accessChange = Objects.requireNonNull(accessChange, "accessChange");
        this.finalChange = Objects.requireNonNull(finalChange, "finalChange");
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public AccessChange getAccess() {
        return this.accessChange;
    }

    public ModifierChange getFinal() {
        return this.finalChange;
    }

    public AccessTransform merge(AccessTransform other) {
        Objects.requireNonNull(other, "other");
        if (isEmpty()) {
            return other;
        } else if (other.isEmpty()) {
            return this;
        }
        return of(getAccess().merge(other.getAccess()), getFinal().merge(other.getFinal()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccessTransform)) {
            return false;
        }

        AccessTransform other = (AccessTransform) o;
        return getAccess() == other.getAccess() && getFinal() == other.getFinal();
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessChange, finalChange);
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "AccessTransform.EMPTY";
        }

        StringJoiner joiner = new StringJoiner(", ", "AccessTransform{", "}");
        if (this.accessChange != AccessChange.NONE) {
            joiner.add("access=" + this.accessChange);
        }
        if (this.finalChange != ModifierChange.NONE) {
            joiner.add("final=" + this.finalChange);
        }
        return joiner.toString();
    }

    public static AccessTransform of(AccessChange accessChange) {
        return of(accessChange, ModifierChange.NONE);
    }

    public static AccessTransform of(ModifierChange finalChange) {
        return of(AccessChange.NONE, finalChange);
    }

    public static AccessTransform of(AccessChange accessChange, ModifierChange finalChange) {
        if (accessChange == AccessChange.NONE && finalChange == ModifierChange.NONE) {
            return AccessTransform.EMPTY;
        }
        return new AccessTransform(accessChange, finalChange);
    }

}
