package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class GameManager {
    private static GameManager instance = new GameManager();
    private static Json json = new Json();
    private static FileHandle scoreFileHandle = Gdx.files.internal("bin/Scores.json");
    private static FileHandle gameDataFileHandle = Gdx.files.internal("bin/GameData.json");

    public static void saveScore(Scores scores){
        if(scores != null)
        {
            scoreFileHandle.writeString(json.prettyPrint(scores), false);
        }
    }

    public static Scores loadScores(){
        if(!scoreFileHandle.exists()){
            return new Scores();
        }
        return json.fromJson(Scores.class, scoreFileHandle);
    }
    public static void saveGameData(GameData gameData){
        if(gameData != null)
        {
            gameDataFileHandle.writeString(json.prettyPrint(gameData), false);
        }
    }

    public static GameData loadGameData(){
        if(!gameDataFileHandle.exists()){
            return new GameData();
        }
        return json.fromJson(GameData.class, gameDataFileHandle);
    }

}
