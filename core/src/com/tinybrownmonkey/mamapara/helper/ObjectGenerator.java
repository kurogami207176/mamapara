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
    private static float personIntervalMin = 0.01f;
    private static float personIntervalRange = 0.055f;

    private static float carIntervalCounter = 0;
    private static float carIntervalMin = 1f;
    private static float carIntervalRange = 5f;

    private static float personRangeMax = GameInfo.HEIGHT * 0.656f;
    private static float personRangeMin = 0;

    private static float personRange = 60;

    private static float totalTime;
    private static float delta;
    private static float groundSpeed;

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

    public static void dispose() {
        for (Texture texture : personTx) {
            texture.dispose();
        }
        for (Texture texture : carsTx) {
            texture.dispose();
        }
    }

    public static void delta(float groundSpeed, float lastDelta){
        ObjectGenerator.delta = lastDelta;
        ObjectGenerator.totalTime = ObjectGenerator.totalTime + lastDelta;
        ObjectGenerator.groundSpeed = groundSpeed;
    }

    public static void resetTime(){
        totalTime = 0;
    }

    public static Person generatePerson(float speedX, float speedY, float angle) {
        if(personIntervalCounter <= 0)
        {
            boolean up = random.nextBoolean();
            float randFloat = random.nextFloat();
            float y = up? (personRangeMax - personRange * randFloat) : (personRangeMin + personRange * randFloat);
            PersonInfo pi = getRandomPersonInfo();
            Person person = new Person(personTx[pi.index], GameInfo.WIDTH, y);
            personIntervalCounter = groundSpeed * (personIntervalMin + random.nextFloat() * personIntervalRange);
            person.setSpeedX(speedX);
            person.setSpeedY(speedY);
            person.setRotation(angle);
            person.setCountdownTime(pi.lifetime);
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

    public static Car generateCar() {
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

    public static PersonInfo getRandomPersonInfo(){
//        float totalLifetime = 0;
//        for(PersonInfo pi: PersonInfo.values())
//        {
//            totalLifetime = totalLifetime + pi.lifetime;
//        }
//        float randFloat = random.nextFloat() * totalLifetime;
//        totalLifetime = 0;
//        for(PersonInfo pi: PersonInfo.values())
//        {
//            totalLifetime = totalLifetime + pi.lifetime;
//            if(randFloat <= totalLifetime)
//            {
//                return  pi;
//            }
//        }
//        return PersonInfo.values()[PersonInfo.values().length - 1];
        return PersonInfo.values()[random.nextInt(PersonInfo.values().length)];
    }

    private enum  PersonInfo
    {
        BLUE (0, 1f),
        RED  (1, 5f),
        GREEN(2, 10f),
        WHITE(3, 20f);
        private static float totalLifetime = 0;

        private int index;
        private float lifetime;

        PersonInfo(int index, float lifetime){
            this.index = index;
            this.lifetime = lifetime;
        }

        public float getLifetime(){
            return lifetime;
        }

        public int getIndex(){
            return index;
        }
    }

}
