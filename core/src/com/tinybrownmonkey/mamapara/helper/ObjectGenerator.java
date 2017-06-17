package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class ObjectGenerator {
    private static Random random = new Random();
    private static float personIntervalCounter = 0;
    //    private static float personIntervalMin = 0.01f;
//    private static float personIntervalRange = 0.05f;
    private static float personIntervalMin = 0.01f;
    private static float personIntervalRange = 0.05f;

    private static float personRangeMax = GameInfo.HEIGHT * 0.656f;
    private static float personRangeMin = 0;

    private static float personRange = 60;

    private static long counter = 0;
    public static Person generatePerson(Texture personTx, float groundSpeed, float speedX, float speedY, float angle, float delta) {
        if(personIntervalCounter <= 0)
        {
            boolean up = random.nextBoolean();
            float randFloat = random.nextFloat();
            float y = up? (personRangeMax - personRange * randFloat) : (personRangeMin + personRange * randFloat);
            Person person = new Person(personTx, GameInfo.WIDTH, y);
            personIntervalCounter = groundSpeed * (personIntervalMin + random.nextFloat() * personIntervalRange);
            person.setSpeedX(speedX);
            person.setSpeedY(speedY);
            person.setRotation(angle);
            person.setCountdownTime(10);
            person.setId(counter);
            counter++;
            return person;
        }
        else
        {
            personIntervalCounter = personIntervalCounter - delta * groundSpeed;
        }
        return null;
    }

}
