package com.game.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import com.game.gfx.Animation;
import com.game.gfx.SoundManager;
import com.game.gfx.Texture;
import com.game.main.Game;
import com.game.object.util.Handler;
import com.game.object.util.ObjectId;

public class EnemyN extends GameObject {

	private float speed = 1.5f; 
	
	private Player player;
	
    private static final float WIDTH = 16;
    private static final float HEIGHT = 16;

    private Handler handler;
    private Texture tex;
    private BufferedImage[] spriteN;
    private Animation enemyWalkN;
    private Animation currAnimation;
    private LinkedList<Block> removeBlocks;

    private boolean forward = false; // 이미지 좌우 판정
    private boolean isShell = false; // 껍질상태 확인
    private boolean canKick = false; // 껍질 차기 가능
    private boolean kickMove = false; // 플레이어가 차면 true
    private boolean recovery = false; // 껍질에서 회복
    private int kickCount = 3;
    
    private Timer recoveryTimer; // 회복 타이머
    private TimerTask recoveryTask; // 회복 타이머 작업

    private boolean movingL = true;
    private boolean movingR = false;
    private boolean sideCheckRight = false;
    
    private boolean wasKickCountTwo = false;
    
    private boolean isVisible = false;
    
    private boolean isAlive_N = true; // 불 맞았을때 죽는 판정
    private boolean isDead = false;
    private boolean deadByStar = false;
    private boolean soundCheck = true;
    
    private SoundManager soundManager;

    public EnemyN(int x, int y, int scale, int layer, Handler handler, SoundManager soundManager) {
        super(x, y, ObjectId.EnemyN, WIDTH, HEIGHT, scale, layer);
        this.handler = handler;
        tex = Game.getTexture();
        removeBlocks = new LinkedList<Block>();
        
        this.player = handler.getPlayer();
        this.soundManager = soundManager;
        
        spriteN = tex.getEnemyN();
        
        // enemyWalkN 초기화
        enemyWalkN = new Animation(5, spriteN[0], spriteN[1]);
        
        // animation 설정
        currAnimation = enemyWalkN;
    }

    @Override
    public void tick() {
//    	System.out.println("isShell : " + isShell+" / cnaKick : "+ canKick +" / kickMove : "+ kickMove + " / recovery : " + recovery);
//    	System.out.println("isAlive_N : "+isAlive_N+" / deadByStar : "+deadByStar+ " / soundCheck : "+soundCheck);
//    	System.out.println("movingL : "+movingL);

        currAnimation.runAnimation();
        applyGravity(0.5f);
    	
    	if(isAlive_N) {
	    	moveN();
	    	setY(getY()+getVelY());
	        jumpToEnemyN();
	        enemyN_State();
	        collision();
	        starDead();
	        
	        if (kickCount == 1 && wasKickCountTwo) {
	            checkDirectionRight();
	            wasKickCountTwo = false; // 플래그를 초기화하여 이후의 호출을 방지합니다.
	        }
	        
	        // kickCount가 2로 변할 때 플래그를 설정합니다.
	        if (kickCount == 2) {
	            wasKickCountTwo = true;
	        }
	        
    	} else if(deadByStar) {
    		setY(getY() + getVelY());
    	}
    }

