package com.vik.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.ArrayList;
import java.util.List;

public class Wall {
    private Model md;
    private ModelInstance mi;
    private float x,z;


    public Wall(float x, float z){
        ModelBuilder modelBuilder = new ModelBuilder();
        this.md = modelBuilder.createBox(1f, 1f, 1f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        this.mi = new ModelInstance(this.md);
        this.x = x;
        this.z = z;
        this.mi.transform.setToTranslation(x+0.5f,0.5f,z+0.5f);

    }

    public ModelInstance getMi(){
        return  this.mi;
    }
}
