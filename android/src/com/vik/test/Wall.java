package com.vik.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

public class Wall {
    private Model md;
    private ModelInstance mi;
    private float x,z;


    public Wall(float x, float z,World world){
        ModelBuilder modelBuilder = new ModelBuilder();
        this.md = modelBuilder.createBox(1f, 1f, 1f,
                new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        this.mi = new ModelInstance(this.md);
        this.x = x;
        this.z = z;
        this.mi.transform.setToTranslation(x+0.5f,0.5f,z+0.5f);
        MyClass.instances.add(mi);
    }

    public ModelInstance getMi(){
        return  this.mi;
    }
}
