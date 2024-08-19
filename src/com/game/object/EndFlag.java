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

public class EndFlag extends GameObject {
	private Texture tex = Game.getTexture();
    private BufferedImage[] spriteEndFlag;
    private SoundManager soundManager;
    private Handler handler;

    private static boolean soundAlreadyPlayed = false;
    private boolean soundPlayed1;
    private boolean soundPlayed2;

	public EndFlag(int x, int y, int width, int height, int index, int scale, int layer, SoundManager soundManager, Handler handler) {
		super(x, y, ObjectId.EndFlag, width, height, scale, layer);
		this.soundManager = soundManager;
        this.handler = handler;
        this.soundPlayed1 = false;
        this.soundPlayed2 = false;

        spriteEndFlag = tex.getEndFlag();
	}

	@Override
	public void tick() {
		endFlagMove();
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(spriteEndFlag[0], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
	}

	@Override
	public Rectangle getBounds() {
		 return new Rectangle((int)getX(),(int)getY(),(int)getWidth(),(int)getHeight());
	}

	public void endFlagMove() {
		if(handler.getPlayer().getEndFlag()) {
			if(getY()>340) {
				setY(getY()-3);	
				new Timer().schedule(new TimerTask() {
        			public void run() {
        				new Timer().schedule(new TimerTask() {
                			public void run() {
                				handler.clearObject();
                				handler.setNextStage(true);
                			}
                		}, 3000);
        			}
        		}, 1000);
			}
		}
	}
}
