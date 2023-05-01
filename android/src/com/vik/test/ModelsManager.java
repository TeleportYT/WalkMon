package com.vik.test;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

import Enemys.EnemyType;

public class ModelsManager {

    public SceneManager getSceneManager() {
        //scene.modelInstance.transform.setTranslation(cam.position.x, cam.position.y,cam.position.z);
        return this.sceneManager;
    }

    private SceneManager sceneManager;
    private SceneAsset sceneAsset;
    private SceneSkybox skybox;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private DirectionalLightEx light;
    private AssetManager manager;

    public ModelsManager(PerspectiveCamera cam, AssetManager manager){
        // create scene
        this.manager = manager;

        this.sceneManager = new SceneManager();
        this.sceneManager.setCamera(cam);

        // setup light
        this.light = new DirectionalLightEx();
        this.light.direction.set(1, 0, 1).nor();
        this.light.color.set(Color.BLACK);
        this.sceneManager.environment.add(light);

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        this.environmentCubemap = iblBuilder.buildEnvMap(1024);
        this.diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        this.specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        // This texture is provided by the library, no need to have it in your assets.
        this.brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        this.sceneManager.setAmbientLight(1f);
        this.sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        this.sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        this.sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // setup skybox
        this.skybox = new SceneSkybox(environmentCubemap);
        this.sceneManager.setSkyBox(skybox);
    }

    public Scene AddEnemy(EnemyType type, float x, float z){

        switch(type){
            case bob:
                this.sceneAsset = manager.get("drone.glb");
                break;
            case  duplicator:
                this.sceneAsset = manager.get("spawner.glb");

                break;
            case warrior:
            case minion:
                this.sceneAsset = manager.get("enemybug.glb");
                break;
        }



        Scene scene1 = new Scene(sceneAsset.scene);
        scene1.modelInstance.transform.scale(0.2f,0.2f,0.2f);
        this.sceneManager.addScene(scene1);
        return scene1;
    }



    public void dispose(){
        this.sceneManager.dispose();
        this.sceneAsset.dispose();
        this.environmentCubemap.dispose();
        this.diffuseCubemap.dispose();
        this.specularCubemap.dispose();
        this.brdfLUT.dispose();
        this.skybox.dispose();
    }


}
