package com.gigamesh.carrotfall;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import ecs.components.PhysicsComponent;

public class CarrotFall extends Game {
    private SpriteBatch batch;

    /* view settings */
    private OrthographicCamera camera;
    private FitViewport viewport;

    /* UI */
    private Stage ui_stage, interactionStage;
    private Skin skin;

    /* Box2D */
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    /* Ashley ECS */
    private Engine ashleyEngine;
    private Entity carrotEntity;

    @Override
    public void create() {
        batch = new SpriteBatch();

        /* 800x600 resolution */
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        viewport.apply();

        /* initializing Box2D */
        Box2D.init();
        world = new World(new Vector2(0, -10), true);
        box2DDebugRenderer = new Box2DDebugRenderer(); // so that we can see the physics for now

        /* ashley ecs */
        ashleyEngine = new Engine(); // a blank engine for now
        carrotEntity = new Entity();
        Entity plateEntity=new Entity();

        carrotEntity.add(new PhysicsComponent());

        /* filling the physics body */
        PhysicsComponent physicsComponent = carrotEntity.getComponent(PhysicsComponent.class);
        physicsComponent.bodyDef = new BodyDef();
        physicsComponent.bodyDef.type = BodyDef.BodyType.DynamicBody;
        physicsComponent.bodyDef.position.set(300, 700);

        physicsComponent.body = world.createBody(physicsComponent.bodyDef);

        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(70, 20);

        physicsComponent.fixtureDef = new FixtureDef();
        physicsComponent.fixtureDef.shape = boxShape;
        physicsComponent.fixtureDef.density = 0.1f;
        physicsComponent.fixtureDef.friction = 0.1f;
        physicsComponent.fixtureDef.restitution = 0.2f;

        physicsComponent.fixture = physicsComponent.body.createFixture(physicsComponent.fixtureDef);

        boxShape.dispose();

        ashleyEngine.addEntity(carrotEntity);

        /* create the ground (should do it as an entity later on) */
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0, 0);

        Body groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(camera.viewportWidth, 20);
        groundBody.createFixture(groundBox, 0);
        groundBox.dispose();

        /* creating the player's moving plate to catch the carrots */
        plateEntity.add(new PhysicsComponent());

        physicsComponent=plateEntity.getComponent(PhysicsComponent.class);
        physicsComponent.bodyDef = new BodyDef();
        physicsComponent.bodyDef.type = BodyDef.BodyType.KinematicBody;
        physicsComponent.bodyDef.position.set(233, 70);

        physicsComponent.body = world.createBody(physicsComponent.bodyDef);

        boxShape = new PolygonShape();
        boxShape.setAsBox(70, 10);

        physicsComponent.fixtureDef = new FixtureDef();
        physicsComponent.fixtureDef.shape = boxShape;
        physicsComponent.fixtureDef.density = 0.1f;
        physicsComponent.fixtureDef.friction = 0.1f;
        physicsComponent.fixtureDef.restitution = 0.2f;

        physicsComponent.fixture = physicsComponent.body.createFixture(physicsComponent.fixtureDef);

        boxShape.dispose();

        ashleyEngine.addEntity(plateEntity);


        /* initialize ui */
        ui_stage = new Stage(new ScreenViewport(), batch);

        skin = new Skin();

        interactionStage = new Stage(viewport, batch); // ui for interaction. this is actually the ui, maybe */

        Actor interactionArea = new Actor(); // the interaction area will be at the bottom of the screen
        // may need to set to portrait view
        interactionArea.setBounds(0, 0, camera.viewportWidth, 200);
        interactionArea.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        interactionStage.addActor(interactionArea);

        interactionStage.setDebugAll(true);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(ui_stage);
        multiplexer.addProcessor(interactionStage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        ashleyEngine.update(Gdx.graphics.getDeltaTime());

        batch.begin();
        batch.end();

        box2DDebugRenderer.render(world, camera.combined);

        interactionStage.act();
        interactionStage.draw();

        ui_stage.act(); // even though we're not using it right now
        ui_stage.draw();

        world.step(1 / 45f, 6, 2);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        ui_stage.getViewport().update(width, height, true);
        interactionStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        ui_stage.dispose();
        interactionStage.dispose();
    }
}
