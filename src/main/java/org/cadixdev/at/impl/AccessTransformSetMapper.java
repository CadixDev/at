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

package org.cadixdev.at.impl;

import org.cadixdev.at.AccessTransformSet;
import org.cadixdev.bombe.type.signature.MethodSignature;
import org.cadixdev.lorenz.MappingSet;
import org.cadixdev.lorenz.model.ClassMapping;
import org.cadixdev.lorenz.model.FieldMapping;
import org.cadixdev.lorenz.model.Mapping;
import org.cadixdev.lorenz.model.MethodMapping;

import java.util.Objects;
import java.util.Optional;

final class AccessTransformSetMapper {

    private AccessTransformSetMapper() {
    }

    static AccessTransformSet remap(AccessTransformSet set, MappingSet mappings) {
        Objects.requireNonNull(set, "set");
        Objects.requireNonNull(mappings, "mappings");

        AccessTransformSet remapped = AccessTransformSet.create();
        set.getClasses().forEach((className, classSet) -> {
            Optional<? extends ClassMapping<?, ?>> mapping = mappings.getClassMapping(className);
            remap(mapping.orElse(null), classSet, remapped.getOrCreateClass(mapping.map(Mapping::getFullDeobfuscatedName).orElse(className)));
        });
        return remapped;
    }

    private static void remap(ClassMapping<?, ?> mapping, AccessTransformSet.Class set, AccessTransformSet.Class remapped) {
        remapped.merge(set.get());
        remapped.mergeAllFields(set.allFields());
        remapped.mergeAllMethods(set.allMethods());

        if (mapping == null) {
            set.getFields().forEach(remapped::mergeField);
            set.getMethods().forEach(remapped::mergeMethod);
        } else {
            set.getFields().forEach((name, transform) ->
                remapped.mergeField(
                    mapping.getFieldMapping(name)
                        .map(FieldMapping::getDeobfuscatedName)
                        .orElse(name),
                    transform
                )
            );

            set.getMethods().forEach((signature, transform) -> {
                remapped.mergeMethod(
                    mapping.getMethodMapping(signature)
                        .map(MethodMapping::getDeobfuscatedSignature)
                        .orElseGet(() -> new MethodSignature(
                            signature.getName(),
                            mapping.getMappings().deobfuscate(signature.getDescriptor())
                        )),
                    transform
                );
            });
        }
    }
}
