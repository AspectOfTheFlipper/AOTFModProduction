package dev.icheppy.aotf.dataclasses;

import java.util.Comparator;

public class Flip implements Comparable<Flip>{
    private int buy_price;
    private String item_name;
    private int sell_price;
    private String tier;
    private String uuid;

    public int getBuyPrice() {
        return buy_price;
    }

    public String getItemName() {
        return item_name;
    }

    public int getSellPrice() {
        return sell_price;
    }

    public String getTier() {
        return tier;
    }

    public String getUuid() {
        return uuid;
    }

    // Return the Return On Investment in %
    public double getRoi(){
        return 100 * ((this.getSellPrice() - this.getBuyPrice()) / (double) this.getBuyPrice());
    }

    @Override
    public int compareTo(Flip o) {
        return (this.getSellPrice() - this.getBuyPrice()) - (o.getSellPrice() - o.getBuyPrice());
    }
}
