package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alaguipo on 5/08/2017.
 */

public class ParticleManager {
    public enum Particles{
        SMOKE, ENERGY, SHAMAN;
    }

    private Map<Particles, List<Texture>> particlesListMap;

    public void init(){
        particlesListMap = new HashMap<Particles, List<Texture>>();
        particlesListMap.put(Particles.ENERGY,
                Arrays.asList(
                        new Texture("explode_particle1.png"),
                        new Texture("explode_particle2.png")
                ));
        particlesListMap.put(Particles.SHAMAN,
                Arrays.asList(
                        new Texture("shaman_particle1.png"),
                        new Texture("shaman_particle2.png"),
                        new Texture("shaman_particle3.png"),
                        new Texture("shaman_particle4.png")
                ));
        particlesListMap.put(Particles.SMOKE,
                Arrays.asList(
                        new Texture("smoke_particle1.png"),
                        new Texture("smoke_particle2.png")
                ));

    }

    public int getTextureSize(Particles particles){
        return particlesListMap.get(particles).size();
    }

    public List<Texture> getTextures(Particles particles){
        return particlesListMap.get(particles);
    }

    public Texture getTexture(Particles particles, int index){
        return getTextures(particles).get(index);
    }

    public List<Sprite> getSprites(Particles particles){
        final List<Sprite> sprites = new ArrayList<Sprite>();
        for(Texture texture: getTextures(particles)){
            sprites.add(new Sprite(texture));
        }
        return sprites;
    }

    public Sprite getSprite(Particles particles, int index){
        return new Sprite(getTexture(particles, index));
    }

    public void dispose(){
        for(List<Texture> textures: particlesListMap.values()){
            for(Texture texture: textures){
                texture.dispose();
            }
        }
    }
}
