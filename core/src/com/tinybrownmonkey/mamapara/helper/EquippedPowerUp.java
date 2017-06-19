package com.tinybrownmonkey.mamapara.helper;

import com.tinybrownmonkey.mamapara.constants.PowerUps;

/**
 * Created by AlainAnne on 19-Jun-17.
 */

public class EquippedPowerUp {
    private PowerUps powerUp;
    private int level;

    public EquippedPowerUp(PowerUps powerUp, int level) {
        this.powerUp = powerUp;
        this.level = level;
    }

    public PowerUps getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUps powerUp) {
        this.powerUp = powerUp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
