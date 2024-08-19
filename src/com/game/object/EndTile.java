package com.game.object;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.game.gfx.SoundManager;
import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.ObjectId;

public class EndTile extends GameObject {
	private Texture tex = Game.getTexture();
	private int pipeType;
	private int index;
	private BufferedImage[] sprite;
    private boolean enterable;

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

    public EndTile(int x, int y, int width, int height, int index, int scale, int layer, SoundManager soundManager){
        super(x, y, ObjectId.EndTile, width, height, scale, layer);
        this.index = index;
        sprite = tex.getEndTile();
    }
    
    public int getPipeType() {
        return pipeType;
    }
}
