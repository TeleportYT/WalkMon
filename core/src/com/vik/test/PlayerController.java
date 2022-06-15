package com.vik.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import java.util.ArrayList;
import java.util.List;

public class PlayerController
{
    private btRigidBody playerBody;
    private ModelInstance player;
    private float speed = 1f;
    private PerspectiveCamera cam;
    private static final int PLAYER = 1;
    public static float xposition;
    public static float yposition;
    private btDynamicsWorld dynamicsWorld;
    public static float angleX;

    public PlayerController(List<ModelInstance> instances,PerspectiveCamera cam,btDynamicsWorld dynamicWorld) {
        this.cam = cam;
        this.dynamicsWorld = dynamicWorld;

        player = new ModelInstance(new ModelBuilder()
                .createCapsule(0.25f, 3, 10, new Material(ColorAttribute.createAmbient(Color.BLACK)), VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position)
        );
        player.transform.translate(0, 5, 0);
        instances.add(player);
        // load player rigid body
        btCapsuleShape playerShape = new btCapsuleShape(0.25f, 2.5f);
        float mass = 10;
        Vector3 localInertia = new Vector3();
        playerShape.calculateLocalInertia(mass, localInertia);
        playerBody = new btRigidBody(mass, null, playerShape, localInertia);
        playerBody.proceedToTransform(player.transform);
        playerBody.setCollisionFlags(playerBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        // set id to find with collision detection
        playerBody.setUserValue(PLAYER);
        dynamicsWorld.addRigidBody(playerBody);


    }

    public void update() {
        // make sure to activate the player body so bullet doesnt put it to sleep
        playerBody.activate();
        // prevent the capsule from falling over
        playerBody.setAngularFactor(new Vector3(0, 0, 0));
        playerBody.getWorldTransform(player.transform);
        player.transform.rotate(Vector3.Y,angleX);

        cam.position.set(player.transform.getTranslation(new Vector3()));
        cam.update();



    }
}
