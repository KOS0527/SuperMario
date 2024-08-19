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
import com.game.object.util.KeyInput;
import com.game.object.util.ObjectId;
import com.game.object.util.UnitState;

public class Player extends GameObject {
    private static final float WIDTH = 16;
    private static final float HEIGHT = 16;

    private Handler handler;
    private Texture tex;
    private BufferedImage[] spriteL, spriteS, spriteF;
    private BufferedImage[] currSpriteS, currSpriteL, currSpriteF;
    private BufferedImage[] sprite_sL_1, sprite_sL_2, sprite_sL_3;
    private BufferedImage[] sprite_sS_1, sprite_sS_2, sprite_sS_3;
    private BufferedImage[] currSpriteSL, currSpriteSS;
    private Animation playerWalkL, playerWalkS, playerWalkF;
    private Animation currAnimationS, currAnimationL, currAnimationF;
    private Animation currAnimationInviStand, currAnimationInviWalk, currAnimationInviJump;
    private Animation currAnimationStarL, currAnimationStarS;
    
    private Animation playerWalkStarL, playerJumpStarL, playerStandStarL, playerCrouchStarL, playerShootStarL;
    private Animation playerWalkStarS, playerJumpStarS, playerStandStarS;
    private Animation playerInvisibleTimeStand, playerInvisibleTimeWalk, playerInvisibleTimeJump;
    private Animation currAnimationStarStandL, currAnimationStarJumpL, currAnimationStarCrouch;
    private Animation currAnimationStarStandS, currAnimationStarJumpS;
    
    private LinkedList<Block> removeBlocks;
    
    private UnitState state;

    private int health = 1;
    
    private boolean crouching = false;
    
    private boolean touchBlock = false;
    
    private boolean jumped = false;
    
    private boolean starTime = false;
    
    private boolean invincibilityTime = false; // 피격시 1초 무적시간
    private boolean isVisible = true; // 가시성 상태
    private TimerTask visibilityTask;
    
    private boolean stopMotion = false;
    private boolean getItem = false;
    private boolean hasJumpedSoundPlayed = false;
    private boolean canJump = true;
    private boolean forward = true;
    private boolean isDead = false;
    private boolean isJumping = false; // 플레이어가 점프 중인지 여부를 나타내는 변수
    private float jumpHeight = 100; // 플레이어가 점프할 높이
    private float jumpSpeed = 5; // 플레이어의 점프 속도
    private float startY; // 캐릭터의 초기 Y 좌표를 저장하기 위한 변수
    private float minY; // 플레이어가 이동 가능한 Y 최소값
    
    // 파이프 이용
    private boolean pipeIn = false;
    private boolean usePipe = false;
    
    // 불꽃 발사 관련
    private boolean readyFireBall = false;
    private int fireBallCount = 3; // 최대 3번발사
    private boolean shooting;
    private boolean fireBallRight = false;
    private boolean fireBallSound = false;
    private boolean isFiring = false;
	private boolean canFired = false;
    
    // 깃발 관련
    private boolean touchFlag = false;
    private boolean imageReversal = false;
    private boolean checkMove = false;
    private boolean walkGoal = false;
    private boolean endFlag = false;
    
    // 마리오 이동 관련
    private float baseSpeed = 0.0f; // 기본 속도는 0으로 설정
    private float maxSpeed = 7.0f; // 최대 속도
    private boolean accelerate = false;
    private float deceleration = 0.1f; // 감속도
    private float acceleration = 0.1f; // 가속도
    private boolean acceleratingRight = false; // 오른쪽 가속 상태
    private boolean acceleratingLeft = false; // 왼쪽 가속 상태
    private boolean moving = false; // 현재 이동 상태
    private boolean atMaxSpeed = false;
    private boolean facingRight = true;  // 현재 방향
    private boolean directionChanged = false;  // 방향 변경 여부

    
    private Game game;
    private SoundManager sound;
    
