package com.game.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import com.game.gfx.Camera;
import com.game.gfx.SoundManager;
import com.game.gfx.Texture;
import com.game.gfx.Windows;
import com.game.main.util.LevelHandler;
import com.game.object.util.Handler;
import com.game.object.util.KeyInput;

public class Game extends Canvas implements Runnable{

    // Game Constants
    private static final int MILLIS_PER_SEC = 1000;
    private static final int NANOS_PER_SECOND = 1000000000;
    private static final double NUM_TICKS = 60.0;
    private static final String NAME = "Super Mario Bros";

    private static final int WINDOW_WIDTH = 980;
    private static final int WINDOW_HEIGHT = 760;
    private static final int SCREEN_WIDTH = WINDOW_WIDTH - 67;
    private static final int SCREEN_HEIGHT = WINDOW_HEIGHT;
    private static final int SCREEN_OFFSET = 16*3;

    // Game Variables
    private boolean running;
    private boolean gameOver;
    private boolean previousStarTime = false;
    private boolean paused = false;
    private boolean monsterCheck = false;

    // Game Components
    private Thread thread;
    private Handler handler;
    private Camera cam;
    private SoundManager sound;
    private static Texture tex;
    private LevelHandler levelHandler;
    private int timeRemaining; // 남은 시간 (초 단위)
    private int timeLimited; // 타임리미트(소리)
    private long lastTimeCheck;
    private int score;
    private int money;
    
    private static Game gameInstance; // 게임 인스턴스를 저장할 정적 변수

    public Game(int timeLimit) {
    	timeRemaining = timeLimit;
    	gameInstance = this;
        initialize();
    }

    public static void main(String[] args) {
    	int timeLimit = 400;
        new Game(timeLimit);
    }

    private void initialize(){
        // Texture Input
    	tex = new Texture();

    	// 핸들러 초기하
        handler = new Handler(this);
        
        this.addKeyListener(new KeyInput(handler));
        KeyInput keyInput = new KeyInput(handler);
        handler.setKeyInput(keyInput);
        
        // LevelHandler 초기화
        levelHandler = new LevelHandler(handler);
        levelHandler.start();

        // camera 초기화
        cam = new Camera(0, SCREEN_OFFSET);
        handler.setCamera(cam);
        new Windows(WINDOW_WIDTH, WINDOW_HEIGHT, NAME, this);
        
        // sound 초기화 & 플레이 사운드 재생
        sound = SoundManager.getInstance();
        if(levelHandler.getCurrentLevel()==1) {
        	sound.playStageSound();
        } else if(levelHandler.getCurrentLevel()==2) {
        	sound.playStage2Sound();
        }
        
        start();
    }
    
    public void switchLevel() {
    	handler = new Handler(this);
    	
    	this.addKeyListener(new KeyInput(handler));
        KeyInput keyInput = new KeyInput(handler);
        handler.setKeyInput(keyInput);
    	
        levelHandler = new LevelHandler(handler);
        levelHandler.nextLevel();
        
        // 카메라 초기화
        cam = new Camera(0, SCREEN_OFFSET);
        handler.setCamera(cam);

        // 사운드 재생
        if (levelHandler.getCurrentLevel() == 1) {
            sound.playStageSound();
        } else if (levelHandler.getCurrentLevel() == 2) {
            sound.playStage2Sound();
        }

        // 시간 초기화
        timeRemaining = 400; // 또는 새 레벨에 맞는 시간
        lastTimeCheck = System.currentTimeMillis();
    }

    private synchronized void start(){
        thread = new Thread(this);
        thread.start();

        running = true;
    }

    private synchronized void stop(){
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        running = false;
    }

