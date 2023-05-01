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
import Enemys.Duplicator;
import Enemys.Enemy;
import Enemys.EnemyManager;
import Enemys.Warrior;

public class Bullet extends Projectiles {

    public Bullet(float damage, float speed, Vector3 position, Vector3 direction) {
        super(damage, speed, position, direction, Game.mapLevel,new ModelInstance(new ModelBuilder()
                .createCapsule(0.05f, .1f, 10, new Material(ColorAttribute.createDiffuse(Color.YELLOW)), VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position)
        ));
        this.position.mulAdd(this.direction,-0.4f);
    }

    @Override
    public void Update() {

        for (Enemy enemy: EnemyManager.enemyies) {
            if(enemy.getPosition().dst2(this.position)<=(0.5*0.5)){
                Collision(enemy);
                break;
            }
        }

        for(Enemy enemy : EnemyManager.Duplicators){
            if(enemy.getPosition().dst2(this.position)<=(0.5*0.5)){
                Collision(enemy);
                break;
            }
        }


        if(!EnemyManager.Duplicators.isEmpty()){
            for (Duplicator duplicator : EnemyManager.Duplicators){
                if(!duplicator.getMinions().isEmpty()){
                    for (Warrior enemy: duplicator.getMinions()){
                        if(enemy.getPosition().dst2(this.position)<=(0.5*0.5)){
                            Collision(enemy);
                            break;
                        }
                    }
                }
            }
        }

        super.Update();
    }

    public void Collision(Enemy enemy){
        enemy.GetDamage(10f);
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug("shoot","hitted");
        Game.instances.remove(this.model);
        BulletManager.bullets.remove(this);
    }
}
