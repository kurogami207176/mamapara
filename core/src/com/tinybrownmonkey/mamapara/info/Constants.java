package com.tinybrownmonkey.mamapara.info;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class Constants {
    public final static float initSkySpeed = 33;
    public final static float initGroundSpeed = 400;
    public final static float topGroundSpeed = 3000;

    public final static int changeLaneSpeed = 300;
    private final static float topLaneY = GameInfo.HEIGHT / 2.28f;
    private final static float bottomLaneY = GameInfo.HEIGHT / 10;
    public final static int laneCount = 5;
    public final static float[] lanePositions = new float[laneCount] ;
    static {
        float interval = (topLaneY - bottomLaneY) / (laneCount - 1);
        lanePositions[0] = topLaneY;
        for(int i = 1; i <laneCount - 1; i++){
            lanePositions[i] = lanePositions[i - 1] - interval;
        }
        lanePositions[laneCount - 1] = bottomLaneY;
    }
    public final static int jeepLoc = 50;
    public final static float groundSpeedIncrement = 10;
    public final static float angleSpeed = 100;
    public final static float grabberRange = 100;
    public final static float grabberSpeed = 200;
    public final static float moneyMultplier = 2;
    public final static int maxPassngersPerSide = 8;

    public final static float bumpX = 200f;
    public final static float bumpY = 200f;
    public final static float bumpRotation = -100f;

    public final static float carBumpX = 50f;
    public final static float carBumpY = 50f;
    public final static float carBumpRotation = -25f;

    public final static float carMinimumSpeedRelativeToGroundSpeed = 0.1f;
    public final static float carMaxSpeedRelativeToGroundSpeed = 0.5f;

    public final static float bumpXMult = 1.2f;

    public final static float timeoutToGameOver = 0.5f;
    public final static float transitionSpeed = 800;

    public final static float offScreenBuffer = 100;

    public final static float ySpeedGetOff = 100;


}
