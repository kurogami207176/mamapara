package com.tinybrownmonkey.mamapara.info;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import com.tinybrownmonkey.mamapara.constants.PowerUps;
import com.tinybrownmonkey.mamapara.helper.EquippedPowerUp;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class GameSave {
    private float distance;
    private float money;

    private int highScore;
    private int moneyTotal;

    private boolean muted = false;
    private int launchCount = 0;

    private int lastShareAsk = 0;
    private boolean disableShareAsk = false;

    private int lastRatingAsk = 0;
    private boolean disableRatingAsk = false;

    private String lastVersionNo;

    private int slotCount = 0;
    private int powerUpType1 = -1;
    private int powerUpType2 = -1;
    private int powerUpType3 = -1;
    private int powerUpType4 = -1;
    private int powerUpType5 = -1;
    private int powerUpType6 = -1;

    private int level1 = 0;
    private int level2 = 0;
    private int level3 = 0;
    private int level4 = 0;
    private int level5 = 0;
    private int level6 = 0;

    //tutorial
    private boolean tutMainMenu = false;
    private boolean tutShop = false;
    private boolean tutPersons = false;
    private boolean tutCar = false;
    private boolean tutChangeLane = false;
    private boolean tutBonus = false;
    private boolean tutPowerUp = false;
    private boolean tutDistance = false;
    private boolean tutMoney = false;

    public void enableTutorials(){
        moneyTotal = 0;
        tutMainMenu = false;
        tutShop = false;
        tutPersons = false;
        tutCar = false;
        tutChangeLane = false;
        tutPowerUp = false;
        tutBonus = false;
        tutDistance = false;
        tutMoney = false;
    }

    public void addDistance(float distance){
        this.distance = this.distance + distance;
        if(this.distance > highScore)
        {
            highScore = (int) this.distance;
        }
    }

    public void addMoney(int money){
        this.money = this.money + money;
        this.moneyTotal = this.moneyTotal + money;
    }

    public int getDistance(){
        return (int) distance;
    }

    public int getMoney(){
        return (int) money;
    }

    public void reset(){
        distance = 0;
        money = 0;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public int getLaunchCount() {
        return launchCount;
    }

    public void setLaunchCount(int launchCount) {
        this.launchCount = launchCount;
    }

    public int getLastShareAsk() {
        return lastShareAsk;
    }

    public void setLastShareAsk(int lastShareAsk) {
        this.lastShareAsk = lastShareAsk;
    }

    public boolean isDisableShareAsk() {
        return disableShareAsk;
    }

    public void setDisableShareAsk(boolean disableShareAsk) {
        this.disableShareAsk = disableShareAsk;
    }

    public int getLastRatingAsk() {
        return lastRatingAsk;
    }

    public void setLastRatingAsk(int lastRatingAsk) {
        this.lastRatingAsk = lastRatingAsk;
    }

    public boolean isDisableRatingAsk() {
        return disableRatingAsk;
    }

    public void setDisableRatingAsk(boolean disableRatingAsk) {
        this.disableRatingAsk = disableRatingAsk;
    }

    public int getMoneyTotal() {
        return moneyTotal;
    }

    public void buy(int moneyTotal) {
        this.moneyTotal = this.moneyTotal - moneyTotal;
    }

    public int getSlotCount() {
        return slotCount;
    }

    public void setSlotCount(int slotCount) {
        this.slotCount = slotCount;
    }

    public int getPowerUpType1() {
        return powerUpType1;
    }

    public void setPowerUpType1(int powerUpType1) {
        this.powerUpType1 = powerUpType1;
    }

    public int getPowerUpType2() {
        return powerUpType2;
    }

    public void setPowerUpType2(int powerUpType2) {
        this.powerUpType2 = powerUpType2;
    }

    public int getPowerUpType3() {
        return powerUpType3;
    }

    public void setPowerUpType3(int powerUpType3) {
        this.powerUpType3 = powerUpType3;
    }

    public int getPowerUpType4() {
        return powerUpType4;
    }

    public void setPowerUpType4(int powerUpType4) {
        this.powerUpType4 = powerUpType4;
    }

    public int getPowerUpType5() {
        return powerUpType5;
    }

    public void setPowerUpType5(int powerUpType5) {
        this.powerUpType5 = powerUpType5;
    }

    public int getPowerUpType6() {
        return powerUpType6;
    }

    public void setPowerUpType6(int powerUpType6) {
        this.powerUpType6 = powerUpType6;
    }

    public int getLevel1() {
        return level1;
    }

    public void setLevel1(int level1) {
        this.level1 = level1;
    }

    public int getLevel2() {
        return level2;
    }

    public void setLevel2(int level2) {
        this.level2 = level2;
    }

    public int getLevel3() {
        return level3;
    }

    public void setLevel3(int level3) {
        this.level3 = level3;
    }

    public int getLevel4() {
        return level4;
    }

    public void setLevel4(int level4) {
        this.level4 = level4;
    }

    public int getLevel5() {
        return level5;
    }

    public void setLevel5(int level5) {
        this.level5 = level5;
    }

    public int getLevel6() {
        return level6;
    }

    public void setLevel6(int level6) {
        this.level6 = level6;
    }

    List<EquippedPowerUp> epu;
    public List<EquippedPowerUp> getPowerUps(){
        if(epu == null)
        {
            epu = generatePowerUps();
        }
        return this.epu;
    }
    public List<EquippedPowerUp> generatePowerUps(){
        List<EquippedPowerUp> retVal = new ArrayList<EquippedPowerUp>();
        if(powerUpType1 >= 0)
        {
            retVal.add(new EquippedPowerUp(PowerUps.values()[powerUpType1], level1));
        }
        if(powerUpType2 >= 0)
        {
            retVal.add(new EquippedPowerUp(PowerUps.values()[powerUpType2], level2));
        }
        if(powerUpType3 >= 0)
        {
            retVal.add(new EquippedPowerUp(PowerUps.values()[powerUpType3], level3));
        }
        if(powerUpType4 >= 0)
        {
            retVal.add(new EquippedPowerUp(PowerUps.values()[powerUpType4], level4));
        }
        if(powerUpType5 >= 0)
        {
            retVal.add(new EquippedPowerUp(PowerUps.values()[powerUpType5], level5));
        }
        if(powerUpType6 >= 0)
        {
            retVal.add(new EquippedPowerUp(PowerUps.values()[powerUpType6], level6));
        }

        return retVal;
    }

    public void setPowerUps(List<EquippedPowerUp> powerUps){
        powerUpType1 = -1;
        powerUpType2 = -1;
        powerUpType3 = -1;
        powerUpType4 = -1;
        powerUpType5 = -1;
        powerUpType6 = -1;
        for(int i = 0; i < powerUps.size(); i++){
            EquippedPowerUp epu = powerUps.get(i);
            switch (i)
            {
                case 0:
                    powerUpType1 = PowerUps.getIndex(epu.getPowerUp());
                    level1 = epu.getLevel();
                    break;
                case 1:
                    powerUpType2 = PowerUps.getIndex(epu.getPowerUp());
                    level2 = epu.getLevel();
                    break;
                case 2:
                    powerUpType3 = PowerUps.getIndex(epu.getPowerUp());
                    level3 = epu.getLevel();
                    break;
                case 3:
                    powerUpType4 = PowerUps.getIndex(epu.getPowerUp());
                    level4 = epu.getLevel();
                    break;
                case 4:
                    powerUpType5 = PowerUps.getIndex(epu.getPowerUp());
                    level5 = epu.getLevel();
                    break;
                case 5:
                    powerUpType6 = PowerUps.getIndex(epu.getPowerUp());
                    level6 = epu.getLevel();
                    break;
            }
        }
        this.epu = powerUps;
    }

    public void addPowerUp(PowerUps pu, int level){
        List<EquippedPowerUp> epus = getPowerUps();
        epus.add(new EquippedPowerUp(pu, level));
        setPowerUps(epus);
    }

    public void removePowerUp(int index){
        List<EquippedPowerUp> epus = getPowerUps();
        if(index < epus.size()) {
            epus.remove(index);
            setPowerUps(epus);
        }
    }

    public void clearAllPowerUps(){
        setPowerUps(new ArrayList<EquippedPowerUp>());
    }

    public String getLastVersionNo() {
        return lastVersionNo;
    }

    public void setLastVersionNo(String lastVersionNo) {
        this.lastVersionNo = lastVersionNo;
    }


    public boolean isTutMainMenu() {
        return tutMainMenu;
    }

    public void setTutMainMenu(boolean tutMainMenu) {
        this.tutMainMenu = tutMainMenu;
    }

    public boolean isTutShop() {
        return tutShop;
    }

    public void setTutShop(boolean tutShop) {
        this.tutShop = tutShop;
    }

    public boolean isTutChangeLane() {
        return tutChangeLane;
    }

    public void setTutChangeLane(boolean tutChangeLane) {
        this.tutChangeLane = tutChangeLane;
    }

    public boolean isTutPowerUp() {
        return tutPowerUp;
    }

    public void setTutPowerUp(boolean tutPowerUp) {
        this.tutPowerUp = tutPowerUp;
    }

    public boolean isTutPersons() {
        return tutPersons;
    }

    public void setTutPersons(boolean tutPersons) {
        this.tutPersons = tutPersons;
    }

    public boolean isTutCar() {
        return tutCar;
    }

    public void setTutCar(boolean tutCar) {
        this.tutCar = tutCar;
    }

    public boolean isTutBonus() {
        return tutBonus;
    }

    public void setTutBonus(boolean tutBonus) {
        this.tutBonus = tutBonus;
    }

    public boolean isTutDistance() {
        return tutDistance;
    }

    public void setTutDistance(boolean tutDistance) {
        this.tutDistance = tutDistance;
    }

    public boolean isTutMoney() {
        return tutMoney;
    }

    public void setTutMoney(boolean tutMoney) {
        this.tutMoney = tutMoney;
    }
}
