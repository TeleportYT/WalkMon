package com.vik.test;
import android.content.Context;
import android.content.Intent;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
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

import java.util.List;


public class PlayerController
{
    private btRigidBody playerBody;
    private ModelInstance player;
    public PerspectiveCamera cam;
    private static final int PLAYER = 1;
    public Vector3 playerMove;
    private btDynamicsWorld dynamicsWorld;
    public static float hp;
    private Level lvl;

    public static Vector3 position;

    float moveSpeed = 2f;

    private Vector3 moveVector;
    private Vector3 tmpVector;

    private GameUI knob;
    private List<Enemy> enemys;
    private List<ModelInstance> inst;
    private boolean attack=false;
    private Context ct;

    private float speed = 5f;

    public PlayerController(List<ModelInstance> instances, PerspectiveCamera cam, btDynamicsWorld dynamicWorld, Level lvl, GameUI ui, List<Enemy> enemys, Context ct) {
        this.ct = ct;
        hp = 100f;
        this.cam = cam;
        this.dynamicsWorld = dynamicWorld;
        this.inst = instances;
        position = new Vector3(lvl.startX+0.5f,0.5f,lvl.startY+0.5f);
        moveVector = new Vector3();
        tmpVector = new Vector3();

        playerMove = new Vector3();
        player = new ModelInstance(new ModelBuilder()
                .createCapsule(0.2f, 3, 10, new Material(ColorAttribute.createAmbient(Color.BLACK)), VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position)
        );
        instances.add(player);
        // load player rigid body
        btCapsuleShape playerShape = new btCapsuleShape(1f, 3f);
        float mass = 10;
        Vector3 localInertia = new Vector3();
        playerShape.calculateLocalInertia(mass, localInertia);
        playerBody = new btRigidBody(mass, null, playerShape, localInertia);
        playerBody.proceedToTransform(player.transform);
        playerBody.setCollisionFlags(playerBody.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
        // set id to find with collision detection
        playerBody.setUserValue(PLAYER);
        dynamicsWorld.addRigidBody(playerBody);
        this.knob = ui;

        this.lvl = lvl;

        this.enemys = enemys;
    }

    public void update() {
        // make sure to activate the player body so bullet doesnt put it to sleep
        playerBody.activate();
        MovePlayer(this.knob.th.getKnobPercentX(),this.knob.th.getKnobPercentY());
        if(this.knob.shotBt.isPressed() && !attack){
            attack = true;
            Fire(enemys);
        }
        if(!this.knob.shotBt.isPressed()){
            attack = false;
        }

        if(hp<100){
            hp+=1*Gdx.graphics.getDeltaTime();
        }


    }

    public void MovePlayer(float x,float y){

        float dt = Gdx.graphics.getDeltaTime();

        moveVector.setZero();

        //Movement
        if (y>0) {
            tmpVector.set(cam.direction);
            tmpVector.y = 0;
            moveVector.add(tmpVector.nor().scl(dt * moveSpeed));
        }
        if (y<0) {
            tmpVector.set(cam.direction);
            tmpVector.y = 0;
            moveVector.add(tmpVector.nor().scl(dt * -moveSpeed));
        }
        if (x<0){
            tmpVector.set(cam.direction).crs(cam.up);
            tmpVector.y = 0;
            moveVector.add(tmpVector.nor().scl(dt * -moveSpeed));
        }
        if (x>0){
            tmpVector.set(cam.direction).crs(cam.up);
            tmpVector.y = 0;
            moveVector.add(tmpVector.nor().scl(dt * moveSpeed));
        }

        float colX = moveVector.x==0 ? 0 : (moveVector.x>0 ? .25f : -0.25f);
        float colZ = moveVector.z==0 ? 0 : (moveVector.z>0 ? .25f : -.25f);

        if (lvl.getCollision((int)(position.x + moveVector.x + colX), (int)position.z) != 0)
            position.add(moveVector.x, 0, 0);
        if (lvl.getCollision((int)position.x, (int)(position.z + moveVector.z + colZ)) != 0)
            position.add(0, 0, moveVector.z);

        if(position.x < 0){
            position.x = 0;
        }
        if(position.z < 0){
            position.z = 0;
        }

        cam.position.set(position.x, position.y, position.z);


        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug("Player Position",""+position.x+","+position.y+","+position.z);
    }

    public void Fire(List<Enemy> enemys){
        Vector3 tmp = new Vector3();
        tmp.set(-cam.direction.x, -cam.direction.y, -cam.direction.z);
        MyClass.blManager.AddBullet(new Bullet(10,10f,position,tmp,lvl));

    }


    public void Damage(float damage){

        if(this.hp>0){
            this.hp -= damage;
        }
        Intent intent=new Intent("Player Damaged");
        intent.putExtra("Player Health", this.hp);
        this.ct.sendBroadcast(intent);
        if(this.hp<=0){
          Die();
        }
    }

    public void Die(){
        inst.remove(this.player);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug("Player Dead","You are dead");
        //cam.
    }

    public ModelInstance getPlayer(){
        return player;
    }

}