    public Player(float x, float y, int scale, int layer, Handler handler, Game game) {
        super(x, y, ObjectId.Player, WIDTH, HEIGHT, scale, layer);
        this.handler = handler;
        tex = Game.getTexture();
        removeBlocks = new LinkedList<Block>();
        
        this.game = game;
        this.sound = SoundManager.getInstance();
        
        // 마리오 작은, 큰, 꽃
        spriteL = tex.getMarioL();
        spriteS = tex.getMarioS();
        spriteF = tex.getMarioF();
        
        // 별 효과
        sprite_sL_1 = tex.getMario_sL_1();
        sprite_sL_2 = tex.getMario_sL_2();
        sprite_sL_3 = tex.getMario_sL_3();
        sprite_sS_1 = tex.getMario_sS_1();
        sprite_sS_2 = tex.getMario_sS_2();
        sprite_sS_3 = tex.getMario_sS_3();
        
        playerWalkL = new Animation(5, spriteL[1], spriteL[2], spriteL[3]);
        playerWalkS = new Animation(5, spriteS[1], spriteS[2], spriteS[3]);
        playerWalkF = new Animation(5, spriteF[1], spriteF[2], spriteF[3]);
        
        playerWalkStarL = new Animation(2, sprite_sL_1[1], sprite_sL_2[2], sprite_sL_3[3]);
        playerJumpStarL = new Animation(2, sprite_sL_1[5], sprite_sL_2[5], sprite_sL_3[5]);
        playerStandStarL = new Animation(2, sprite_sL_1[0], sprite_sL_2[0], sprite_sL_3[0]);
        playerCrouchStarL = new Animation(2, sprite_sL_1[6], sprite_sL_2[6], sprite_sL_3[6]);
        playerShootStarL = new Animation(2, sprite_sL_1[1], sprite_sL_2[1], sprite_sL_3[1]);
        
        playerWalkStarS = new Animation(2, sprite_sS_1[1], sprite_sS_2[2], sprite_sS_3[3]);
        playerJumpStarS = new Animation(2, sprite_sS_1[5], sprite_sS_2[5], sprite_sS_3[5]);
        playerStandStarS = new Animation(2, sprite_sS_1[0], sprite_sS_2[0], sprite_sS_3[0]);
        
        playerInvisibleTimeStand = new Animation(5, spriteS[14], spriteS[0]);
        playerInvisibleTimeWalk = new Animation(5, spriteS[1], spriteS[14], spriteS[2], spriteS[14], spriteS[3], spriteS[14]);
        playerInvisibleTimeJump = new Animation(5, spriteS[14], spriteS[5]);
        		
        state = UnitState.Mario_Small;
        currSpriteS = spriteS;
        currSpriteL = spriteL;
        currSpriteF = spriteF;
        currAnimationS = playerWalkS;
        currAnimationL = playerWalkL;
        currAnimationF = playerWalkF;
        
        currAnimationInviStand = playerInvisibleTimeStand;
        currAnimationInviWalk = playerInvisibleTimeWalk;
        currAnimationInviJump = playerInvisibleTimeJump;
        
        currAnimationStarL = playerWalkStarL;
        currAnimationStarStandL = playerStandStarL;
        currAnimationStarJumpL = playerJumpStarL;
        currAnimationStarCrouch = playerCrouchStarL;
        currAnimationStarS = playerWalkStarS;
        currAnimationStarStandS = playerStandStarS;
        currAnimationStarJumpS = playerJumpStarS;
        
        
        KeyInput keyInput = handler.getKeyInput();
        
        if (handler.getKeyInput() == null) {
            handler.setKeyInput(new KeyInput(handler));
        }
        
        if (keyInput == null) {
            keyInput = new KeyInput(handler);
            handler.setKeyInput(keyInput);
        }
        
//        collision();
        
        minY = 0;
        startY = y;        
    }


    @Override
    public void tick() {
    	if (acceleratingRight) {
            setVelX(Math.min(getVelX() + acceleration, maxSpeed));
            if (!facingRight) {
                // 방향 전환 감지
                facingRight = true;
                directionChanged = true;
                stopMotion = false;  // 방향 전환 시 stopMotion 비활성화
            }
        } else if (acceleratingLeft) {
            setVelX(Math.max(getVelX() - acceleration, -maxSpeed));
            if (facingRight) {
                // 방향 전환 감지
                facingRight = false;
                directionChanged = true;
                stopMotion = false;  // 방향 전환 시 stopMotion 비활성화
            }
        } else if (moving) {
            // 키를 떼었을 때 점진적으로 감속
            if (getVelX() > 0) {
                setVelX(Math.max(getVelX() - acceleration, baseSpeed));
            } else if (getVelX() < 0) {
                setVelX(Math.min(getVelX() + acceleration, baseSpeed));
            }
        } else {
            if (getVelX() > 0) {
                setVelX(Math.max(getVelX() - acceleration, 0.0f));
                stopMotion = true;
            } else if (getVelX() < 0) {
                setVelX(Math.min(getVelX() + acceleration, 0.0f));
                stopMotion = true;
            }
        }
    	
    	moveMario();
	    	
    	if(isDead) {
        	handleDeathAnimation();
        	return;
        } else {
        	
        	// 중력 & 가속
	    	if(touchFlag) {
		        applyGravity(0.0000000000000001f);
	    	} else {
		        applyGravity(0.5f);	
	    	}
    		collision();
    		invincibilityTime();
//        	if (!invincibilityTime) {
//        	}
    		usePipe();
        }
    	
//    	System.out.println("pipeIn : "+pipeIn);
	        
	        
        // 마리오 상태 체크(small && large)
        marioState();
	        
        marioAnimation();
        currAnimationS.runAnimation();
        currAnimationL.runAnimation();
        currAnimationF.runAnimation();
        currAnimationStarL.runAnimation();
        currAnimationStarStandL.runAnimation();
        currAnimationStarJumpL.runAnimation();
        currAnimationStarCrouch.runAnimation();
        currAnimationStarS.runAnimation();
        currAnimationStarStandS .runAnimation();
        currAnimationStarJumpS.runAnimation();
        
        if (hasJumped() && !hasJumpedSoundPlayed && getMarioState()==UnitState.Mario_Small) {
            sound.playJumpSound();
            hasJumpedSoundPlayed = true; // 점프 사운드가 한 번 재생되었음을 표시
        }else if(hasJumped() && !hasJumpedSoundPlayed && getMarioState()==UnitState.Mario_Large
        		|| hasJumped() && !hasJumpedSoundPlayed && getMarioState()==UnitState.Mario_Flower) {
        	sound.bigMarioJump();
            hasJumpedSoundPlayed = true; // 점프 사운드가 한 번 재생되었음을 표시
        }
        
        if (!jumped && !isJumping && !getItem) {
            canJump = true;
        }
        
        if (isFiring && shooting) {
        	canFired = true;
        }
        
        if (getY() > Game.getScreenHeight()) {
            handleGameOver();
            handler.removeObj(this);
        }
        
//        System.out.println("shooting : "+shooting+ " / isFiring " + isFiring + " / canFired " + canFired);
        
//        shootFireBall();
//        System.out.println("shooting : "+this.getX()+ " / jumped" + this.getY());
    }

