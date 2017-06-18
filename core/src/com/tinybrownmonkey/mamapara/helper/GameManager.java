package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.tinybrownmonkey.mamapara.info.GameData;
import com.tinybrownmonkey.mamapara.info.Scores;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class GameManager {
    private static GameManager instance = new GameManager();
    private static Json json = new Json();
    private static Preferences pref = Gdx.app.getPreferences("MyPrefs");
//    private static FileHandle scoreFileHandle = Gdx.files.internal("bin/Scores.json");
//    private static FileHandle gameDataFileHandle = Gdx.files.internal("bin/GameData.json");

    public static void saveScore(Scores scores){
        if(scores != null)
        {
            pref.putString("score", json.prettyPrint(scores));
            pref.flush();
        }
    }

    public static Scores loadScores(){
        String gdString = pref.getString("score");
        if(gdString == null || gdString.length() <= 0){
            return new Scores();
        }
        return json.fromJson(Scores.class, gdString);
    }
    public static void saveGameData(GameData gameData){
        if(gameData != null)
        {
            pref.putString("gameData", json.prettyPrint(gameData));
            pref.flush();
        }
    }

    public static GameData loadGameData(){
        String gdString = pref.getString("gameData");
        if(gdString == null || gdString.length() <= 0){
            return new GameData();
        }
        System.out.println("gdString=" + gdString);
        return json.fromJson(GameData.class, gdString);
    }

}
