package com.game.object;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.game.gfx.SoundManager;
import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.Handler;
import com.game.object.util.ObjectId;
import com.game.object.util.UnitState;

public class ItemBlocks extends GameObject {
    private SoundManager soundManager;
    private Texture tex = Game.getTexture();
    private BufferedImage[] blockSprite;
    
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

    public ItemBlocks(int x, int y, int width, int height, int index, int scale, boolean hasCoin, boolean hasMushroom, boolean hasFlower, boolean hasStar, int layer, SoundManager soundManager, Handler handler) {
        super(x, y, ObjectId.ItemBlocks, width, height, scale, layer);
        this.index = index;
        this.hasItemCheck = false;
        this.hasItem = true;
        this.hasFlower = hasFlower;
        this.hasMushroom = hasMushroom;
        this.hasCoin = hasCoin;
        this.hasStar = hasStar;
        this.soundManager = soundManager;
        this.handler = handler;
        this.blockSprite = tex.getItemBlocks();

        // 인덱스 유효성 확인
        if (index < 0 || index >= blockSprite.length) {
            throw new IllegalArgumentException("Invalid index value for sprite array: " + index);
        }
    }

    @Override
    public void tick() {
//    	System.out.println("hasItem : " + hasItem + " hasCoin : " + hasCoin + " hasMushroom : " + hasMushroom);
    	if (hasItem) {
            animationCounter++;
            if (animationCounter >= animationSpeed) {
                animationCounter = 0;
                index = (index + 1) % 3; // 0, 1, 2번 스프라이트 반복
            }
        } else {
            index = 3; // 아이템이 없는 경우 3번째 스프라이트로 설정
        
            // 아이템이 없는 경우에 한 번만 소리를 재생하도록 설정
            if (!emptyBlockSoundPlayed && !hasItemCheck) {
                soundManager.emptyItemBlock();
                hasItemCheck = true;
//                System.out.println("hasItem : " + hasItem + " hasCoin : " + hasCoin + " hasMushroom : " + hasMushroom);
                if(hasItemCheck && hasCoin) {
	                emptyBlockSoundPlayed = true; // 소리가 재생되었음을 표시
	                soundManager.getCoinSound();
                }if(hasItemCheck && hasMushroom) {
                	soundManager.getMushroomSound();
                    emptyBlockSoundPlayed = true; // 소리가 재생되었음을 표시
                }if(hasItemCheck && hasFlower) {
                	soundManager.getMushroomSound();
                    emptyBlockSoundPlayed = true; // 소리가 재생되었음을 표시
                }if(hasItemCheck && hasStar) {
                	soundManager.getMushroomSound();
                    emptyBlockSoundPlayed = true; // 소리가 재생되었음을 표시
                }
            }
        }
    	 
    	// 아이템 생성 처리
    	if (hasCoin() && !hasItem) {
            generateItem("Coin");
            System.out.println("코인 생성");
        } else if (hasMushroom() && !hasItem) {
            generateItem("Mushroom");
            System.out.println("버섯 생성");
        } else if (hasFlower() && !hasItem) {
        	if(handler.getPlayer().getMarioState() == UnitState.Mario_Small) {
        		hasFlower = false;
        		hasMushroom = true;
        	} else {
	            generateItem("Flower");
	            System.out.println("꽃 생성");
        	}
        } else if (hasStar() && !hasItem) {
            generateItem("Star");
            System.out.println("별 생성");
        }
    }

    @Override
    public void render(Graphics g) {
        if (hasItem && shaking) {
        	g.drawImage(blockSprite[index], (int)getX(), (int)(getY()), (int)getWidth(), (int)getHeight(), null);
        } else if(!hasItem){
            g.drawImage(blockSprite[3], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight(), null);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    }

    // 아이템 생성 메서드
    public void generateItem(String itemType) {
    	float coinX = getX() + (getWidth() - 48) / 2;
        float coinY = getY() - 48;

    	float mushroomX = getX() + (getWidth() - 48) / 2;
        float mushroomY = getY() - 48;

    	float flowerX = getX() + (getWidth() - 48) / 2;
        float flowerY = getY() - 41;
        
        switch (itemType) {
        case "Coin":
            Coin coin = new Coin(coinX, coinY, 48, 48, 0, 1, 1, soundManager, handler);
            handler.addObj(coin);
            hasCoin = false;
//            System.out.println("generation Coin");
//            System.out.println("block X : " + coinX + " / block Y : " + coinY);
            getScore();
            getMoney();
            break;
            
        case "Mushroom":
            Mushroom mushroom = new Mushroom(mushroomX, mushroomY, 48, 48, 0, 1,  1, soundManager, handler);
            handler.addObj(mushroom);
            hasMushroom = false;
//            System.out.println("generation Mushroom");
//            System.out.println("block X : " + mushroomX + " / block Y : " + mushroomY);
            getScore();
            break;
            
        case "Flower":
            Flower flower = new Flower(flowerX, flowerY, 48, 48, 0, 1, 1, soundManager, handler);
            handler.addObj(flower);
            hasFlower = false;
//            System.out.println("generation flower");
//            System.out.println("block X : " + flowerX + " / block Y : " + flowerY);
            getScore();
            break;
            
        case "Star":
            Star star= new Star(flowerX, flowerY, 48, 48, 0, 1, 1, soundManager, handler);
            handler.addObj(star);
            hasStar= false;
//            System.out.println("generation star");
//            System.out.println("block X : " + flowerX + " / block Y : " + flowerY);
            getScore();
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
    
    public boolean hasMushroom() {
    	return hasMushroom;
    }
    
    public void setHasMushroom(boolean hasMushroom) {
    	this.hasMushroom = hasMushroom;
    }
    
    public boolean hasFlower() {
    	return hasFlower;
    }
    
    public void setHasFlower(boolean hasFlower) {
    	this.hasFlower = hasFlower;
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