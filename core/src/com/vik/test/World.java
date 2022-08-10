package com.vik.test;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.Array;

public class World {
    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public btDynamicsWorld getDynamicsWorld() {
        return dynamicsWorld;
    }

    public void setDynamicsWorld(btDynamicsWorld dynamicsWorld) {
        this.dynamicsWorld = dynamicsWorld;
    }

    public btDefaultCollisionConfiguration getCollisionConfig() {
        return collisionConfig;
    }

    public void setCollisionConfig(btDefaultCollisionConfiguration collisionConfig) {
        this.collisionConfig = collisionConfig;
    }

    public btCollisionDispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(btCollisionDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public btDbvtBroadphase getBroadphase() {
        return broadphase;
    }

    public void setBroadphase(btDbvtBroadphase broadphase) {
        this.broadphase = broadphase;
    }

    public btConstraintSolver getConstraintSolver() {
        return constraintSolver;
    }

    public void setConstraintSolver(btConstraintSolver constraintSolver) {
        this.constraintSolver = constraintSolver;
    }

    private Environment environment;
    private btDynamicsWorld dynamicsWorld;
    private btDefaultCollisionConfiguration collisionConfig;
    private btCollisionDispatcher dispatcher;
    private btDbvtBroadphase broadphase;
    private btConstraintSolver constraintSolver;

    public World(){
        this.environment = new Environment();
        this.environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 100f));
        this.environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        Bullet.init();
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        dynamicsWorld.setGravity(new Vector3(0, -10f, 0));



    }

    public void Update(float delta) {
        dynamicsWorld.stepSimulation(delta);
    }

    public void AddRigidBody(btRigidBody body,float mass){
        Vector3 localInertia = new Vector3();
        Bullet.init();
        body.getCollisionShape().calculateLocalInertia(mass,localInertia);
        body.setMassProps(mass,localInertia);
        body.updateInertiaTensor();
        body.activate();

        this.dynamicsWorld.addRigidBody(body);
    }

    public void Dispose() {
        this.dynamicsWorld.dispose();
        this.broadphase.dispose();
        this.collisionConfig.dispose();
        this.constraintSolver.dispose();
        this.dispatcher.dispose();
    }

}
