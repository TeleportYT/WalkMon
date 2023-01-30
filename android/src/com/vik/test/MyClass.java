package com.vik.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


import java.util.ArrayList;
import java.util.List;

import Enemys.EnemyManager;


public class MyClass implements Screen {
	public static Context context;
	private PerspectiveCamera cam;
	public static Level mapLevel;
	public ModelBatch modelBatch;
	public ModelBuilder modelBuilder;
    private FirstPersonCameraController camController;
    public static PlayerController pc;
    public static List<ModelInstance> instances;
    public static EnemyManager enemies;

    private World world;
	public static GameUI GameUI;
	public static GameScene scene;
	public static AssetManager manager;
	private boolean isRunning = false;

	public boolean isLoaded(){
		return manager.isFinished();
	}

    public MyClass(Context ct){
    	context = ct;
	}
	public void Load(){

		manager = new AssetManager();
		manager.load("dungeon.png",Texture.class);
		manager.load("floor.png",Texture.class);
		manager.load("uiskin.json", Skin.class);
		manager.load("fire_button.json",Skin.class);

	}

	public void create () {


		IntentFilter intentFilter = new IntentFilter("Player Damaged");
		intentFilter.addAction("Player Dead");
		intentFilter.addAction("Pause Game");
		intentFilter.addAction("Player Healed");
        context.registerReceiver(receiver,intentFilter);




        modelBuilder = new ModelBuilder();
		// load world
		world = new World();
		// setup camera
		cam = new PerspectiveCamera(65, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0, 10, 0);
		cam.near = .10f;
		cam.far = 30f;
		cam.update();
		scene = new GameScene(cam);

		// setup controller for camera
		camController = new FirstPersonCameraController(cam);
		instances = new ArrayList<>();
		modelBatch = new ModelBatch();

				mapLevel = new Level(20,8,50,world);
				List<Wall> walls = mapLevel.getWalls();

				for (int i = 0; i < walls.size(); i++){
					instances.add(walls.get(i).getMi());
				}
				cam.position.set(mapLevel.startX,0.5f,mapLevel.startY);
				enemies = new EnemyManager();


		GameUI = new GameUI();


		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(GameUI.st);
		multiplexer.addProcessor(camController);
        Gdx.input.setInputProcessor(multiplexer);

		loadPlayer();

	}

	private void loadPlayer() {
		// setup player
		pc = new PlayerController(cam);
		isRunning = true;
	}

	@Override
	public void render (float delta) {

		camController.update();
		cam.position.y = 0.5f;
		world.Update(Gdx.graphics.getDeltaTime());
		pc.update();


		Gdx.gl20.glClearColor(0, 0f, 0, 0);
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		cam.update();
		enemies.Update();
		GameUI.Update();



		scene.Update(cam);

		modelBatch.begin(cam);
		modelBatch.render(instances, world.getEnvironment());
		modelBatch.end();





		GameUI.st.act(Gdx.graphics.getDeltaTime());
		GameUI.st.draw();

	}

	@Override
	public void show() {
		create();
	}


	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose () {
		GameUI.st.dispose();
		modelBatch.dispose();
		world.Dispose();
		manager.dispose();
	}


	//region Broadcast Receiver
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals("Player Damaged")){
				Toast.makeText(context,"Player Damaged",Toast.LENGTH_SHORT).show();
			}
			else if(intent.getAction().equals("Player Dead")){
				Toast.makeText(context,"Player Died",Toast.LENGTH_SHORT);
			}
			else if(intent.getAction().equals("Pause Game")){
			}
			else if(intent.getAction().equals("Player Healed")){

			}
		}
	};

	//endregion

}
