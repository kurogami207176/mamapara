package com.tinybrownmonkey.mamapara.info;

import com.tinybrownmonkey.mamapara.constants.Constants;
import com.tinybrownmonkey.mamapara.constants.GameState;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class GameData {

    public GameState currState;
    public float groundSpeed = Constants.initGroundSpeed;

    public float timeoutToGameOverAccum = 0;
    public boolean timeoutToGameOverBool = false;
    public int laneIndex = Math.round(((float)Constants.lanePositions.length)/ 2f) - 1;
    {
        System.out.println("laneIndex=" + laneIndex);
    }

}
