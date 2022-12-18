package Enemys;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.vik.test.MyClass;

import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

import static com.vik.test.MyClass.*;
import static com.vik.test.MyClass.pc;

public abstract class Enemy {

    protected float HP=100f;
    protected float Damage = 10f;

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
    public Enemy(float x, float z){
        position = new Vector3(x+0.5f,0.5f,z+0.5f);
        moveVector = new Vector3();
        tmpVector = new Vector3();

        modelInstance = new ModelInstance(new ModelBuilder()
                .createBox(0.25f, 0.25f, 0.25f, new Material(ColorAttribute.createAmbient(Color.BLACK)), VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position)
        );
        modelInstance.transform.translate(x+0.5f,0.5f,z+0.5f);

        instances.add(modelInstance);
    }

    public void Update(){
        Vector3 position1 = new Vector3(), position2 = new Vector3();
        direction = new Vector3();
        position2.set(pc.cam.position.x,pc.cam.position.y,pc.cam.position.z);

        modelInstance.transform.getTranslation(position1);
        direction = (position2).sub(position1).nor();

        direction.set(-direction.x, -direction.y, -direction.z);

        quaternion = new Quaternion();
        Matrix4 instanceRotation = modelInstance.transform.cpy().mul(modelInstance.transform);

        instanceRotation.setToLookAt(direction, new Vector3(0,-1,0));
        instanceRotation.rotate(0, 0, 1, 180);
        instanceRotation.getRotation(quaternion);

        modelInstance.transform.set(position1, quaternion);
        ChangeUpdate(direction,quaternion);
    }
 
    public Boolean ifSeePlayer(Vector3 direction){
        Vector3 tmp = new Vector3();
        tmp.set(position).mulAdd(direction, -2f);
        if(pc.position.dst2(position) < (16) * (16) && mapLevel.lineOfSightCheap(position, pc.position)){
           return true;
        }
        return false;
    }


    public void ChangeUpdate(Vector3 direction,Quaternion quaternion){

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

    }



}
