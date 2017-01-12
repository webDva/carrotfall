package ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by jeast on 1/12/2017.
 */

public class TextureComponent implements Component {
    public Texture texture;

    public TextureComponent(String path) {
        this.texture = new Texture(Gdx.files.internal(path)); // constructor's here, because don't wanna load texture elsewhere
    }
}
