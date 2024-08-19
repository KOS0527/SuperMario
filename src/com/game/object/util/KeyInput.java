package com.game.object.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.game.gfx.SoundManager;
import com.game.main.Game;

// A KeyEvent Listener takes action when certain events take place (keyPress, keyRelease)
public class KeyInput extends KeyAdapter {

	private boolean moveLeft;
	private boolean moveRight;
    private boolean[] keyDown = new boolean[6];
    private Handler handler;
    private SoundManager sound;
    private Game game;
    
    private boolean isShooting;
    private boolean canFire = true; // 발사 가능 여부
    private long lastFireTime; // 마지막 발사 시간
    
    public KeyInput(Handler handler){
        this.handler = handler;
        this.sound = SoundManager.getInstance();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        
    	if(key == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
        	
    	if(!handler.getPlayer().getCehckMove()) {
            // jump
            if(key == KeyEvent.VK_UP){
                if(!handler.getPlayer().hasJumped()){
                    handler.getPlayer().setVelY(-15);
                    keyDown[0] = true;
                    handler.getPlayer().setJumped(true);
                }
            }

            // move left
            if(key == KeyEvent.VK_LEFT){
            	keyDown[1] = true;
            	handler.getPlayer().startAcceleratingLeft();
            }

            // move right
            if(key == KeyEvent.VK_RIGHT){
                keyDown[2] = true;
                handler.getPlayer().startAcceleratingRight();
            }
            
            // use pipe
            if(key == KeyEvent.VK_DOWN) {
            	keyDown[3] = true;
            	handler.getPlayer().setCrouching(true);
            }
            
            if(key == KeyEvent.VK_P) {
            	handler.getGame().pauseGame();
            	sound.gamePause();
            	keyDown[4] = true;
            	System.out.println("paused");
            }
            
            if(handler.getPlayer().getMarioState() == UnitState.Mario_Flower 
            		&& !handler.getPlayer().getCrouching()) {
            	if (key == KeyEvent.VK_E && !isShooting) { // isShooting이 false일 때만 발사
            	    isShooting = true;
            	    handler.getPlayer().setShooting(true);
            	    handler.getPlayer().shootFireBall();
            	    sound.isFireBall();
            	  }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    	int key = e.getKeyCode();
    	
    	if(!handler.getPlayer().getCehckMove()) {
            if(key == KeyEvent.VK_UP){
                keyDown[0] = false;
            }

            if(key == KeyEvent.VK_LEFT){
            	keyDown[1] = false;
            	handler.getPlayer().stopAccelerating();
            	handler.getPlayer().setStopMotion(false);
            }

            if(key == KeyEvent.VK_RIGHT){
            	keyDown[2] = false;
            	handler.getPlayer().stopAccelerating();
            	handler.getPlayer().setStopMotion(false);
            }
            
            if(key == KeyEvent.VK_DOWN) {
            	keyDown[3] = false;
            	handler.getPlayer().setCrouching(false);
            }

            if(!keyDown[1] && !keyDown[2]){
                handler.getPlayer().setVelX(0);
            }
            
            if (key == KeyEvent.VK_P) {
            	keyDown[4] = false;
            }
            
            if(handler.getPlayer().getMarioState() == UnitState.Mario_Flower 
            		&& !handler.getPlayer().getCrouching()) {
            	if (key == KeyEvent.VK_E) {
            	    isShooting = false;
            	    handler.getPlayer().setShooting(false);
            	  }
            }
    	}
    }
    
    public boolean getKeyDown(int index) {
        return keyDown[index];
    }

    public void setKeyDown(int index, boolean value) {
        keyDown[index] = value;
    }
}