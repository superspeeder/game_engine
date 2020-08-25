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

import java.lang.Math;
import java.util.List;

// Author andy
// Created 11:28 AM
public class Player extends Sprite implements CameraController, InputHandler {
    private static final Vector2f SIZE = new Vector2f(32,32);
    private Matrix4f modelMat = new Matrix4f();
    private Quad quad = new Quad(new Vector3f(0,0,0), new Vector3f(1,1,1),new Vector4f(0,0,1,1));

    public Player(Vector4f startingUVs, Vector2f position) {
        super(position);
        quad.setUVs(startingUVs);
        System.out.println("position = " + position);
        modelMat
                .translate(position.x,position.y,0)
                .scale(SIZE.x,SIZE.y,1)
                ;
    }

    @Override
    public Vector2f getExpectedCameraPosition() {
        return null;

    }

    public void move(Vector2f delta, Scene scene, Camera camera) {

        position.add(delta.x,0);

        if (scene.rectCollide(getBoundingBox())) {
            System.out.println("E x");
            if (delta.x > 0) { // going right
                List<Vector2i> collidingTiles = scene.getCollisions(getBoundingBox());
                int max_c_x = (int) position.x;
                for (Vector2i collidingTile : collidingTiles) {
                    if (collidingTile.x < max_c_x) {
                        max_c_x = collidingTile.x;
                    }
                }
                position.set(max_c_x - getSize().x,position.y);
            } else if (delta.x < 0) { // going left
                List<Vector2i> collidingTiles = scene.getCollisions(getBoundingBox());
                int max_c_x = (int) position.x;
                for (Vector2i collidingTile : collidingTiles) {
                    if (collidingTile.x > max_c_x) {
                        max_c_x = collidingTile.x;
                    }
                }
                position.set(max_c_x,position.y);
            }
        }


        position.add(0,delta.y);

        if (scene.rectCollide(getBoundingBox())) {
            System.out.println("E y");
            if (delta.y > 0) { // going right
                List<Vector2i> collidingTiles = scene.getCollisions(getBoundingBox());
                int max_c_y = (int) position.y;
                for (Vector2i collidingTile : collidingTiles) {
                    if (collidingTile.y < max_c_y) {
                        max_c_y = collidingTile.y;
                    }
                }
                position.set(position.x,max_c_y - getSize().y);
            } else if (delta.y < 0) { // going left
                List<Vector2i> collidingTiles = scene.getCollisions(getBoundingBox());
                int max_c_y = (int) position.y;
                for (Vector2i collidingTile : collidingTiles) {
                    if (collidingTile.y > max_c_y) {
                        max_c_y = collidingTile.y;
                    }
                }
                position.set(position.x,max_c_y);
            }
        }


        camera.setPosition(position);



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
