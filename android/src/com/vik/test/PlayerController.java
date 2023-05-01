package com.vik.test;
import android.content.Intent;
import android.util.Log;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

import Enemys.EnemyType;


public class PlayerController
{
    public PerspectiveCamera cam;

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
        this.hp = 100f;
        this.cam = cam;
        this.position = new Vector3(Game.mapLevel.startX+0.5f,0.5f, Game.mapLevel.startY+0.5f);
        this.moveVector = new Vector3();
        this.tmpVector = new Vector3();

        // load player rigid body
        this.knob = Game.GameUI;
        this.blManager = new BulletManager();
    }

    public void update() {
        this.blManager.Update();
        MovePlayer(this.knob.th.getKnobPercentX(),this.knob.th.getKnobPercentY());
        if (this.attackTimer >= 50){
            this.attack = true;
            this.attackTimer = 0;
        }
        else if(!this.attack){
            this.attackTimer += (0.015/Gdx.graphics.getDeltaTime() * 10);
            Log.d("Shot","Timer: "+ this.attackTimer+" can attack: "+this.attack);
        }

        if(hp<100){
            this.hp+= 1*Gdx.graphics.getDeltaTime();
        }

    }


    public void MovePlayer(float x,float y){
        boolean isWalking = false;
        float dt = Gdx.graphics.getDeltaTime();

        this.moveVector.setZero();

        //Movement
        if (y>0) {
            this.tmpVector.set(this.cam.direction);
            this.tmpVector.y = 0;
            this.moveVector.add(this.tmpVector.nor().scl(dt * this.moveSpeed));
            isWalking = true;
        }
        if (y<0) {
            this.tmpVector.set(this.cam.direction);
            this.tmpVector.y = 0;
            this.moveVector.add(this.tmpVector.nor().scl(dt * -this.moveSpeed));
            isWalking = true;
        }
        if (x<0){
            this.tmpVector.set(this.cam.direction).crs(this.cam.up);
            this.tmpVector.y = 0;
            this.moveVector.add(this.tmpVector.nor().scl(dt * -this.moveSpeed));
            isWalking = true;
        }
        if (x>0){
            this.tmpVector.set(this.cam.direction).crs(this.cam.up);
            this.tmpVector.y = 0;
            this.moveVector.add(this.tmpVector.nor().scl(dt * this.moveSpeed));
            isWalking = true;
        }

        float colX = this.moveVector.x==0 ? 0 : (this.moveVector.x>0 ? .25f : -0.25f);
        float colZ = this.moveVector.z==0 ? 0 : (this.moveVector.z>0 ? .25f : -.25f);

        if (Game.mapLevel.getCollision((int)(this.position.x + this.moveVector.x + colX), (int)this.position.z) != 0)
            this.position.add(this.moveVector.x, 0, 0);
        if (Game.mapLevel.getCollision((int)this.position.x, (int)(this.position.z + this.moveVector.z + colZ)) != 0)
            this.position.add(0, 0, this.moveVector.z);

        if(this.position.x < 0){
            this.position.x = 0;
        }
        if(this.position.z < 0){
            this.position.z = 0;
        }

        this.cam.position.set(this.position.x, this.position.y, this.position.z);




        Game.sd.Walk(isWalking);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug("Player Position",""+this.position.x+","+this.position.y+","+this.position.z);
    }

    public void Fire(){
        if (!this.attack){
            return;
        }
        Vector3 tmp = new Vector3();
        tmp.set(-this.cam.direction.x, -this.cam.direction.y, -this.cam.direction.z);
        this.blManager.AddBullet(new Bullet(10,10f,this.position,tmp));
        this.attack = false;
        this.attackTimer  = 0;
        Game.sd.Shoot();
    }


    public void Damage(float damage, EnemyType Type){

        if(this.hp>0){
            this.hp -= damage;
        }
        Intent intent=new Intent("Player Damaged");
        intent.putExtra("Player Health", this.hp);
        intent.putExtra("Type",""+Type);
        ((AndroidApplication) Gdx.app).getContext().sendBroadcast(intent);
        if(this.hp<=0){
            this.Die();
        }
    }

    public void Die(){
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug("Player Dead","You are dead");
        Intent intent=new Intent("Player Dead");
        ((AndroidApplication) Gdx.app).getContext().sendBroadcast(intent);
    }

}
