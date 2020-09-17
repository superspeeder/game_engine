package org.delusion.engine.objects;

// Author andy
// Created 11:48 AM
@FunctionalInterface
public interface ObjectConstructor<T> {

    T create();
}
