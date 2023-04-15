package Enemys;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.vik.test.PlayerController;

import static com.vik.test.MyClass.*;
import static com.vik.test.MyClass.pc;

import android.util.Log;

import net.mgsx.gltf.scene3d.scene.Scene;

public abstract class Enemy {

    protected float HP=100f;
    protected float Damage = 10f;
    protected EnemyType type;
    private Scene enemyModel;
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
    protected float moveSpeed = 2f;
    protected Vector3 moveVector;
    protected Vector3 tmpVector;
    public Enemy(float x, float z, EnemyType Type){
        position = new Vector3(x+0.5f,0,z+0.5f);
        moveVector = new Vector3();
        tmpVector = new Vector3();
        enemyModel = mg.AddEnemy(Type,x,z);
        modelInstance = enemyModel.modelInstance;
        modelInstance.transform.setTranslation(x+0.5f,0,z+0.5f);
        this.type = Type;
    }

    public void Update(){
        Vector3 position1 = new Vector3(), position2 = new Vector3();
        direction = new Vector3();
        position2.set(pc.cam.position.x,pc.cam.position.y,pc.cam.position.z);

        modelInstance.transform.getTranslation(position1);
        direction = (position2).sub(position1).nor();

        direction.set(-direction.x, 0, -direction.z);

        quaternion = new Quaternion();
        Matrix4 instanceRotation = modelInstance.transform.cpy().mul(modelInstance.transform);

        instanceRotation.setToLookAt(direction, new Vector3(0,-1,0));
        instanceRotation.rotate(0, 0, 1, 180);
        if(this.type == EnemyType.warrior){
            instanceRotation.rotate(0,1,0,-90);
        }
        instanceRotation.getRotation(quaternion);

        modelInstance.transform.set(position1, quaternion);
    }
 
    public Boolean ifSeePlayer(Vector3 direction){
        Vector3 tmp = new Vector3();
        tmp.set(position).mulAdd(direction, -2f);
        if(mapLevel.lineOfSightCheap(position,pc.position)){
            Log.d("dst ","tmp: "+tmp.toString());
            Log.d("dst ","dst: "+pc.position.dst2(position)+" sight: "+mapLevel.lineOfSightCheap(position, PlayerController.position));
        }
        if(PlayerController.position.dst2(position) < (16) * (16) && mapLevel.lineOfSightCheap(position, PlayerController.position)){
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
        mg.getSceneManager().removeScene(enemyModel);
    }



}
