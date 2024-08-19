package com.game.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.game.gfx.Animation;
import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.Handler;
import com.game.object.util.ObjectId;

public class FireBall extends GameObject {
    private static final float SPEED = 10.0f;

    private Animation playFireBall;
    private Animation currAnimationFireBall;
    private Handler handler;
    
    private Texture tex = Game.getTexture();
    private BufferedImage[] sprite;
    private int animationCounter = 0;
    private int animationSpeed = 5; // 애니메이션 속도 조절
    private int index;
    
    private boolean initialForward; // 생성 시 player의 forward 상태 저장
    private boolean fireAlive;

    // 생성자에 Handler 추가
    public FireBall(float x, float y, float width, float height, int index, int scale, int layer, Handler handler) {
        super(x, y, ObjectId.FireBall, width, height, scale, layer);
        this.handler = handler;
        this.index = index;
        this.sprite = tex.getFireBall();
        this.initialForward = handler.getPlayer().getForward();
    }

    @Override
    public void tick() {
    	
    	fireBallAnimation();
    	applyGravity(0.3f);

        setY(getY() + getVelY());
        // 불꽃 이동
    	if(initialForward) {
    		setX(getX() + SPEED);
    	} else {
    		setX(getX() - SPEED);
    	}
        
        // 화면 밖으로 나갔을 경우 삭제
//        if (getX() < 0 || getX() > Game.WIDTH) {
//            handler.removeObj(this);
//        }

        // 충돌 검사
        collision();
        
//        System.out.println("fireX : "+this.getX()+" / fireY : "+this.getY());
//        System.out.println("playerX : "+handler.getPlayer().getX()+" / playerY : "+handler.getPlayer().getY());
    }

    @Override
    public void render(Graphics g) {
//    	showBounds(g);
    	g.drawImage(sprite[index], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight(), null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
    }
    
    public void fireBallAnimation() {
        animationCounter++;
        if (animationCounter >= animationSpeed) {
            animationCounter = 0;
            index = (index + 1) % sprite.length; // 스프라이트 배열 길이에 따라 인덱스 반복
        }
    }

    private void collision() {
        for (int i = 0; i < handler.getGameObjs().size(); i++) {
            GameObject temp = handler.getGameObjs().get(i);
            if (temp.getId() == ObjectId.Block) {
            	if(getBounds().intersects(temp.getBounds())) {
                	setY(temp.getY() - temp.getHeight());
                	setVelY(-3);
            	}
            }
            
            if (temp.getId() == ObjectId.Pipe){
            	if(getBounds().intersects(temp.getBounds())) {
                	handler.removeObj(this);
            	}
            }
            
            if (temp.getId() == ObjectId.EndTile){
            	if(getBounds().intersects(temp.getBounds())) {
                	handler.removeObj(this);
            	}
            }
            
            if (temp.getId() == ObjectId.ItemBlocks
            		|| temp.getId() == ObjectId.CoinBlocks){
            	if(getBounds().intersects(temp.getBounds())) {
                	handler.removeObj(this);
            	}
            }
        }
    }
    
//    public void lifeTime() {
//    	new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//            	fireAlive = false;
//            }
//        }, 1500); // 1000ms = 1초
//    }
    
//    public void hitEnemy() {
//    	if(handler.getCheckToFire()) {
//    		
//    	}
//    }
    
    public void showBounds(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.RED);
        g2d.draw(getBounds());
    }
}