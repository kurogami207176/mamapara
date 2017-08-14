package com.tinybrownmonkey.mamapara.helper;

/**
 * Created by alaguipo on 7/08/2017.
 */

public interface PlayServices
{
    enum Achievements{
        FIRST_DRIVE("CgkIq-Cdg6IBEAIQBg"), // first drive
        HALF_KILO("CgkIq-Cdg6IBEAIQBw"), // 500m
        MILLENNIAL("CgkIq-Cdg6IBEAIQCA"), // 1k
        Y2k("CgkIq-Cdg6IBEAIQCQ"), // 2k
        ROAD_RUNNER("CgkIq-Cdg6IBEAIQCg"), //50k

        POWER("CgkIq-Cdg6IBEAIQCw"), // get powerup
        POWER_OF_TWO("CgkIq-Cdg6IBEAIQDA"), // buy two power up slots
        FULL_POWER("CgkIq-Cdg6IBEAIQDQ"); // unlock 6 power up slots

        private String id;
        private boolean done;

        Achievements(String id){
            this.id = id;
            this.done = false;
        }
        public String getId(){
            return id;
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }
    }

    void signIn();
    void signOut();
    void rateGame();
    void unlockAchievement(Achievements achievements);
    void submitDistanceScore(int highScore);
    void submitMoneyScore(int highScore);
    void submitMoneyTripScore(int highScore);
    void showAchievement();
    void showScore();
    void showAllScore();
    boolean isSignedIn();
}
