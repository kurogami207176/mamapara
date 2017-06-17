package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

import static com.tinybrownmonkey.mamapara.helper.Constants.carMaxSpeedRelativeToGroundSpeed;
import static com.tinybrownmonkey.mamapara.helper.Constants.carMinimumSpeedRelativeToGroundSpeed;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class ObjectGenerator {
    private static Random random = new Random();
    private static float personIntervalCounter = 0;
    private static float personIntervalMin = 0.1f;
    private static float personIntervalRange = 0.55f;

    private static float carIntervalCounter = 0;
    private static float carIntervalMin = 1f;
    private static float carIntervalRange = 5f;

    private static float personRangeMax = GameInfo.HEIGHT * 0.656f;
    private static float personRangeMin = 0;

    private static float personRange = 60;

    private static long counter = 0;
    private static Texture personTx;
    private static Texture[] carsTx;

    public static void loadTextures(){
        personTx = new Texture("person.png");
        carsTx = new Texture[]{
                new Texture("car_beetle_blue.png"),
                new Texture("car_beetle_green.png"),
                new Texture("car_beetle_red.png")
        };
    }

    public static void dispose(){
        personTx.dispose();
        for(Texture texture: carsTx){
            texture.dispose();
        }
    }

    public static Person generatePerson(float groundSpeed, float speedX, float speedY, float angle, float lifeTime, float delta) {
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
            person.setCountdownTime(lifeTime);
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


    public static Car generateCar(float groundSpeed,float delta) {
        if(carIntervalCounter <= 0)
        {
            boolean up = random.nextBoolean();
            float randFloat = random.nextFloat();
            float y = Constants.lanePositions[random.nextInt(Constants.lanePositions.length)];
            Car car = new Car(carsTx[random.nextInt(carsTx.length)], GameInfo.WIDTH, y);
            carIntervalCounter = groundSpeed * (carIntervalMin + random.nextFloat() * carIntervalRange);
            float minSpeed = groundSpeed * carMinimumSpeedRelativeToGroundSpeed;
            float maxSpeed = groundSpeed * carMaxSpeedRelativeToGroundSpeed;
            float range = maxSpeed - minSpeed;
            float speedX =  minSpeed + range * random.nextFloat();
            car.setSpeedX(speedX);
            car.setSpeedY(0);
            car.setRotation(0);
            car.setCountdownTime(10);
            car.setId(counter);
            counter++;
            return car;
        }
        else
        {
            carIntervalCounter = carIntervalCounter - delta * groundSpeed;
        }
        return null;
    }

}
