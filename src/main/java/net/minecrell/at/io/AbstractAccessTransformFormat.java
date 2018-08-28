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

package net.minecrell.at.io;

import net.minecrell.at.AccessTransformSet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public abstract class AbstractAccessTransformFormat implements AccessTransformFormat {

    protected abstract void read(BufferedReader reader, AccessTransformSet set) throws IOException;

    @Override
    public void read(Reader reader, AccessTransformSet set) throws IOException {
        if (reader instanceof BufferedReader) {
            read((BufferedReader) reader, set);
        } else {
            read(new BufferedReader(reader), set);
        }
    }

    protected abstract void write(BufferedWriter writer, AccessTransformSet set) throws IOException;

    @Override
    public void write(Writer writer, AccessTransformSet set) throws IOException {
        if (writer instanceof BufferedWriter) {
            write((BufferedWriter) writer, set);
        } else {
            write(new BufferedWriter(writer), set);
        }
    }

}
