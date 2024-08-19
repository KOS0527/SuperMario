package com.game.object;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.ObjectId;

public class Mountain2 extends GameObject{
	private Texture tex = Game.getTexture();
	private int index;
	private BufferedImage[] sprite;

	public Mountain2(int x, int y, int width, int height, int index, int scale, int layer) {
		super(x, y, ObjectId.Mountain2, width, height, scale, layer);
		this.index = index;
        sprite = tex.getMountain_2();
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
        return new Rectangle((int)getX(),(int)getY(),(int)getWidth(),(int)getHeight());
    }
	
}