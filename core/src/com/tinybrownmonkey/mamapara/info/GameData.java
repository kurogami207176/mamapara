package com.tinybrownmonkey.mamapara.info;

import com.tinybrownmonkey.mamapara.actors.Person;
import com.tinybrownmonkey.mamapara.helper.GroundMover;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class GameData {

    public GameState currState;
    public GroundMover<Person> groundMover;
    public float groundSpeed = Constants.initGroundSpeed;
    public float skySpeed = Constants.initSkySpeed;

    public float timeoutToGameOverAccum = 0;
    public boolean timeoutToGameOverBool = false;
    public int laneIndex = Math.round(((float)Constants.lanePositions.length)/ 2f) - 1;
    {
        System.out.println("laneIndex=" + laneIndex);
    }

    public enum GameState{
        MAIN_MENU, HIGH_SCORE, TRANSITION_TO_GAME, GAME_PLAY, GAME_END, TRANSITION_TO_MENU;
    }

}