    @Override
    public void render(Graphics g) {
//        showBounds(g);
    	
        if (isAlive_N) {
            if (isShell) {
                // 껍질 상태일 때 껍질 스프라이트를 그립니다.
                g.drawImage(spriteN[2], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
            } else {
                if (movingL) {
                    currAnimation.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
                    forward = true;
                } else if (!movingL) {
                	currAnimation.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight());
                    forward = false;
                }
            }
        } else {
            if(isShell) {
            	g.drawImage(spriteN[2], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
            } else {
            	if(sideCheckRight) {
            		g.drawImage(spriteN[0], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
            	} else {
            		g.drawImage(spriteN[0], (int) (getX() + getWidth()), (int) getY(), (int) -getWidth(), (int) getHeight(), null);
            	}
            }
        }
    }
    
    private void collision() {
        for (int i = 0; i < handler.getGameObjs().size(); i++) {
            GameObject temp = handler.getGameObjs().get(i);
            
            if(isAlive_N) {
            	if (removeBlocks.contains(temp)) continue;
            	
            	if(kickCount == 1) {
            		if (temp.getId() == ObjectId.Block) {
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
                        }

                        if (getBoundsLeft().intersects(temp.getBounds())) {
                            setX(temp.getX() + temp.getWidth() + 1);
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
                                movingL = false;
                                sideCheckRight = false;
                                soundManager.playKick();	
                            }

                            if (getBoundsLeft().intersects(temp.getBounds())) {
                                setX(temp.getX() + temp.getWidth() + 1);
                                movingL = true;
                                sideCheckRight = false;
                                soundManager.playKick();	
                            }
                        }
                    
                    if(temp.getId() == ObjectId.EnemyG) {
//                    	if (getBounds().intersects(temp.getBounds())) {
//                            setY(temp.getY() - getHeight());
//                            setVelY(0);
//                        }
//
//                        if (getBoundsTop().intersects(temp.getBounds())) {
//                            setY(temp.getY() + temp.getHeight());
//                            setVelY(0);
//                        }
                        
                        if (handler.getLeftToRight()) {
                        	movingL = false;
                            handler.setEnemyN_Touch_enemyG(true);
                        }
                        
                        if (handler.getRightToLeft()) {
                        	movingL = true;
                        	handler.setEnemyN_Touch_enemyG(true);
                        }
                        
//                        if (getBoundsRight().intersects(temp.getBounds())) {
//                            setX(temp.getX() - getWidth() - 1);
//                        	movingL = true;
//                        	handler.setEnemyN_Touch_enemyG(true);
//                            soundManager.playKick();
//                        }
//
//                        if (getBoundsLeft().intersects(temp.getBounds())) {
//                            setX(temp.getX() + temp.getWidth() + 1);
//                            movingL = false;
//                            handler.setEnemyN_Touch_enemyG(true);
//                            soundManager.playKick();
//                        }
                    }if(temp.getId() == ObjectId.FireBall) {
		            	if(getBoundsLeft().intersects(temp.getBounds())){
		            		handler.setCheckToFire(true);
		            		soundManager.playKick();
		            		isAlive_N = false;
		            		deadByStar = true;
		                    handler.removeObj(temp);
//		                    handler.removeObj(this);
		                    System.out.println("N에 맞았다!");
		            	}
		            	if(getBoundsRight().intersects(temp.getBounds())){
		            		handler.setCheckToFire(true);
		            		soundManager.playKick();
		            		isAlive_N = false;
		            		deadByStar = true;
		                    handler.removeObj(temp);
//		                    handler.removeObj(this);
		                    System.out.println("N에 맞았다!");
		            	}
		            	if(getBoundsTop().intersects(temp.getBounds())){
		            		handler.setCheckToFire(true);
		            		soundManager.playKick();
		            		isAlive_N = false;
		            		deadByStar = true;
		                    handler.removeObj(temp);
//		                    handler.removeObj(this);
		                    System.out.println("N에 맞았다!");
		            	}
		            	if(getBounds().intersects(temp.getBounds())){
		            		handler.setCheckToFire(true);
		            		soundManager.playKick();
		            		isAlive_N = false;
		            		deadByStar = true;
		                    handler.removeObj(temp);
//		                    handler.removeObj(this);
		                    System.out.println("N에 맞았다!");
		            	}
		            } else {
                    	continue;
                    }
            	} else if(kickCount == 2) {
            		if (temp.getId() == ObjectId.Block) {
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
                        }

                        if (getBoundsLeft().intersects(temp.getBounds())) {
                            setX(temp.getX() + temp.getWidth() + 1);
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
                                movingL = false;
                                sideCheckRight = false;
                            }

                            if (getBoundsLeft().intersects(temp.getBounds())) {
                                setX(temp.getX() + temp.getWidth() + 1);
                                movingL = true;
                                sideCheckRight = false;
                            }
                        }
                    
                    if(temp.getId() == ObjectId.EnemyG) {
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
                        }

                        if (getBoundsLeft().intersects(temp.getBounds())) {
                            setX(temp.getX() + temp.getWidth() + 1);
                        }
                    }
                    
                    if(temp.getId() == ObjectId.FireBall) {
		            	if(getBoundsLeft().intersects(temp.getBounds())){
		            		handler.setCheckToFire(true);
		            		soundManager.playKick();
		            		isAlive_N = false;
		            		deadByStar = true;
		                    handler.removeObj(temp);
//		                    handler.removeObj(this);
		                    System.out.println("N에 맞았다!");
		            	}
		            	if(getBoundsRight().intersects(temp.getBounds())){
		            		handler.setCheckToFire(true);
		            		soundManager.playKick();
		            		isAlive_N = false;
		            		deadByStar = true;
		                    handler.removeObj(temp);
//		                    handler.removeObj(this);
		                    System.out.println("N에 맞았다!");
		            	}
		            	if(getBoundsTop().intersects(temp.getBounds())){
		            		handler.setCheckToFire(true);
		            		soundManager.playKick();
		            		isAlive_N = false;
		            		deadByStar = true;
		                    handler.removeObj(temp);
//		                    handler.removeObj(this);
		                    System.out.println("N에 맞았다!");
		            	}
		            	if(getBounds().intersects(temp.getBounds())){
		            		handler.setCheckToFire(true);
		            		soundManager.playKick();
		            		isAlive_N = false;
		            		deadByStar = true;
		                    handler.removeObj(temp);
//		                    handler.removeObj(this);
		                    System.out.println("N에 맞았다!");
		            	}
		            } 
            	} else if(kickCount ==3) {
            		if (temp.getId() == ObjectId.Block) {
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
                        }

                        if (getBoundsLeft().intersects(temp.getBounds())) {
                            setX(temp.getX() + temp.getWidth() + 1);
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
                                movingL = false;
                                sideCheckRight = false;
                                soundManager.playKick();	
                            }

                            if (getBoundsLeft().intersects(temp.getBounds())) {
                                setX(temp.getX() + temp.getWidth() + 1);
                                movingL = true;
                                sideCheckRight = false;
                                soundManager.playKick();	
                            }
                        }
                    
                    if(temp.getId() == ObjectId.EnemyG) {
                    	if (getBounds().intersects(temp.getBounds())) {
                            setY(temp.getY() - getHeight());
                            setVelY(0);
                        }

                        if (getBoundsTop().intersects(temp.getBounds())) {
                            setY(temp.getY() + temp.getHeight());
                            setVelY(0);
                        }
                        
                        if (handler.getLeftToRight()) {
                            movingL = false;
//                            System.out.println("접촉");
                        }
                        
                        if (handler.getRightToLeft()) {
                        	movingL = true;
//                            System.out.println("접촉");
                        }

//                        if (getBoundsRight().intersects(temp.getBounds())) {
//                            setX(temp.getX() - getWidth() - 1);
//                        	movingL = true;
//                            System.out.println("접촉");
//                        }
//
//                        if (getBoundsLeft().intersects(temp.getBounds())) {
//                            setX(temp.getX() + temp.getWidth() + 1);
//                            movingL = false;
//                            System.out.println("접촉");
//                        }
                    }
                    
                    if(temp.getId() == ObjectId.FireBall) {
		            	if(getBoundsLeft().intersects(temp.getBounds())){
		            		handler.setCheckToFire(true);
		            		soundManager.playKick();
		            		isAlive_N = false;
		            		deadByStar = true;
		                    handler.removeObj(temp);
//		                    handler.removeObj(this);
		                    System.out.println("N에 맞았다!");
		            	}
		            	if(getBoundsRight().intersects(temp.getBounds())){
		            		handler.setCheckToFire(true);
		            		soundManager.playKick();
		            		isAlive_N = false;
		            		deadByStar = true;
		                    handler.removeObj(temp);
//		                    handler.removeObj(this);
		                    System.out.println("N에 맞았다!");
		            	}
		            	if(getBoundsTop().intersects(temp.getBounds())){
		            		handler.setCheckToFire(true);
		            		soundManager.playKick();
		            		isAlive_N = false;
		            		deadByStar = true;
		                    handler.removeObj(temp);
//		                    handler.removeObj(this);
		                    System.out.println("N에 맞았다!");
		            	}
		            	if(getBounds().intersects(temp.getBounds())){
		            		handler.setCheckToFire(true);
		            		soundManager.playKick();
		            		isAlive_N = false;
		            		deadByStar = true;
		                    handler.removeObj(temp);
//		                    handler.removeObj(this);
		                    System.out.println("N에 맞았다!");
		            	}
		            } 
            	}
            } else {
            	continue;
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

    public LinkedList<Block> getAndResetRemoveBlock() {
        LinkedList<Block> output = new LinkedList<Block>();
        for (Block removeBlock : removeBlocks) {
            if (!removeBlock.shouldRemove()) continue;
            output.add(removeBlock);
        }
        for (Block removeBlock : output) {
            removeBlocks.remove(removeBlock);
        }
        return output;
    }
    
    public void showBounds(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.RED);
        g2d.draw(getBounds());
        g2d.draw(getBoundsRight());
        g2d.draw(getBoundsLeft());
        g2d.draw(getBoundsTop());
    }

