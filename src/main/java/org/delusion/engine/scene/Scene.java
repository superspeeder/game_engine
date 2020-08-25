package org.delusion.engine.scene;

import org.delusion.engine.component.SceneComponent;
import org.joml.Rectanglef;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Author andy
// Created 6:33 PM
public class Scene {
    private int tx, ty;
    private Map<Class<? extends SceneComponent>, List<SceneComponent>> components = new HashMap<>();

    private Map<Vector2i, Boolean> collisionTileMask = new HashMap<>();

    public <T extends SceneComponent> void addComponent(T component) {
        @SuppressWarnings("unchecked")
        Class<T> componentType = (Class<T>) component.getClass();
        if (this.components.containsKey(componentType)) {
            this.components.get(componentType).add(component);
        } else {
            this.components.put(componentType, new ArrayList<>());
            this.components.get(componentType).add(component);
        }
    }

    public <T extends SceneComponent> List<SceneComponent> getComponentsByType(Class<T> type) {
        return components.get(type);
    }

    public Scene(int tx, int ty) {
        this.tx = tx;
        this.ty = ty;
    }

    public void addCollidersRect(Vector2i min, Vector2i max) {
        for (int x = min.x; x < max.x; x++) {
            for (int y = min.y; y < max.y; y++) {
                addCollider(x,y);
            }
        }
    }

    public void addCollider(int x, int y) {
        collisionTileMask.put(new Vector2i(x,y), true);
    }

    public void removeCollider(int x, int y) {
        collisionTileMask.put(new Vector2i(x,y), false);
    }

    public boolean getCollides(int x, int y) {
        return collisionTileMask.getOrDefault(new Vector2i(x,y), false);
    }

    public void printCollisionTiles() {
        for (Vector2i v : collisionTileMask.keySet()) {
            System.out.println(v.x * tx + ", " + v.y * ty);
        }
    }

    public boolean pointcollide(double x, double y) {
        int tile_x = (int) (x / tx);
        int tile_y = (int) (y / ty);

        boolean c = getCollides(tile_x, tile_y);

        System.out.println(tile_x + ", " + tile_y + "  " + tile_x * tx + ", " + tile_y * ty + "  " + c);

        return c;
    }

    public boolean rectCollide(Rectanglef boundingBox) {
        int min_tx = (int) (boundingBox.minX / tx);
        int max_tx = (int) (boundingBox.maxX / tx);
        int min_ty = (int) (boundingBox.minY / ty);
        int max_ty = (int) (boundingBox.maxY / ty);
        if (min_tx == 0 && boundingBox.minX < 0) {
            min_tx = -1;
        }
        if (min_ty == 0 && boundingBox.minY < 0) {
            min_ty = -1;
        }
        if (max_tx == 0 && boundingBox.maxX < 0) {
            max_tx = -1;
        }
        if (max_ty == 0 && boundingBox.maxY < 0) {
            max_ty = -1;
        }

//        System.out.println("boundingBox = " + boundingBox);
//        System.out.println(min_tx + ", " + min_ty + " ,, " + max_tx + ", " + max_ty);

        return getCollides(min_tx,min_ty) ||
                getCollides(max_tx,min_ty) ||
                getCollides(min_tx,max_ty) ||
                getCollides(max_tx,max_ty);
    }

    public List<Vector2i> getCollisions(Rectanglef boundingBox) {
        int min_tx = (int) (boundingBox.minX / tx);
        int max_tx = (int) (boundingBox.maxX / tx);
        int min_ty = (int) (boundingBox.minY / ty);
        int max_ty = (int) (boundingBox.maxY / ty);
        if (min_tx == 0 && boundingBox.minX < 0) {
            min_tx = -1;
        }
        if (min_ty == 0 && boundingBox.minY < 0) {
            min_ty = -1;
        }
        if (max_tx == 0 && boundingBox.maxX < 0) {
            max_tx = -1;
        }
        if (max_ty == 0 && boundingBox.maxY < 0) {
            max_ty = -1;
        }

//        System.out.println("boundingBox = " + boundingBox);
//        System.out.println(min_tx + ", " + min_ty + " ,, " + max_tx + ", " + max_ty);

        boolean t_nxny = getCollides(min_tx,min_ty);
        boolean t_mxny = getCollides(max_tx,min_ty);
        boolean t_nxmy = getCollides(min_tx,max_ty);
        boolean t_mxmy = getCollides(max_tx,max_ty);

        List<Vector2i> tilesC = new ArrayList<>();
        if (t_nxny) {
            tilesC.add(new Vector2i(min_tx,min_ty));
        }
        if (t_mxny) {
            tilesC.add(new Vector2i(max_tx, min_ty));
        }
        if (t_nxmy) {
            tilesC.add(new Vector2i(min_tx,max_ty));
        }
        if (t_mxmy) {
            tilesC.add(new Vector2i(max_tx,max_ty));
        }

        return tilesC;
    }
}
