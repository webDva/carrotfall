package ecs.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by jeast on 1/9/2017.
 */

public class PositionComponent implements Component {
    // Not a vector since the player's plate entity will usually only be updated with the x-value
    public float x;
    public float y;

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