	public boolean kickEnemy() {
		return canKick;
	}
	
	public void kickEnemy(boolean kickEnemy) {
		this.canKick = kickEnemy;
	}
	
	public boolean isShell() {
		return isShell;
	}
	
	public void getShell(boolean shell) {
		this.isShell = shell;
	}
	
	public void stopMoving() {
		kickMove = false;
	}
	
	public void moveN() {

        setY(getY() + getVelY());
        
		if (isVisible) { // 카메라에 보일 때
			if(deadByStar) { // 별 맞고 죽을떄
				setVelY(-8);
			} else {
				
				if (kickCount == 3) {
					
					speed = 1.5f;
	            
		            if (movingL) {
		                setX(getX() - speed); // 오른쪽으로 이동
		            } else {
		                setX(getX() + speed); // 왼쪽으로 이동
	            	}
	            
		        } else if (kickCount == 2) {
		            speed = 0;
		            // kickCount가 2일 때는 이동하지 않음
		            
		        } else if (kickCount == 1) {
		            speed = 1.5f;
		            if (sideCheckRight) {
		                setX(getX() + speed * 5); // 오른쪽으로 이동
		            } else {
		                setX(getX() - speed * 5); // 왼쪽으로 이동
		            }
		            
		        }
			}
	    }
    }

