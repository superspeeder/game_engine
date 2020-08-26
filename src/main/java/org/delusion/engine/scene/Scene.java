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
        for (int x = min.x; x <= max.x; x++) {
            for (int y = min.y; y <= max.y; y++) {
                addCollider(x,y);
                System.out.println(x + ", " + y);
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
//            System.out.println(v.x * tx + ", " + v.y * ty);
        }
    }

    public boolean pointcollide(double x, double y) {
        int tile_x = (int) (x / tx);
        int tile_y = (int) (y / ty);

        boolean c = getCollides(tile_x, tile_y);

//        System.out.println(tile_x + ", " + tile_y + "  " + tile_x * tx + ", " + tile_y * ty + "  " + c);

        return c;
    }

    public boolean rectCollide(Rectanglef boundingBox) {
        float min_tx = (boundingBox.minX + 1) / tx;
        float max_tx = (boundingBox.maxX - 1) / tx;
        float min_ty = (boundingBox.minY + 1) / ty;
        float max_ty = (boundingBox.maxY - 1) / ty;

        if ((int)min_tx == 0 && boundingBox.minX <= 0) {
            min_tx -= 1;
        }
        if ((int)min_ty == 0 && boundingBox.minY <= 0) {
            min_ty -= 1;
        }
        if ((int)max_tx == 0 && boundingBox.maxX <= 0) {
            max_tx -= 1;
        }
        if ((int)max_ty == 0 && boundingBox.maxY <= 0) {
            max_ty -= 1;
        }

        int min_tx_i = (int)Math.floor(min_tx);
        int max_tx_i = (int)Math.floor(max_tx);
        int min_ty_i = (int)Math.floor(min_ty);
        int max_ty_i = (int)Math.floor(max_ty);

//        System.out.println("boundingBox = " + boundingBox);
//        System.out.println(min_tx + ", " + min_ty + " ,, " + max_tx + ", " + max_ty);

        return getCollides(min_tx_i,min_ty_i) ||
                getCollides(max_tx_i,min_ty_i) ||
                getCollides(min_tx_i,max_ty_i) ||
                getCollides(max_tx_i,max_ty_i);
    }

    public List<Vector2i> getCollisions(Rectanglef boundingBox) {
        float min_tx = (boundingBox.minX + 1) / tx;
        float max_tx = (boundingBox.maxX - 1) / tx;
        float min_ty = (boundingBox.minY + 1) / ty;
        float max_ty = (boundingBox.maxY - 1) / ty;

        if ((int)min_tx == 0 && boundingBox.minX <= 0) {
            min_tx -= 1;
        }
        if ((int)min_ty == 0 && boundingBox.minY <= 0) {
            min_ty -= 1;
        }
        if ((int)max_tx == 0 && boundingBox.maxX <= 0) {
            max_tx -= 1;
        }
        if ((int)max_ty == 0 && boundingBox.maxY <= 0) {
            max_ty -= 1;
        }


        int min_tx_i = (int)Math.floor(min_tx);
        int max_tx_i = (int)Math.floor(max_tx);
        int min_ty_i = (int)Math.floor(min_ty);
        int max_ty_i = (int)Math.floor(max_ty);

        boolean t_nxny = getCollides(min_tx_i,min_ty_i);
        boolean t_mxny = getCollides(max_tx_i,min_ty_i);
        boolean t_nxmy = getCollides(min_tx_i,max_ty_i);
        boolean t_mxmy = getCollides(max_tx_i,max_ty_i);

        List<Vector2i> tilesC = new ArrayList<>();
        if (t_nxny) {
            tilesC.add(new Vector2i(min_tx_i*tx,min_ty_i*ty));
        }
        if (t_mxny) {
            tilesC.add(new Vector2i(max_tx_i*tx, min_ty_i*ty));
        }
        if (t_nxmy) {
            tilesC.add(new Vector2i(min_tx_i*tx,max_ty_i*ty));
        }
        if (t_mxmy) {
            tilesC.add(new Vector2i(max_tx_i*tx,max_ty_i*ty));
        }

        return tilesC;
    }

    public int getTX() {
        return tx;
    }

    public int getTY() {
        return ty;
    }
}
