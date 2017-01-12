package ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ecs.Mappers;
import ecs.components.PhysicsComponent;
import ecs.components.PositionComponent;
import ecs.components.TextureComponent;

/**
 * Created by jeast on 1/12/2017.
 */

public class DrawSpritesSystem extends IteratingSystem {
    private SpriteBatch batch;

    public DrawSpritesSystem(SpriteBatch batch) {
        super(Family.all(PhysicsComponent.class, TextureComponent.class, PositionComponent.class).get());
        this.batch = batch; // don't really wanna do this
    }

    public void processEntity(Entity entity, float deltaTime) {
        Entity spriteObject = (Entity) Mappers.physicsComponentMapper.get(entity).body.getUserData(); // maybe redundant, but it looks cool and it's new to me

        if (spriteObject != null) {
            spriteObject.getComponent(PositionComponent.class).x = spriteObject.getComponent(PhysicsComponent.class).body.getPosition().x;
            spriteObject.getComponent(PositionComponent.class).y = spriteObject.getComponent(PhysicsComponent.class).body.getPosition().y;
        }

        batch.draw(spriteObject.getComponent(TextureComponent.class).texture,
                spriteObject.getComponent(PositionComponent.class).x - spriteObject.getComponent(TextureComponent.class).texture.getWidth() / 2,
                spriteObject.getComponent(PositionComponent.class).y - spriteObject.getComponent(TextureComponent.class).texture.getHeight() / 2);
    }
}
