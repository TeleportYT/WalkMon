package com.vik.test;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import java.util.List;


public class PlayerController
{
    public PerspectiveCamera cam;
    public Vector3 playerMove;
    public float hp;


    public static Vector3 position;
    float moveSpeed = 2f;
    private Vector3 moveVector;
    private Vector3 tmpVector;



    private GameUI knob;
    private boolean attack=false;
    public static BulletManager blManager;


    public double attackTimer = 0;


    public PlayerController(PerspectiveCamera cam) {
        hp = 100f;
        this.cam = cam;
        position = new Vector3(MyClass.mapLevel.startX+0.5f,0.5f,MyClass.mapLevel.startY+0.5f);
        moveVector = new Vector3();
        tmpVector = new Vector3();

        playerMove = new Vector3();
        // load player rigid body
        this.knob = MyClass.GameUI;
        blManager = new BulletManager();
    }

    public void update() {
        blManager.Update();
        MovePlayer(this.knob.th.getKnobPercentX(),this.knob.th.getKnobPercentY());
        RotateHead(this.knob.shoot.getKnobPercentX(),this.knob.shoot.getKnobPercentY());
        if (attackTimer >= 10){
            attack = true;
            attackTimer = 0;
        }
        else if(!attack){
            attackTimer += (0.015/Gdx.graphics.getDeltaTime());
            Log.d("Shot","Timer: "+ attackTimer+" can attack: "+attack);
        }

        if(this.knob.shoot.isTouched() &&  attack){
            Fire();
            attack = false;
            attackTimer  = 0;
        }

        if(hp<100){
            hp+= 1*Gdx.graphics.getDeltaTime();
        }

    }
    private float curY =0,curX = 0;

    public void RotateHead(float x,float y){
        float dt = Gdx.graphics.getDeltaTime();
        Vector3 tmpView = new Vector3(0,1,0);
        Vector3 rotateV = new Vector3();
        rotateV.setZero();
        int angleX = 0;
        int angleY = 0;
        //Movement
        if (y>0) {
            curY =y*moveSpeed  ;
        }
        if (y<0) {
            curY =y*moveSpeed;
        }
        if (x<0){
            curX = x;
        }
        if (x>0){
            curX = x;
        }


        cam.direction.x += curX*dt;


        cam.direction.y=curY;
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug("Player Head",""+position.x+","+position.y+","+position.z);
        Gdx.app.debug("Player Head",""+x+","+y);
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

        if (MyClass.mapLevel.getCollision((int)(position.x + moveVector.x + colX), (int)position.z) != 0)
            position.add(moveVector.x, 0, 0);
        if (MyClass.mapLevel.getCollision((int)position.x, (int)(position.z + moveVector.z + colZ)) != 0)
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

    public void Fire(){
        Vector3 tmp = new Vector3();
        tmp.set(-cam.direction.x, -cam.direction.y, -cam.direction.z);
        blManager.AddBullet(new Bullet(10,10f,position,tmp));
    }


    public void Damage(float damage){

        if(this.hp>0){
            this.hp -= damage;
        }
        Intent intent=new Intent("Player Damaged");
        intent.putExtra("Player Health", this.hp);
        MyClass.context.sendBroadcast(intent);
        if(this.hp<=0){
          Die();
        }
    }

    public void Die(){
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug("Player Dead","You are dead");
        //cam.
    }

}
