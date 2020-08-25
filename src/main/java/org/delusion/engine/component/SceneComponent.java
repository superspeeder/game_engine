package org.delusion.engine.component;

import org.delusion.engine.scene.Scene;

// Author andy
// Created 7:42 AM
public abstract class SceneComponent {

    private Scene owningScene;

    public void AddToScene(Scene scene) {
        owningScene = scene;
        OnAddedToScene();
    }


    public void OnAddedToScene() { }
    public void OnUpdate(float dt) { }
    public void OnRemovedFromScene() { }
}
