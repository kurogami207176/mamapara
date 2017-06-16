package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

/**
 * Created by alaguipo on 13/06/2017.
 */

public class Person extends Sprite{
    private static Random random = new Random();
    private static float personIntervalCounter = 0;
    private static float personIntervalMin = 0.01f;
    private static float personIntervalRange = 0.05f;

    private static float personRangeMax = GameInfo.HEIGHT * 0.656f;
    private static float personRangeMin = 0;

    private static float personRange = 60;

    public Person(Texture texture, float x, float y){
        super(texture);
        this.setPosition(x, y);
    }


    public static Person generatePerson(Texture personTx, GroundMover groundMoverUp, GroundMover groundMoverDown, float groundSpeed, float delta) {
        if(personIntervalCounter <= 0)
        {
            boolean up = random.nextBoolean();
            float randFloat = random.nextFloat();
            float y = up? (personRangeMax - personRange * randFloat) : (personRangeMin + personRange * randFloat);
            Person person = new Person(personTx, GameInfo.WIDTH-100, y);
            if(up) {
                groundMoverUp.addItem(person, 0, 0, 0, null);
            }
            else {
                groundMoverDown.addItem(person, 0, 0, 0, null);
            }
            personIntervalCounter = groundSpeed * (personIntervalMin + random.nextFloat() * personIntervalRange);
            return person;
        }
        else
        {
            personIntervalCounter = personIntervalCounter - delta * groundSpeed;
        }
        return null;
    }
}
