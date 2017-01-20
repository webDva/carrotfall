package ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.gigamesh.carrotfall.CarrotFall;

import ecs.Mappers;

/**
 * Created by jeast on 1/12/2017.
 */

public class BucketContainerComponent implements Component {
    /*
    just to differentiate between the bucket and carrots--and to contain the bucket's two walls
     */
    public BodyDef bodyDef;
    public Body body_1, body_2;

    public FixtureDef fixtureDef;
    public Fixture fixture_1, fixture_2;

    public BucketContainerComponent(Entity bucketEntity, World world) {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.position.set(Mappers.physicsComponentMapper.get(bucketEntity).bodyDef.position.x - CarrotFall.BUCKET_WIDTH, Mappers.physicsComponentMapper.get(bucketEntity).bodyDef.position.y + CarrotFall.BUCKET_HEIGHT);
        body_1 = world.createBody(bodyDef);

        bodyDef.position.set(Mappers.physicsComponentMapper.get(bucketEntity).bodyDef.position.x + CarrotFall.BUCKET_WIDTH, Mappers.physicsComponentMapper.get(bucketEntity).bodyDef.position.y + CarrotFall.BUCKET_HEIGHT);
        body_2 = world.createBody(bodyDef);

        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(1, CarrotFall.BUCKET_HEIGHT);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = boxShape;
        fixtureDef.density = 20f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0f;

        fixture_1 = body_1.createFixture(fixtureDef);
        fixture_2 = body_2.createFixture(fixtureDef);

        boxShape.dispose();

        // bucket walls welded to the end of the bucket bottom
        WeldJointDef jointDef_1 = new WeldJointDef();
        jointDef_1.initialize(body_1, Mappers.physicsComponentMapper.get(bucketEntity).body, new Vector2(body_1.getPosition().x, body_1.getPosition().y - CarrotFall.BUCKET_HEIGHT));

        WeldJointDef jointDef_2 = new WeldJointDef();
        jointDef_2.initialize(body_2, Mappers.physicsComponentMapper.get(bucketEntity).body, new Vector2(body_2.getPosition().x, body_2.getPosition().y - CarrotFall.BUCKET_HEIGHT));

        world.createJoint(jointDef_1);
        world.createJoint(jointDef_2);
    }
}
