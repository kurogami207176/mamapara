package com.tinybrownmonkey.mamapara.constants;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by AlainAnne on 19-Jun-17.
 */

public enum PowerUps {
    EXPLODE ("Tabi", "Cars avoid you!", 100, Color.FIREBRICK),
    RANGE ("Barker", "Range increase.", 150, Color.GOLD),
    SHADOW ("Side swipe", "Immunity from bumps", 200, Color.NAVY),
    SHAMAN ("Shaman", "More passengers", 150, Color.FOREST),
    SLOT ("SLOT", "Increase slots!", 1000, Color.WHITE);
    private String title;
    private String desc;
    private int price;
    private Color color;

    PowerUps(String title, String desc, int price, Color color){
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.color = new Color(color.r, color.g, color.b, 0.2f);
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

    public Color getColor() {
        return color;
    }
}
