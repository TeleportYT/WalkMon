package com.vik.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;


public class Wall {
    private Model md;
    private ModelInstance mi;


    public Wall(float x, float z,float y){
        ModelBuilder modelBuilder = new ModelBuilder();
        this.md = CreateBox();
        this.mi = new ModelInstance(this.md);
        this.mi.transform.setToTranslation(x+0.5f,y,z+0.5f);
        Game.instances.add(mi);
    }

    public ModelInstance getMi(){
        return  this.mi;
    }

    public Model CreateBox(){
        // Load the brick texture from a file
        Texture brickTexture = Game.manager.get("dungeon.png",Texture.class);

// Create a new Material
        Material brickMaterial = new Material(TextureAttribute.createDiffuse(brickTexture), ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f));
        ModelBuilder modelBuilder = new ModelBuilder();

// Create a model from the box shape, using the brick Material
        Model cubeModel = modelBuilder.createBox(1, 1, 1, brickMaterial,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        return cubeModel;
    }

}
