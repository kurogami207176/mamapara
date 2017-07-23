package com.tinybrownmonkey.mamapara.constants;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by AlainAnne on 19-Jun-17.
 */

public enum PowerUps {
    EXPLODE ("Tabbi", "Cars avoid you!", Color.FIREBRICK, 100, 300, 900),
    RANGE ("Barker", "Range increase.", Color.GOLD, 150, 450, 1050),
    SHADOW ("iLag", "Immunity from bumps", Color.NAVY, 200, 800, 2500),
    SHAMAN ("Shaman", "More passengers", Color.FOREST, 180, 600, 1600),
    SLOT ("SLOT", "Increase slots!", Color.WHITE, 1000, 3000, 10000, 25000, 100000, 500000);
    private String title;
    private String desc;
    private int[] price;
    private Color color;

    PowerUps(String title, String desc, Color color, int... price){
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.color = new Color(color.r, color.g, color.b, 0.5f);
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public int getPrice(int level) {
        return price[level - 1];
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
