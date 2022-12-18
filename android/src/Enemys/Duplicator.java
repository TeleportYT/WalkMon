package Enemys;

import android.support.annotation.NonNull;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;

public class Duplicator extends Enemy{

    public ArrayList<Minion> getMinions() {
        return minions;
    }

    public void setMinions(ArrayList<Minion> minions) {
        this.minions = minions;
    }

    private ArrayList<Minion> minions;
    public float spawnTimer = 10f;


    public Duplicator(float x, float z) {
        super(x, z);
        minions = new ArrayList<Minion>();
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
        Minion minion = new Minion(position.x,position.z,this);
        minions.add(minion);
    }

    @Override
    public void Die(){
        for (Minion minion : minions) {
            minion.Die();
        }
        EnemyManager.Duplicators.remove(this);
        super.Die();
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
