package com.vik.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
	private double gameTime=0;
	public static SoundEffects sd;
	public static ModelsManager mg;
	InputMultiplexer multiplexer;
	public boolean isLoaded(){
		return this.manager.isFinished();
	}

	public Game(){
	}
	public void Load(){

		this.manager = new AssetManager();
		this.manager.load("dungeon.png",Texture.class);
		this.manager.load("floor.png",Texture.class);
		this.manager.load("uiskin.json", Skin.class);
		this.manager.load("fire_button.json",Skin.class);
		this.manager.load("sound effects/running.mp3", Music.class);
		this.manager.load("sound effects/inGameMusic.mp3", Music.class);
		this.manager.load("sound effects/flyingEnemy.mp3", Sound.class);
		this.manager.load("sound effects/punch.mp3", Sound.class);
		this.manager.load("sound effects/shoot.mp3", Sound.class);

		this.manager.setLoader(SceneAsset.class, ".glb", new GLBAssetLoader());
		this.manager.load("spawner.glb",SceneAsset.class);
		this.manager.load("enemybug.glb", SceneAsset.class);
		this.manager.load("drone.glb", SceneAsset.class);

	}

	public void create () {


		IntentFilter intentFilter = new IntentFilter("Player Damaged");
		intentFilter.addAction("Player Dead");
		intentFilter.addAction("Pause Game");
		intentFilter.addAction("Player Healed");
		intentFilter.addAction("Main Menu");
		intentFilter.addAction("Stage Cleared");
		((AndroidApplication) Gdx.app).getContext().registerReceiver(this.receiver,intentFilter);



		this.modelBuilder = new ModelBuilder();
		// setup camera
		this.cam = new PerspectiveCamera(65, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.cam.position.set(0, 10, 0);
		this.cam.near = .10f;
		this.cam.far = 30f;
		this.cam.update();
		this.mg = new ModelsManager(this.cam,this.manager);
		// setup controller for camera
		this.camController = new MyFPS(this.cam);
		this.instances = new ArrayList<>();
		this.modelBatch = new ModelBatch();

		this.mapLevel = new Level(20,8,50);
		List<Wall> walls = this.mapLevel.getWalls();

		for (int i = 0; i < walls.size(); i++){
			this.instances.add(walls.get(i).getMi());
		}
		this.cam.position.set(this.mapLevel.startX,0.5f,this.mapLevel.startY);
		this.enemies = new EnemyManager(Difficulty.Easy);


		this.GameUI = new GameUI();


		this.multiplexer = new InputMultiplexer();
		this.multiplexer.addProcessor(this.GameUI.getSt());
		this.multiplexer.addProcessor(this.camController);
		Gdx.input.setInputProcessor(this.multiplexer);

		loadPlayer();




	}
	private void loadPlayer() {
		// setup player
		this.pc = new PlayerController(this.cam);
		this.camController.setPl(this.pc);
		this.stats = new GameStats();
		this.sd = new SoundEffects(this.manager);

	}

	private boolean isPaused = false;


	@Override
	public void render (float delta) {
		if (!this.isPaused) {
			Log.d("Delta", "F: " + Gdx.graphics.getDeltaTime() + " G: " + delta);
			this.gameTime += Gdx.graphics.getDeltaTime();
			Log.d("TimeD", "F: " + this.gameTime);
			this.camController.update();
			this.cam.position.y = 0.5f;
			this.pc.update();


			Gdx.gl20.glClearColor(0, 0f, 0, 0);
			Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

			this.cam.update();
			this.enemies.Update();
		}





		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		this.mg.getSceneManager().update(Gdx.graphics.getDeltaTime());
		this.mg.getSceneManager().render();


		this.modelBatch.begin(this.cam);
		this.modelBatch.render(this.instances);
		this.modelBatch.end();

		this.GameUI.Update();


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
		this.GameUI.st.dispose();
		this.modelBatch.dispose();
		this.manager.dispose();
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
