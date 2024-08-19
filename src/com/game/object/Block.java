package com.game.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.game.gfx.SoundManager;
import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.Handler;
import com.game.object.util.ObjectId;

public class Block extends GameObject{
	
	private Texture tex = Game.getTexture();
	private int index;
	private BufferedImage[] sprite;
	
	private Debris debris;
	
    private boolean transparent = true; // 투명 여부 필드 추가
    private boolean hit;
    private boolean shaking = false;
    
    private SoundManager soundManager;
    private Handler handler;
    
    @Override
    public void tick(){
    	if (hit) {
    		debris.tick();
    	}
    }

    @Override
    public void render(Graphics g) {
//    	showBounds(g);
    	if(!hit) {
    		g.drawImage(sprite[index], (int)getX(), (int)getY(), (int)getHeight(), (int)getWidth(), null);
    	}else if(hit){
    		debris.draw(g);
    	}else if(shaking) {
    		 g.drawImage(sprite[index], (int)getX(), (int)(getY() - 10), (int)getWidth(), (int)getHeight(), null);
    	}
    }
    
    public boolean shouldRemove() {
    	if(debris.shouldRemove()) {
    		return true;
    	}
    	return false;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    }

    public Block(int x, int y, int width, int height, int index, int scale, int layer, SoundManager soundManager) {
        super(x, y, ObjectId.Block, width, height, scale, layer);
        this.index = index;
        sprite = tex.getTile1();
        
        this.soundManager = soundManager;
    }
    
    public void hit() {
    	hit = true;
    	debris = new Debris(getX(), getY(), getWidth(), getHeight(), getScale());
    	
    	if(soundManager != null) {
            soundManager.blockDestroy(); // SoundManager 객체가 null이 아닌 경우에만 호출
        } else {
            System.out.println("SoundManager is null, unable to play block destroy sound.");
        }
    }
    
    public boolean shaking() {
    	return shaking;
    }
    
    public void setShaking(boolean shaking) {
    	this.shaking = shaking;
    }
    public void showBounds(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.RED);
        g2d.draw(getBounds());
    }
}