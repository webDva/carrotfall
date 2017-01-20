package com.gigamesh.carrotfall;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import ecs.CarrotFactory;
import ecs.Mappers;
import ecs.components.BucketContainerComponent;
import ecs.components.PhysicsComponent;
import ecs.components.PositionComponent;
import ecs.components.TextureComponent;
import ecs.systems.DrawSpritesSystem;
import ecs.systems.StopBucketMovementSystem;

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

    public static final int BUCKET_WIDTH = 58, BUCKET_HEIGHT = 68;

    /* ground texture */
    private Texture groundTexture;

    @Override
    public void create() {
        batch = new SpriteBatch();

        groundTexture = new Texture(Gdx.files.internal("ground.png"));

        /* 800x600 resolution */
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);
        viewport.apply();

        /* initializing Box2D */
        Box2D.init();
        world = new World(new Vector2(0, -10), true);
        box2DDebugRenderer = new Box2DDebugRenderer(); // so that we can see the physics for now

        /* ashley ecs */
        ashleyEngine = new Engine();

        /* adding the new StopBucketMovementSystem */
        StopBucketMovementSystem stopBucketMovementSystem = new StopBucketMovementSystem(camera.viewportWidth);
        ashleyEngine.addSystem(stopBucketMovementSystem);

        /* and adding the new UpdateSpritePositionSystem */
        DrawSpritesSystem drawSpritesSystem = new DrawSpritesSystem(batch);
        ashleyEngine.addSystem(drawSpritesSystem);

        /* using the new carrot factory */
//        for (int i = 0; i < 100; i++) {
//            new CarrotFactory(ashleyEngine, world, new Vector2(MathUtils.random(camera.viewportWidth), 550));
//        }

        final Entity bucketEntity = new Entity();

        /* create the ground (should do it as an entity later on) */
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0, 0);

        Body groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(camera.viewportWidth, 20);
        groundBody.createFixture(groundBox, 0);
        groundBox.dispose();

        /* creating the walls too now */
        BodyDef wallBodyDef = new BodyDef();

        PolygonShape wallBoxShape = new PolygonShape();
        wallBoxShape.setAsBox(1, camera.viewportHeight); // one pixel-wide walls

        /* creating wall one */
        wallBodyDef.position.set(0, 0);
        Body wallBody1 = world.createBody(wallBodyDef);
        wallBody1.createFixture(wallBoxShape, 0);

        /* and creating wall 2 */
        wallBodyDef.position.set(camera.viewportWidth, 0);
        Body wallBody2 = world.createBody(wallBodyDef);
        wallBody2.createFixture(wallBoxShape, 0);

        wallBoxShape.dispose();

        /*
        at this point, since we're writing the same entity creation code twice, i decide
        to design entity creation to be in a separate scope as a function
         */

        /* creating the player's moving bucket to catch the carrots */
        bucketEntity.add(new PositionComponent(70, 10)); // don't know which position is used
        bucketEntity.add(new PhysicsComponent());
        bucketEntity.add(new TextureComponent("bucket.png"));

        Mappers.physicsComponentMapper.get(bucketEntity).bodyDef = new BodyDef();
        Mappers.physicsComponentMapper.get(bucketEntity).bodyDef.type = BodyDef.BodyType.KinematicBody;
        Mappers.physicsComponentMapper.get(bucketEntity).bodyDef.position.set(233, 70); // don't know which position is used

        Mappers.physicsComponentMapper.get(bucketEntity).body = world.createBody(Mappers.physicsComponentMapper.get(bucketEntity).bodyDef);

        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(BUCKET_WIDTH, 1);

        Mappers.physicsComponentMapper.get(bucketEntity).fixtureDef = new FixtureDef();
        Mappers.physicsComponentMapper.get(bucketEntity).fixtureDef.shape = boxShape;
        Mappers.physicsComponentMapper.get(bucketEntity).fixtureDef.density = 20f; // might not really matter since it's a kinematic body
        Mappers.physicsComponentMapper.get(bucketEntity).fixtureDef.friction = 0.5f;
        Mappers.physicsComponentMapper.get(bucketEntity).fixtureDef.restitution = 0f;

        Mappers.physicsComponentMapper.get(bucketEntity).fixture = Mappers.physicsComponentMapper.get(bucketEntity).body.createFixture(Mappers.physicsComponentMapper.get(bucketEntity).fixtureDef);

        boxShape.dispose();

        bucketEntity.add(new BucketContainerComponent(bucketEntity, world));

        // need this for rendering
        Mappers.physicsComponentMapper.get(bucketEntity).body.setUserData(bucketEntity);

        ashleyEngine.addEntity(bucketEntity);


        /* initialize ui */
        ui_stage = new Stage(new ScreenViewport(), batch);

        skin = new Skin();

        interactionStage = new Stage(viewport, batch); // ui for interaction. this is actually the ui, maybe */

        Actor interactionArea = new Actor(); // the interaction area will be at the bottom of the screen
        // may need to set to portrait view
        interactionArea.setBounds(0, 0, camera.viewportWidth, 200);
        /*
        movement will be controlled here instead of in a separate scope such as an entity system
         */
        interactionArea.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!(Mappers.physicsComponentMapper.get(bucketEntity).body.getPosition().x >= x)) {
                    Mappers.physicsComponentMapper.get(bucketEntity).body.setLinearVelocity(400, 0);
                } else if (!(Mappers.physicsComponentMapper.get(bucketEntity).body.getPosition().x <= x)) {
                    Mappers.physicsComponentMapper.get(bucketEntity).body.setLinearVelocity(-400, 0);
                }
                return true;
            }
        });
        interactionStage.addActor(interactionArea);

        interactionStage.setDebugAll(true);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(ui_stage);
        multiplexer.addProcessor(interactionStage);
        Gdx.input.setInputProcessor(multiplexer);

        /*
        Timer scheduler for dropping carrots
         */
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                new CarrotFactory(ashleyEngine, world, new Vector2(MathUtils.random(camera.viewportWidth), 550));
            }
        }, 0, 3f);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(groundTexture, 0, 0);
        ashleyEngine.update(Gdx.graphics.getDeltaTime()); // this might be bad, but it works for now
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
