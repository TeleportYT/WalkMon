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
        return sceneManager;
    }

    private SceneManager sceneManager;
    private SceneAsset sceneAsset;
    private Scene scene;
    private SceneSkybox skybox;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private float time;
    private DirectionalLightEx light;
    private PerspectiveCamera cam;
    private AssetManager manager;

    public ModelsManager(PerspectiveCamera cam, AssetManager manager){
        // create scene
        this.manager = manager;
        this.cam = cam;

        sceneManager = new SceneManager();
        sceneManager.setCamera(cam);

        // setup light
        light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(1024);
        diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        // This texture is provided by the library, no need to have it in your assets.
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // setup skybox
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);
    }

    public Scene AddEnemy(EnemyType type, float x, float z){

        switch(type){
            case bob:
                sceneAsset = manager.get("drone.glb");
                break;
            case  duplicator:
                sceneAsset = manager.get("spawner.glb");

                break;
            case warrior:
            case minion:
                sceneAsset = manager.get("enemybug.glb");
                break;
        }



        Scene scene1 = new Scene(sceneAsset.scene);
        scene1.modelInstance.transform.scale(0.2f,0.2f,0.2f);
        sceneManager.addScene(scene1);
        return scene1;
    }



    public void dispose(){
        sceneManager.dispose();
        sceneAsset.dispose();
        environmentCubemap.dispose();
        diffuseCubemap.dispose();
        specularCubemap.dispose();
        brdfLUT.dispose();
        skybox.dispose();
    }


}
