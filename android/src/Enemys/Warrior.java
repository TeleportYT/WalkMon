package Enemys;


import androidx.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.vik.test.MyClass;
import com.vik.test.PlayerController;

public class Warrior extends Enemy{

    private float attackTimer = 1f;

    public Warrior(float x, float z) {
        super(x, z);
    }

    public void Update(){
        super.Update();

        float dt = Gdx.graphics.getDeltaTime();
        position = new Vector3();
        this.getModelInstance().transform.getTranslation(position);
        position.mulAdd(direction, -Gdx.graphics.getDeltaTime());


        if(ifSeePlayer(direction) && position.dst2(PlayerController.position)>0.25f * 0.25f) {
            this.getModelInstance().transform.set(position,quaternion);
        }
        else if(position.dst2(PlayerController.position)<0.25f * 0.25f){
            if(attackTimer <= 0){
                if(ifSeePlayer(direction)){
                    if(MyClass.mapLevel.lineOfSightCheap(position, PlayerController.position)){
                        Attack(direction);
                    }
                }
                attackTimer = 1;
            }
            else{
                attackTimer-= dt;
            }
        }
    }

    @Override
    public void Attack(Vector3 direction){
        MyClass.pc.Damage(2f);
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
