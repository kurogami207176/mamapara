package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.graphics.Texture;
import com.tinybrownmonkey.mamapara.actors.Car;
import com.tinybrownmonkey.mamapara.actors.Person;
import com.tinybrownmonkey.mamapara.constants.Constants;
import com.tinybrownmonkey.mamapara.info.GameData;
import com.tinybrownmonkey.mamapara.info.GameInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.tinybrownmonkey.mamapara.constants.Constants.carMaxSpeedRelativeToGroundSpeed;
import static com.tinybrownmonkey.mamapara.constants.Constants.carMinimumSpeedRelativeToGroundSpeed;
import static com.tinybrownmonkey.mamapara.constants.Constants.lanePositions;
import static com.tinybrownmonkey.mamapara.constants.Constants.laneWidth;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class ObjectGenerator {
    private static Random random = new Random();
    private static float personIntervalCounter = 0;
    private static float personIntervalMin = 0.05f;
    private static float personIntervalRange = 0.2f;

    private static float carIntervalCounter = 0;
    private static float carIntervalMin = 1f;
    private static float carIntervalRange = 5f;

    private static float sedanMinTimer = 30f;
    private static float truckMinTimer = 200f;

    private static float constructionMinTimer = 300f;
    private static float constructionIntervalCounter = 0;
    private static float constructionIntervalMin = 45f;
    private static float constructionIntervalRange = 180f;

    private static float personRangeMax = GameInfo.HEIGHT * 0.656f;
    private static float personRangeMin = 0;

    private static float personMovingMinTimer = 45;
    private static float personRange = 40;
    private static float personMovingRange = 200;

    private static int pedestrianGenMax = 200;
    private static int pedestrianGenMin = 20;
    private static int pedestrianGenCtr = 0;

    private static float totalTime;
    private static float delta;
    private static float groundSpeed;

    private static long counter = 0;
    private static Texture[] personTx;

    private static Texture copTx;

    private static GameData gameData;

    public static void loadTextures(GameData gameData){
        ObjectGenerator.gameData = gameData;

        personTx = new Texture[]{
                new Texture("person_blue.png"),
                new Texture("person_red.png"),
                new Texture("person_green.png"),
                new Texture("person_white.png")
        };
        copTx = new Texture("person_cop.png");
        for(Obstructions obs: Obstructions.values()){
            obs.loadTexture();
        }
    }

    private enum Obstructions{
        MOTORCYCLE_BLUE("motorcycle_blue.png", 1f),
        MOTORCYCLE_RED("motorcycle_red.png", 1f),
        MOTORCYCLE_GREEN("motorcycle_green.png", 1f),
        SEDAN_BLUE("car_sedan_blue.png", 1f),
        SEDAN_GREEN("car_sedan_green.png", 1f),
        SEDAN_RED("car_sedan_red.png", 1f),
        SEDAN_PINK("car_sedan_pink.png", 1f),
        SEDAN_BLACK("car_sedan_black.png", 1f),
        JEEPNEY_GREY("jeepney_grey.png", 1f),
        TRUCK("truck.png", 7f),
        CONE("cone.png", 0.01f),
        MMDA_BARRIER("mmda_barrier.png", 0.7f),
        CONCRETE_BARRIER("concrete_barrier.png", 2f),
        STEAMROLLER("steamroller.png", 3f);
        private Texture texture;
        private String textureFile;
        private float weight;
        Obstructions(String textureFile, float weight){
            this.textureFile = textureFile;
            this.weight = weight;
        }

        public void loadTexture(){
            this.texture = new Texture(textureFile);
        }

        public float getWeight() {
            return weight;
        }

        public Texture getTexture() {
            return texture;
        }
    }

    public static void dispose() {
        for (Texture texture : personTx) {
            texture.dispose();
        }
        for (Obstructions texture : Obstructions.values()) {
            texture.getTexture().dispose();
        }
    }

    public static void delta(float groundSpeed, float lastDelta){
        ObjectGenerator.delta = lastDelta;
        ObjectGenerator.totalTime = ObjectGenerator.totalTime + lastDelta;
        ObjectGenerator.groundSpeed = groundSpeed;
    }

    public static void resetTime(){
        totalTime = 0;
        personIntervalCounter = 0;
        carIntervalCounter = 0;
        constructionIntervalCounter = 0;
    }

    public static Person generatePerson(float angle) {
        float speedX = 0;
        float speedY = 0;
        if(personIntervalCounter <= 0)
        {
            int pedestrianGen = pedestrianGenMax - pedestrianGenCtr;
            if(pedestrianGen < pedestrianGenMin){
                pedestrianGen = pedestrianGenMin;
            }
            int ran = random.nextInt(pedestrianGen);
            boolean up = random.nextBoolean();
            float randFloat = random.nextFloat();
            float y;
            if(totalTime > personMovingMinTimer && ran == 0)
            {
                pedestrianGenCtr++;
                speedY = up? -50 : 50;
                y = up? (personRangeMax - personMovingRange * randFloat) : (personRangeMin + personMovingRange * randFloat);
            }
            else
            {
                speedY = 0;
                y = up? (personRangeMax - personRange * randFloat) : (personRangeMin + personRange * randFloat);
            }

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
    private static List<Car> generatedCars = new ArrayList<Car>();
    public static List<Car> generateCar() {
        generatedCars.clear();
        int carIndex = -1;
        int level = 3;
        if(carIntervalCounter <= 0)
        {
            if(totalTime >= sedanMinTimer){
                level = 9;
            }
            if(totalTime >= truckMinTimer){
                level = 10;
            }
            int index = gameData.laneIndex;
            int dice = random.nextInt(100);
            int adder = 0;
            if(dice > 90){
                adder = 2;
            }
            else if(dice > 70){
                adder = 1;
            }
            adder = random.nextBoolean()? adder : -adder;
            index = index + adder;
            if(index < 0) {
                index = 0;
            }
            else if(index > Constants.lanePositions.length - 1){
                index = Constants.lanePositions.length - 1;
            }
            //car
            carIndex = index;
            float y = Constants.lanePositions[index];
            Obstructions obs = Obstructions.values()[random.nextInt(level)];
            Car car = new Car(obs.getTexture(), GameInfo.WIDTH, y, obs.getWeight());
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
            generatedCars.add(car);
        }
        else if(totalTime > constructionMinTimer){
            carIntervalCounter = carIntervalCounter - delta * groundSpeed;
            if(constructionIntervalCounter <= 0) {
                constructionIntervalCounter = groundSpeed * (constructionIntervalMin + random.nextFloat() * constructionIntervalRange);
                int laneCount = (lanePositions.length + 1) / 2;
                int laneIndex = carIndex;
                while(laneIndex == carIndex) {
                    laneIndex = random.nextInt(laneCount);
                    laneIndex = 2 * laneIndex;
                }
                float quartWidth = laneWidth / 4;
                Car cone1 = new Car(Obstructions.CONE.getTexture(), GameInfo.WIDTH, lanePositions[laneIndex] - 2*quartWidth, Obstructions.CONE.getWeight());
                Car cone2 = new Car(Obstructions.CONE.getTexture(), GameInfo.WIDTH, lanePositions[laneIndex], Obstructions.CONE.getWeight());
                Car cone3 = new Car(Obstructions.CONE.getTexture(), GameInfo.WIDTH, lanePositions[laneIndex] + 2*quartWidth, Obstructions.CONE.getWeight());
                Car mmda = new Car(Obstructions.MMDA_BARRIER.getTexture(),GameInfo.WIDTH + 300, lanePositions[laneIndex], Obstructions.MMDA_BARRIER.getWeight());
                Car cone4 = new Car(Obstructions.CONE.getTexture(), GameInfo.WIDTH + 600, lanePositions[laneIndex] - 2*quartWidth, Obstructions.CONE.getWeight());
                Car cone5 = new Car(Obstructions.CONE.getTexture(), GameInfo.WIDTH + 600, lanePositions[laneIndex], Obstructions.CONE.getWeight());
                Car cone6 = new Car(Obstructions.CONE.getTexture(), GameInfo.WIDTH + 600, lanePositions[laneIndex] + 2*quartWidth, Obstructions.CONE.getWeight());
                Car steamroller = new Car(Obstructions.STEAMROLLER.getTexture(), GameInfo.WIDTH + 875, lanePositions[laneIndex] + quartWidth, Obstructions.STEAMROLLER.getWeight());
                Car bar1 = new Car(Obstructions.CONCRETE_BARRIER.getTexture(),GameInfo.WIDTH + 855, lanePositions[laneIndex] - quartWidth, Obstructions.CONCRETE_BARRIER.getWeight());
                Car bar2 = new Car(Obstructions.CONCRETE_BARRIER.getTexture(),GameInfo.WIDTH + 905, lanePositions[laneIndex] - quartWidth, Obstructions.CONCRETE_BARRIER.getWeight());
                Car bar3 = new Car(Obstructions.CONCRETE_BARRIER.getTexture(),GameInfo.WIDTH + 955, lanePositions[laneIndex] - quartWidth, Obstructions.CONCRETE_BARRIER.getWeight());

                generatedCars.add(cone1);
                generatedCars.add(cone2);
                generatedCars.add(cone3);
                generatedCars.add(mmda);
                generatedCars.add(cone4);
                generatedCars.add(cone5);
                generatedCars.add(cone6);
                generatedCars.add(bar1);
                generatedCars.add(bar2);
                generatedCars.add(bar3);
                generatedCars.add(steamroller);
            }
            else
            {
                constructionIntervalCounter = constructionIntervalCounter - delta * groundSpeed;
            }
        }
        else
        {
            carIntervalCounter = carIntervalCounter - delta * groundSpeed;
        }
        return generatedCars;
    }

    public static PersonInfo getRandomPersonInfo(){
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