    public void jumpToEnemyN() {
	    if (player.getBounds().intersects(getBoundsTop())) {
	        player.hitEnemy(); // 플레이어가 적을 밟은 경우
	        
	        if(kickCount==3) {
	        	kickCount--;
	        	recovery();
	        	soundManager.playKick();
	        } else if(kickCount==2) {
	        	kickCount--;
	        	soundManager.playKick();
	        } else if(kickCount==1) {
	        	kickCount++;
	        	soundManager.playKick();
	        }
	    }
    }
    
    public void recovery() {
    	// 기존 타이머가 있는 경우 취소
        cancelRecovery();

        // 새 타이머와 타이머 작업을 생성
        recoveryTimer = new Timer();
        recoveryTask = new TimerTask() {
            @Override
            public void run() {
                if (kickCount == 2) { // 회복 상태가 유지되고 있을 때만 kickCount를 3으로 설정
                    kickCount = 3;
                }
            }
        };

        // 타이머 작업을 2초 후에 실행
        recoveryTimer.schedule(recoveryTask, 2000);
    }
    
    private void cancelRecovery() {
        if (recoveryTask != null) {
            recoveryTask.cancel(); // 타이머 작업 취소
            recoveryTask = null; // 타이머 작업 참조 해제
        }
        if (recoveryTimer != null) {
            recoveryTimer.cancel(); // 타이머 취소
            recoveryTimer = null; // 타이머 참조 해제
        }
    }
    
    public void enemyN_State() {
    	if(kickCount==3) {
    		isShell=false;
    		canKick=false;
    		kickMove=false;
    		recovery = false;
    	} else if(kickCount==2) {
    		isShell=true;
    		canKick=true;
    		kickMove=false;
    		recovery=true;
    	} else if(kickCount==1) {
    		isShell=true;
    		canKick=false;
    		kickMove=true;
    		recovery=false;
    	}
    }
    
    public void starDead() {
    	if(deadByStar && soundCheck) {
			soundManager.playKick();
			soundCheck = false;
			isAlive_N = false;
    	}
    }
    
    public void checkDirectionRight() {
    	final int playerX = (int) player.getX();
		final int enemyN_X = (int) this.getX();
		// 플레이어와 적의 X 좌표 차이를 계산하여 kickSide에 저장합니다.
		final int kickSide = playerX - enemyN_X;
		
		if(kickSide > 0) {
			sideCheckRight = false;
		} else {
			sideCheckRight = true;
		}
		
    }
    
    public boolean getAlive_N() {
    	return isAlive_N;
    }
    
    public void setAlive_N() {
    	this.isAlive_N = isAlive_N;
    }
    
    public boolean getMovingL() {
    	return movingL;
    }
    
    public void setMovingL(boolean movingL) {
    	this.movingL = movingL;
    }
    
    public boolean getIsShell() {
    	return isShell;
    }
    
    public void setIsShell(boolean isShell) {
    	this.isShell = isShell;
    }
    
    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
    
    public int getKickCount() {
    	return kickCount;
    }
    
    public void setKickCount(int kickCount) {
    	this.kickCount = kickCount;
    }
    
    public void hitByFireBall() {
    	
    }
    
    public boolean getDeadByStar() {
    	return deadByStar;
    }
    
    public void setDeadByStar(boolean deadByStar) {
    	this.deadByStar = deadByStar;
    }
}