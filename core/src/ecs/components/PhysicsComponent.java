package ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Created by jeast on 1/8/2017.
 */

public class PhysicsComponent implements Component {
    public BodyDef bodyDef;
    public Body body;

    public FixtureDef fixtureDef;
    public Fixture fixture;
}
