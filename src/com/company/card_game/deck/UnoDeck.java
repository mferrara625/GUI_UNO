package com.company.card_game.deck;

import com.company.card_game.uno.Table;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnoDeck implements Deck {
    private static final Color RED = Color.red;
    private static final Color GREEN = Color.green;
    private static final Color YELLOW = Color.yellow;
    private static final Color BLUE = Color.blue;
    public List<Card> cardList = new ArrayList<>();
    final private int[] values = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
    final private Color[] colors = {RED, GREEN, YELLOW, BLUE};

    public UnoDeck(){
        for(Color color : colors){
            for(int val : values){
                if(val == 0)
                    cardList.add(new Card(val, color));
                if(val > 0 && val < 13){
                    cardList.add(new Card(val, color));
                    cardList.add(new Card(val, color));
                }
                if(val > 12){
                    cardList.add(new Card(val, Color.black ));
                }
            }
        }
    }

    public Color[] getColors(){return colors;}

    @Override
    public void shuffle() {
        Collections.shuffle(cardList);
    }

    @Override
    public Card deal() {
        if(cardList.size() <= 1){
            reshuffle();
        }
        return (cardList.remove((cardList.size() - 1)));
    }

    public void reshuffle(){
        for(int i = 0; i < (Table.discardPile.size() - 1); i++){
            if(Table.discardPile.get(i).getRank() > 12)
                Table.discardPile.get(i).color = Color.black;
            cardList.add(Table.discardPile.remove(i));
            Collections.shuffle(cardList);
        }
    }
}
