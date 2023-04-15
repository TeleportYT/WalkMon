package Enemys;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

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
        super(x, z, EnemyType.duplicator);
        position = new Vector3(x+0.5f,0.75f,z+0.5f);
        modelInstance.transform.setTranslation(x+0.5f,0.75f,z+0.5f);
        minions = new ArrayList<Minion>();
    }

    public void Update(){

        super.Update();

        float dt = Gdx.graphics.getDeltaTime();
        this.modelInstance.transform.scale(0.001f,0.001f,0.001f);
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

    @Override
    public String toString() {
        return super.toString();
    }
}
