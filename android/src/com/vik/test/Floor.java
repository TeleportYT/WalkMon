package com.vik.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Floor {

    private Model md;
    private ModelInstance mi;
    private float x,z,y,degrees;


   public Floor(float x,float y,float z,float degrees){
       this.md = BuildFloor();
       this.mi = new ModelInstance(this.md);
       this.x = x;
       this.z = z;
       this.degrees = degrees;
       this.mi.transform.setToTranslation(x,y,z);
       this.mi.transform.rotate(1,0,1,degrees);
       MyClass.instances.add(mi);



   }

   public Model BuildFloor(){
       ModelBuilder modelBuilder = new ModelBuilder();
       modelBuilder.begin();
       Texture brickTexture = MyClass.manager.get("floor.png",Texture.class);
       Material brickMaterial = new Material(TextureAttribute.createDiffuse(brickTexture), ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f));
       MeshPartBuilder builder = modelBuilder.part("floor", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, brickMaterial);
       builder.rect(0, 0, 1,
               1, 0, 1,
               1, 0, 0,
               0, 0, 0,
               0, 2, 0);
       builder.setVertexTransform(new Matrix4().rotate(new Vector3(1,0,1),90));
       Model model = modelBuilder.end();
       return  model;
   }

}
