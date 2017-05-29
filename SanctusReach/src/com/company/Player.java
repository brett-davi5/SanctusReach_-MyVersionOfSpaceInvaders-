package com.company;

import javax.swing.*;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;


/**
 * Created by Family on 5/26/2017.
 */
public class Player extends Sprite implements Commons{

    private final int START_Y = 280;  //starting positions of the player
    private final int START_X = 270;

    private final String playerImg = "C:\\Users\\Family\\IdeaProjects\\SanctusReach\\src\\resources\\Images\\spaceWolf.PNG";
    private int width;

    public Player(){
        initPlayer();
    }

    private void initPlayer(){
        ImageIcon ii = new ImageIcon(playerImg);
        width = ii.getImage().getWidth(null);
        setImage(ii.getImage());
        setX(START_X);
        setY(START_Y);
    }


    public void act(){

        x += dx;

        if(x <= 2){
            x = 2;
        }

        if(x >= BOARD_WIDTH - 2 * width){
            x = BOARD_WIDTH - 2 * width;
        }
    }

    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_A){
            dx = -2; //moves left
        }

        if(key == KeyEvent.VK_D){
            dx = 2; //moves right
        }
    }

    public void keyReleased(KeyEvent e){

        int key = e.getKeyCode(); //both of these reset the key codes to 0 so we don't go too far too fast

        if(key == KeyEvent.VK_A){
            dx = 0;
        }

        if(key == KeyEvent.VK_D){
            dx = 0;
        }




    }




}
