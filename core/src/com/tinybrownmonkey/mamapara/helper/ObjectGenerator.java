package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.Texture;
import com.tinybrownmonkey.mamapara.actors.Car;
import com.tinybrownmonkey.mamapara.actors.Person;
import com.tinybrownmonkey.mamapara.constants.Constants;
import com.tinybrownmonkey.mamapara.info.GameInfo;

import java.util.Random;

import static com.tinybrownmonkey.mamapara.constants.Constants.carMaxSpeedRelativeToGroundSpeed;
import static com.tinybrownmonkey.mamapara.constants.Constants.carMinimumSpeedRelativeToGroundSpeed;

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
    private static Texture[] personTx;
    private static Texture[] carsTx;

    private static Texture copTx;

    public static void loadTextures(){
        personTx = new Texture[]{
                new Texture("person_blue.png"),
                new Texture("person_red.png"),
                new Texture("person_green.png"),
                new Texture("person_white.png")
        };
        copTx = new Texture("person_cop.png");
        carsTx = new Texture[]{
                new Texture("car_sedan_blue.png"),
                new Texture("car_sedan_green.png"),
                new Texture("car_sedan_red.png"),
                new Texture("car_sedan_pink.png"),
                new Texture("car_sedan_black.png"),
                new Texture("motorcycle_blue.png"),
                new Texture("motorcycle_red.png"),
                new Texture("motorcycle_green.png"),
                new Texture("jeepney_grey.png")
        };
    }

    public static void dispose(){
        for(Texture texture: personTx){
            texture.dispose();
        }
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
            Person person = new Person(personTx[random.nextInt(personTx.length)], GameInfo.WIDTH, y);
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


    public static Car generateCar(float groundSpeed, float delta) {
        if(carIntervalCounter <= 0)
        {
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
