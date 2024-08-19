package com.game.object;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.game.gfx.SoundManager;
import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.Handler;
import com.game.object.util.ObjectId;

public class LifeMushroom extends GameObject{
	private Texture tex = Game.getTexture();
	private int index;
	private int speed = 2; // 버섯의 이동 속도
	private boolean movingRight = true; // 버섯의 현재 이동 방향
	private BufferedImage[] sprite;
	private SoundManager soundManager;
	private Handler handler;
	private boolean soundCheck;
	
	public LifeMushroom(float x, float y, int width, int height, int index, int scale, int layer, SoundManager soundManager, Handler handler) {
		super(x, y, ObjectId.LifeMushroom, width, height, scale, layer);
		this.index = index;
        sprite = tex.getLifeMushroom();
        
        this.soundManager = soundManager;
        this.handler = handler;
        this.soundCheck = false;
	}

	@Override
	public void tick() {
		applyGravity(0.5f);
        collision();
        moveLifeMushroom();
	}

	@Override
	public void render(Graphics g) {
		// 여기에 배경을 다시 그리는 부분을 추가할 수 있습니다.
		// 예: g.clearRect(0, 0, Game.WIDTH, Game.HEIGHT);

		// 버섯 이미지를 현재 위치에 그립니다.
		g.drawImage(sprite[0], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight(), null);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    }
    
    public Rectangle getBoundsRight(){
        return new Rectangle((int)(getX() + getWidth() + 1), 
        		(int)(getY() + 5), 
        		1, 
        		(int)(getHeight() - 10));
    }

    public Rectangle getBoundsLeft(){
        return new Rectangle((int) getX()-1,
        		(int)(getY() + 5),
        		1,
        		(int)(getHeight() - 10));
    }

    private void moveLifeMushroom() {
    	setY(getY() + getVelY());
    	
    	// 버섯의 이동 로직
        if (movingRight) {
            setX(getX() + speed);
        } else {
            setX(getX() - speed);
        }
    }
    
    private void collision() {
   	 for (int i = 0; i < handler.getGameObjs().size(); i++) {
            GameObject temp = handler.getGameObjs().get(i);
            if (temp.getId() == ObjectId.Block
            		|| temp.getId() == ObjectId.Pipe
            		|| temp.getId() == ObjectId.TransparentBlock
            		|| temp.getId() == ObjectId.ItemBlocks
            		|| temp.getId() == ObjectId.CoinBlocks){
           	 if (getBounds().intersects(temp.getBounds())) {
                    setY(temp.getY() - getHeight());
                    setVelY(0);
                }

                if (getBoundsRight().intersects(temp.getBounds())) {
                    setX(temp.getX() - getWidth() - 1);
                    movingRight = false; // 방향을 바꿉니다.
                }

                if (getBoundsLeft().intersects(temp.getBounds())) {
                    setX(temp.getX() + temp.getWidth() + 1);
                    movingRight = true; // 방향을 바꿉니다.
                }
            }
        }
   }
    
    public boolean getSoundCheck() {
    	return soundCheck;
    }
    
    public void setSoundCheck(boolean soundCheck) {
    	this.soundCheck = soundCheck;
    }
}