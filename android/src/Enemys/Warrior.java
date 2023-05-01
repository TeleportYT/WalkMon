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
        this.position = new Vector3(x+0.5f,0.25f,z+0.5f);
        this.modelInstance.transform.setTranslation(x+0.5f,.25f,z+0.5f);
    }


    public void Update(){
        super.Update();

        float dt = Gdx.graphics.getDeltaTime();
        this.position = new Vector3();
        this.modelInstance.transform.getTranslation(this.position);
        this.position.mulAdd(this.direction, -Gdx.graphics.getDeltaTime());
        this.position.y = 0.25f;
        if(ifSeePlayer(this.direction) && this.position.dst2(PlayerController.position)>0.5f * 0.5f) {
            this.modelInstance.transform.set(this.position,this.quaternion);
        }
        else if(this.position.dst2(PlayerController.position)<0.5f * 0.5f){
            if(this.attackTimer <= 0){
                if(ifSeePlayer(this.direction)){
                    if(Game.mapLevel.lineOfSightCheap(this.position, PlayerController.position)){
                        Attack(this.direction);
                    }
                }
                this.attackTimer = 1;
            }
            else{
                this.attackTimer-= dt;
            }
        }

        this.modelInstance.transform.scale(0.2f,0.2f,0.2f);
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
