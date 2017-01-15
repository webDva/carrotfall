package ecs;

import com.badlogic.ashley.core.ComponentMapper;

import ecs.components.PhysicsComponent;
import ecs.components.BucketContainerComponent;
import ecs.components.PositionComponent;
import ecs.components.TextureComponent;

/**
 * Created by jeast on 1/8/2017.
 */

public class Mappers {
    public static final ComponentMapper<PhysicsComponent> physicsComponentMapper = ComponentMapper.getFor(PhysicsComponent.class);
    public static final ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<TextureComponent> textureComponentMapper = ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<BucketContainerComponent> bucketContainerMapper = ComponentMapper.getFor(BucketContainerComponent.class);
}
