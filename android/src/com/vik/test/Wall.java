package com.vik.test;

import static com.vik.test.MyClass.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;


public class Wall {
    private Model md;
    private ModelInstance mi;
    private float x,z;


    public Wall(float x, float z,float y){
        ModelBuilder modelBuilder = new ModelBuilder();
        this.md = CreateBox();
        this.mi = new ModelInstance(this.md);
        this.x = x;
        this.z = z;
        this.mi.transform.setToTranslation(x+0.5f,y,z+0.5f);
        MyClass.instances.add(mi);
    }

    public ModelInstance getMi(){
        return  this.mi;
    }

    public Model CreateBox(){
        // Load the brick texture from a file
        Texture brickTexture = MyClass.manager.get("dungeon.png",Texture.class);

// Create a new Material
        Material brickMaterial = new Material(TextureAttribute.createDiffuse(brickTexture), ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f));
        ModelBuilder modelBuilder = new ModelBuilder();

// Create a model from the box shape, using the brick Material
        Model cubeModel = modelBuilder.createBox(1, 1, 1, brickMaterial,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        return cubeModel;
    }

}
