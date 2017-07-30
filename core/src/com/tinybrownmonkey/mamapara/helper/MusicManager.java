package com.tinybrownmonkey.mamapara.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alaguipo on 17/06/2017.
 */

public class MusicManager {
//    private static MusicManager instance = new MusicManager();
//    public static MusicManager getInstance(){
//        return instance;
//    }

    private MusicState currState;
    private MusicState prevState;

    private Map<MusicState, Music> musicMap;

    private Map<SoundState, Sound> soundMap;

    public MusicManager(){
        init();
    }


    public void playSound(SoundState soundState)
    {
        if(!muted && soundMap.get(soundState) != null) {
            soundMap.get(soundState).play();
        }
    }
    public void setMusic(MusicState musicState)
    {
        if(musicState != currState){
            prevState = currState;
            currState = musicState;
            Music music = musicMap.get(currState);
            if(music != null) {
                if (muted) {
                    music.setVolume(0);
                } else {
                    music.setVolume(1f);
                }
                music.play();
            }
            if(prevState != null) {
                if(musicMap.get(prevState) != null){
                    musicMap.get(prevState).stop();
                }
            }
        }
    }

    public void init(){
        musicMap = new HashMap<MusicState, Music>();
        musicMap.put(MusicState.TITLE, Gdx.audio.newMusic(Gdx.files.internal("music/TitleScreen.wav")));
        musicMap.put(MusicState.L1, Gdx.audio.newMusic(Gdx.files.internal("music/Level01.wav")));
        musicMap.put(MusicState.L2, Gdx.audio.newMusic(Gdx.files.internal("music/Level02.wav")));
        musicMap.put(MusicState.L3, Gdx.audio.newMusic(Gdx.files.internal("music/Level03.wav")));
        musicMap.put(MusicState.END, Gdx.audio.newMusic(Gdx.files.internal("music/Ending.wav")));
        for(Music music : musicMap.values()){
            music.setVolume(1f);
            music.setLooping(true);
        }
        soundMap = new HashMap<SoundState, Sound>();
        soundMap.put(SoundState.COIN, Gdx.audio.newSound(Gdx.files.internal("sound/money.wav")));
        soundMap.put(SoundState.HIT_PERSON, Gdx.audio.newSound(Gdx.files.internal("sound/hit_human.wav")));
        soundMap.put(SoundState.HIT_CAR, Gdx.audio.newSound(Gdx.files.internal("sound/hit_car.wav")));
        soundMap.put(SoundState.BUTTON, Gdx.audio.newSound(Gdx.files.internal("sound/button.wav")));
    }

    boolean muted = false;
    public void mute(){
        if(!muted && currState != null) {
            Music music = musicMap.get(currState);
            if (music != null) {
                muted = true;
                music.setVolume(0);
            }
        }
    }

    public void unmute(){
        if(muted && currState != null) {
            Music music = musicMap.get(currState);
            if (music != null) {
                muted = false;
                music.setVolume(1f);
            }
        }
    }

    public void dispose(){
        for(Music music : musicMap.values()){
            music.dispose();
        }
        musicMap.clear();
        for(Sound sound : soundMap.values()){
            sound.dispose();
        }
        soundMap.clear();
    }

    public enum MusicState{
        TITLE, L1, L2, L3, END;
    }

    public enum SoundState{
        COIN, HIT_PERSON, HIT_CAR, BUTTON;
    }
}
