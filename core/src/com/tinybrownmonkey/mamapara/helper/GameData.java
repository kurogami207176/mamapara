package com.tinybrownmonkey.mamapara.helper;

import com.tinybrownmonkey.mamapara.scenes.GameScene;

import java.util.List;

import static com.tinybrownmonkey.mamapara.helper.Constants.initGroundSpeed;
import static com.tinybrownmonkey.mamapara.helper.Constants.initSkySpeed;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class GameData {
    public List<MovingObject> persons;

    public GameState currState;
    public GroundMover<Person> groundMover;
    public float groundSpeed = initGroundSpeed;
    public float skySpeed = initSkySpeed;

    public float timeoutToGameOverAccum = 0;
    public boolean timeoutToGameOverBool = false;
    public int laneIndex = 1;

    public enum GameState{
        MAIN_MENU, HIGH_SCORE, TRANSITION_TO_GAME, GAME_PLAY, GAME_END, TRANSITION_TO_MENU;
    }

}
