package com.game.object;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.game.gfx.SoundManager;
import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.ObjectId;

public class Flag1 extends GameObject{
	private Texture tex = Game.getTexture();
	private int index;
	private BufferedImage[] spriteFlag1;
	private boolean gameClear;
	private SoundManager soundManager;
	
	@Override
	public void tick() {
		
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(spriteFlag1[0], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
	}
	
	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)getX(),(int)getY(),(int)getWidth(),(int)getHeight());
	}
	
	public Flag1(int x, int y, int width, int height, int index, int scale, int layer, SoundManager soundManager) {
		super(x, y, ObjectId.Flag1, width, height, scale, layer);
		this.index = index;
		spriteFlag1 = tex.getFlag_1();
	}
	
	public boolean gameClear() {
		return gameClear;
	}
	
	public void setGameClear() {
		this.gameClear = gameClear;
	}
}
