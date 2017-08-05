package com.tinybrownmonkey.mamapara.actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tinybrownmonkey.mamapara.constants.GameState;
import com.tinybrownmonkey.mamapara.info.GameInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alaguipo on 29/07/2017.
 */

public class SequenceLabeller { // extends Labeller {
    List<Labeller> labels;
    public SequenceLabeller(){
        this.labels = new ArrayList<Labeller>();
    }
    public SequenceLabeller(List<Labeller> labels){
        this.labels = labels;
    }
    public void add(Labeller label){
        this.labels.add(label);
    }

    //@Override
    public void update(float delta, GameState state){
        if(labels.size() <= 0) return;
        labels.get(0).update(delta, state);
        if(labels.get(0).isExpired())
        {
            labels.get(0).onExpire();
            labels.remove(0);
        }
    }

    //@Override
    public void draw(SpriteBatch spriteBatch, GameState state){
        if(labels.size() <= 0) return;
        labels.get(0).draw(spriteBatch, state);
    }

    //@Override
    public void draw(ShapeRenderer shapeRenderer, GameState state){
        if(labels.size() <= 0) return;
        labels.get(0).draw(shapeRenderer, state);
    }

    //@Override
    public boolean isExpired() {
        boolean retVal;
        if(labels.size() <= 0) {
            retVal = true;
        }
        else {
            retVal = labels.get(0).isExpired();
        }
        return retVal;
    }

    //@Override
    public boolean isVisible(GameState state){
        if(labels.size() <= 0) return false;
        return labels.get(0).isVisible(state);
    }

    //@Override
    public void onExpire(){

    }

}
