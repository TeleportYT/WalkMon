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

import Enemys.EnemyType;

public class FireBall extends Projectiles{

    public FireBall(float damage, float speed, Vector3 position, Vector3 direction, Level lvl) {
        super(damage, speed, position, direction, lvl,new ModelInstance(new ModelBuilder()
                //.createBox(0.25f, 0.25f, 0.25f, new Material(ColorAttribute.createAmbient(Color.BLACK)), VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position)
                .createCapsule(0.25f, .5f, 10, new Material(ColorAttribute.createDiffuse(Color.RED)), VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position)
        ));
    }

    @Override
    public void Update() {
        if(PlayerController.position.dst2(this.position) < (0.5) * (0.5)){
            Collision();
        }
        super.Update();
    }

    public void Collision(){
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug("Enemy","i see you");
        Game.pc.Damage(this.damage, EnemyType.bob);
        Game.instances.remove(this.model);
        FireballManager.fireballs.remove(this);
    }
}
