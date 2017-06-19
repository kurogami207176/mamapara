package com.tinybrownmonkey.mamapara.constants;

/**
 * Created by AlainAnne on 19-Jun-17.
 */

public enum PowerUps {
    EXPLODE ("Tabby", "Cars avoid you!", 100),
    RANGE ("Barker", "Range increase.", 150),
    SHADOW ("Side swipe", "Immunity from bumps", 200),
    SHAMAN ("Shaman", "More passengers", 150),
    SLOT ("SLOT", "Increase slots!", 1000);
    private String title;
    private String desc;
    private int price;

    PowerUps(String title, String desc, int price){
        this.title = title;
        this.desc = desc;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public int getPrice() {
        return price;
    }

    public static int getIndex(PowerUps pu){
        for(int i = 0; i < PowerUps.values().length; i++)
        {
            if(PowerUps.values()[i]== pu)
            {
                return i;
            }
        }
        return -1;
    }
}
