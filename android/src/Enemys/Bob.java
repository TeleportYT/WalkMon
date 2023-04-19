package Enemys;

import androidx.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.vik.test.FireBall;
import com.vik.test.Game;
import com.vik.test.PlayerController;

public class Bob extends Enemy{

    private float shootTimer = 0;

    public Bob(float x, float z) {
        super(x, z, EnemyType.bob);
        position = new Vector3(x+0.5f,0.5f,z+0.5f);
        modelInstance.transform.setTranslation(x+0.5f,0.5f,z+0.5f);
    }

    public void Update(){

        super.Update();


        float dt = Gdx.graphics.getDeltaTime();
        position = new Vector3();
        this.modelInstance.transform.getTranslation(position);
        position.mulAdd(direction, -Gdx.graphics.getDeltaTime());
        position.y = 0.5f;
        if(ifSeePlayer(direction) && position.dst2(PlayerController.position)>2f * 1f) {
            modelInstance.transform.set(position,quaternion);
        }

        if(shootTimer <= 0){
            if(ifSeePlayer(direction)){
                if(Game.mapLevel.lineOfSightCheap(position, PlayerController.position)){
                    Attack(direction);
                }
            }
            shootTimer = 2.5f;
        }
        else{
            shootTimer-= dt;
        }


        modelInstance.transform.scale(0.2f,0.2f,0.2f);
    }


    public void Attack(Vector3 direction){
        EnemyManager.fbManager.AddFireBall(new FireBall(10,2,position,direction, Game.mapLevel));
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
