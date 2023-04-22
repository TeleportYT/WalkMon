package com.vik.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


import net.mgsx.gltf.loaders.glb.GLBAssetLoader;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

import java.util.ArrayList;
import java.util.List;

import Enemys.EnemyManager;


public class Game implements Screen {
	public static Context context;
	private PerspectiveCamera cam;
	public static Level mapLevel;
	public ModelBatch modelBatch;
	public ModelBuilder modelBuilder;
    private MyFPS camController;
    public static PlayerController pc;
    public static List<ModelInstance> instances;
    public static EnemyManager enemies;
	public static GameStats stats;
	public static GameUI GameUI;
	public static AssetManager manager;
	private boolean isRunning = false;
	private double gameTime=0;
	public static SoundEffects sd;
	public static ModelsManager mg;
	InputMultiplexer multiplexer;
	public boolean isLoaded(){
		return manager.isFinished();
	}

    public Game(Context ct){
    	context = ct;
	}
	public void Load(){

		manager = new AssetManager();
		manager.load("dungeon.png",Texture.class);
		manager.load("floor.png",Texture.class);
		manager.load("uiskin.json", Skin.class);
		manager.load("fire_button.json",Skin.class);
		manager.load("sound effects/running.mp3", Music.class);
		manager.load("sound effects/inGameMusic.mp3", Music.class);
		manager.load("sound effects/flyingEnemy.mp3", Sound.class);
		manager.load("sound effects/punch.mp3", Sound.class);
		manager.load("sound effects/shoot.mp3", Sound.class);

		manager.setLoader(SceneAsset.class, ".glb", new GLBAssetLoader());
		manager.load("spawner.glb",SceneAsset.class);
		manager.load("enemybug.glb", SceneAsset.class);
		manager.load("drone.glb", SceneAsset.class);

	}

	public void create () {


		IntentFilter intentFilter = new IntentFilter("Player Damaged");
		intentFilter.addAction("Player Dead");
		intentFilter.addAction("Pause Game");
		intentFilter.addAction("Player Healed");
		intentFilter.addAction("Main Menu");
		intentFilter.addAction("Stage Cleared");
        context.registerReceiver(receiver,intentFilter);



        modelBuilder = new ModelBuilder();
		// setup camera
		cam = new PerspectiveCamera(65, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0, 10, 0);
		cam.near = .10f;
		cam.far = 30f;
		cam.update();
		mg = new ModelsManager(cam,manager);
		// setup controller for camera
		camController = new MyFPS(cam);
		instances = new ArrayList<>();
		modelBatch = new ModelBatch();

				mapLevel = new Level(20,8,50);
				List<Wall> walls = mapLevel.getWalls();

				for (int i = 0; i < walls.size(); i++){
					instances.add(walls.get(i).getMi());
				}
				cam.position.set(mapLevel.startX,0.5f,mapLevel.startY);

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(((AndroidApplication) Gdx.app).getContext());

		Difficulty choosen;

		switch(prefs.getInt("Difficulty",-1)){

			case 0:
				choosen = Difficulty.Easy;
				break;
			case 1:
				choosen = Difficulty.Medium;
				break;
			case 2:
				choosen = Difficulty.Hard;
				break;
			default:
				choosen = Difficulty.Easy;
				break;

		}


				enemies = new EnemyManager(choosen);


		GameUI = new GameUI();


		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(GameUI.getSt());
		multiplexer.addProcessor(camController);
        Gdx.input.setInputProcessor(multiplexer);

		loadPlayer();




	}
	private void loadPlayer() {
		// setup player
		pc = new PlayerController(cam);
		camController.setPl(pc);
		stats = new GameStats();
		isRunning = true;
		sd = new SoundEffects(manager);

	}

	private boolean isPaused = false;


	@Override
	public void render (float delta) {
		if (!isPaused) {
			Log.d("Delta", "F: " + Gdx.graphics.getDeltaTime() + " G: " + delta);
			gameTime += Gdx.graphics.getDeltaTime();
			Log.d("TimeD", "F: " + gameTime);
			camController.update();
			cam.position.y = 0.5f;
			pc.update();


			Gdx.gl20.glClearColor(0, 0f, 0, 0);
			Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

			cam.update();
			enemies.Update();
		}





		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		mg.getSceneManager().update(Gdx.graphics.getDeltaTime());
		mg.getSceneManager().render();


			modelBatch.begin(cam);
			modelBatch.render(instances);
			modelBatch.end();

		GameUI.Update();


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
		manager.dispose();
	}


	//region Broadcast Receiver
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals("Player Damaged")){

			}
			else if(intent.getAction().equals("Player Dead")){
				Intent nt = new Intent(((AndroidApplication) Gdx.app).getContext(), GameOver.class);
				float minutes = (float)Math.floor(gameTime / 60.0f);
				float seconds = (float)(gameTime - minutes * 60.0f);
				Log.d("Time","GameTime: "+gameTime+" MINs "+minutes+" "+seconds);
				stats.setTime(String.format("%.0fm%.0fs", minutes, seconds));
				stats.setPlayerHealth(0);
				nt.putExtra("StatsId",""+stats.FinishGame(GameState.Lose,((AndroidApplication) Gdx.app).getContext()));
				((AndroidApplication) Gdx.app).getContext().unregisterReceiver(receiver);
				((AndroidApplication) Gdx.app).finish();

				((AndroidApplication) Gdx.app).getContext().startActivity(nt);
			}
			else if(intent.getAction().equals("Pause Game")){
				isPaused = intent.getBooleanExtra("isPaused",isPaused);
			}
			else if(intent.getAction().equals("Player Healed")){

			}
			else if(intent.getAction().equals("Stage Cleared")){
				Intent nt = new Intent(((AndroidApplication) Gdx.app).getContext(), GameOver.class);
				float minutes = (float)Math.floor(gameTime / 60.0f);
				float seconds = (float)(gameTime - minutes * 60.0f);
				Log.d("Time","GameTime: "+gameTime+" MINs "+minutes+" "+seconds);
				stats.setTime(String.format("%.0fm%.0fs", minutes, seconds));
				stats.setPlayerHealth(pc.hp);
				nt.putExtra("StatsId",""+stats.FinishGame(GameState.Win,((AndroidApplication) Gdx.app).getContext()));
				((AndroidApplication) Gdx.app).getContext().unregisterReceiver(receiver);
				((AndroidApplication) Gdx.app).finish();

				((AndroidApplication) Gdx.app).getContext().startActivity(nt);
			}
			else if(intent.getAction().equals("Main Menu")){

				Intent nt = new Intent(((AndroidApplication) Gdx.app).getContext(), AndroidLauncher.class);
				((AndroidApplication) Gdx.app).getContext().unregisterReceiver(receiver);
				((AndroidApplication) Gdx.app).finish();

				((AndroidApplication) Gdx.app).getContext().startActivity(nt);
			}
		}
	};

	//endregion

}
