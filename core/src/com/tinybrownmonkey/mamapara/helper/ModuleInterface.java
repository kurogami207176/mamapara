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
}
