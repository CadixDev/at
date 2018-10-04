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

import org.cadixdev.at.impl.AccessTransformSetImpl;
import org.cadixdev.bombe.analysis.InheritanceCompletable;
import org.cadixdev.bombe.analysis.InheritanceProvider;
import org.cadixdev.bombe.type.signature.MethodSignature;
import org.cadixdev.lorenz.MappingSet;

import java.util.Map;
import java.util.Optional;

public interface AccessTransformSet {

    Map<String, Class> getClasses();
    Optional<Class> getClass(String name);
    Class getOrCreateClass(String name);
    Optional<Class> removeClass(String name);

    AccessTransformSet remap(MappingSet mappings);
    void merge(AccessTransformSet other);

    static AccessTransformSet create() {
        return new AccessTransformSetImpl();
    }

    interface Class extends InheritanceCompletable {
        AccessTransformSet getParent();
        String getName();

        AccessTransform get();
        AccessTransform merge(AccessTransform transform);
        AccessTransform replace(AccessTransform transform);

        AccessTransform allFields();
        AccessTransform mergeAllFields(AccessTransform transform);
        AccessTransform replaceAllFields(AccessTransform transform);

        AccessTransform allMethods();
        AccessTransform mergeAllMethods(AccessTransform transform);
        AccessTransform replaceAllMethods(AccessTransform transform);

        Map<String, AccessTransform> getFields();
        AccessTransform getField(String name);
        AccessTransform mergeField(String name, AccessTransform transform);
        AccessTransform replaceField(String name, AccessTransform transform);

        Map<MethodSignature, AccessTransform> getMethods();
        AccessTransform getMethod(MethodSignature signature);
        AccessTransform mergeMethod(MethodSignature signature, AccessTransform transform);
        AccessTransform replaceMethod(MethodSignature signature, AccessTransform transform);

        void merge(Class other);

        @Override
        default Optional<InheritanceProvider.ClassInfo> provideInheritance(InheritanceProvider provider, Object context) {
            return provider.provide(getName(), context);
        }

    }

}
