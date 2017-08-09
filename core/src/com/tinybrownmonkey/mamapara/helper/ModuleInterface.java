package com.tinybrownmonkey.mamapara.helper;

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by alaguipo on 26/07/2017.
 */

public class ModuleInterface {
    public void share(){
        System.out.println("ModuleInterface.share()");
    }
    public void share(byte[] file) {
        System.out.println("ModuleInterface.share(" + DatatypeConverter.printHexBinary(file)+ ")");
    }
    public void rate(){
        System.out.println("ModuleInterface.rate()");
    }
    public void sendAnalyticsEvent(String category, String action){
        System.out.println("sendAnalyticsEvent: " + category + "/" + action);
    }
    public void setAnalyticsScreen(String name){
        System.out.println("setAnalyticsScreen: " + name);
    }
    public void showRewardAd(RewardAdResponse adResponse){

    }

    public static class RewardItem{
        String type;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        int amount;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public interface RewardAdResponse{
        public void onRewarded(RewardItem reward) ;

    }
}
