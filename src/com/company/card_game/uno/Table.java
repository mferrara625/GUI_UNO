package com.company.card_game.uno;

import com.company.card_game.actor.Player;
import com.company.card_game.deck.Card;
import com.company.card_game.deck.UnoDeck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Table extends JPanel implements ActionListener {
    private static java.util.List<Hand> hands = new ArrayList<>();
    public static java.util.List<Card> discardPile = new ArrayList<>();
    private static UnoDeck deck = new UnoDeck();
    private static int playerCount = 0;
    private int botCount;
    private static boolean isReversed = false;
    private static boolean wasSkipped = false;
    private static boolean isDraw2 = false;
    private static boolean isDraw4 = false;
    private boolean didWin = false;
    private static boolean startGame = false;
    private boolean canEndTurn = false;
    private boolean triedAuto = false;


    int dealCount;
    static int count;
    int cardSpacer2 = 75;
    static boolean wasColorChosen = false, isGameActive = true;
    static int activePlayerIndex = 0;
    static Hand hand;
    static Player winner;
    int x = 50, y = 300;


    public Table() {
        super.setDoubleBuffered(true);
        t = new Timer(7, this);
        t.start();
    }

    public void setup() {

        for (int count = 0; count < playerCount; count++) {
            Player newPlayer = new Player("Player " + (count + 1));
            hands.add(new Hand(newPlayer));
        }
//        if (botCount > 0) {
//            for (int count = 0; count < botCount; count++) {
//                Player newPlayer = new Player("Bot " + (count + 1));
//                hands.add(new Hand(newPlayer));
//            }
//        }
    }

    public void deal() {
        deck.shuffle();
        discardPile.add(deck.deal());
        if (discardPile.get(discardPile.size() - 1).getRank() > 9) {
            deck.cardList.add(discardPile.remove(0));
            deal();
        }
        for (Hand hand : hands) {
            while (hand.getHandSize() < 7) {
                hand.addCardToHand(deck.deal());
            }
        }

    }

    public int calculateScore() {
        int playerScore = 0;
        for (Hand hand : hands) {
            for (Card card : hand.cards) {
                if (card.getRank() < 10) {
                    playerScore += card.getRank();
                } else if (card.getRank() >= 10 && card.getRank() <= 12) {
                    playerScore += 20;
                } else {
                    playerScore += 50;
                }
            }
        }

        return playerScore;
    }

    public int autoPlay(Hand hand) {
        int result = 0;
        for (int i = 0; i < hand.cards.size(); i++) {
            if ((hand.cards.get(i).getRank() == discardPile.get(discardPile.size() - 1).getRank()) || ((hand.cards.get(i).getColor().equals(discardPile.get(discardPile.size() - 1).getColor()))) || hand.cards.get(i).getRank() > 12) {
                result = (i + 1);
                triedAuto = true;
            }
        }
        return result;
    }


    // #########################################################################

    private Timer t;

    public static boolean validateCard(int i) {
        boolean result = false;
        if ((hand.cards.get(i).getRank() == discardPile.get(discardPile.size() - 1).getRank()) || ((hand.cards.get(i).getColor().equals(discardPile.get(discardPile.size() - 1).getColor()))) || hand.cards.get(i).getRank() > 12) {
            if (hand.cards.get(i).getRank() == 10) {
                wasSkipped = true;
            }
            if (hand.cards.get(i).getRank() == 11) {
                if (playerCount > 2)
                    isReversed = !isReversed;
                if (playerCount < 3)
                    wasSkipped = true;
            }
            if (hand.cards.get(i).getRank() == 12) {
                isDraw2 = true;
                count = 0;
            }
            if (hand.cards.get(i).getRank() > 12)
                wasColorChosen = false;
            if (hand.cards.get(i).getRank() == 14)
                isDraw4 = true;

            result = true;
        }
        return result;
    }

    public static void changePlayer() {
        if (isReversed) {
            if (activePlayerIndex > -1) {
                activePlayerIndex--;
                if (wasSkipped) {
                    activePlayerIndex--;
                    if (activePlayerIndex >= -1)
                        wasSkipped = false;
                }
            }
            if (activePlayerIndex < 0) {
                activePlayerIndex = hands.size() - 1;
                if (wasSkipped) {
                    activePlayerIndex = hands.size() - 2;
                    wasSkipped = false;
                }

            }
        } else {
            if (activePlayerIndex < hands.size()) {
                activePlayerIndex++;
                if (wasSkipped) {
                    activePlayerIndex++;
                    if (activePlayerIndex <= hands.size())
                        wasSkipped = false;
                }
            }
            if (activePlayerIndex >= hands.size()) {
                activePlayerIndex = 0;
                if (wasSkipped) {
                    activePlayerIndex = 1;
                    wasSkipped = false;
                }
            }
        }
    }

    public static String displayCardKeys(int i) {
        String output = "";
        switch (i) {
            case 10 -> output = "q";
            case 11 -> output = "w";
            case 12 -> output = "e";
            case 13 -> output = "r";
            case 14 -> output = "t";
            case 15 -> output = "y";
            case 16 -> output = "u";
            case 17 -> output = "i";
            case 18 -> output = "o";
            case 19 -> output = "p";
            case 20 -> output = "d";
            case 21 -> output = "f";
            case 22 -> output = "g";
            case 23 -> output = "h";
            case 24 -> output = "j";
            case 25 -> output = "k";
            case 26 -> output = "l";
            case 27 -> output = "b";
            case 28 -> output = "n";
            case 29 -> output = "m";
            case 30 -> output = ",";
            default -> output = String.valueOf(i);
        }
        return output;
    }

    public static KeyListener listener = new KeyListener() {
        public void keyPressed(KeyEvent event) {
            if (startGame) {
                if (event.getKeyChar() == 's') {
                    changePlayer();
                    event.consume();
                }
                if (event.getKeyChar() == 'a') {
                    if (hand.cards.size() < 30)
                        hand.addCardToHand(deck.deal());
                    else
                        changePlayer();
                    event.consume();
                }
                if (event.getKeyChar() == '1' && hand.cards.size() >= 1) {
                    if (validateCard(0)) {
                        discardPile.add(hand.cards.remove(0));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == '2' && hand.cards.size() >= 2) {
                    if (validateCard(1)) {
                        discardPile.add(hand.cards.remove(1));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == '3' && hand.cards.size() >= 3) {
                    if (validateCard(2)) {
                        discardPile.add(hand.cards.remove(2));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == '4' && hand.cards.size() >= 4) {
                    if (validateCard(3)) {
                        discardPile.add(hand.cards.remove(3));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == '5' && hand.cards.size() >= 5) {
                    if (validateCard(4)) {
                        discardPile.add(hand.cards.remove(4));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == '6' && hand.cards.size() >= 6) {
                    if (validateCard(5)) {
                        discardPile.add(hand.cards.remove(5));
                        changePlayer();
                    }
                }

                if (event.getKeyChar() == '7' && hand.cards.size() >= 7) {
                    if (validateCard(6)) {
                        discardPile.add(hand.cards.remove(6));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == '8' && hand.cards.size() >= 8) {
                    if (validateCard(7)) {
                        discardPile.add(hand.cards.remove(7));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == '9' && hand.cards.size() >= 9) {
                    if (validateCard(8)) {
                        discardPile.add(hand.cards.remove(8));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'q' && hand.cards.size() >= 10) {
                    if (validateCard(9)) {
                        discardPile.add(hand.cards.remove(9));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'w' && hand.cards.size() >= 11) {
                    if (validateCard(10)) {
                        discardPile.add(hand.cards.remove(10));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'e' && hand.cards.size() >= 12) {
                    if (validateCard(11)) {
                        discardPile.add(hand.cards.remove(11));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'r' && hand.cards.size() >= 13) {
                    if (validateCard(12)) {
                        discardPile.add(hand.cards.remove(12));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 't' && hand.cards.size() >= 14) {
                    if (validateCard(13)) {
                        discardPile.add(hand.cards.remove(13));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'y' && hand.cards.size() >= 15) {
                    if (validateCard(14)) {
                        discardPile.add(hand.cards.remove(14));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'u' && hand.cards.size() >= 16) {
                    if (validateCard(15)) {
                        discardPile.add(hand.cards.remove(15));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'i' && hand.cards.size() >= 17) {
                    if (validateCard(16)) {
                        discardPile.add(hand.cards.remove(16));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'o' && hand.cards.size() >= 18) {
                    if (validateCard(17)) {
                        discardPile.add(hand.cards.remove(17));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'p' && hand.cards.size() >= 19) {
                    if (validateCard(18)) {
                        discardPile.add(hand.cards.remove(18));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'd' && hand.cards.size() >= 20) {
                    if (validateCard(19)) {
                        discardPile.add(hand.cards.remove(19));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'f' && hand.cards.size() >= 21) {
                    if (validateCard(20)) {
                        discardPile.add(hand.cards.remove(20));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'g' && hand.cards.size() >= 22) {
                    if (validateCard(21)) {
                        discardPile.add(hand.cards.remove(21));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'h' && hand.cards.size() >= 23) {
                    if (validateCard(22)) {
                        discardPile.add(hand.cards.remove(22));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'j' && hand.cards.size() >= 24) {
                    if (validateCard(23)) {
                        discardPile.add(hand.cards.remove(23));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'k' && hand.cards.size() >= 25) {
                    if (validateCard(24)) {
                        discardPile.add(hand.cards.remove(24));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'l' && hand.cards.size() >= 26) {
                    if (validateCard(25)) {
                        discardPile.add(hand.cards.remove(25));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'b' && hand.cards.size() >= 27) {
                    if (validateCard(26)) {
                        discardPile.add(hand.cards.remove(26));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'n' && hand.cards.size() >= 28) {
                    if (validateCard(27)) {
                        discardPile.add(hand.cards.remove(27));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == 'm' && hand.cards.size() >= 29) {
                    if (validateCard(28)) {
                        discardPile.add(hand.cards.remove(28));
                        changePlayer();
                    }
                }
                if (event.getKeyChar() == ',' && hand.cards.size() >= 30) {
                    if (validateCard(29)) {
                        discardPile.add(hand.cards.remove(29));
                        changePlayer();
                    }
                }
                if (discardPile.get(discardPile.size() - 1).getRank() > 12 && !wasColorChosen) {
                    if (event.getKeyChar() == 'z') {
                        discardPile.get(discardPile.size() - 1).color = Color.red;
                        wasColorChosen = true;
                    }
                    if (event.getKeyChar() == 'x') {
                        discardPile.get(discardPile.size() - 1).color = Color.yellow;
                        wasColorChosen = true;
                    }
                    if (event.getKeyChar() == 'c') {
                        discardPile.get(discardPile.size() - 1).color = Color.blue;
                        wasColorChosen = true;
                    }
                    if (event.getKeyChar() == 'v') {
                        discardPile.get(discardPile.size() - 1).color = Color.green;
                        wasColorChosen = true;
                    }
                }
            }
            if (!startGame) {
                if (event.getKeyChar() == '1') {
                    playerCount = 1;
                    startGame = true;
                }
                if (event.getKeyChar() == '2') {
                    playerCount = 2;
                    startGame = true;
                }
                if (event.getKeyChar() == '3') {
                    playerCount = 3;
                    startGame = true;
                }
                if (event.getKeyChar() == '4') {
                    playerCount = 4;
                    startGame = true;
                }
                if (event.getKeyChar() == '5') {
                    playerCount = 5;
                    startGame = true;
                }
                if (event.getKeyChar() == '6') {
                    playerCount = 6;
                    startGame = true;
                }

            }

        }

        public void keyReleased(KeyEvent event) {
            if (event.getKeyChar() == KeyEvent.VK_M) {
                event.consume();
            }
            if (event.getKeyChar() == KeyEvent.VK_N) {

                event.consume();
            }
            if (event.getKeyChar() == KeyEvent.VK_H) {
                event.consume();
            }
        }

        public void keyTyped(KeyEvent event) {
        }

    };

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (isGameActive) {
            if (!startGame) {
                g2d.drawString("HOW MANY PLAYERS? (1 - 6)", 300, 400);
            } else {
                hand = hands.get(activePlayerIndex);
                g2d.drawString("CONTROLS:", 10, 20);
                g2d.drawString("A : Draw Card", 10, 35);
                g2d.drawString("S : End Turn", 10, 50);

                if (discardPile.get(discardPile.size() - 1).getRank() > 12 && !wasColorChosen) {
                    g2d.drawString("SELECT A COLOR", 500, 100);
                    g2d.setColor(Color.red);
                    g2d.fillRect(500, 150, 25, 25);
                    g2d.setColor(Color.black);
                    g2d.drawString("Z", 510, 185);
                    g2d.setColor(Color.yellow);
                    g2d.fillRect(550, 150, 25, 25);
                    g2d.setColor(Color.black);
                    g2d.drawString("X", 560, 185);
                    g2d.setColor(Color.blue);
                    g2d.fillRect(600, 150, 25, 25);
                    g2d.setColor(Color.black);
                    g2d.drawString("C", 610, 185);
                    g2d.setColor(Color.green);
                    g2d.fillRect(650, 150, 25, 25);
                    g2d.setColor(Color.black);
                    g2d.drawString("V", 660, 185);
                }


                if (dealCount > 0) {
                    g2d.setColor(discardPile.get(discardPile.size() - 1).getColor());
                    g2d.fillRect(300, 150, 50, 25);
                    g2d.fillRect(300, 150 + 75, 50, 25);
                    g2d.setColor(Color.black);
                    g2d.drawRect(300, 150, 50, 100);
                    g2d.drawString(discardPile.get(discardPile.size() - 1).display(), 315, 150 + 50);
                }

                g2d.fillRect(300 + cardSpacer2, 150, 50, 25);
                g2d.fillRect(300 + cardSpacer2, 150 + 75, 50, 25);
                g2d.drawRect(300 + cardSpacer2, 150, 50, 100);
                g2d.drawString("    UNO", 300 + cardSpacer2, 150 + 50);

                for (int i = 0; i < hand.cards.size(); i++) {
                    if (i < 15) {
                        g2d.drawString(hand.player.name + "'s Turn", x, y - 150);
                        g2d.drawString("Score: " + hand.player.score, x, y - 135);
                        g2d.setColor(hand.cards.get(i).getColor());
                        g2d.fillRect(x + (cardSpacer2 * i), y, 50, 25);
                        g2d.fillRect(x + (cardSpacer2 * i), y + 75, 50, 25);
                        g2d.setColor(Color.black);
                        g2d.drawRect(x + (cardSpacer2 * i), y, 50, 100);
                        g2d.drawString(hand.cards.get(i).display(), x + (cardSpacer2 * i) + 15, y + 50);
                        g2d.drawString("(" + displayCardKeys(i + 1) + ")", x + (cardSpacer2 * i) + 15, y + 110);
                    } else {
                        g2d.setColor(hand.cards.get(i).getColor());
                        g2d.fillRect(x + (cardSpacer2 * (i - 15)), y + 115, 50, 25);
                        g2d.fillRect(x + (cardSpacer2 * (i - 15)), y + 115 + 75, 50, 25);
                        g2d.setColor(Color.black);
                        g2d.drawRect(x + (cardSpacer2 * (i - 15)), y + 115, 50, 100);
                        g2d.drawString(hand.cards.get(i).display(), x + (cardSpacer2 * (i - 15)) + 15, y + 115 + 50);
                        g2d.drawString("(" + displayCardKeys(i + 1) + ")", x + (cardSpacer2 * (i - 15)) + 15, y + 115 + 110);
                    }

                }

            }

        } else {
            g2d.drawString(winner.name + " WINS!!!", winX, winY);
//            g2d.setColor(Color.getHSBColor((float)(Math.random() * 4),(float)(Math.random() * 4),(float)(Math.random() * 4)));
//            g2d.drawString(winner.name + " WINS!!!", winX, winY + 15);
//            g2d.setColor(Color.black);

        }
    }


    public static int xV = 2;
    public static int yV = 2;
    public static int winX = 524;
    public static int winY = 264;

    public void move() {
        winX = winX + xV;
        winY = winY + yV;
    }

    public void actionPerformed(ActionEvent e) {
        if(!isGameActive)
        move();

        if(winX >= 1100){
            xV = -2;
        }
        if(winX <= 0){
            xV = 2;
        }

        if(winY >= 560){
            yV = -2;
        }
        if(winY <= 10){
            yV = 2;
        }

        if (startGame) {

            if (dealCount == 0) {
                setup();
                deal();
                dealCount++;
            }
            for (Hand hand : hands) {
                if (hand.cards.size() == 0) {
                    hand.player.score += calculateScore();
                    didWin = true;
                    if (hand.player.score >= 500) {
                        isGameActive = false;
                        winner = hand.player;
                    }
                }
            }
            if (didWin) {
                for (Hand hand : hands) {
                    hand.cards.clear();
                }
                if (isGameActive)
                    deal();
                didWin = false;
            }


            if (wasSkipped) {
                changePlayer();
            }

            if (isDraw2 && count > 15) {
                hand.addCardToHand(deck.deal());
                hand.addCardToHand(deck.deal());
                isDraw2 = false;
                changePlayer();
            }

            count++;

            if (wasColorChosen && isDraw4) {
                for (int i = 0; i < 4; i++)
                    hand.addCardToHand(deck.deal());
                isDraw4 = false;
                changePlayer();
            }

        }

        repaint();
    }
}