package com.vik.test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ArrayMap;

import java.util.ArrayList;
import java.util.List;



public class MyClass extends ApplicationAdapter {
	public static Context context;
	SpriteBatch batch;
	private PerspectiveCamera cam;
	public Level mapLevel;
	public ModelBatch modelBatch;
	public ModelBuilder modelBuilder;
    private  FirstPersonCameraController camController;
	ArrayMap<String, GameObject.Constructor> constructors;
    public static PlayerController pc;
    private BroadcastReceiver bd;
    public static List<ModelInstance> instances;

    private World world;
	GameUI ui;
	private Stage st;
    public static List<Enemy> enemys;
    public static FireballManager fbManager;
    public static BulletManager blManager;


    public MyClass(Context ct){
    	context = ct;
	}

	@Override
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
		// setup controller for camera
		camController = new FirstPersonCameraController(cam);
		batch = new SpriteBatch();
		instances = new ArrayList<>();
		modelBatch = new ModelBatch();
		mapLevel = new Level(20,8,50,world);
		List<Wall> walls = mapLevel.getWalls();

		for (int i = 0; i < walls.size(); i++){
			instances.add(walls.get(i).getMi());
		}

		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		Material material = new Material(ColorAttribute.createDiffuse(Color.GRAY));
		MeshPartBuilder builder = modelBuilder.part("testplane", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);
		builder.rect(0, 0, 1000f,
				1000f, 0, 1000f,
				1000f, 0, 0,
				0, 0, 0,
				0, 2, 0);
		builder.setVertexTransform(new Matrix4().rotate(new Vector3(1,0,1),90));
		Model model = modelBuilder.end();
		ModelInstance modelInstance = new ModelInstance(model);
        instances.add(modelInstance);



		cam.position.set(mapLevel.startX,0.5f,mapLevel.startY);
		ui = new GameUI();
		st = ui.GetStage();
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(st);
		multiplexer.addProcessor(camController);

        Gdx.input.setInputProcessor(multiplexer);

        enemys = new ArrayList<Enemy>();
        new Enemy(instances,mapLevel, mapLevel.startX, mapLevel.startY,enemys);


		loadPlayer();

		fbManager = new FireballManager();
        blManager = new BulletManager();

	}

	private void loadPlayer() {

		// setup player/camera movement
		pc = new PlayerController(instances,cam,world.getDynamicsWorld(),mapLevel,ui,enemys,context);
	}

	@Override
	public void render () {
		camController.update();
		pc.update();


		cam.position.y = 0.5f;
		world.Update(Gdx.graphics.getDeltaTime());

		Gdx.gl20.glClearColor(0, 0f, 0, 0);
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		cam.update();
        ui.Update();
        fbManager.Update();
        blManager.Update();
		for (Enemy enemy : enemys) {
			enemy.Update(pc);
		}

		modelBatch.begin(cam);
		modelBatch.render(instances, world.getEnvironment());
		modelBatch.end();





		st.act(Gdx.graphics.getDeltaTime());
		st.draw();

	}

	@Override
	public void dispose () {
		st.dispose();
		modelBatch.dispose();
		world.Dispose();
		batch.dispose();
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
