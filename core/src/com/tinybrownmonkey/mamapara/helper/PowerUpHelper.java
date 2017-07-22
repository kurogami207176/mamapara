package com.tinybrownmonkey.mamapara.helper;

import com.tinybrownmonkey.mamapara.constants.PowerUps;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlainAnne on 23-Jun-17.
 */

public class PowerUpHelper {


    public float[] effectTime = new float[PowerUps.values().length - 1];

    public float[] effectMaxTime = new float[PowerUps.values().length - 1];


    public float[] effectAccum = new float[PowerUps.values().length - 1];

    public boolean[] effectFlag = new boolean[PowerUps.values().length - 1];

    public void resetAll(){
        for(int i = 0; i < effectAccum.length; i++){
            effectAccum[i] = 0;
        }
        for(int i = 0; i < effectTime.length; i++){
            effectTime[i] = 0;
        }
        for(int i = 0; i < effectTime.length; i++){
            effectMaxTime[i] = 0;
        }
    }

    public void update(float delta){
        for(int i = 0; i < effectTime.length; i++){
            effectTime[i] =  effectTime[i] - delta;
            if(effectTime[i] < 0)
            {
                effectTime[i] = 0;
                effectMaxTime[i] = 0;
            }
        }

        addAccumulator(PowerUps.RANGE, delta);
        addAccumulator(PowerUps.EXPLODE, delta);
        addAccumulator(PowerUps.SHAMAN, delta);
        addAccumulator(PowerUps.SHADOW, delta);
    }

    private void addAccumulator(PowerUps pu, float delta) {
        if(isEffectActive(pu))
        {
            addEffectAccumulator(pu, delta);
        }
        else
        {
            resetEffectAccumulator(pu);
        }
    }

    public boolean isEffectActive(PowerUps pu){
        return effectTime[pu.ordinal()] > 0;
    }

    public float getEffectTime(PowerUps pu){
        return effectTime[pu.ordinal()];
    }

    public float getEffectMaxTime(PowerUps pu){
        return effectMaxTime[pu.ordinal()];
    }

    public float getEffectAccumulator(PowerUps pu){
        return effectAccum[pu.ordinal()];
    }

    public boolean getEffectFlag(PowerUps pu){
        return effectFlag[pu.ordinal()];
    }

    public void setEffectFlag(PowerUps pu, boolean val){
        effectFlag[pu.ordinal()] = val;
    }

    public void resetEffectAccumulator(PowerUps pu){
        effectAccum[pu.ordinal()] = 0;
    }

    public void addEffectTime(PowerUps pu, float adder){
        effectTime[pu.ordinal()] =  effectTime[pu.ordinal()] + adder;
        effectMaxTime[pu.ordinal()] =  effectMaxTime[pu.ordinal()] + adder;
    }

    public void addEffectAccumulator(PowerUps pu, float adder){
        effectAccum[pu.ordinal()] =  effectAccum[pu.ordinal()] + adder;
    }

    public List<ActivePowerUp> getActivePowerupList(){
        List<ActivePowerUp> retVa = new ArrayList<ActivePowerUp>();
        for(PowerUps pu: PowerUps.values()){
           if(pu != PowerUps.SLOT && isEffectActive(pu))
           {
               ActivePowerUp apu = new ActivePowerUp();
               apu.pu = pu;
               apu.timeLeft = getEffectTime(pu);
               apu.timeMax = getEffectMaxTime(pu);
               retVa.add(apu);
           }
        }
        return retVa;
    }

    public static class ActivePowerUp{
        public PowerUps pu;
        public float timeLeft;
        public float timeMax;
    }

}
