package com.game.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.game.gfx.SoundManager;
import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.Handler;
import com.game.object.util.ObjectId;

public class Star extends GameObject{
	private int animationCounter = 0;
    private int animationSpeed = 5; // 애니메이션 속도 조절
    private Texture tex = Game.getTexture();
    private int index;
    private BufferedImage[] sprite;
    private SoundManager soundManager;
    private Handler handler;
    private boolean soundCheck;

    // 이동 관련
    private int speed = 2; // 별의 이동 속도

    public Star(float x, float y, int width, int height, int index, int scale, int layer, SoundManager soundManager, Handler handler) {
        super(x, y, ObjectId.Star, width, height, scale, layer);
        this.index = index;
        sprite = tex.getStar();

        this.soundManager = soundManager;
        this.handler = handler;
        this.soundCheck = false;
    }

    @Override
    public void tick() {
        coinAnimation();
        starMove();
        collision();
        applyGravity(0.4f);
    	setY(getY()+getVelY());
    }

    @Override
    public void render(Graphics g) {
        showBounds(g);
        Graphics2D g2d = (Graphics2D) g;

        // 회전 중심을 이미지의 중앙으로 설정
        int centerX = (int) (getX() + getWidth() / 2);
        int centerY = (int) (getY() + getHeight() / 2);

        // 회전 각도 설정 (예: 회전 각도를 시간에 따라 변화하도록 설정)
        double angle = Math.toRadians(animationCounter * 10); // 예시로 애니메이션 카운터를 이용한 회전

        // 그래픽 컨텍스트의 회전 변환 설정
        g2d.rotate(angle, centerX, centerY);

        // 이미지 그리기
        g2d.drawImage(sprite[index], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight(), null);

        // 회전 변환 리셋
        g2d.rotate(-angle, centerX, centerY);
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

    private void collision() {
        for (int i = 0; i < handler.getGameObjs().size(); i++) {
            GameObject temp = handler.getGameObjs().get(i);
            if ((temp.getId() == ObjectId.Block
            		|| temp.getId() == ObjectId.Pipe
            		|| temp.getId() == ObjectId.CoinBlocks)
                    && getBounds().intersects(temp.getBounds())) {
                setY(temp.getY() - getHeight());
                setVelY(-15);
            }
        }
    }

    public void starMove() {
        setX(getX() + speed); // 현재 X 좌표에 속도를 더하여 오른쪽으로 이동
    }

    public void showBounds(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.RED);
        g2d.draw(getBounds());
    }
    

    public boolean getSoundCheck() {
    	return soundCheck;
    }
    
    public void setSoundCheck(boolean soundCheck) {
    	this.soundCheck = soundCheck;
    }
}