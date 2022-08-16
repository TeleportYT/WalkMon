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

public class FireBall extends Projectiles{

    public FireBall(float damage, float speed, Vector3 position, Vector3 direction, Level lvl) {
        super(damage, speed, position, direction, lvl,new ModelInstance(new ModelBuilder()
                //.createBox(0.25f, 0.25f, 0.25f, new Material(ColorAttribute.createAmbient(Color.BLACK)), VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position)
                .createCapsule(0.25f, .5f, 10, new Material(ColorAttribute.createAmbient(Color.YELLOW)), VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position)
        ));
    }

    @Override
    public void Collision(String collidorName,Enemy enemy){
        if(collidorName.equals("Player")){
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
            Gdx.app.debug("Enemy","i see you");
            MyClass.pc.Damage(this.damage);
            MyClass.instances.remove(this.model);
            FireballManager.fireballs.remove(this);
        }
    }
}
