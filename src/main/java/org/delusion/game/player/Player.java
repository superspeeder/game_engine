package org.delusion.game.player;

import org.delusion.engine.Textures;
import org.delusion.engine.camera.Camera;
import org.delusion.engine.camera.CameraController;
import org.delusion.engine.input.InputHandler;
import org.delusion.engine.renderer.scene.Quad;
import org.delusion.engine.renderer.texture.Texture2D;
import org.delusion.engine.scene.Scene;
import org.delusion.engine.sprite.Sprite;
import org.joml.*;

import java.awt.*;
import java.lang.Math;
import java.util.Arrays;
import java.util.List;

// Author andy
// Created 11:28 AM
public class Player extends Sprite implements CameraController, InputHandler {
    private static final Vector2f SIZE = new Vector2f(32,32);
    private Matrix4f modelMat = new Matrix4f();
    private Quad quad = new Quad(new Vector3f(0,0,0), new Vector3f(1,1,1),new Vector4f(0,0,1,1));
    private Vector2f origin;
    private Vector2f delta = new Vector2f();
    private boolean onGround;
    public int direction = 1;

    public Player(Vector4f startingUVs, Vector2f position) {
        super(position);
        quad.setUVs(startingUVs);
        origin = new Vector2f(position);
//        System.out.println("position = " + position);
        modelMat
                .translate(position.x,position.y,0)
                .scale(SIZE.x,SIZE.y,1)
                ;
    }

    @Override
    public Vector2f getExpectedCameraPosition() {
        return null;

    }

    private float yv = 0;
    private float G = -1.2f;
    private float LOWER_MAX = -24f;
    private float UPPER_MAX = 24f;
    private float JS = 31f;

    public void jump() {
        if (onGround) {
            yv += JS;
        }
    }

    public void move(Vector2f d) {
        delta.add(d);
    }


    @Override
    public void render() {

        super.render();
    }

    public void update(Scene scene, Camera camera) {

        yv += G;
        yv = Float.max(yv,LOWER_MAX);
        yv = Float.min(yv,UPPER_MAX);
        delta.add(0,yv);

        position.add(delta.x,0);
        onGround = false;

        if (scene.rectCollide(getBoundingBox()) && delta.x != 0) {

            List<Vector2i> collidingTiles = scene.getCollisions(getBoundingBox());
            if (delta.x > 0) { // going right

                int max_c_x = collidingTiles.get(0).x;
                Vector2i t;
                for (Vector2i collidingTile : collidingTiles) {
                    if (collidingTile.x < max_c_x) {
                        max_c_x = collidingTile.x;
                        t = collidingTile;
                    }
                }

                position.set(max_c_x - getSize().x,position.y);
            } else if (delta.x < 0) { // going left

                int max_c_x = collidingTiles.get(0).x;
                Vector2i t;
                for (Vector2i collidingTile : collidingTiles) {
                    if (collidingTile.x > max_c_x) {
                        max_c_x = collidingTile.x;
                        t = collidingTile;
                    }
                }

                position.set(max_c_x + scene.getTX(),position.y);
            }
        }


        position.add(0,delta.y);

        if (scene.rectCollide(getBoundingBox()) && delta.y != 0) {
            List<Vector2i> collidingTiles = scene.getCollisions(getBoundingBox());

            if (delta.y > 0) { // going up

                int max_c_y = collidingTiles.get(0).y;
                Vector2i t;
                for (Vector2i collidingTile : collidingTiles) {
                    if (collidingTile.y < max_c_y) {
                        max_c_y = collidingTile.y;
                        t = collidingTile;
                    }
                }
                yv = 0;

                position.set(position.x,max_c_y - getSize().y);
            } else if (delta.y < 0) { // going down

                int max_c_y = collidingTiles.get(0).y;
                Vector2i t;
                for (Vector2i collidingTile : collidingTiles) {
                    if (collidingTile.y > max_c_y) {
                        max_c_y = collidingTile.y;
                        t = collidingTile;
                    }
                }

                onGround = true;
                yv = 0;


                position.set(position.x,max_c_y + scene.getTY());
            }
        }


        camera.setPosition(new Vector2f(position).sub(origin).mul(-1));
        camera.update();
        if (delta.x > 0) {
            direction = 1;
        } else if (delta.x < 0) {
            direction = -1;
        }
        delta.set(0,0);

    }

    @Override
    public Quad getRenderQuad() {
        return quad;
    }

    @Override
    public Texture2D getTexture() {
        return Textures.PLAYER;
    }

    @Override
    public Matrix4f getModelMatrix() {
        return modelMat;
    }

    @Override
    public Vector2f getSize() {
        return SIZE;
    }

    public void bindTex(int i) {
        getTexture().bind(i);
    }
}
