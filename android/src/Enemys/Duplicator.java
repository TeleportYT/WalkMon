package Enemys;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Duplicator extends Enemy{

    public ArrayList<Minion> getMinions() {
        return minions;
    }

    private ArrayList<Minion> minions;
    private float spawnTimer = 10f;


    public Duplicator(float x, float z) {
        super(x, z, EnemyType.duplicator);
        this.position = new Vector3(x+0.5f,0.75f,z+0.5f);
        this.modelInstance.transform.setTranslation(x+0.5f,0.75f,z+0.5f);
        this.minions = new ArrayList<Minion>();
    }

    public void Update(){

        super.Update();

        float dt = Gdx.graphics.getDeltaTime();
        this.modelInstance.transform.scale(0.001f,0.001f,0.001f);
        if(ifSeePlayer(this.direction) && this.spawnTimer <= 0){
            Duplicate();
            this.spawnTimer = 10f;
        }
        else{
            this.spawnTimer-= dt;
        }

        for (Enemy minion : this.minions) {
            minion.Update();
        }
    }

    public void Duplicate(){
        Minion minion = new Minion(this.position.x,this.position.z,this);
        this.minions.add(minion);
    }

    @Override
    public void Die(){
        for (Minion minion : this.minions) {
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
