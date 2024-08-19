package com.game.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import com.game.gfx.SoundManager;
import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.Handler;
import com.game.object.util.ObjectId;

public class Flag2 extends GameObject{
	private Texture tex = Game.getTexture();
    private BufferedImage[] spriteFlag2;
    private SoundManager soundManager;
    private Handler handler;

    private static boolean soundAlreadyPlayed = false;
    private boolean soundPlayed1;
    private boolean soundPlayed2;

    public Flag2(int x, int y, int width, int height, int index, int scale, int layer, SoundManager soundManager, Handler handler) {
        super(x, y, ObjectId.Flag2, width, height, scale , layer);
        this.soundManager = soundManager;
        this.handler = handler;
        this.soundPlayed1 = false;
        this.soundPlayed2 = false;

        spriteFlag2 = tex.getFlag_2();
    }

    @Override
    public void tick() {
    	if (handler != null && !soundPlayed1) {
            Player player = handler.getPlayer();
            if (player != null && getBounds().intersects(player.getBoundsTop())) {
                System.out.println("게임 클리어"); // 디버깅용 메시지
                if (!soundAlreadyPlayed) {
                	soundManager.stopAllSounds(); // 모든 사운드 정지
                    soundManager.touchFlag(); // 플래그에 닿았을 때 사운드 재생
                    soundPlayed1 = true; // 사운드가 한 번만 재생되도록 플래그 설정
                    
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            playGameClearSound();
                        }
                    }, 1500); // 1.5초 후에 실행 (1500밀리초 = 1.5초)
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
//    	showBounds(g);
    	
        g.drawImage(spriteFlag2[0], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight(), null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) getX() + 20,
        		(int) getY(),
        		(int) getWidth() / 6,
        		(int) getHeight());
    }

    public void playGameClearSound() {
        if (!soundPlayed2) {
            soundManager.gameClear();
            soundPlayed2 = true;
        }
    }
    
    public void showBounds(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.RED);
        g2d.draw(getBounds());
    }
}