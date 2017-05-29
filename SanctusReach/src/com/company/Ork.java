package com.company;

import javax.swing.*;
import javax.swing.ImageIcon;

/**
 * Created by Family on 5/26/2017.
 */
public class Ork extends Sprite {

    private Bomb bomb;
    private final String orkImg = "C:\\Users\\Family\\IdeaProjects\\SanctusReach\\src\\resources\\Images\\ork.PNG";

    public Ork(int x, int y){
        initOrk(x, y);
    }

    private void initOrk(int x, int y){
        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);
        ImageIcon ii = new ImageIcon(orkImg);
        setImage(ii.getImage());
    }

    public void act(int direction){ //the method act() causes the Ork to move left and right
        this.x += direction;
    } //used to position the ork in horizontal direction

    public Bomb getBomb(){ //this is called when the ork is about to fire a shot
        return bomb;
    }

    public class Bomb extends Sprite{ //this is the inner class Bomb ... this is the shot that the ork fires

        private final String bombImg = "C:\\Users\\Family\\IdeaProjects\\SanctusReach\\src\\resources\\Images\\orkBlast (2).png";
        private boolean destroyed;

        public Bomb(int x, int y){
            initBomb(x, y);
        }

        private void initBomb(int x, int y){
            setDestroyed(true);
            this.x = x;
            this.y = y;
            ImageIcon ii = new ImageIcon(bombImg);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed){
            this.destroyed = destroyed;
        }

        public boolean isDestroyed(){
            return destroyed;
        }
    }
}
