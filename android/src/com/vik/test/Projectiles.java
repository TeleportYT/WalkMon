package com.vik.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import Enemys.Enemy;

public class Projectiles {

    protected ModelInstance model;
    protected float damage;
    protected float speed;
    protected Vector3 position;
    protected Vector3 direction;
    protected Level lvl;

    public Projectiles(float damage, float speed, Vector3 position, Vector3 direction,Level lvl,ModelInstance model){
        this.damage = damage;
        this.speed = speed;
        this.position = new Vector3();
        this.position.set(position);
        this.direction = new Vector3();
        this.direction.set(direction);
        this.lvl = lvl;
        this.model = model;
        Game.instances.add(model);

    }

    public void Update(){
        this.position.mulAdd(direction, -speed*Gdx.graphics.getDeltaTime());

        Quaternion quaternion = new Quaternion();
        Matrix4 instanceRotation = this.model.transform.cpy().mul(this.model.transform);

        instanceRotation.setToLookAt(this.direction, new Vector3(0,-1,0));
        instanceRotation.rotate(0, 0, 1, 180);
        instanceRotation.getRotation(quaternion);

        float colX = this.position.x==0 ? 0 : (this.position.x>0 ? .25f : -0.25f);
        float colZ = this.position.z==0 ? 0 : (this.position.z>0 ? .25f : -.25f);

        model.transform.set(position,quaternion);

        if (this.lvl.getCollision((int)(this.model.transform.getTranslation(new Vector3()).x+colX),(int)this.model.transform.getTranslation(new Vector3()).z)==0){
            Game.instances.remove(model);
            FireballManager.fireballs.remove(this);
        }else if(this.lvl.getCollision((int)(this.model.transform.getTranslation(new Vector3()).x),(int)(this.model.transform.getTranslation(new Vector3()).z+colZ))==0){
            Game.instances.remove(this.model);
            FireballManager.fireballs.remove(this);
        }

    }


}
