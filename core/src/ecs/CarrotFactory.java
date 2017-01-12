package ecs;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import ecs.components.PhysicsComponent;
import ecs.components.PositionComponent;
import ecs.components.TextureComponent;

/**
 * Created by jeast on 1/11/2017.
 */

public class CarrotFactory {
    public CarrotFactory(Engine ashleyEngine, World box2DWorld, Vector2 carrotPosition) {
        Entity carrotEntity = new Entity();

        // add texture component first so that the physics body can have the same size
        carrotEntity.add(new TextureComponent("carrot.png"));
        carrotEntity.add(new PositionComponent(carrotPosition.x, carrotPosition.y));

        carrotEntity.add(new PhysicsComponent());

        Mappers.physicsComponentMapper.get(carrotEntity).bodyDef = new BodyDef();
        Mappers.physicsComponentMapper.get(carrotEntity).bodyDef.type = BodyDef.BodyType.DynamicBody;
        Mappers.physicsComponentMapper.get(carrotEntity).bodyDef.position.set(carrotPosition.x, carrotPosition.y);

        Mappers.physicsComponentMapper.get(carrotEntity).body = box2DWorld.createBody(Mappers.physicsComponentMapper.get(carrotEntity).bodyDef);

        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(Mappers.textureComponentMapper.get(carrotEntity).texture.getWidth() / 2, Mappers.textureComponentMapper.get(carrotEntity).texture.getHeight() / 2);

        Mappers.physicsComponentMapper.get(carrotEntity).fixtureDef = new FixtureDef();
        Mappers.physicsComponentMapper.get(carrotEntity).fixtureDef.shape = boxShape;
        Mappers.physicsComponentMapper.get(carrotEntity).fixtureDef.density = 0.05f;
        Mappers.physicsComponentMapper.get(carrotEntity).fixtureDef.friction = 0.05f;
        Mappers.physicsComponentMapper.get(carrotEntity).fixtureDef.restitution = 0.29f;

        Mappers.physicsComponentMapper.get(carrotEntity).fixture = Mappers.physicsComponentMapper.get(carrotEntity).body.createFixture(Mappers.physicsComponentMapper.get(carrotEntity).fixtureDef);

        boxShape.dispose();

        // don't know if this will actually work. may have to create a carrot object
        Mappers.physicsComponentMapper.get(carrotEntity).body.setUserData(carrotEntity);

        ashleyEngine.addEntity(carrotEntity);
    }
}
