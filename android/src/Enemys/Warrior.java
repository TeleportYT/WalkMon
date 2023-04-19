package Enemys;


import androidx.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.vik.test.Game;
import com.vik.test.PlayerController;

public class Warrior extends Enemy{

    private float attackTimer = 1f;
    public Warrior(float x, float z) {
        super(x, z,EnemyType.warrior);
        position = new Vector3(x+0.5f,0.25f,z+0.5f);
        modelInstance.transform.setTranslation(x+0.5f,.25f,z+0.5f);
    }


    public void Update(){
        super.Update();

        float dt = Gdx.graphics.getDeltaTime();
        position = new Vector3();
        this.modelInstance.transform.getTranslation(position);
        position.mulAdd(direction, -Gdx.graphics.getDeltaTime());
        position.y = 0.25f;
        if(ifSeePlayer(direction) && position.dst2(PlayerController.position)>0.5f * 0.5f) {
            modelInstance.transform.set(position,quaternion);
        }
        else if(position.dst2(PlayerController.position)<0.5f * 0.5f){
            if(attackTimer <= 0){
                if(ifSeePlayer(direction)){
                    if(Game.mapLevel.lineOfSightCheap(position, PlayerController.position)){
                        Attack(direction);
                    }
                }
                attackTimer = 1;
            }
            else{
                attackTimer-= dt;
            }
        }

        modelInstance.transform.scale(0.2f,0.2f,0.2f);
    }

    @Override
    public void Attack(Vector3 direction){
        Game.pc.Damage(2f,EnemyType.warrior);
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
