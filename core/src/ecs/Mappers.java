package ecs;

import com.badlogic.ashley.core.ComponentMapper;

import ecs.components.PhysicsComponent;

/**
 * Created by jeast on 1/8/2017.
 */

public class Mappers {
    public static final ComponentMapper<PhysicsComponent> physics = ComponentMapper.getFor(PhysicsComponent.class);
}
