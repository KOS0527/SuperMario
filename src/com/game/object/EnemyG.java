package com.game.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import com.game.gfx.Animation;
import com.game.gfx.Camera;
import com.game.gfx.SoundManager;
import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.Handler;
import com.game.object.util.ObjectId;

public class EnemyG extends GameObject {
	 private float speed = 1.5f; 

	    private Player player;
	    private EnemyN enemyN;

	    private static final float WIDTH = 16;
	    private static final float HEIGHT = 16;

	    private Handler handler;
	    private Texture tex;
	    private BufferedImage[] spriteG;
	    private Animation enemyWalkG;
	    private Animation currAnimation;

	    private boolean isAlive;
	    private boolean isDead = false;
	    private boolean checkDead = false;
	    private boolean deadByStar = false;
	    private boolean deadByJump = false;
	    private boolean isVisible = false;
	    private boolean soundCheck = true;

		private boolean deadSideRight = false;
	    
	    private boolean movingL = false;

	    private Camera camera;
	    private SoundManager soundManager;
	    private Timer deathTimer;

	    public EnemyG(float x, float y, int scale, int layer, Handler handler, SoundManager soundManager) {
	    	super(x, y, ObjectId.EnemyG, WIDTH, HEIGHT, scale, layer);

	        this.handler = handler;
	        this.player = handler.getPlayer();
	        this.soundManager = soundManager;
	        
	        tex = Game.getTexture();

	        spriteG = tex.getEnemyG(); // spriteG 초기화

	        // enemyWalkG 애니메이션 초기화
	        enemyWalkG = new Animation(5, spriteG[0], spriteG[1]);
	        
	        // 현재 애니메이션 설정
	        currAnimation = enemyWalkG;
	    }

    @Override
    public void tick() {
    	// 굼바 애니메이션
    	currAnimation.runAnimation();
        applyGravity(0.5f);
        checkDirectionRight();
        
        if(!isDead) {
    		moveG();
            collision();
            killEnemyG();    
            starDead();
            isAlive = true;
        } else if (deadByStar){
        	setY(getY() + getVelY());
        }
        
//        System.out.println("deadByStar : "+deadByStar+" / deadSideRight : "+deadSideRight);
    }

    @Override
    public void render(Graphics g) {
//    	showBounds(g);
    	
    	if (isDead) {
    		if(deadByJump) {
    			g.drawImage(spriteG[2], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
    		}
    		if(deadByStar) {
    			if(deadSideRight) {
    				g.drawImage(spriteG[0], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
    			} else {
    				g.drawImage(spriteG[1], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
    			}
    		}
        } else {
            // 적이 살아 있는 상태인 경우
        	currAnimation.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight());
        }
    }

    private void collision() {
    	
        for (int i = 0; i < handler.getGameObjs().size(); i++) {
            GameObject temp = handler.getGameObjs().get(i);
            
            if(isDead) {
            	 continue;
            } else {
		            if (temp.getId() == ObjectId.Block
		            		|| temp.getId() == ObjectId.ItemBlocks
		            		|| temp.getId() == ObjectId.CoinBlocks) {
		                if (getBounds().intersects(temp.getBounds())) {
		                    setY(temp.getY() - getHeight());
		                    setVelY(0);
		                }
		
		                if (getBoundsTop().intersects(temp.getBounds())) {
		                    setY(temp.getY() + temp.getHeight());
		                    setVelY(0);
		                }
		
		                if (getBoundsRight().intersects(temp.getBounds())) {
		                    setX(temp.getX() - getWidth());
		                }
		
		                if (getBoundsLeft().intersects(temp.getBounds())) {
		                    setX(temp.getX() + temp.getWidth());
		                }
		            }
		            
		            if (temp.getId() == ObjectId.Pipe
		            		|| temp.getId() == ObjectId.EndTile) {
		                if (getBounds().intersects(temp.getBounds())) {
		                    setY(temp.getY() - getHeight());
		                    setVelY(0);
		                }
		
		                if (getBoundsTop().intersects(temp.getBounds())) {
		                    setY(temp.getY() + temp.getHeight());
		                    setVelY(0);
		                }
		
		                if (getBoundsRight().intersects(temp.getBounds())) {
		                    setX(temp.getX() - getWidth() - 1);
		                    movingL = false; // 방향을 바꿉니다.
		                }
		
		                if (getBoundsLeft().intersects(temp.getBounds())) {
		                    setX(temp.getX() + temp.getWidth() + 1);
		                    movingL = true; // 방향을 바꿉니다.
		                }
		            }
		            
		            if (temp.getId() == ObjectId.EnemyN) {
		            	if(temp instanceof EnemyN) {
		            		if(handler.getEnemyN_Touch_enemyG()) { // enemyN_Touch_enemyG가 true이면
			                	if (getBounds().intersects(temp.getBounds())) {
			                        setY(temp.getY() - getHeight());
			                        setDeadByStar(true);
			                        isDead = true;
			                    }
			
			                    if (getBoundsTop().intersects(temp.getBounds())) {
			                        setY(temp.getY() + temp.getHeight());
			                        setDeadByStar(true);
			                        isDead = true;
			                    }
			
			                    if (getBoundsRight().intersects(temp.getBounds())) {
			                        setX(temp.getX() - getWidth() - 1);
			                        setDeadByStar(true);
			                        isDead = true;
			                    }
			
			                    if (getBoundsLeft().intersects(temp.getBounds())) {
			                        setX(temp.getX() + temp.getWidth() + 1);
			                        setDeadByStar(true);
			                        isDead = true;
			                    }
		            		} else {
		            			if (getBounds().intersects(temp.getBounds())) {
			                        setY(temp.getY() - getHeight());
			                        setVelY(0);
			                    }
			
			                    if (getBoundsTop().intersects(temp.getBounds())) {
			                        setY(temp.getY() + temp.getHeight());
			                        setVelY(0);
			                    }
			
			                    if (getBoundsRight().intersects(temp.getBounds())) {
			                        setX(temp.getX() - getWidth() - 1);
			                        movingL = false;
			                    }
			
			                    if (getBoundsLeft().intersects(temp.getBounds())) {
			                        setX(temp.getX() + temp.getWidth() + 1);
			                        movingL = true;
			                    }
		            		}
		                }
		            }
		            
		            if(temp.getId() == ObjectId.FireBall) {
		            	if(getBoundsLeft().intersects(temp.getBounds())){
		            		handler.setCheckToFire(true);
		            		soundManager.playKick();
		            		isDead = true;
		            		deadByStar = true;
		                    handler.removeObj(temp);
//		                    handler.removeObj(this);
		                    System.out.println("G에 맞았다!");
		            	}
		            	if(getBoundsRight().intersects(temp.getBounds())){
		            		handler.setCheckToFire(true);
		            		soundManager.playKick();
		            		isDead = true;
		            		deadByStar = true;
		                    handler.removeObj(temp);
//		                    handler.removeObj(this);
		                    System.out.println("G에 맞았다!");
		            	}
		            	if(getBoundsTop().intersects(temp.getBounds())){
		            		handler.setCheckToFire(true);
		            		soundManager.playKick();
		            		isDead = true;
		            		deadByStar = true;
		                    handler.removeObj(temp);
//		                    handler.removeObj(this);
		                    System.out.println("G에 맞았다!");
		            	}
		            	if(getBounds().intersects(temp.getBounds())){
		            		handler.setCheckToFire(true);
		            		soundManager.playKick();
		            		isDead = true;
		            		deadByStar = true;
		                    handler.removeObj(temp);
//		                    handler.removeObj(this);
		                    System.out.println("G에 맞았다!");
		            	}
		            }
            }
        }
    }

    @Override
    public Rectangle getBounds() {
    	return new Rectangle((int)(getX() + getWidth() / 2 - getWidth() / 4), 
                (int)(getY() + getHeight() / 2 + 10), 
                (int)(getWidth() / 2), 
                (int)getHeight() / 4);
    }

    public Rectangle getBoundsTop() {
    	return new Rectangle((int)(getX() + 5), 
                (int)getY(), 
                (int)(getWidth() - 10), 
                (int)(getHeight() / 4));
    }

    public Rectangle getBoundsRight() {
            return new Rectangle((int)(getX() + getWidth() - 5), 
                    (int)(getY() + 13), 
                    5, 
                    (int)(getHeight() - 20));
    }

    public Rectangle getBoundsLeft() {
    	return new Rectangle((int) getX(),
                (int)(getY() + 13),
                5,
                (int)(getHeight() - 20));
    }
    
    public void showBounds(Graphics g){
    	Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.RED);
        g2d.draw(getBounds());
        g2d.draw(getBoundsRight());
        g2d.draw(getBoundsLeft());
        g2d.draw(getBoundsTop());
    }
    
    // 몹 사망 관련
    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }

