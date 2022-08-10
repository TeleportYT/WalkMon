package com.vik.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btConeShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.utils.ArrayMap;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

import jdk.internal.org.jline.utils.Log;

public class MyClass extends ApplicationAdapter {
	SpriteBatch batch;
	private PerspectiveCamera cam;
	public Level mapLevel;
	public ModelBatch modelBatch;
	public ModelBuilder modelBuilder;
    private  FirstPersonCameraController camController;
	ArrayMap<String, GameObject.Constructor> constructors;
    private PlayerController pc;
    private List<ModelInstance> instances;

    private World world;
	GameUI ui;
	private Stage st;
    private List<Enemy> enemys;

	@Override
	public void create () {

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



	}

	private void loadPlayer() {

		// setup player/camera movement
		pc = new PlayerController(instances,cam,world.getDynamicsWorld(),mapLevel,ui,enemys);
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

}
