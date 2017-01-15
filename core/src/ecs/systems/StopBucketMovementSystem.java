package ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import ecs.Mappers;
import ecs.components.PhysicsComponent;
import ecs.components.BucketContainerComponent;
import ecs.components.PositionComponent;

/**
 * Created by jeast on 1/9/2017.
 */

public class StopBucketMovementSystem extends IteratingSystem {
    private float camerasViewportWidth;

    public StopBucketMovementSystem(float camerasViewportWidth) {
        super(Family.all(PositionComponent.class, PhysicsComponent.class, BucketContainerComponent.class).get());

        this.camerasViewportWidth = camerasViewportWidth;
    }

    /*
    this isn't good game design, but it works!
     */
    public void processEntity(Entity entity, float deltaTime) {
        if (Mappers.physicsComponentMapper.get(entity).body.getPosition().x <= 0) {
            Mappers.physicsComponentMapper.get(entity).body.setLinearVelocity(1, 0);
        } else if (Mappers.physicsComponentMapper.get(entity).body.getPosition().x >= camerasViewportWidth) {
            Mappers.physicsComponentMapper.get(entity).body.setLinearVelocity(-1, 0);
        }
    }
}
