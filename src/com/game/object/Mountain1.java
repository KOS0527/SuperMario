package com.game.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.ObjectId;

public class Mountain1 extends GameObject{
	private Texture tex = Game.getTexture();
	private int index;
	private BufferedImage[] sprite;

	public Mountain1(int x, int y, int width, int height, int index, int scale, int layer) {
		super(x, y, ObjectId.Mountain1, width, height, scale, layer);
		this.index = index;
        sprite = tex.getMountain_1();
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		showBounds(g);
		
		g.drawImage(sprite[index], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
    }

	@Override
	public Rectangle getBounds() {
        return new Rectangle((int)getX(),(int)getY(),(int)getWidth(),(int)getHeight());
    }
	
	public void showBounds(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.RED);
        g2d.draw(getBounds());
    }
}
