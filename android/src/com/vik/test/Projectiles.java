package com.vik.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import java.lang.annotation.Inherited;

public class Projectiles {

    public ModelInstance model;
    public float damage;
    public float speed;
    public Vector3 position;
    public Vector3 direction;
    public Level lvl;

    public Projectiles(float damage, float speed, Vector3 position, Vector3 direction,Level lvl,ModelInstance model){
        this.damage = damage;
        this.speed = speed;
        this.position = new Vector3();
        this.position.set(position);
        this.direction = new Vector3();
        this.direction.set(direction);
        this.lvl = lvl;
        this.model = model;
        MyClass.instances.add(model);

    }

    public void Update(){
        position.mulAdd(direction, -speed*Gdx.graphics.getDeltaTime());

        Quaternion quaternion = new Quaternion();
        Matrix4 instanceRotation = model.transform.cpy().mul(model.transform);

        instanceRotation.setToLookAt(direction, new Vector3(0,-1,0));
        instanceRotation.rotate(0, 0, 1, 180);
        instanceRotation.getRotation(quaternion);

        float colX = position.x==0 ? 0 : (position.x>0 ? .25f : -0.25f);
        float colZ = position.z==0 ? 0 : (position.z>0 ? .25f : -.25f);

        if(PlayerController.position.dst2(position) < (0.5) * (0.5)){
            Collision("Player",null);
        }

        for (Enemy enemy:MyClass.enemys) {
            if(enemy.position.dst2(position)<=(0.5*0.5)){
                Collision("Enemy",enemy);
            }
        }

        model.transform.set(position,quaternion);

        if (lvl.getCollision((int)(model.transform.getTranslation(new Vector3()).x+colX),(int)model.transform.getTranslation(new Vector3()).z)==0){
            MyClass.instances.remove(model);
            FireballManager.fireballs.remove(this);
        }else if(lvl.getCollision((int)(model.transform.getTranslation(new Vector3()).x),(int)(model.transform.getTranslation(new Vector3()).z+colZ))==0){
            MyClass.instances.remove(model);
            FireballManager.fireballs.remove(this);
        }

    }

    public void Collision(String collidorName,Enemy enemy) {

    }


}
