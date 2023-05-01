package Enemys;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.vik.test.Game;
import com.vik.test.PlayerController;

import static com.vik.test.Game.*;
import static com.vik.test.Game.pc;

import android.util.Log;

import net.mgsx.gltf.scene3d.scene.Scene;

public abstract class Enemy {

    protected float HP=100f;
    protected EnemyType type;
    protected Scene enemyModel;
    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    protected ModelInstance modelInstance;

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    protected Vector3 position;
    protected Vector3 direction;
    protected Quaternion quaternion;
    protected Vector3 moveVector;
    protected Vector3 tmpVector;
    public Enemy(float x, float z, EnemyType Type){
        this.position = new Vector3(x+0.5f,0,z+0.5f);
        this.moveVector = new Vector3();
        this.tmpVector = new Vector3();
        this.enemyModel = mg.AddEnemy(Type,x,z);
        this.modelInstance = enemyModel.modelInstance;
        this.modelInstance.transform.setTranslation(x+0.5f,0,z+0.5f);
        this.type = Type;
    }

    public void Update(){
        Vector3 position1 = new Vector3(), position2 = new Vector3();
        this.direction = new Vector3();
        position2.set(pc.cam.position.x,pc.cam.position.y,pc.cam.position.z);

        this.modelInstance.transform.getTranslation(position1);
        this.direction = (position2).sub(position1).nor();

        this.direction.set(-this.direction.x, 0, -this.direction.z);

        this.quaternion = new Quaternion();
        Matrix4 instanceRotation = this.modelInstance.transform.cpy().mul(this.modelInstance.transform);

        instanceRotation.setToLookAt(this.direction, new Vector3(0,-1,0));
        instanceRotation.rotate(0, 0, 1, 180);
        if(this.type == EnemyType.warrior){
            instanceRotation.rotate(0,1,0,-90);
        }
        instanceRotation.getRotation(this.quaternion);

        this.modelInstance.transform.set(position1, this.quaternion);
    }

    public Boolean ifSeePlayer(Vector3 direction){
        Vector3 tmp = new Vector3();
        tmp.set(this.position).mulAdd(direction, -2f);
        if(mapLevel.lineOfSightCheap(this.position,pc.position)){
            Log.d("dst ","tmp: "+tmp.toString());
            Log.d("dst ","dst: "+pc.position.dst2(this.position)+" sight: "+mapLevel.lineOfSightCheap(this.position, PlayerController.position));
        }
        if(PlayerController.position.dst2(this.position) < (16) * (16) && mapLevel.lineOfSightCheap(this.position, PlayerController.position)){
            return true;
        }
        return false;
    }



    public void Attack(Vector3 direction){

    }


    public void GetDamage(float damage){
        this.HP-= damage;
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug("shoot","shooting");
        if(this.HP<=0){
            Gdx.app.setLogLevel(Application.LOG_DEBUG);
            Gdx.app.debug("shoot","Enemy is Dead");
            Die();
        }
    }


    public void Die(){
        EnemyManager.RemoveEnemy(this);
        mg.getSceneManager().removeScene(this.enemyModel);
    }



}
