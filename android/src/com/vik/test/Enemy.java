package com.vik.test;

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

import java.util.List;

public class Enemy {

    private float HP=100f;
    private float Damage=10f;
    private ModelInstance modelInstance;
    private float speed=10f;
    private Level lvl;
    Vector3 position;
    float moveSpeed = 2f;
    private Vector3 moveVector;
    private Vector3 tmpVector;
    public Vector3 EnemyMove;
    private List<ModelInstance> objects;
    private List<Enemy> enemys;
    private float shootTimer = 0;

    public Enemy(List<ModelInstance> instances, Level lvl, float x, float z,List<Enemy> enemys){
        position = new Vector3(x+0.5f,0.5f,z+0.5f);
        moveVector = new Vector3();
        tmpVector = new Vector3();

        EnemyMove = new Vector3();
        modelInstance = new ModelInstance(new ModelBuilder()
                .createBox(0.25f, 0.25f, 0.25f, new Material(ColorAttribute.createAmbient(Color.BLACK)), VertexAttributes.Usage.Normal | VertexAttributes.Usage.Position)
        );
        modelInstance.transform.translate(x+0.5f,0.5f,z+0.5f);
        instances.add(modelInstance);
        this.objects = instances;
        this.enemys = enemys;
        this.lvl = lvl;
        enemys.add(this);


    }

    public void Update(PlayerController player){

        float dt = Gdx.graphics.getDeltaTime();

        Vector3 position1 = new Vector3(), position2 = player.cam.position, direction = new Vector3();

        modelInstance.transform.getTranslation(position1);
        direction = (position2).sub(position1).nor();

        direction.set(-direction.x, -direction.y, -direction.z);

        Quaternion quaternion = new Quaternion();
        Matrix4 instanceRotation = modelInstance.transform.cpy().mul(modelInstance.transform);

        instanceRotation.setToLookAt(direction, new Vector3(0,-1,0));
        instanceRotation.rotate(0, 0, 1, 180);
        instanceRotation.getRotation(quaternion);

        modelInstance.transform.set(position1, quaternion);

        if(shootTimer <= 0){
            ifSeePlayer(direction,player);
            shootTimer = 5;
        }
        else{
            shootTimer-= dt;
        }


    }

    public Boolean ifSeePlayer(Vector3 direction,PlayerController pl){
        Vector3 tmp = new Vector3();
        tmp.set(position).mulAdd(direction, -2f);
        if(pl.position.dst2(position) < (16) * (16) && lvl.lineOfSightCheap(position, pl.position)){
            ShootPlayer(pl,direction);
        }
        return true;
    }

    public void ShootPlayer(PlayerController pl,Vector3 direction){
          MyClass.fbManager.AddFireBall(new FireBall(10,2,position,direction,lvl));
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
        enemys.remove(this);
        objects.remove(modelInstance);
    }

    public Vector3 GetPostion(){
          return position;
    }



}
