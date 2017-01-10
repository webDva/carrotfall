package ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import ecs.Mappers;
import ecs.components.PhysicsComponent;
import ecs.components.PositionComponent;

/**
 * Created by jeast on 1/9/2017.
 */

public class UpdatePositionSystem extends IteratingSystem {
    /*
    this might actually be pointless
     */
    public UpdatePositionSystem() {
        super(Family.all(PositionComponent.class, PhysicsComponent.class).get());
    }

    public void processEntity(Entity entity, float deltaTime) {
        // lol, should've made PositionComponent use Vector2 instead, so i wouldn't have to type so much!
        Mappers.positionComponentMapper.get(entity).x = Mappers.physicsComponentMapper.get(entity).body.getPosition().x;
        Mappers.positionComponentMapper.get(entity).y = Mappers.physicsComponentMapper.get(entity).body.getPosition().y;
    }
}
