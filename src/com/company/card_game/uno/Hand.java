package com.company.card_game.uno;

import com.company.card_game.actor.Player;
import com.company.card_game.deck.Card;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    public List<Card> cards = new ArrayList<>();
    public Player player;

    public Hand(Player player){
        this.player = player;
    }


    public int getHandSize(){
        return cards.size();
    }

    public void addCardToHand(Card card){
        cards.add(card);
    }
}
