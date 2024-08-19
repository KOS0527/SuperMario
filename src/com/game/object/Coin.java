package com.game.object;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.game.gfx.SoundManager;
import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.Handler;
import com.game.object.util.ObjectId;

public class Coin extends GameObject{
	private Texture tex = Game.getTexture();
    private int index;
    private BufferedImage[] sprite;
    private SoundManager soundManager;
    private Handler handler;

    private int animationCounter = 0;
    private int animationSpeed = 3; // 애니메이션 속도 조절    
    private float jumpHeight = 50; // 코인이 올라가는 높이
    private float jumpSpeed = 8; // 코인이 올라가는 속도
    private float disappearHeight = 30; // 사라지는 높이

    private float initialX; // 초기 X 좌표
    private float initialY; // 초기 Y 좌표

    private boolean jumping = true;
    private boolean disappearing = false; // 사라지는 상태

    public Coin(float x, float y, int width, int height, int index, int scale, int layer, SoundManager soundManager, Handler handler) {
        super(x, y, ObjectId.Coin, width, height, scale, layer);
        this.index = index;
        this.sprite = tex.getCoinAnimation();
        this.soundManager = soundManager;
        this.handler = handler;
        this.initialX = this.getX();
        this.initialY = this.getY();
    }

    @Override
    public void tick() {
        coinAnimation(); // 애니메이션 업데이트

        if (disappearing) {
        	handler.removeObj(this); // 객체 제거
        } else {
            coinJumping(); // 점핑 업데이트
        }
    }

    @Override
    public void render(Graphics g) {
        if (sprite[index] == null) {
            System.out.println("Coin sprite is null at index " + index);
        }
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

    public void coinJumping() {
        if (jumping) {
            if (this.getY() > initialY - jumpHeight) {
                this.setY(this.getY() - jumpSpeed); // 코인이 위로 튀어오르는 동작 구현
            } else {
                jumping = false; // 코인이 최고점에 도달하면 떨어지도록 설정
            }
        } else {
            if (this.getY() < initialY) {
                this.setY(this.getY() + jumpSpeed); // 코인이 아래로 떨어지는 동작 구현
            } 
            else {
                this.setY(initialY); // 코인이 초기 위치에 도달하면 멈춤
                // 점프 후 사라지기 시작하도록 설정
                startDisappearing();
            }
        }
    }

    public void startDisappearing() {
        this.disappearing = true;
    }
}