package com.tinybrownmonkey.mamapara.constants;

import com.tinybrownmonkey.mamapara.info.GameInfo;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class Constants {
    public final static float skySpeedFraction = 0.25f;
    public final static float groundSpeedFraction = 1f;
    public final static float initGroundSpeed = 600;// 200;
    public final static float topGroundSpeed = 4000;

    public final static float groundSpeedIncrement = 6;

    public final static int changeLaneSpeed = 900;
    private final static float topLaneY = GameInfo.HEIGHT / 2.2f;
    private final static float bottomLaneY = GameInfo.HEIGHT / 10;
    public final static int laneCount = 5;
    public final static float[] lanePositions = new float[laneCount] ;
    public final static float laneWidth = (topLaneY - bottomLaneY) / (laneCount - 1);
    static {
        lanePositions[0] = topLaneY;
        for(int i = 1; i <laneCount - 1; i++){
            lanePositions[i] = lanePositions[i - 1] - laneWidth;
        }
        lanePositions[laneCount - 1] = bottomLaneY;
    }
    public final static int jeepLoc = 50;
    public final static float angleSpeed = 100;
    public final static float grabberRange = 100;
    public final static float grabberSpeed = 200;
    public final static float moneyMultplier = 2;
    public final static int maxPassngersPerSide = 6;
    public final static int maxPassngersPerSideShaman = 9;

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
    public final static float transitionSpeed = 1600;

    public final static float offScreenBuffer = 100;

    public final static float ySpeedGetOff = 100;

    // ask
    public final static int rateAskInterval = 5;

    public final static int shareAskInterval = 7;

    public final static float explodeCycle = 5f;

    public final static float shadowCycle = 0.25f;

    public final static float shamanCycle = .25f;

    public final static int tutMaxPersons = 3;
    public final static int tutMaxCars = 3;
    public final static int tutBonusMoney = 1200;

    public interface ConfirmButton{
        float xOkLeft = 530;
        float xCancelLeft = 249;
        float buttonWidth = 175;

        float yDown = 168;
        float buttonHeight = 79;
    }

    public interface ConfirmButtonTrance{
        float xOkLeft = 580;
        float xCancelLeft = 199;
        float xShare = 349;
        float buttonWidth = 175;

        float yDown = 168;
        float buttonHeight = 79;
    }

    public static float[][] effectTimeMatrix;
    static{
        effectTimeMatrix = new float[PowerUps.values().length - 1][3];

        effectTimeMatrix[PowerUps.EXPLODE.ordinal()][0] = 30;
        effectTimeMatrix[PowerUps.EXPLODE.ordinal()][1] = 60;
        effectTimeMatrix[PowerUps.EXPLODE.ordinal()][2] = 120;

        effectTimeMatrix[PowerUps.RANGE.ordinal()][0] = 30;
        effectTimeMatrix[PowerUps.RANGE.ordinal()][1] = 60;
        effectTimeMatrix[PowerUps.RANGE.ordinal()][2] = 120;

        effectTimeMatrix[PowerUps.SHADOW.ordinal()][0] = 30;
        effectTimeMatrix[PowerUps.SHADOW.ordinal()][1] = 60;
        effectTimeMatrix[PowerUps.SHADOW.ordinal()][2] = 120;

        effectTimeMatrix[PowerUps.SHAMAN.ordinal()][0] = 30;
        effectTimeMatrix[PowerUps.SHAMAN.ordinal()][1] = 60;
        effectTimeMatrix[PowerUps.SHAMAN.ordinal()][2] = 120;

    }

}
