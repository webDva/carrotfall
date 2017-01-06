package com.gigamesh.carrotfall;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class CarrotFall extends Game {
    SpriteBatch batch;
    Texture img;

    /* view settings */
    OrthographicCamera camera;
    FitViewport viewport;

    /* UI */
    Stage ui_stage, interactionStage;
    Skin skin;

    /* Box2D */
    World world;
    Box2DDebugRenderer box2DDebugRenderer;

    /* Ashley ECS */
    Engine ashleyEngine;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");

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

        /* initialize ui */
        ui_stage = new Stage(new ScreenViewport(), batch);

        skin = new Skin();

        interactionStage = new Stage(viewport, batch); // ui for interaction. this is actually the ui, maybe */

        Actor interactionArea = new Actor(); // the interaction area will be at the bottom of the screen
        // may need to set to portrait view
        interactionArea.setBounds(0, 0, camera.viewportWidth, 110);
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

        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();

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
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        ui_stage.dispose();
    }
}
