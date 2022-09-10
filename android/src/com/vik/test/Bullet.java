package com.vik.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.vik.test.Enemys.Enemy;

public class Bullet extends Projectiles {

    public Bullet(float damage, float speed, Vector3 position, Vector3 direction) {
        super(damage, speed, position, direction, MyClass.mapLevel,new ModelInstance(new ModelBuilder()
                //.createBox(0.25f, 0.25f, 0.25f, new Material(ColorAttribute.createAmbient(Color.BLACK)), VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position)
                .createCapsule(0.05f, .1f, 10, new Material(ColorAttribute.createAmbient(Color.YELLOW)), VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position)
        ));
        this.position.mulAdd(this.direction,-0.4f);
    }

    public void Collision(String collidorName, Enemy enemy){
        if(collidorName.equals("Enemy")){
            enemy.GetDamage(10f);
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
            Gdx.app.debug("shoot","hitted");
            MyClass.instances.remove(this.model);
            BulletManager.bullets.remove(this);
        }
    }
}
