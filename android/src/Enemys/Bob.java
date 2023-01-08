package Enemys;

import androidx.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.vik.test.FireBall;
import com.vik.test.MyClass;
import com.vik.test.PlayerController;

public class Bob extends Enemy{

    private float shootTimer = 0;

    public Bob(float x, float z) {
        super(x, z);
    }

    public void Update(){

        super.Update();

        float dt = Gdx.graphics.getDeltaTime();

        if(shootTimer <= 0){
            if(ifSeePlayer(direction)){
                if(PlayerController.position.dst2(position) < (16) * (16) && MyClass.mapLevel.lineOfSightCheap(position, PlayerController.position)){
                    Attack(direction);
                }
            }
            shootTimer = 5;
        }
        else{
            shootTimer-= dt;
        }
    }


    public void Attack(Vector3 direction){
        EnemyManager.fbManager.AddFireBall(new FireBall(10,2,position,direction,MyClass.mapLevel));
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
