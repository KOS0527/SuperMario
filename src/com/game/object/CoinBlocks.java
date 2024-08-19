package com.game.object;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.game.gfx.SoundManager;
import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.Handler;
import com.game.object.util.ObjectId;

public class CoinBlocks extends GameObject {
	 private SoundManager soundManager;
	    private Texture tex = Game.getTexture();
	    private BufferedImage[] blockSprite;
	    
	    private int coinMax = 10;
	    private int index;
	    private int animationCounter = 0;
	    private int animationSpeed = 10; // 애니메이션 속도 조절
	    private int initialX; // 초기 X 좌표를 저장할 변수
	    private int initialY; // 초기 Y 좌표를 저장할 변수
	    
	    private Handler handler;
	    
	    private boolean hasItemCheck;
	    private boolean hasItem;
	    private boolean hasMushroom;
	    private boolean hasCoin;
	    private boolean hasFlower;
	    private boolean hasStar;

	    private boolean emptyBlockSoundPlayed = false;
	    private boolean shaking = true;

	    public CoinBlocks(int x, int y, int width, int height, int index, int scale, int layer, boolean hasCoin, boolean hasStar, SoundManager soundManager, Handler handler) {
	        super(x, y, ObjectId.CoinBlocks, width, height, scale, layer);
	        this.index = index;
	        this.hasItemCheck = false;
	        this.hasItem = true;
	        this.hasCoin = hasCoin;
	        this.hasStar = hasStar;
	        this.soundManager = soundManager;
	        this.handler = handler;
	        this.blockSprite = tex.getCoinBlocks();

	        // 인덱스 유효성 확인
	        if (index < 0 || index >= blockSprite.length) {
	            throw new IllegalArgumentException("Invalid index value for sprite array: " + index);
	        }
	    }

	    @Override
	    public void tick() {
	    	
	    	if (!emptyBlockSoundPlayed && !hasItemCheck) {
                hasItemCheck = true;
//                System.out.println("hasItem : " + hasItem + " hasCoin : " + hasCoin + " hasMushroom : " + hasMushroom);

    	    	if(hasItemCheck && hasCoin) {
                    emptyBlockSoundPlayed = true; // 소리가 재생되었음을 표시
                }
            }
	    	 
	    	// 아이템 생성 처리
	    	if (hasCoin() && !hasItem) {
	            generateItem("Coin");
	            if (coinMax == 0) {
		    		hasItem = false;
		    		hasCoin = false;
		    		shaking = false;
		    	} else if (coinMax > 0) {
		    		hasItem = true;
		    		hasCoin = true;
		    	}
	        }
	    	if (hasStar() && !hasItem) {
	    		generateItem("Star");
	    		hasItem = false;
	    		hasStar = false;
	    		shaking = false;
	    	}
	    }

	    @Override
	    public void render(Graphics g) {
	        if(coinMax == 0 || !hasItem){
	            g.drawImage(blockSprite[1], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight(), null);
	        } else {
	        	g.drawImage(blockSprite[index], (int)getX(), (int)(getY()), (int)getWidth(), (int)getHeight(), null);
	        }
	    }

	    @Override
	    public Rectangle getBounds() {
	        return new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
	    }

	    // 아이템 생성 메서드
	    public void generateItem(String itemType) {
	    	float coinX = getX() + (getWidth() - 48) / 2; // 블록의 중앙 위치에 코인을 생성
	        float coinY = getY() - 48; // 블록 바로 위에 생성되도록 Y 좌표 설정
	        
	        switch (itemType) {
	        case "Coin":
	            // 코인을 생성하고 초기 Y 좌표를 설정
	            Coin coin = new Coin(coinX, coinY, 48, 48, 0, 1, 1, soundManager, handler);
	            handler.addObj(coin);
	            hasCoin = false;
//	            System.out.println("generation Coin");
//	            System.out.println("block X : " + coinX + " / block Y : " + coinY);
	            getScore();
	            getMoney();
	            coinMax --;
	            soundManager.getCoinSound();
	            break;
	            
	        case "Star":
	        	// 코인을 생성하고 초기 Y 좌표를 설정
	            Star star = new Star(coinX, coinY, 48, 48, 0, 1, 1, soundManager, handler);
	            handler.addObj(star);
	            hasStar = false;
//	            System.out.println("generation Coin");
//	            System.out.println("block X : " + coinX + " / block Y : " + coinY);
	            getScore();
	            getMoney();
	            soundManager.getCoinSound();
	            break;
	    }

		    emptyBlockSoundPlayed = true;
		    hasItem = false;
	    }
	    
	    public boolean hasItem() {
	        return hasItem;
	    }

	    public void setHasItem(boolean hasItem) {
	        this.hasItem = hasItem;
	        if (hasItem) {
	        	emptyBlockSoundPlayed = false; // 아이템이 다시 생겼을 때 소리 재생 여부를 초기화
	        }
	    }
	    
	    public boolean hasCoin() {
	    	return hasCoin;
	    }
	    
	    public void setHasCoin(boolean hasCoin) {
	    	this.hasCoin = hasCoin;
	    }
	    
	    public boolean hasStar() {
	    	return hasStar;
	    }
	    
	    public void setHasStar(boolean hasStar) {
	    	this.hasStar = hasStar;
	    }
	    
	    public boolean shaking() {
	    	return shaking;
	    }
	    
	    public void setShaking(boolean shaking) {
	    	this.shaking = shaking;
	    }
	    
	    public void setInitialPosition(int x, int y) {
	        this.initialX = x;
	        this.initialY = y;
	    }
	    
	    public void getScore() {
	    	Game game = Game.getGameInstance();
	        game.addScore(200);
	    }
	    
	    public void getMoney() {
	    	Game game = Game.getGameInstance();
	        game.addMoney(1);
	    }
}