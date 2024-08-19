package com.game.object;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.game.gfx.SoundManager;
import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.Handler;
import com.game.object.util.ObjectId;

public class Flower extends GameObject{

	private int animationCounter = 0;
    private int animationSpeed = 10; // 애니메이션 속도 조절    
	private Texture tex = Game.getTexture();
	private int index;
	private int speed = 2; // 버섯의 이동 속도
	private boolean movingRight = true; // 버섯의 현재 이동 방향
	private BufferedImage[] sprite;
	private SoundManager soundManager;
	private Handler handler;
	private boolean soundCheck;
	
	public Flower(float x, float y, int width, int height, int index, int scale, int layer, SoundManager soundManager, Handler handler) {
		super(x, y, ObjectId.Flower, width, height, scale, layer);
		this.index = index;
        sprite = tex.getFlower();
        
        this.soundManager = soundManager;
        this.handler = handler;
        this.soundCheck = soundCheck;
	}

	@Override
	public void tick() {
		coinAnimation();
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(sprite[index], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight(), null);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
	}

	public void coinAnimation() {
        animationCounter++;
        if (animationCounter >= animationSpeed) {
            animationCounter = 0;
            index = (index + 1) % sprite.length; // 스프라이트 배열 길이에 따라 인덱스 반복
        }
    }

    public boolean getSoundCheck() {
    	return soundCheck;
    }
    
    public void setSoundCheck(boolean soundCheck) {
    	this.soundCheck = soundCheck;
    }
}
