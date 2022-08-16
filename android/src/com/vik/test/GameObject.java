package com.vik.test;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;

public class GameObject extends ModelInstance implements Disposable {

    public btCollisionObject getBody() {
        return body;
    }

    public btRigidBody getRigidBody() {
        return rigidBody;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public final btCollisionObject body;
    public final btRigidBody rigidBody;
    public boolean moving;

    public GameObject(Model model, String node, btCollisionShape shape,btRigidBody rigidBody) {
        super(model, node);
        body = new btCollisionObject();
        body.setCollisionShape(shape);
        this.rigidBody = rigidBody;
    }

    @Override
    public void dispose() {
        body.dispose();
    }

    public static class Constructor implements Disposable {
        public final Model model;
        public final String node;
        public final btCollisionShape shape;
        public final btRigidBody rigidBody;
        public Constructor(Model model, String node, btCollisionShape shape,World world,float mass) {
            this.model = model;
            this.node = node;
            this.shape = shape;
            this.rigidBody = new btRigidBody(mass, null, this.shape, new Vector3());
            world.AddRigidBody(rigidBody,mass);
        }

        public GameObject construct() {
            return new GameObject(model, node, shape,this.rigidBody);
        }

        @Override
        public void dispose () {
            shape.dispose();
        }
    }

}
