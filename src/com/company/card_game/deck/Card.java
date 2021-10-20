package com.company.card_game.deck;

import java.awt.*;

public class Card {
    private int rank;
    public Color color;

    public Card(int rank, Color color){
        this.rank = rank;
        this.color = color;
    }

    public int getRank(){return rank;}

    public Color getColor(){return color;}


    public String display(){
        String output = "";
        switch(rank){
            case 10 -> output = " Skp ";
            case 11 -> output = " Rev ";
            case 12 -> output = " +2  ";
            case 13 -> output = " Wld ";
            case 14 -> output = " W+4 ";
            default -> output = "  " + rank + "  ";
        }
        return output;
    }
}
