package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class TextureManager {
    public static Texture get(String location){
        return new Texture(location);
    }
}
