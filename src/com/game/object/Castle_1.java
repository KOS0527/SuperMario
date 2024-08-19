package com.game.object;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.Handler;
import com.game.object.util.ObjectId;

public class Castle_1 extends GameObject{
	private Texture tex = Game.getTexture();
	private int index;
	private BufferedImage[] sprite;
	private Handler handler;

	public Castle_1(float x, float y,  float width, float height, int index, int scale, int layer, Handler handler) {
		super(x, y, ObjectId.Castle, width, height, scale, layer);
		this.index = index;
		this.handler = handler;
		sprite = tex.getCastle_1();
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(sprite[index], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)getX(),
				(int)getY(),
				(int)getWidth(),
				(int)getHeight());
	}
}