    @Override
    public void run(){
        long lastTime = System.nanoTime();
        double amountOfTicks = NUM_TICKS;
        double ns = NANOS_PER_SECOND / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        int updates = 0;

        while (running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                if (!paused) {
                    tick();
                    updates++;
                }
                delta--;
            }

            if(running){
                render();
                frames++;
            }

            if(System.currentTimeMillis() - timer >= MILLIS_PER_SEC){
                timer += MILLIS_PER_SEC;
                System.out.println("FPS: "+frames+ " TPS: "+updates);
                updates = 0;
                frames = 0;
                
                if (timeRemaining <= 0) {
                    // 시간이 다 되면 게임 오버 처리
                    handler.getPlayer().setDead(true);
                    sound.stopAllSounds();
                    sound.playDie();

                    gameOver = true; // 게임 종료 상태로 설정
                    running = false; // running을 false로 설정하여 게임 루프 종료
                }
            }
        }
        stop();
    }

    private void tick(){
    	if (!paused) {
            handler.tick();
            cam.tick(handler.getPlayer());
            updateTime();
        }
    	
    	if (handler.getNextStage()) {
            switchLevel();
        }
    }

    private void render(){
        BufferStrategy buf = this.getBufferStrategy();
        if(buf == null){
            this.createBufferStrategy(3);
            return;
        }
        
        // draw graphics
        Graphics g = buf.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;
        Color lightBlue = new Color(148, 148, 255);

        if(levelHandler.getCurrentLevel()==1){
            g.setColor(lightBlue);
        } else if(levelHandler.getCurrentLevel()==2){
        	g.setColor(Color.BLACK);
        }
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        g2d.translate(cam.getX(), cam.getY());
        handler.render(g);
        g2d.translate(-cam.getX(), cam.getY());
        
        // 게임 종료 상태일 때 "Game Over" 폰트 그리기
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Serif", Font.BOLD, 48));
            String gameOverMsg = "Game Over";
            g.drawString(gameOverMsg, WINDOW_WIDTH / 2 - 80, WINDOW_HEIGHT / 2 - 100);
        } else if(paused) {
        	g.setColor(Color.YELLOW);
            g.setFont(new Font("Serif", Font.BOLD, 48));
            String pauseMsg = "Paused";
            g.drawString(pauseMsg, WINDOW_WIDTH / 2 - 80, WINDOW_HEIGHT / 2 - 100);
        }
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.BOLD, 16));
        g.drawString("MONEY", 375, -30);
        g.drawString(String.valueOf(money), 405, 0);
        
        
        // draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.BOLD, 18));
        g.drawString("MARIO", 150, -30);
        g.drawString(String.valueOf(score), 165, 0);
        
        // draw world
        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.BOLD, 18));
        g.drawString("WORLD", 550, -30);
        g.drawString("1-1", 575, 0);
        
        // draw timelimit
        g.setColor(Color.WHITE);
        g.setFont(new Font("Serif", Font.BOLD, 18));
        g.drawString("TIME", 850, -30);
        g.drawString(String.valueOf(timeRemaining), 860, 0);
        
        // clean for next frame
        g.dispose();
        buf.show();
    }
    
    private void updateTime() {
    	 long currentTime = System.currentTimeMillis();
         if (currentTime - lastTimeCheck >= 1000) {
             timeRemaining--;
             lastTimeCheck = currentTime;
             
             if (timeRemaining == 30) {
            	 if(levelHandler.getCurrentLevel()==1) {
	            	 sound.stopSound("/sounds/bgm/01.Ground-Theme.wav");
	            	 sound.timeLimit();            	 
            	 } else if(levelHandler.getCurrentLevel()==2) {
            		 sound.stopSound("/sounds/bgm/03. Underground BGM.wav");
	            	 sound.timeLimit2();     
            	 }
             }else if (timeRemaining <= 0) {
                 // 시간 종료 시 처리 (예: 게임 오버)
                 
            	 handler.getPlayer().setDead(true);
            	 sound.stopAllSounds();
            	 sound.playDie();
                 
                 running = false;
             }
             boolean currentStarTime = handler.getPlayer().getStarTime();
             if (currentStarTime) {
            	 if(levelHandler.getCurrentLevel()==1) {
            		 sound.stopSound("/sounds/bgm/01.Ground-Theme.wav");
            	 } else if(levelHandler.getCurrentLevel()==2) {
            		 sound.stopSound("/sounds/bgm/03. Underground BGM.wav");
            	 }
             } else if (previousStarTime && !currentStarTime) {
                 // starTime이 true에서 false로 변할 때
            	 if(levelHandler.getCurrentLevel()==1) {
                     sound.playStageSound(); 
            	 } else if(levelHandler.getCurrentLevel()==2) {
            		 sound.playStage2Sound();
            	 }
             }
             previousStarTime = currentStarTime;
         }
    }
    
    private void exampleSoundUsage() {
        sound.playStageSound();
        
        if (handler.getPlayer() != null && handler.getPlayer().hasJumped()) {
            sound.playJumpSound();
        }
    }
    
    public static Game getGameInstance() {
        return gameInstance;
    }

    public static int getWindowHeight(){
        return WINDOW_HEIGHT;
    }

    public static int getWindowWidth(){
        return WINDOW_WIDTH;
    }

    public static int getScreenHeight(){
        return SCREEN_HEIGHT;
    }

    public static int getScreenWidth(){
        return SCREEN_WIDTH;
    }

    public static Texture getTexture(){
        return tex;
    }
    
    public boolean gameOver() {
    	return gameOver;
    }
    
    public void setGameOver(boolean gameOver) {
    	this.gameOver = gameOver;
    }
    
    public int getScore() {
    	return score;
    }
    
    public void setScore() {
    	this.score = score;
    }
    
    public void addScore(int points) {
        this.score += points;
    }
    
    public int getMoney() {
    	return money;
    }
    
    public void setMoney() {
    	this.money = money;
    }
    
    public void addMoney(int getMoney) {
    	this.money += getMoney;
    }
    
    public void pauseGame() {
        paused = !paused;
        if (paused) {
            sound.stopAllSounds(); // 일시정지 시 모든 사운드 정지
        } else {
        	if(levelHandler.getCurrentLevel()==1) {
        		sound.playStageSound(); // 재개 시 배경 음악 재생
        	} else if(levelHandler.getCurrentLevel()==2) {
        		sound.playStage2Sound(); // 재개 시 배경 음악 재생
        	}
        }
    }
    
    public boolean isMonsterCheck() {
    	return monsterCheck;
    }
    
    public void setMonsterCheck(boolean monsterCheck) {
    	this.monsterCheck = monsterCheck;
    }
}