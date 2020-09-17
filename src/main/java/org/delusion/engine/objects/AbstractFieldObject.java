package org.delusion.engine.objects;

import org.joml.Vector2f;

// Author andy
// Created 11:50 AM
public abstract class AbstractFieldObject extends BasicObject {

    private final Vector2f position;
    private final Vector2f size;

    public AbstractFieldObject(Vector2f position, Vector2f size) {

        this.position = position;
        this.size = size;
    }


}
