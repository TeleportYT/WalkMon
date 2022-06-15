package com.vik.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;

import java.util.ArrayList;
import java.util.List;

public class MyClass extends ApplicationAdapter {
	SpriteBatch batch;
	private PerspectiveCamera cam;
	public Level mapLevel;
	public ModelBatch modelBatch;
	public ModelBuilder modelBuilder;
    public Environment environment;
    private  CameraInputController camController;
    private PlayerController pc;
    private List<ModelInstance> instances;
	btDynamicsWorld dynamicsWorld;
	btConstraintSolver constraintSolver;
	btDefaultCollisionConfiguration collisionConfig;
	btCollisionDispatcher dispatcher;
	btDbvtBroadphase broadphase;

	@Override
	public void create () {

modelBuilder = new ModelBuilder();
		// load enviroment
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		// setup camera
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0, 0, 0);
		cam.lookAt(0, 0, 0);
		cam.update();
		// setup controller for camera
		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		batch = new SpriteBatch();
		instances = new ArrayList<>();
		modelBatch = new ModelBatch();
		mapLevel = new Level(20,8,50);
		List<Wall> walls = mapLevel.getWalls();

		for (int i = 0; i < walls.size(); i++){
			instances.add(walls.get(i).getMi());
		}

		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		Material material = new Material(ColorAttribute.createDiffuse(Color.RED));
		MeshPartBuilder builder = modelBuilder.part("testplane", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);
		builder.rect(0, 0, 20f,
				20f, 0, 20f,
				20f, 0, 0,
				0, 0, 0,
				0, 1, 0);

		Model model = modelBuilder.end();
		ModelInstance modelInstance = new ModelInstance(model);
        instances.add(modelInstance);



		Bullet.init();
		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		constraintSolver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
		dynamicsWorld.setGravity(new Vector3(0, -10f, 0));


		loadPlayer();

	}

	private void loadPlayer() {

		// setup player/camera movement
		pc = new PlayerController(instances,cam,dynamicsWorld);
	}

	@Override
	public void render () {
		camController.update();

		pc.update();

		Gdx.gl20.glClearColor(0, 0f, 0, 0);
		Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		cam.update();
		//batch.setProjectionMatrix(cam.combined);
		//lvl.getMI().setView(cam.combined, cam.position.x - cam.viewportWidth * 0.5f , cam.position.y, cam.viewportWidth, cam.viewportHeight);
		//lvl.getMI().render();

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}

	@Override
	public void dispose () {
		dynamicsWorld.dispose();
		modelBatch.dispose();
		batch.dispose();
	}

}
