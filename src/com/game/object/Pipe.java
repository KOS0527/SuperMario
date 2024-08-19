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

public class Pipe extends GameObject {
	private Texture tex = Game.getTexture();
	private int index;
	private BufferedImage[] sprite;
    private boolean enterable;
    private Handler handler;
    private SoundManager sound;

    public Pipe(int x, int y, int width, int height, int index, int scale, int layer, boolean enterable, Handler handler){
        super(x, y, ObjectId.Pipe, width, height, scale, layer);
        this.enterable = enterable;
        this.handler = handler;
        this.index = index;
        this.sound = SoundManager.getInstance();
        sprite = tex.getPipe_1();
    }
    
    @Override
    public void tick() {
    	collision();
//    	System.out.println("enterable : " + enterable);
    }

    @Override
    public void render(Graphics g) {
//    	showBounds(g);
    	
    	g.drawImage(sprite[index], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)getX() -1,
        		(int)getY(),
        		(int)getWidth(),
        		(int)getHeight());
    }
    
    public boolean getEnterable() {
        return enterable;
    }
    
    public void setEnterable(boolean enterable) {
    	this.enterable = enterable;
    }
    
    public void showBounds(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.RED);
        g2d.draw(getBounds());
    }
    

    public void collision() {
//   	 boolean foundWall = false; // 벽과 충돌 여부를 나타내는 변수

   	    // 블록과 파이프 객체만 검사
   	 for (int i = 0; i < handler.getGameObjs().size(); i++) {
            GameObject temp = handler.getGameObjs().get(i);
            if (temp.getId() == ObjectId.EnemyN || temp.getId() == ObjectId.EnemyG) {
                if (temp.getId()==ObjectId.EnemyN) {
//                    foundWall = true;
                    // 벽 충돌 상태를 로그로 출력
//           	    System.out.println("WallCollision: " + foundWall);
                }
            }
            
            if(temp.getId() == ObjectId.Player) {
            	if(getBounds().intersects(getBounds())) {
                	if(enterable && handler.getPlayer().getCrouching()) {
                		handler.getPlayer().setPipeIn(true);
                		sound.usePipe();
                		enterable = false;
                	}
            	}    
        	}
   	 	}
    }
}
