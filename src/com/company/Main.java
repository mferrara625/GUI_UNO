package com.company;

import com.company.card_game.uno.Table;

import javax.swing.*;

public class Main {


    public static void main(String[] args) {
        new Main();
    }

    public Main(){

        JFrame frame = new JFrame();
        frame.setTitle("UNO");
        frame.setSize(1200, 600);
        frame.setLocation(50, 50);
        frame.addKeyListener(Table.listener);
        frame.add(new Table());
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
