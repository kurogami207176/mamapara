package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class MusicManager {
    private Audio audio;
    private static Music currentMusic;
    public MusicManager(){

    }
    private static String[] tracks = new String[]{
            "POL-secret-alchemy-short.wav"
    };

    public static void play(int trackNo, boolean isLooping){
        if(currentMusic != null)
        {
            if(currentMusic.isPlaying()){
                currentMusic.stop();
            }
            currentMusic.dispose();
        }
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(tracks[trackNo]));
        currentMusic.setLooping(isLooping);
        currentMusic.play();
    }

    public static void stop(){
        if(currentMusic != null)
        {
            currentMusic.stop();
        }
    }

    public static void pause(){
        if(currentMusic != null)
        {
            currentMusic.pause();
        }
    }
    public static void isPlaying(){
        if(currentMusic != null)
        {
            currentMusic.isPlaying();
        }
    }

    public static void dispose(){
        if(currentMusic != null)
        {
            if(currentMusic.isPlaying()){
                currentMusic.stop();
            }
            currentMusic.dispose();
            currentMusic = null;
        }
    }
}