    @Override
    public void render(Graphics g) {
//    	showBounds(g);
    	
    	// 별 상태 일때
    	if(starTime) {
    		if(health == 1) {
    			if(jumped) {
	        		if(forward) {
	        			currAnimationStarJumpS.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
	        		}else {
	        			currAnimationStarJumpS.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight());
	        		}
	        	} else if(getVelX() > 0){
	        		playerWalkStarS.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
	        		forward = true;
	        	} else if(getVelX() < 0){
	        		playerWalkStarS.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight());
	        		forward = false;
	        	} else {
	        	    if (forward) {
	        	    	playerStandStarS.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
	        	    } else {
	        	    	playerStandStarS.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight());
	        	    }
	        	}
    		} else if(health == 2) {
    			if(jumped) {
	        		if(forward) {
	        			playerJumpStarL.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2);
	        		}else {
	        			playerJumpStarL.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2);
	        		}
	        	}else if(getVelX() > 0){
	        		playerWalkStarL.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2);
	        		forward = true;
	        	}else if(getVelX() < 0){
	        		playerWalkStarL.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2);
	        		forward = false;
	        	}else if(crouching) {
	        		if(forward) {
	        			playerCrouchStarL.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2);
	        		}else {
	        			playerCrouchStarL.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2);
	        		}
	        	} else {
	        	    if (forward) {
	        	    	playerStandStarL.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2);
	        	    } else {
	        	    	playerStandStarL.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2);
	        	    }
	        	}
    		} else if(health == 3) {
    			if(jumped) {
	        		if(forward) {
	        			if(shooting) {
	        				playerShootStarL.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2);
	        			} else {
	        				playerJumpStarL.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2);
	        			}
	        		}else {
	        			if(shooting) {
	        				playerShootStarL.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2);
	        			} else {
	        				playerJumpStarL.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2);
	        			}
	        		}
	        	}else if(getVelX() > 0){
	        		if(shooting) {
	        			playerShootStarL.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2);
	        		} else{
	        			playerWalkStarL.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2);
	            		forward = true;
	        		}
	        	}else if(getVelX() < 0){
	        		if(shooting) {
	        			playerShootStarL.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2);
	        		} else {
	        			playerWalkStarL.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2);
		        		forward = false;
	        		}
	        	}else if(crouching) {
	        		if(forward) {
	        			playerCrouchStarL.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2);
	        		}else {
	        			playerCrouchStarL.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2);
	        		}
	        	}else if (shooting){
	        		if(forward) {
	        			playerShootStarL.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2);
	        		}else {
	        			playerShootStarL.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2);
	        		}
	        	}else {
	        		if (forward) {
	        	    	playerStandStarL.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2);
	        	    } else {
	        	    	playerStandStarL.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2);
	        	    }
	        	}
    		}
    		
    		// 적한테 맞고 무적상태
