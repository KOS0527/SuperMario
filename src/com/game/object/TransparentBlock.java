package com.game.object;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.game.gfx.SoundManager;
import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.Handler;
import com.game.object.util.ObjectId;

public class TransparentBlock extends GameObject {
    private SoundManager soundManager;
    private Texture tex = Game.getTexture();
    private BufferedImage[] blockSprite;
    private Coin coin;
    private Mushroom mushroom;
    
    private int index;
    private int animationCounter = 0;
    private int animationSpeed = 10; // 애니메이션 속도 조절
    
    private Handler handler;
    
    private boolean hasItem;
    private boolean hasItemCheck;
    private boolean hasLifeMushroom;
    private boolean transparent = true; // 투명 여부 필드 추가
    private boolean coinSoundPlayed = false;
    private boolean emptyBlockSoundPlayed = false;

    public TransparentBlock(int x, int y, int width, int height, int index, int scale, int layer, boolean transparent, boolean hasLifeMushroom, SoundManager soundManager, Handler handler) {
        super(x, y, ObjectId.TransparentBlock, width, height, scale, layer);
        this.index = index;
        this.hasItem = true;
        this.hasItemCheck = false;
        this.hasLifeMushroom = hasLifeMushroom;
        this.soundManager = soundManager;
        this.handler = handler;
        this.blockSprite = tex.getItemBlocks();
        this.transparent = transparent;

        // 인덱스 유효성 확인
        if (index < 0 || index >= blockSprite.length) {
            throw new IllegalArgumentException("Invalid index value for sprite array: " + index);
        }
    }

    @Override
    public void tick() {
//    	System.out.println("hasItem : "+hasItem+" / hasItemCheck : "+hasItemCheck+" / hasMushroom : "+hasLifeMushroom+" / transparent : "+transparent);
    	
    	// 플레이어와의 충돌 감지 및 투명 상태 변경 로직 추가
        if (handler != null) {
            Player player = handler.getPlayer();
            if (player != null && getBounds().intersects(player.getBoundsTop())) {
            	System.out.println("플레이어랑 충돌");
                transparent = false; // 투명 블록을 플레이어 머리에 닿았을 때 보이게 변경
            }
        }
        
    	if (hasItem) {
            animationCounter++;
            if (animationCounter >= animationSpeed) {
                animationCounter = 0;
                index = (index + 1) % 3; // 0, 1, 2번 스프라이트 반복
            }
        } else {
            index = 3; // 아이템이 없는 경우 3번째 스프라이트로 설정
            if (!emptyBlockSoundPlayed && !hasItemCheck) {
                soundManager.emptyItemBlock();
                hasItemCheck = true;
//                System.out.println("hasItem : " + hasItem + " hasCoin : " + hasCoin + " hasMushroom : " + hasMushroom);
                if(hasItemCheck && hasLifeMushroom) {
	                emptyBlockSoundPlayed = true; // 소리가 재생되었음을 표시
	                soundManager.getMushroomSound();
                }
            }
            // 아이템이 없는 경우에 한 번만 소리를 재생하도록 설정
            if (!emptyBlockSoundPlayed) {
                soundManager.emptyItemBlock();
                emptyBlockSoundPlayed = true; // 소리가 재생되었음을 표시
            }
        }
    	
    	// 아이템 생성 처리
    	if (hasLifeMushroom() && !hasItem) {
            generateItem("LifeMushroom");
//            System.out.println("목숨버섯 생성");
        }
    }

    @Override
    public void render(Graphics g) {
        if(!transparent) {    
        	g.drawImage(blockSprite[3], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight(), null);
        }

        if (coin != null) {
            coin.render(g);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    }

    // 아이템 생성 메서드
    
    public void generateItem(String itemType) {
    	float itemX = getX() + (getWidth() - 48) / 2; // 블록의 중앙 위치에 코인을 생성
        float itemY = getY() - 48; // 블록 바로 위에 생성되도록 Y 좌표 설정
        switch (itemType) {
	        case "LifeMushroom":
	        LifeMushroom lifeMushroom= new LifeMushroom(itemX, itemY, 48, 48, 0, 1, 1, soundManager, handler); // 코인 객체 생성
	        handler.addObj(lifeMushroom); // 생성된 코인 객체를 handler에 추가
	        hasItemCheck = true;
	        hasLifeMushroom = false;
        }
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
    
    public boolean hasLifeMushroom() {
    	return hasLifeMushroom;
    }
    
    public void setHasLifeMushroom() {
    	this.hasLifeMushroom = hasLifeMushroom;
    }

    // 투명 여부 getter 메서드
    public boolean isTransparent() {
        return transparent;
    }

    // 투명 여부 setter 메서드
    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }
}