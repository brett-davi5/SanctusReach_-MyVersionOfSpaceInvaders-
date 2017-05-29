package com.company;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Created by Family on 5/26/2017.
 */
public class Board extends JPanel implements Runnable, Commons{

    private Dimension d;
    private ArrayList<Ork> orks;
    private Player player;
    private Shot shot;

    private final int ORK_INIT_Y = 5;
    private final int ORK_INIT_X = 150;
    private int direction = -1;
    private int deaths = 0;

    private boolean ingame = true;
    private final String explImg = "C:\\Users\\Family\\IdeaProjects\\SanctusReach\\src\\resources\\Images\\explosion.PNG";
    private String message = "Game Over";

    private Thread animator;

    public Board(){
        initBoard();
    }

    private void initBoard(){
        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.black);

        gameInit();
        setDoubleBuffered(true);

    }

    public void addNotify(){
        super.addNotify();
        gameInit();
    }

    public void gameInit(){
        orks = new ArrayList();       //here I created a unit of 24 orks in a 4x6 matrix
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 6; j++){
                Ork ork = new Ork(ORK_INIT_X + 18 * j, ORK_INIT_Y + 18 * i);
                orks.add(ork);
            }
        }

        player = new Player();   //the player object
        shot = new Shot();       //the shot object

        if(animator == null || !ingame){
            animator = new Thread(this);
            animator.start();
        }
    }

    public void drawOrks(Graphics g){
        Iterator it = orks.iterator();

        for(Ork ork: orks){
            if(ork.isVisible()){
                g.drawImage(ork.getImage(), ork.getX(), ork.getY(), this);
            }
            if(ork.isDying()){
                ork.die();
            }
        }
    }

    public void drawPlayer(Graphics g){
        if(player.isVisible()){
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }
        if(player.isDying()){
            player.die();
            ingame = false;
        }
    }

    public void drawShot(Graphics g){
        if(shot.isVisible()){
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    public void drawBombing(Graphics g){  //draws the bombs blasted by the orks
        for(Ork o: orks){
            Ork.Bomb b = o.getBomb();

            if(!b.isDestroyed()){
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }

        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(Color.black);
        g.fillRect(0,0,d.width, d.height);
        g.setColor(Color.green);

        if(ingame){  //draw the ground, the orks, the player, the shots, and the bombs
            g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
            drawOrks(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void gameOver(){
        Graphics g = this.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0,0,BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(new Color(0,32,48));
        g.fillRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) /2, BOARD_WIDTH / 2);
    }

    public void animationCycle(){

        if(deaths == NUMBER_OF_ALIENS_TO_DESTROY){   //if we destroy all the aliens, we win!
            ingame = false;
            message = "Game Won!";
        }

        //player----------------------------------------

        player.act();

        //shot-------------------------------------------

        if(shot.isVisible()){

            int shotX = shot.getX();
            int shotY = shot.getY();

            for(Ork ork: orks){       //if the shot triggered by the player collides with the ork, the ork is destroyed
                int orkX = ork.getX();
                int orkY = ork.getY();
                if(ork.isVisible() && shot.isVisible()){
                    if(shotX >= (orkX) && shotX <= (orkX + ORK_WIDTH) && shotY >= (orkY) && shotY <= (orkY + ORK_HEIGHT)){
                        ImageIcon ii = new ImageIcon (explImg);
                        ork.setImage(ii.getImage());
                        ork.setDying(true);   //the dying flag is set! we make it set equal to displaying the explosion
                        deaths++;  //increment the number of deaths
                        shot.die(); //the shot sprite is removed
                    }
                }
            }

            int y = shot.getY();
            y -= 4;

            if(y < 0){
                shot.die();
            }else{
                shot.setY(y);
            }
        }


        //orks--------------------------------------------

        for(Ork ork : orks){

            int x = ork.getX();
            if(x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1){   //if the orks reach the far right end of the board, they step "down" a step and go left
                direction = -1;
                Iterator i1 = orks.iterator();

                while(i1.hasNext()){
                    Ork o2 = (Ork) i1.next();
                    o2.setY(o2.getY() + GO_DOWN);
                }
            }

            if(x <= BORDER_LEFT && direction != 1){
                direction = 1;
                Iterator i2 = orks.iterator();

                while(i2.hasNext()){
                    Ork o = (Ork) i2.next();
                    o.setY(o.getY() + GO_DOWN);
                }
            }
        }

        Iterator it = orks.iterator();

        while(it.hasNext()){
            Ork ork = (Ork) it.next();
            if(ork.isVisible()){
                int y = ork.getY();
                if(y > GROUND - ORK_HEIGHT){  //if the orks reach the bottom of the board, the WAAGH is underway and we lose!
                    ingame = false;
                    message = "Here comes the WAAAGH!";
                }

                ork.act(direction);
            }
        }

        //bombs -------------------------------------------------

        Random generator  = new Random();

        for(Ork ork: orks){
            int shot = generator.nextInt(15);
            Ork.Bomb b = ork.getBomb();

            if(shot == CHANCE && ork.isVisible() && b.isDestroyed()){
                b.setDestroyed(false);
                b.setX(ork.getX());
                b.setY(ork.getY());
            }

            int bombX = b.getX();
            int bombY = b.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            if(player.isVisible() && !b.isDestroyed()){
                if(bombX >= (playerX) && bombX <= (playerX + PLAYER_WIDTH) &&
                        bombY >= (playerY) &&
                        bombY <= (playerY + PLAYER_HEIGHT));{
                            ImageIcon ii = new ImageIcon(explImg);
                            player.setImage(ii.getImage());
                            player.setDying(true);
                            b.setDestroyed(true);
                }
            }

            if(!b.isDestroyed()){
                b.setY(b.getY() + 1);

                if(b.getY() >= GROUND - BOMB_HEIGHT){
                    b.setDestroyed(true);
                }
            }

        }
    }

    @Override
    public void run(){
        long beforeTime;
        long timeDiff;
        long sleep;

        beforeTime = System.currentTimeMillis();

        while(ingame){

            repaint();
            animationCycle();
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if(sleep < 0){
                sleep = 2;
            }

            try{
                Thread.sleep(sleep);
            }catch (InterruptedException e){
                System.out.println("interrupted");
            }

            beforeTime = System.currentTimeMillis();
        }

        gameOver();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e){
            player.keyReleased(e);
        }

        public void keyPressed(KeyEvent e){
            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if(key == KeyEvent.VK_SPACE){
                if(ingame){
                    if(!shot.isVisible()){
                        shot = new Shot(x, y);
                    }
                }
            }
        }

    }




}