//    	} else if(invincibilityTime) {
//    		if(health == 1) {
//    			if(jumped) {
//	        		if(forward) {
//	        			currAnimationInviJump.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
//	        		}else {
//	        			currAnimationInviJump.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight());
//	        		}
//	        	} else if(getVelX() > 0){
//	        		currAnimationInviWalk.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
//	        		forward = true;
//	        	} else if(getVelX() < 0){
//	        		currAnimationInviWalk.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight());
//	        		forward = false;
//	        	} else {
//	        	    if (forward) {
//	        	    	currAnimationInviStand.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
//	        	    } else {
//	        	    	currAnimationInviStand.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight());
//	        	    }
//	        	}
//    		}
    	} else { // 별 아닐때
	        if(health == 1){
	        	if (isDead) {
	                g.drawImage(currSpriteS[6], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
	                return;
	            }
	        	
	        	if(walkGoal) {
	        		currAnimationS.drawAnimation(g, (int)(getX() + 5), (int)getY(), (int) getWidth(), (int)getHeight());
	        		return;
	        	}
	        	
	        	if (touchFlag) {
	                if (imageReversal) {
	                    // 이미지 반전
	                    g.drawImage(currSpriteS[8], (int) (getX() + getWidth()*2 - getWidth() / 2), (int) getY(), (int) -getWidth(), (int) getHeight(), null);
	                    return;
	                } else {
	                    g.drawImage(currSpriteS[8], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight(), null);
	                    return;
	                }
	            }
	        	if (invincibilityTime && isVisible) {
	        	    // 무적 상태이고 현재 보이는 경우
	        	    if (forward) {
	        	        g.drawImage(currSpriteL[15], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
	        	    } else {
	        	        g.drawImage(currSpriteL[15], (int)(getX() + getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight(), null);
	        	    }
	        	} else if (jumped) {
	        	    // 점프 상태인 경우
	        	    if (forward) {
	        	        g.drawImage(currSpriteS[5], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
	        	    } else {
	        	        g.drawImage(currSpriteS[5], (int)(getX() + getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight(), null);
	        	    }
	        	} else if (getVelX() > 0) {
	        	    // 오른쪽으로 이동 중인 경우
	        	    currAnimationS.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
	        	    forward = true;
	        	} else if (getVelX() < 0) {
	        	    // 왼쪽으로 이동 중인 경우
	        	    currAnimationS.drawAnimation(g, (int)(getX() + getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight());
	        	    forward = false;
	        	} else {
	        	    // 이동하지 않는 경우
	        	    if (forward) {
	        	        g.drawImage(currSpriteS[0], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
	        	    } else {
	        	        g.drawImage(currSpriteS[0], (int)(getX() + getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight(), null);
	        	    }
	        	}
	        	
//	        	if(invincibilityTime && isVisible) {
//	        		if(forward) {
//	        			g.drawImage(currSpriteS[5], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
//	        		}else {
//	        			g.drawImage(currSpriteS[5], (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight(), null);
//	        		}
//	        	} else if(getVelX() > 0){
//	        		currAnimationS.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
//	        		forward = true;
//	        	} else if(getVelX() < 0){
//	        		currAnimationS.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight());
//	        		forward = false;
//	        	} else {
//	        	    if (forward) {
//	        	        g.drawImage(currSpriteS[0], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight(), null);
//	        	    } else {
//	        	        g.drawImage(currSpriteS[0], (int) (getX() + getWidth()), (int) getY(), (int) -getWidth(), (int) getHeight(), null);
//	        	    }
//	        	}   
//	        	} else if(jumped) {
//	        		if(forward) {
//	        			g.drawImage(currSpriteS[5], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
//	        		}else {
//	        			g.drawImage(currSpriteS[5], (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight(), null);
//	        		}
//	        	} else if(getVelX() > 0){
//	        		currAnimationS.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
//	        		forward = true;
//	        	} else if(getVelX() < 0){
//	        		currAnimationS.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight());
//	        		forward = false;
//	        	} else {
//	        	    if (forward) {
//	        	        g.drawImage(currSpriteS[0], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight(), null);
//	        	    } else {
//	        	        g.drawImage(currSpriteS[0], (int) (getX() + getWidth()), (int) getY(), (int) -getWidth(), (int) getHeight(), null);
//	        	    }
//	        	}            
	        } else if (health == 2){
	        	if (isDead) {
	                g.drawImage(currSpriteS[5], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
	                return;
	            }
	        	
	        	if(walkGoal) {
	        		currAnimationL.drawAnimation(g, (int)(getX() + 5), (int)getY(), (int) getWidth(), (int)getHeight()*2);
	        		return;
	        	}
	
	        	if (touchFlag) {
	                if (imageReversal) {
	                    // 이미지 반전
	                    g.drawImage(currSpriteL[7], (int) (getX() + getWidth()*2 - getWidth() / 8), (int) getY(), (int) -getWidth(), (int) getHeight()*2, null);
	                    return;
	                } else {
	                    g.drawImage(currSpriteL[7], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight()*2, null);
	                    return;
	                }
	            }
	        	
	        	if(jumped) {
	        		if(forward) {
	        			g.drawImage(currSpriteL[5], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2, null);
	        		}else {
	        			g.drawImage(currSpriteL[5], (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2, null);
	        		}
	        	}else if(getVelX() > 0){
	        		currAnimationL.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2);
	        		forward = true;
	        	}else if(getVelX() < 0){
	        		currAnimationL.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2);
	        		forward = false;
	        	}else if(crouching) {
	        		if(forward) {
	        			g.drawImage(currSpriteL[6], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2, null);
	        		}else {
	        			g.drawImage(currSpriteL[6], (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2, null);
	        		}
	        	} else {
	        	    if (forward) {
	        	        g.drawImage(currSpriteL[0], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight()*2, null);
	        	    } else {
	        	        g.drawImage(currSpriteL[0], (int) (getX() + getWidth()), (int) getY(), (int) -getWidth(), (int) getHeight()*2, null);
	        	    }
	        	}
	        } else if (health == 3){
	        	if (isDead) {
	                g.drawImage(currSpriteS[5], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
	                return;
	            }
	        	
	        	if(walkGoal) {
	        		currAnimationF.drawAnimation(g, (int)(getX() + 5), (int)getY(), (int) getWidth(), (int)getHeight()*2);
	        		return;
	        	}
	
	    		if (readyFireBall) {
	    			if(forward) {
	    				g.drawImage(currSpriteF[1], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2, null);
	    			} else{
	    				g.drawImage(currSpriteF[1], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2, null);
	    			}
	    		}
	
	    		if (touchFlag) {
	                if (imageReversal) {
	                    // 이미지 반전
	                    g.drawImage(currSpriteF[7], (int) (getX() + getWidth()*2 - getWidth() / 8), (int) getY(), (int) -getWidth(), (int) getHeight()*2, null);
	                    return;
	                } else {
	                    g.drawImage(currSpriteF[7], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight()*2, null);
	                    return;
	                }
	            }
	        	
	        	if(jumped) {
	        		if(forward) {
	        			if(shooting) {
	        				g.drawImage(currSpriteF[1], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2, null);
	        			} else {
	        				g.drawImage(currSpriteF[5], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2, null);
	        			}
	        		}else {
	        			if(shooting) {
	        				g.drawImage(currSpriteF[1], (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2, null);
	        			} else {
	        				g.drawImage(currSpriteF[5], (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2, null);
	        			}
	        		}
	        	}else if(getVelX() > 0){
	        		if(shooting) {
	        			g.drawImage(currSpriteF[1], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2, null);
	        		} else{
	            		currAnimationF.drawAnimation(g, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2);
	            		forward = true;
	        		}
	        	}else if(getVelX() < 0){
	        		if(shooting) {
	        			g.drawImage(currSpriteF[1], (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2, null);
	        		} else {
		        		currAnimationF.drawAnimation(g, (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2);
		        		forward = false;
	        		}
	        	}else if(crouching) {
	        		if(forward) {
	        			g.drawImage(currSpriteF[6], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2, null);
	        		}else {
	        			g.drawImage(currSpriteF[6], (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2, null);
	        		}
	        	}else if (shooting){
	        		if(forward) {
	        			g.drawImage(currSpriteF[1], (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight()*2, null);
	        		}else {
	        			g.drawImage(currSpriteF[1], (int)(getX()+getWidth()), (int)getY(), (int)-getWidth(), (int)getHeight()*2, null);
	        		}
	        	}else {
	        	    if (forward) {
	        	        g.drawImage(currSpriteF[0], (int) getX(), (int) getY(), (int) getWidth(), (int) getHeight()*2, null);
	        	    } else {
	        	        g.drawImage(currSpriteF[0], (int) (getX() + getWidth()), (int) getY(), (int) -getWidth(), (int) getHeight()*2, null);
	        	    }
	        	}
	        } 
    	}
    }

    private void collision(){
        for(int i=0; i<handler.getGameObjs().size(); i++){
            GameObject temp = handler.getGameObjs().get(i);
            if(removeBlocks.contains(temp)) continue;
            
            // Block과 충돌, 작은 상태일때 파괴 불가
            if(state == UnitState.Mario_Large && temp.getId() == ObjectId.Block && getBoundsTop().intersects(temp.getBounds())
            		|| state == UnitState.Mario_Flower && temp.getId() == ObjectId.Block && getBoundsTop().intersects(temp.getBounds())) {
                canJump = false;
            	setY(temp.getY() + temp.getHeight());
            	setVelY(0);
            	((Block)temp).hit();
            	removeBlocks.add((Block)temp);
            }else if (state == UnitState.Mario_Small && temp.getId() == ObjectId.Block && getBoundsTop().intersects(temp.getBounds())) {
                sound.emptyItemBlock();
                final int originalY = (int) temp.getY(); // 원래 위치 저장
                canJump = false;
                // 블록을 위로 이동시키는 부분
                temp.setY(temp.getY() - 7);

                // 일정 시간이 지난 후 블록을 원래 위치로 되돌리는 작업
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ((Block) temp).setShaking(false); // 들썩임 상태 해제

                        // 원래 위치로 돌아오게 하기 위해 다시 원래 Y 위치로 설정
                        temp.setY(originalY);
                    }
                }, 100); // 1초 후에 들썩임 상태 해제 및 원래 위치로 복귀 (1000ms)
            }

            // 투명 블록과 충돌
            if (temp.getId() == ObjectId.TransparentBlock && getBoundsTop().intersects(temp.getBounds())) {
                TransparentBlock transBlock = (TransparentBlock) temp;
                System.out.println("LifeMushroom : " + transBlock.hasLifeMushroom());
                transBlock.tick(); // ItemBlocks에서의 tick 메소드 호출
                transBlock.setTransparent(false); // 투명 상태로 설정
                transBlock.setHasItem(false);
                canJump = false;
            }
            
            // ItemBlock과 충돌
            if (temp.getId() == ObjectId.ItemBlocks && getBoundsTop().intersects(temp.getBounds())) {
                ItemBlocks itemBlock = (ItemBlocks) temp; // 이 부분이 문제입니다
                canJump = false;
                itemBlock.tick(); // ItemBlocks에서의 tick 메소드 호출
                itemBlock.setHasItem(false); // 아이템을 획득하지 않은 상태로 설정
                if(itemBlock.shaking()) {
                    final int originalY = (int) temp.getY(); // 원래 위치 저장

                    // 블록을 위로 이동시키는 부분
                    temp.setY(temp.getY() - 7);

                    // 일정 시간이 지난 후 블록을 원래 위치로 되돌리는 작업
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            itemBlock.setShaking(false); // 들썩임 상태 해제
                            // 원래 위치로 돌아오게 하기 위해 다시 원래 Y 위치로 설정
                            temp.setY(originalY);
                        }
                    }, 100); // 1초 후에 들썩임 상태 해제 및 원래 위치로 복귀 (1000ms)
                } else {
                    sound.emptyItemBlock();
                }
            }
            
            // CoinBlock과 충돌
            if (temp.getId() == ObjectId.CoinBlocks && getBoundsTop().intersects(temp.getBounds())) {
            	CoinBlocks coinBlock = (CoinBlocks) temp;
                canJump = false;
                coinBlock.tick(); // ItemBlocks에서의 tick 메소드 호출
                coinBlock.setHasItem(false); // 아이템을 획득하지 않은 상태로 설정
                if(coinBlock.shaking()) {
                    final int originalY = (int) temp.getY(); // 원래 위치 저장

                    // 블록을 위로 이동시키는 부분
                    temp.setY(temp.getY() - 7);

                    // 일정 시간이 지난 후 블록을 원래 위치로 되돌리는 작업
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // 원래 위치로 돌아오게 하기 위해 다시 원래 Y 위치로 설정
                            temp.setY(originalY);
                        }
                    }, 100); // 1초 후에 들썩임 상태 해제 및 원래 위치로 복귀 (1000ms)
                } else {
                    sound.emptyItemBlock();
                }
            }
            
            // Mushroom과 충돌
            if (temp.getId() == ObjectId.Mushroom && getBounds().intersects(temp.getBounds())
            		|| temp.getId() == ObjectId.Mushroom && getBoundsLeft().intersects(temp.getBounds())
    				|| temp.getId() == ObjectId.Mushroom && getBoundsRight().intersects(temp.getBounds())
					|| temp.getId() == ObjectId.Mushroom && getBoundsTop().intersects(temp.getBounds())) {
            	Mushroom mushroom = (Mushroom) temp;
            	if(mushroom.getSoundCheck() == false) {
            		sound.powerUp();
            		mushroom.setSoundCheck(true);
            		handler.removeObj(mushroom);
            		if(health != 3) {
            			health = 2;
            		}
            	}
            }
            
            // LifeMushroom과 충돌
            if (temp.getId() == ObjectId.LifeMushroom && getBounds().intersects(temp.getBounds())
            		|| temp.getId() == ObjectId.LifeMushroom && getBoundsLeft().intersects(temp.getBounds())
    				|| temp.getId() == ObjectId.LifeMushroom && getBoundsRight().intersects(temp.getBounds())
					|| temp.getId() == ObjectId.LifeMushroom && getBoundsTop().intersects(temp.getBounds())) {
            	LifeMushroom lifemushroom = (LifeMushroom) temp;
            	if(lifemushroom.getSoundCheck() == false) {
            		sound.getLifeMushroomSound();
            		lifemushroom.setSoundCheck(true);
            		handler.removeObj(lifemushroom);;
            	}
            }
            
            // Flower과 충돌
            if (temp.getId() == ObjectId.Flower && getBounds().intersects(temp.getBounds())
            		|| temp.getId() == ObjectId.Flower && getBoundsLeft().intersects(temp.getBounds())
    				|| temp.getId() == ObjectId.Flower && getBoundsRight().intersects(temp.getBounds())
					|| temp.getId() == ObjectId.Flower && getBoundsTop().intersects(temp.getBounds())) {
            	Flower flower = (Flower) temp;
            	if(flower.getSoundCheck() == false) {
            		sound.powerUp();
            		flower.setSoundCheck(true);
            		handler.removeObj(flower);
            		health = 3;
            	}
            }
            
            // Star과 충돌
            if (temp.getId() == ObjectId.Star && getBounds().intersects(temp.getBounds())
            		|| temp.getId() == ObjectId.Star && getBoundsLeft().intersects(temp.getBounds())
    				|| temp.getId() == ObjectId.Star && getBoundsRight().intersects(temp.getBounds())
					|| temp.getId() == ObjectId.Star && getBoundsTop().intersects(temp.getBounds())) {
            	Star star = (Star) temp;
            	if(star.getSoundCheck() == false) {
            		sound.invincibilityTime();
            		star.setSoundCheck(true);
            		handler.removeObj(star);
            		starTime = true;
            		System.out.println("스타 시간 시작 : "+starTime);
            		 // 10초 타이머 설정
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                        	sound.stopAllSounds();
                            starTime = false;
                            System.out.println("스타 시간 끝"+starTime);
                            // 필요한 경우 타이머 종료
                            timer.cancel();
                        }
                    }, 10000); // 10초 후 실행
                } else {
                    starTime = false;
                }
            }
            
            if (temp.getId() == ObjectId.Coin2 && getBounds().intersects(temp.getBounds())
            		|| temp.getId() == ObjectId.Coin2 && getBoundsLeft().intersects(temp.getBounds())
    				|| temp.getId() == ObjectId.Coin2 && getBoundsRight().intersects(temp.getBounds())
					|| temp.getId() == ObjectId.Coin2 && getBoundsTop().intersects(temp.getBounds())) {
            	Coin2 coin2 = (Coin2) temp;
            	if(coin2.getSoundCheck() == false) {
            		sound.getCoinSound();
            		handler.removeObj(coin2);
            		coin2.setSoundCheck(true);
            	}
            }

            if(temp.getId() == ObjectId.Block
            		|| temp.getId() == ObjectId.TransparentBlock
            		|| temp.getId() == ObjectId.EndTile
            		|| temp.getId() == ObjectId.ItemBlocks
            		|| temp.getId() == ObjectId.CoinBlocks){
                if(getBounds().intersects(temp.getBounds())){
                	if(health == 1) {
                		setY(temp.getY() - getHeight());
                        setVelY(0);
                	}else if(health == 2 || health == 3) {
                		setY(temp.getY() - getHeight() * 2);
                        setVelY(0);
                	}
                    jumped = false;
                    canJump = false;
                    getItem = true;
                }

                if(getBoundsTop().intersects(temp.getBounds())){
                	if(health == 1) {
                    	setY(temp.getY() + temp.getHeight());
                        setVelY(0);
                	}else if(health == 2 || health == 3) {
                    	setY(temp.getY() + temp.getHeight());
                        setVelY(0);
                	}
                }

                if(getBoundsRight().intersects(temp.getBounds())){
                    if(health ==1) {
                    	setX(temp.getX() - getWidth());
                    }else if(health == 2 || health == 3) {
                    	setX(temp.getX() - getWidth() - getWidth() / 2 + 5);
                    }
                }

                if(getBoundsLeft().intersects(temp.getBounds())){
                    if(health == 1) {
                    	setX(temp.getX() + temp.getWidth());
                    }else if(health == 2 || health == 3) {
                    	setX(temp.getX() + temp.getWidth() + getWidth() / 2 - 5);
                    }
                }
            }
            
            if(temp.getId() == ObjectId.Pipe){
                if(getBounds().intersects(temp.getBounds())){
                	if(health == 1) {
                		setY(temp.getY() - getHeight());
                        setVelY(0);
                	}else if(health == 2 || health == 3) {
                		setY(temp.getY() - getHeight() * 2);
                        setVelY(0);
                	}
                    jumped = false;
                    canJump = false;
                    getItem = true;
                }

                if(getBoundsTop().intersects(temp.getBounds())){
                	if(health == 1) {
                    	setY(temp.getY() + temp.getHeight());
                        setVelY(0);
                	}else if(health == 2 || health == 3) {
                    	setY(temp.getY() + temp.getHeight());
                        setVelY(0);
                	}
                }

                if(getBoundsRight().intersects(temp.getBounds())){
                    if(health ==1) {
                    	setX(temp.getX() - getWidth());
                    }else if(health == 2 || health == 3) {
                    	setX(temp.getX() - getWidth() - getWidth() / 2 + 5);
                    }
                }

                if(getBoundsLeft().intersects(temp.getBounds())){
                    if(health == 1) {
                    	setX(temp.getX() + temp.getWidth());
                    }else if(health == 2 || health == 3) {
                    	setX(temp.getX() + temp.getWidth() + getWidth() / 2 - 5);
                    }
                }
            }
            
            if(temp.getId() == ObjectId.Flag2) {
            	if(getBounds().intersects(temp.getBounds())){
            		touchFlag = true;
            	}
            }
        }
    }

    @Override
    public Rectangle getBounds() {
        if (health == 1) {
            return new Rectangle((int)(getX() + getWidth() / 2 - getWidth() / 4), 
                    (int)(getY() + getHeight() / 2), 
                    (int)(getWidth() / 2), 
                    (int)getHeight() / 2);
        } else if (health == 2){
            return new Rectangle((int)(getX()), 
                    (int)(getY() + getHeight() + 25), 
                    (int)(getWidth()), 
                    (int)getHeight() / 2);
        } else {
        	return new Rectangle((int)(getX()), 
                    (int)(getY() + getHeight()), 
                    (int)(getWidth()), 
                    (int)getHeight());
        }
    }
    
    public Rectangle getBoundsTop() {
        if (health == 1) {
            return new Rectangle((int)(getX() + getWidth() / 2 - getHeight() / 4), 
                    (int)getY(), 
                    (int)(getWidth() / 2), 
                    (int)(getHeight() / 2));
        } else if (health == 2){
            return new Rectangle((int)(getX()), 
                    (int)getY(), 
                    (int)(getWidth()), 
                    (int)getHeight() / 2);
        } else {
        	return new Rectangle((int)(getX()), 
                    (int)getY(), 
                    (int)(getWidth()), 
                    (int)getHeight());
        }
    }

    public Rectangle getBoundsRight() {	
        if (health == 1) {
            return new Rectangle((int)(getX() + getWidth() - 5), 
                    (int)(getY() + 5), 
                    5, 
                    (int)(getHeight() - 10));
        } else if (health == 2){
        	return new Rectangle((int)(getX() + getWidth() + 10), 
                    (int)(getY() + getWidth() / 4), 
                    5, 
                    (int)(getHeight() * 2 - getWidth() / 2));
        } else {
        	return new Rectangle((int)(getX() + getWidth() + 10), 
                    (int)(getY() + getWidth() / 4), 
                    5, 
                    (int)(getHeight() * 2 - getWidth() / 2));
        }
    }

    public Rectangle getBoundsLeft() {
        if (health == 1) {
            return new Rectangle((int) getX(),
                    (int)(getY() + 5),
                    5,
                    (int)(getHeight() - 10));
        } else if (health == 2){
        	return new Rectangle((int) getX() - 15,
                    (int)(getY() + getWidth() / 4),
                    5,
                    (int)(getHeight() * 2 - getWidth() / 2));
        } else {
        	return new Rectangle((int) getX() - 15,
                    (int)(getY() + getWidth() / 4),
                    5,
                    (int)(getHeight() * 2 - getWidth() / 2));
        }
    }

    public void showBounds(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.RED);
        g2d.draw(getBounds());
        g2d.draw(getBoundsRight());
        g2d.draw(getBoundsLeft());
        g2d.draw(getBoundsTop());
    }

    public boolean hasJumped(){
        return jumped;
    }
    
    public boolean getItem() {
    	return getItem;
    }
    public void setJumped(boolean jumped) {
    	this.jumped = jumped;
        this.hasJumpedSoundPlayed = false;
        
        if (canJump) {
            isJumping = true;
            setVelY(-jumpSpeed); // 점프 처리
            canJump = false; // 점프 후 다시 점프 금지
        }
    }
    
    public void setShooting(boolean shooting) {
        this.shooting = shooting;
        
        if(canFired) {
        	isFiring = true;
        	canFired = false;
        }
    }
    
    public boolean isTouchBlock() {
        return touchBlock;
    }
    
    public void setTouchBlock(boolean touchBlock) {
        this.touchBlock = touchBlock;
    }
    
    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }
    
    public boolean isDead() {
        return isDead;
    }
    
    public LinkedList<Block>getAndResetRemoveBlock(){
    	LinkedList<Block> output = new LinkedList<Block>();
    	for(Block removeBlock : removeBlocks) {
    		if(!removeBlock.shouldRemove()) continue;
    		output.add(removeBlock);
    	}
    	for(Block removeBlock : output) {
    		removeBlocks.remove(removeBlock);
    	}
    	return output;
    }
    
    public boolean intersects(GameObject obj) {
        Rectangle r1 = new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        Rectangle r2 = new Rectangle((int)obj.getX(), (int)obj.getY(), (int)obj.getWidth(), (int)obj.getHeight());
        
        return r1.intersects(r2);
    }
    
    private void handleDeathAnimation() {
    	
    }
    
    private void moveMario() {
    	if(!touchFlag) {
    		setX(getX() + getVelX());
            setY(getY() + getVelY());
            
            if(imageReversal) {
        		canJump = false;
        		jumped = false;
        		isJumping = false;
        		new Timer().schedule(new TimerTask() {
        			public void run() {
        				walkGoal = true;
	            		final int speed = 4;
	            		setX(getX() + speed);
	            		new Timer().schedule(new TimerTask() {
	            			public void run() {
	            				handler.removeObj(Player.this);
	            				endFlag = true;
	            			}
	            		}, 1300);
        			}
        		}, 300);
            }
    	} else {
//    		setVelX(0);
//    		setVelY(0);
    	}
    }
    
    private void handleGameOver() {
        handler.getPlayer().setDead(true); // 플레이어를 죽은 상태로 설정
        sound.stopAllSounds(); // 모든 사운드 정지
        sound.playDie(); // 죽음 사운드 재생
        setVelY(-jumpSpeed);
        handler.getGame().setGameOver(true); // 게임 오버 상태 설정
    }
    
    private void marioAnimation() {
    	currAnimationS.runAnimation();
        currAnimationL.runAnimation();
        currAnimationF.runAnimation();
    }
    
    public void hitEnemy() {
        // 플레이어가 적을 밟았을 때의 처리
        setVelY(-jumpSpeed); // 약간 띄워줍니다.
        jumped = true;
        canJump = false;
    }
    
    public void shootFireBall() {
    	
    	if(forward) {
    		fireBallRight = true;
    	} else {
    		fireBallRight = false;
    	}
    	
        if(shooting) {
        	float fireX = getX() + (getWidth() - 20) / 2; // 블록의 중앙 위치에 코인을 생성
	        float fireY = getY() + 30; // 블록 바로 위에 생성되도록 Y 좌표 설정

	        // 플레이어 위치에서 불꽃 발사
            FireBall fire = new FireBall(fireX, fireY, 16, 16, 0, 1, 3, handler);
            handler.addObj(fire);
            System.out.println("불꽃생성");
        }
    }
    
    public boolean getStarTime() {
    	return starTime;
    }
    
    public void setStarTime(boolean starTime) {
    	this.starTime = starTime;
    }
    
    public UnitState getMarioState() {
    	return state;
    }
    
    public void setMarioState(UnitState state) {
    	this.state = state;
    }
    
    public void marioState() {
    	if(health == 1) {
    		state = UnitState.Mario_Small;
    	} else if(health == 2) {
    		state = UnitState.Mario_Large;
    	} else if(health == 3) {
    		state = UnitState.Mario_Flower;
    	} else if(health ==4) {
    		state = UnitState.Mario_Star;
    	}
    }
    
    private void startVisibilityToggle() {
        Timer timer = new Timer();
        visibilityTask = new TimerTask() {
            @Override
            public void run() {
                isVisible = !isVisible; // 가시성 상태 전환
            }
        };
        // 0초 후에 시작하여 1초마다 실행
        timer.scheduleAtFixedRate(visibilityTask, 0, 1000);
        
        // 10초 후에 타이머 작업을 취소
        TimerTask stopTask = new TimerTask() {
            @Override
            public void run() {
                visibilityTask.cancel(); // 가시성 전환 작업 취소
                timer.cancel(); // 타이머 취소
            }
        };
        timer.schedule(stopTask, 1500); // 3초 후에 종료
    }

    
    public int getHealth() {
    	return health;
    }
    
    public void setHealth(int health) {
    	this.health = health;
    }
    
    public boolean getCrouching() {
    	return crouching;
    }
    
    public void setCrouching(boolean crouching) {
    	this.crouching = crouching;
    }
    
    public boolean getAccelerate() {
    	return accelerate;
    }
    
    public void setAccelerate(boolean accelerate) {
    	this.accelerate = accelerate;
    }
    
    public boolean getReadyFireBall() {
    	SoundManager.getInstance().isFireBall();
    	return readyFireBall;
    }
    
    public void setReadyFireBall(boolean readyFireBall) {
    	this.readyFireBall = readyFireBall;
    }
    
    public boolean getTouchFlag() {
    	return touchFlag;
    }
    
    public void setTouchFlag(boolean touchFlag) {
    	this.touchFlag = touchFlag;
    }
    
    public boolean getImageReversal() {
    	return imageReversal;
    }
    
    public void setImageReversal(boolean imageReversal) {
    	this.imageReversal = imageReversal;
    }
    
    public boolean getCehckMove() {
    	return checkMove;
    }
    
    public void setCheckMove(boolean checkMove) {
    	this.checkMove = checkMove;
    }
    
    public boolean getEndFlag() {
    	return endFlag;
    }
    
    public void setEndFlag(boolean endFlag) {
    	this.endFlag = endFlag;
    }
    
    public boolean getForward() {
    	return forward;
    }
    
    public void setForward() {
    	this.forward = forward;
    }
    
    public void startAcceleratingRight() {
        acceleratingRight = true;
        moving = true; // 가속 상태에서 이동 상태로 전환
    }

    public void startAcceleratingLeft() {
        acceleratingLeft = true;
        moving = true; // 가속 상태에서 이동 상태로 전환
    }

    public void stopAccelerating() {
        acceleratingRight = false;
        acceleratingLeft = false;
        // 속도 감소 상태로 전환
        moving = false;
    }
    
    public boolean getStopMotion() {
    	return stopMotion;
    }
    
    public void setStopMotion(boolean stopMotion) {
    	this.stopMotion = stopMotion;
    }
    
    public boolean getInvincibilityTime() {
    	return invincibilityTime;
    }
    
    public void setInvincibilityTime(boolean invincibilityTime) {
    	this.invincibilityTime = invincibilityTime;
    }
    
    public void invincibilityTime() {
    	if(invincibilityTime) {
    		Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    invincibilityTime = false;
                }
            }, 1500); // (1.5초)
            
            startVisibilityToggle();
    	}

    }
    
    public boolean getFireBallSound() {
    	return fireBallSound;
    }
    
    public void setFireBallSound(boolean fireBallSound) {
    	this.fireBallSound = fireBallSound;
    }
    
    public int getFireBallCount() {
    	return fireBallCount;
    }
    
    public void setFireBallCount(int fireBallCount) {
    	this.fireBallCount = fireBallCount;
    }
    
    public boolean getPipeIn() {
    	return pipeIn;
    }
    
    public void setPipeIn(boolean pipeIn) {
    	this.pipeIn = pipeIn;
    }
    
    public boolean getUsePipe() {
    	return usePipe;
    }
    
    public void setUsePipe(boolean usePipe) {
    	this.usePipe = usePipe;
    }
    
    public void usePipe() {
    	if(pipeIn) {
//    		sound.usePipe();
    		usePipe = true;
    		pipeIn = false;
    		setVelY(50);
    	}
    }
}