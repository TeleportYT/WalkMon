package com.vik.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.vik.test.Enemys.Duplicator;
import com.vik.test.Enemys.Enemy;
import com.vik.test.Enemys.EnemyManager;

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

            for (Enemy enemy: MyClass.enemies.enemyies) {
                if(enemy.getPosition().dst2(position)<=(0.5*0.5)){
                    Collision("Enemy",enemy);
                    break;
                }
            }

        for (Duplicator duplicator : EnemyManager.Duplicators){
            for (Enemy enemy: duplicator.minions){
                if(enemy.getPosition().dst2(position)<=(0.5*0.5)){
                    Collision("Enemy",enemy);
                    break;
                }
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
