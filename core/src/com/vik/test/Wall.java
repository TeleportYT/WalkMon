package com.vik.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class Wall {
    private Model md;
    private GameObject obj;
    private ModelInstance mi;
    private float x,z;


    public Wall(float x, float z,World world){
        ModelBuilder modelBuilder = new ModelBuilder();
        this.md = modelBuilder.createBox(1f, 1f, 1f,
                new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        this.mi = new ModelInstance(this.md);
        this.x = x;
        this.z = z;
        this.mi.transform.setToTranslation(x+0.5f,0.5f,z+0.5f);
        BoundingBox bx = new BoundingBox();
        Vector3 localInertia = new Vector3();
        mi.calculateBoundingBox(bx);
        btCollisionShape shape = new btBoxShape(bx.getDimensions(localInertia));
        obj = new GameObject.Constructor(md,"Wall",shape,world,0f).construct();
        obj.getRigidBody().proceedToTransform(mi.transform);
        obj.getRigidBody().setCollisionFlags(obj.getRigidBody().getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        // set id to find with collision detection
        obj.getRigidBody().setUserValue(2);
        obj.getRigidBody().setWorldTransform(mi.transform);
    }

    public ModelInstance getMi(){
        return  this.mi;
    }
}
