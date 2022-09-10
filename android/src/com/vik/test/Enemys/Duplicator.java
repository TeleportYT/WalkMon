package com.vik.test.Enemys;

import static com.vik.test.MyClass.mapLevel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.vik.test.MyClass;

import java.util.ArrayList;

public class Duplicator extends Enemy{
    public float spawnTimer = 10f;

    public ArrayList<Warrior> minions;


    public Duplicator(float x, float z) {
        super(x, z);
        minions = new ArrayList<Warrior>();
    }

    public void Update(){

        super.Update();

        float dt = Gdx.graphics.getDeltaTime();

        if(ifSeePlayer(direction) && spawnTimer <= 0){
            Duplicate();
            spawnTimer = 10f;
        }
        else{
            spawnTimer-= dt;
        }

        for (Enemy minion : minions) {
            minion.Update();
        }
    }

    public void Duplicate(){
        Warrior minion = new Warrior(position.x,position.z);
        minions.add(minion);
    }

    public void Die(){
        for (Enemy minion : minions) {
            MyClass.instances.remove(minion.getModelInstance());
        }
        minions = null;
        super.Die();
    }

}
