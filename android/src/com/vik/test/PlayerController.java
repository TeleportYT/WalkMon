package com.vik.test;
import android.content.Intent;
import android.util.Log;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

import Enemys.EnemyType;


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
        position = new Vector3(Game.mapLevel.startX+0.5f,0.5f, Game.mapLevel.startY+0.5f);
        moveVector = new Vector3();
        tmpVector = new Vector3();

        playerMove = new Vector3();
        // load player rigid body
        this.knob = Game.GameUI;
        blManager = new BulletManager();
    }

    public void update() {
        blManager.Update();
        MovePlayer(this.knob.th.getKnobPercentX(),this.knob.th.getKnobPercentY());
        if (attackTimer >= 50){
            attack = true;
            attackTimer = 0;
        }
        else if(!attack){
            attackTimer += (0.015/Gdx.graphics.getDeltaTime() * 10);
            Log.d("Shot","Timer: "+ attackTimer+" can attack: "+attack);
        }

        if(hp<100){
            hp+= 1*Gdx.graphics.getDeltaTime();
        }

    }


    public void MovePlayer(float x,float y){
        boolean isWalking = false;
        float dt = Gdx.graphics.getDeltaTime();

        moveVector.setZero();

        //Movement
        if (y>0) {
            tmpVector.set(cam.direction);
            tmpVector.y = 0;
            moveVector.add(tmpVector.nor().scl(dt * moveSpeed));
            isWalking = true;
        }
        if (y<0) {
            tmpVector.set(cam.direction);
            tmpVector.y = 0;
            moveVector.add(tmpVector.nor().scl(dt * -moveSpeed));
            isWalking = true;
        }
        if (x<0){
            tmpVector.set(cam.direction).crs(cam.up);
            tmpVector.y = 0;
            moveVector.add(tmpVector.nor().scl(dt * -moveSpeed));
            isWalking = true;
        }
        if (x>0){
            tmpVector.set(cam.direction).crs(cam.up);
            tmpVector.y = 0;
            moveVector.add(tmpVector.nor().scl(dt * moveSpeed));
            isWalking = true;
        }

        float colX = moveVector.x==0 ? 0 : (moveVector.x>0 ? .25f : -0.25f);
        float colZ = moveVector.z==0 ? 0 : (moveVector.z>0 ? .25f : -.25f);

        if (Game.mapLevel.getCollision((int)(position.x + moveVector.x + colX), (int)position.z) != 0)
            position.add(moveVector.x, 0, 0);
        if (Game.mapLevel.getCollision((int)position.x, (int)(position.z + moveVector.z + colZ)) != 0)
            position.add(0, 0, moveVector.z);

        if(position.x < 0){
            position.x = 0;
        }
        if(position.z < 0){
            position.z = 0;
        }

        cam.position.set(position.x, position.y, position.z);




        Game.sd.Walk(isWalking);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug("Player Position",""+position.x+","+position.y+","+position.z);
    }

    public void Fire(){
        if (!attack){
            return;
        }
        Vector3 tmp = new Vector3();
        tmp.set(-cam.direction.x, -cam.direction.y, -cam.direction.z);
        blManager.AddBullet(new Bullet(10,10f,position,tmp));
        attack = false;
        attackTimer  = 0;
        Game.sd.Shoot();
    }


    public void Damage(float damage, EnemyType Type){

        if(this.hp>0){
            this.hp -= damage;
        }
        Intent intent=new Intent("Player Damaged");
        intent.putExtra("Player Health", this.hp);
        intent.putExtra("Type",""+Type);
        Game.context.sendBroadcast(intent);
        if(this.hp<=0){
          Die();
        }
    }

    public void Die(){
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug("Player Dead","You are dead");
        Intent intent=new Intent("Player Dead");
        Game.context.sendBroadcast(intent);
    }

}