    public boolean getDead() {
        return isDead;
    }
    
    public void hitByPlayer() {
        // 적이 플레이어에 의해 밟힐 때 호출되는 메서드
        setVelY(+40); // 플레이어를 밟으면 약간 띄워줍니다.
        setDead(true); // 적을 죽인다는 플래그를 설정합니다.
        player.hasJumped();
    }
    
    public void destroy() {
    	Game game = Game.getGameInstance();
    	// Enemy를 죽은 상태로 설정
    	setDead(true);
	    // Handler에서 Enemy를 제거하도록 요청
	    handler.removeObj(this);
	    game.addScore(100);
    }
    
    public void killEnemyG() {
        if (handler.getPlayer().getBounds().intersects(getBoundsTop())) {
        	handler.getPlayer().hitEnemy();
        	
            soundManager.jumpToEnemy();
            isDead = true;
            checkDead = true;
            deadByJump = true;
            
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    destroy();
                }
            }, 800); // 1000ms = 1초
        }
    }
    
    // 몹 이동 관련
    public void moveG() {

        setY(getY() + getVelY());
        
        if (isVisible) {
            if(deadByStar) {
				if (deadSideRight) {
	                setVelY(-10);
	            } else {
	                setVelY(-10);
	            }
            } else {
                // 플레이어가 적의 오른쪽에 위치할 때 이동
                if (movingL) {
                    setX(getX() + speed); // 오른쪽으로 이동
                } else {
                    setX(getX() - speed); // 왼쪽으로 이동
                }
            }
        }
    }
    
    public void checkDirectionRight() {
        final int playerX = (int) handler.getPlayer().getX();
		final int enemyN_X = (int) this.getX();
		// 플레이어와 적의 X 좌표 차이를 계산하여 kickSide에 저장합니다.
		final int kickSide = playerX - enemyN_X;
		
		if(kickSide > 0) {
			deadSideRight = false;
		} else {
			deadSideRight = true;
		}
    }
    
    public boolean getMovingL() {
    	return movingL;
    }
    
    public void setMovingL(boolean movingL) {
    	this.movingL = movingL;
    }
    
    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
    
    public void starDead() {
    	if(deadByStar && soundCheck) {
			soundManager.playKick();
			soundCheck = false;
			isDead = true;
    	}
    }
    
    public boolean getDeadByStar() {
    	return deadByStar;
    }
    
    public void setDeadByStar(boolean deadByStar) {
    	this.deadByStar = deadByStar;
    }
}