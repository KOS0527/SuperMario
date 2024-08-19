package com.game.object;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import com.game.gfx.SoundManager;
import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.Handler;
import com.game.object.util.ObjectId;

public class Flag3 extends GameObject{
	private Texture tex = Game.getTexture();
	private BufferedImage[] spriteFlag3;
	private boolean stopped = false; // 멈춤 상태를 나타내는 변수
	private boolean gameClearSoundPlaying = false;
	private SoundManager soundManager;
	private Handler handler;
	
	@Override
	public void tick() {
		
		collision();
		
		final boolean isFlag2TouchFlag = handler.getPlayer().getTouchFlag();
	    if (isFlag2TouchFlag) {
	        flagMove(); // Flag2의 touchFlag가 true일 때만 flagMove() 호출
//	        System.out.println("플래그 이동"); // 디버깅을 위한 로그 출력
	    } else {
//	        System.out.println("플래그 이동 중지"); // touchFlag가 false일 때의 로그 출력
	    }
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(spriteFlag3[0], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)getX(),(int)getY(),(int)getWidth(),(int)getHeight());
	}
	
	public Flag3(int x, int y, int width, int height, int index, int scale, int layer, Handler handler) {
		super(x, y, ObjectId.Flag3, width, height, scale, layer);
		spriteFlag3 = tex.getFlag_3();
		this.soundManager = SoundManager.getInstance();
		this.handler = handler;
	}
	
	public void flagMove() {
		if(handler.getPlayer().getTouchFlag()) {
			setY(getY() + 3);
		}
	}
	
	public void collision() {
		for(int i=0; i<handler.getGameObjs().size(); i++){
            GameObject temp = handler.getGameObjs().get(i);
            if(temp.getId() == ObjectId.EndTile) {
            	if(getBounds().intersects(temp.getBounds())){
            		handler.getPlayer().setTouchFlag(false);
            		handler.getPlayer().setCheckMove(true);
            		new Timer().schedule(new TimerTask() {
            			public void run() {
                    		handler.getPlayer().setImageReversal(true);
            			}
            		}, 2000);
            	}
            }
		}
	}
}