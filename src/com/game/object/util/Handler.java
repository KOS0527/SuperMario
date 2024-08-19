package com.game.object.util;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.game.gfx.Camera;
import com.game.gfx.SoundManager;
import com.game.main.Game;
import com.game.object.Block;
import com.game.object.EnemyG;
import com.game.object.EnemyN;
import com.game.object.Flag2;
import com.game.object.Flag3;
import com.game.object.GameObject;
import com.game.object.Mushroom;
import com.game.object.Pipe;
import com.game.object.Player;

public class Handler {
	private List<GameObject> gameObjs;
    private Player player;
    private KeyInput keyInput;
    private Game game;
    private List<EnemyG> enemiesG;
    private List<EnemyN> enemiesN;
    private SoundManager sound;
    private Flag2 flag2;
    private Flag3 flag3;
    private Camera camera;
   
    private boolean enemyN_Touch_enemyG = false;
    private boolean rightToLeft = false; // 적의 왼쪽과 오른쪽 충돌 감지
    private boolean leftToRight = false; // 적의 왼쪽과 오른쪽 충돌 감지
    
    private boolean enemyDeadCheck = false;
    
    private boolean checkToFire = false;
    
    private boolean gameOver = false;
    private boolean nextStage = false;

    public Handler(Game game) {
    	this.game = game;
        gameObjs = new ArrayList<>();
        enemiesG = new ArrayList<>();
        enemiesN = new ArrayList<>();
        sound = SoundManager.getInstance();
    }

    public void tick() {
        if (!gameOver) {
            // 모든 게임 오브젝트의 tick() 메서드 호출
            List<GameObject> copyObjs = new ArrayList<>(gameObjs);
            for (GameObject obj : copyObjs) {
                obj.tick();
            }

            // 플레이어와 모든 적 간의 충돌 체크
            checkPlayerEnemyCollision();
            
            // 플레이어가 제거할 블록 리스트 가져오기
            LinkedList<Block> removeBlocks = player.getAndResetRemoveBlock();
            // 제거할 블록들을 게임 오브젝트 리스트에서 제거
            for (Block removeBlock : removeBlocks) {
                removeObj(removeBlock);
            }
            updateEnemyVisibility();
            enmyRightToLeftCheck();
        }
    }

    private void checkPlayerEnemyCollision() {
        for (EnemyG enemyG : enemiesG) {
        	if(player.getStarTime()) { // Star 아이템 시간동안이라면
        		if(!enemyG.getDead()) {
        			if(player.getBounds().intersects(enemyG.getBoundsLeft()) ||
	                    player.getBounds().intersects(enemyG.getBoundsRight()) ||
	                    player.getBoundsTop().intersects(enemyG.getBoundsLeft()) ||
	                    player.getBoundsTop().intersects(enemyG.getBoundsRight())) {
        				enemyG.setDeadByStar(true);
        			}
        		}
        	} else { // Star가 아니라면
	            	if(!enemyG.getDead() 
	            			&& player.getMarioState() == UnitState.Mario_Small
	            			&& !player.getStarTime()
	            			&& !player.getInvincibilityTime()) {
		                if (player.getBounds().intersects(enemyG.getBoundsLeft()) ||
		                        player.getBounds().intersects(enemyG.getBoundsRight()) ||
		                        player.getBoundsTop().intersects(enemyG.getBoundsLeft()) ||
		                        player.getBoundsTop().intersects(enemyG.getBoundsRight())) {
		                    // 충돌 시 게임 종료 처리
		                    gameOver = true;
		                    sound.stopAllSounds();
		                    getPlayer().setDead(true);
		                    sound.stopAllSounds();
//		                    sound.playDie();
		                }
	                } else if(!enemyG.getDead()
	                		&& player.getMarioState() == UnitState.Mario_Large && !player.getStarTime()
	                		&& !player.getInvincibilityTime()) {
		                if (player.getBounds().intersects(enemyG.getBoundsLeft()) ||
		                        player.getBounds().intersects(enemyG.getBoundsRight()) ||
		                        player.getBoundsTop().intersects(enemyG.getBoundsLeft()) ||
		                        player.getBoundsTop().intersects(enemyG.getBoundsRight())) {
		                    // 충돌 시 게임 종료 처리
		                    player.setHealth(1);
		                    player.setInvincibilityTime(true);
		                    sound.powerDown();
		                }
	                } else if(!enemyG.getDead()
	                		&& player.getMarioState() == UnitState.Mario_Flower
	                		&& !player.getStarTime()
	                		&& !player.getInvincibilityTime()) {
		                if (player.getBounds().intersects(enemyG.getBoundsLeft()) ||
		                        player.getBounds().intersects(enemyG.getBoundsRight()) ||
		                        player.getBoundsTop().intersects(enemyG.getBoundsLeft()) ||
		                        player.getBoundsTop().intersects(enemyG.getBoundsRight())) {
		                    // 충돌 시 게임 종료 처리
		                    player.setHealth(1);
		                    player.setInvincibilityTime(true);
		                    sound.powerDown();
		                }
	                }
        	}
        }

        for (EnemyN enemyN : enemiesN) {
        	if(player.getStarTime()) { // Star 아이템 시간동안이라면
        		if(enemyN.getAlive_N()) {
        			if(player.getBounds().intersects(enemyN.getBoundsLeft()) ||
	                    player.getBounds().intersects(enemyN.getBoundsRight()) ||
	                    player.getBoundsTop().intersects(enemyN.getBoundsLeft()) ||
	                    player.getBoundsTop().intersects(enemyN.getBoundsRight())) {
        				enemyN.setDeadByStar(true);
        			}
        		}
        	} else {
        		if(enemyN.getKickCount() == 3) {
        			if(player.getMarioState() == UnitState.Mario_Small 
    					&& player.getMarioState() != UnitState.Mario_Star
                		&& !player.getStarTime()
                		&& !player.getInvincibilityTime()) {
        				if (player.getBounds().intersects(enemyN.getBoundsLeft()) ||
	                        player.getBounds().intersects(enemyN.getBoundsRight()) ||
	                        player.getBoundsTop().intersects(enemyN.getBoundsLeft()) ||
	                        player.getBoundsTop().intersects(enemyN.getBoundsRight())) {
        					gameOver = true;
 		                    sound.stopAllSounds();
 		                    getPlayer().setDead(true);
 		                    sound.stopAllSounds();
        				}
        			} else if(player.getMarioState() == UnitState.Mario_Large
        					&& player.getMarioState() != UnitState.Mario_Star
                    		&& !player.getStarTime()
                    		&& !player.getInvincibilityTime()){
        				if (player.getBounds().intersects(enemyN.getBoundsLeft()) ||
	                        player.getBounds().intersects(enemyN.getBoundsRight()) ||
	                        player.getBoundsTop().intersects(enemyN.getBoundsLeft()) ||
	                        player.getBoundsTop().intersects(enemyN.getBoundsRight())) {
		                    player.setHealth(1);
		                    player.setInvincibilityTime(true);
		                    sound.powerDown();
        				}
        			} else if(player.getMarioState() == UnitState.Mario_Flower 
        					&& player.getMarioState() != UnitState.Mario_Star
                    		&& !player.getStarTime()
                    		&& !player.getInvincibilityTime()){
        				if (player.getBounds().intersects(enemyN.getBoundsLeft()) ||
	                        player.getBounds().intersects(enemyN.getBoundsRight()) ||
	                        player.getBoundsTop().intersects(enemyN.getBoundsLeft()) ||
	                        player.getBoundsTop().intersects(enemyN.getBoundsRight())) {
    		                    player.setHealth(1);
    		                    player.setInvincibilityTime(true);
    		                    sound.powerDown();
            				}
        			}
        		} 
//        			else if(enemyN.getKickCount() == 2) {
//        			if(player.getMarioState() == UnitState.Mario_Small 
//        					&& player.getMarioState() != UnitState.Mario_Star
//                    		&& !player.getStarTime()
//                    		&& !player.getInvincibilityTime()) {
//            				if (player.getBounds().intersects(enemyN.getBoundsLeft()) ||
//    	                        player.getBounds().intersects(enemyN.getBoundsRight()) ||
//    	                        player.getBoundsTop().intersects(enemyN.getBoundsLeft()) ||
//    	                        player.getBoundsTop().intersects(enemyN.getBoundsRight())) {
//
//            				}
//            			} else if(player.getMarioState() == UnitState.Mario_Large
//            					&& player.getMarioState() != UnitState.Mario_Star
//                        		&& !player.getStarTime()
//                        		&& !player.getInvincibilityTime()){
//            				
//            			} else if(player.getMarioState() == UnitState.Mario_Flower 
//            					&& player.getMarioState() != UnitState.Mario_Star
//                        		&& !player.getStarTime()
//                        		&& !player.getInvincibilityTime()){
//            				
//            			}
//        		} else if(enemyN.getKickCount() == 1) {
//        			if(player.getMarioState() == UnitState.Mario_Small 
//        					&& player.getMarioState() != UnitState.Mario_Star
//                    		&& !player.getStarTime()
//                    		&& !player.getInvincibilityTime()) {
//            				if (player.getBoundsTop().intersects(enemyN.getBoundsLeft()) ||
//    	                        player.getBoundsTop().intersects(enemyN.getBoundsRight())) {
//
//            				}
//            			} else if(player.getMarioState() == UnitState.Mario_Large
//            					&& player.getMarioState() != UnitState.Mario_Star
//                        		&& !player.getStarTime()
//                        		&& !player.getInvincibilityTime()){
//            				if (player.getBoundsTop().intersects(enemyN.getBoundsLeft()) ||
//    	                        player.getBoundsTop().intersects(enemyN.getBoundsRight())) {
//    		                    player.setHealth(1);
//    		                    player.setInvincibilityTime(true);
//    		                    sound.powerDown();
//            				}
//            				
//            			} else if(player.getMarioState() == UnitState.Mario_Flower 
//            					&& player.getMarioState() != UnitState.Mario_Star
//                        		&& !player.getStarTime()
//                        		&& !player.getInvincibilityTime()){
//            				if (player.getBoundsTop().intersects(enemyN.getBoundsLeft()) ||
//    	                        player.getBoundsTop().intersects(enemyN.getBoundsRight())) {
//
//            				}
//            			}
//        		}
        	}
        }
    }
    
    public boolean isTouchingWall_Mushroom(Mushroom mushroom) {
    	for (GameObject obj : gameObjs) {
            if (obj instanceof Pipe) { // Pipe 객체와 충돌 검사
                if (mushroom.getBoundsLeft().intersects(obj.getBounds()) ||
            		mushroom.getBoundsRight().intersects(obj.getBounds())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void enmyRightToLeftCheck() {
    	for (EnemyG enemyG : enemiesG) {
			for(EnemyN enemyN : enemiesN) {
				if(enemyG.getBoundsLeft().intersects(enemyN.getBoundsRight())){
					setRightToLeft(false);
					setLeftToRight(true);
//					System.out.println("rightToLeft : "+rightToLeft+" / G와 N 접촉");
				}else if (enemyG.getBoundsRight().intersects(enemyN.getBoundsLeft())) {
					setRightToLeft(true);
					setLeftToRight(false);
//					System.out.println("rightToLeft : "+rightToLeft+" / G와 N 접촉");
				}
			}
    	}
    }
    
    private void updateEnemyVisibility() {
        int cameraCenterX = -camera.getX() + (Game.getScreenWidth() / 2);
        int cameraCenterY = -camera.getY() + (Game.getScreenHeight() / 2);

        int screenLeft = cameraCenterX - (Game.getScreenWidth() / 2);
        int screenRight = cameraCenterX + (Game.getScreenWidth() / 2);
        int screenTop = cameraCenterY - (Game.getScreenHeight() / 2);
        int screenBottom = cameraCenterY + (Game.getScreenHeight() / 2);

        for (GameObject obj : gameObjs) {
            if (obj instanceof EnemyG) {
                EnemyG enemy = (EnemyG) obj;
                int enemyLeft = (int) enemy.getX();
                int enemyRight = (int) enemy.getX() + (int) enemy.getWidth();
                int enemyTop = (int) enemy.getY();
                int enemyBottom = (int) enemy.getY() + (int) enemy.getHeight();

                if (enemyRight >= screenLeft && enemyLeft <= screenRight && 
                    enemyBottom >= screenTop && enemyTop <= screenBottom) {
                    enemy.setVisible(true);
                    continue;
                }
            }
        }
        
        for (GameObject obj : gameObjs) {
            if (obj instanceof EnemyN) {
                EnemyN enemy = (EnemyN) obj;
                int enemyLeft = (int) enemy.getX();
                int enemyRight = (int) enemy.getX() + (int) enemy.getWidth();
                int enemyTop = (int) enemy.getY();
                int enemyBottom = (int) enemy.getY() + (int) enemy.getHeight();

                if (enemyRight >= screenLeft && enemyLeft <= screenRight && 
                    enemyBottom >= screenTop && enemyTop <= screenBottom) {
                    enemy.setVisible(true);
                    continue;
                }
            }
        }
    }

    public void addObject(GameObject object) {
        this.gameObjs.add(object);
    }

    public void render(Graphics g) {
        List<GameObject> gameObjsCopy = new ArrayList<>(gameObjs);

        gameObjsCopy.sort(Comparator.comparingInt(GameObject::getLayer));
        
        for (GameObject obj : gameObjsCopy) {
            obj.render(g);
        }
    }

    public void addObj(GameObject obj) {
        gameObjs.add(obj);
    }

    public void removeObj(GameObject obj) {
        gameObjs.remove(obj);
    }

    public List<GameObject> getGameObjs() {
        return gameObjs;
    }

    public KeyInput getKeyInput() {
    	return keyInput;
    }
    
    public void setKeyInput(KeyInput keyInput) {
    	this.keyInput = keyInput;
    }
    
    public Game getGame() {
    	return game;
    }
    
    public void setGame(Game game) {
    	this.game = game;
    }
    
    public Player getPlayer() {
    	return player;
    }
    
    public int setPlayer(Player player) {
    	if (player == null) {
            return -1;
        }
        if (this.player != null) {
            return -1;
        }
        addObj(player);
        this.player = player;
        return 0;
    }

    public void addEnemyG(EnemyG enemyG) {
        enemiesG.add(enemyG);
        addObj(enemyG);
    }

    public void addEnemyN(EnemyN enemyN) {
        enemiesN.add(enemyN);
        addObj(enemyN);
    }

    public List<EnemyG> getEnemiesG() {
        return enemiesG;
    }

    public List<EnemyN> getEnemiesN() {
        return enemiesN;
    }

    public int removePlayer() {
        if (player == null) {
            return -1;
        }
        removeObj(player);
        this.player = null;
        return 0;
    }
    
    public void setFlag2(Flag2 flag2) {
        this.flag2 = flag2;
    }
    
    public Flag2 getFlag2() {
    	return flag2;
    }
    
    public void setFlag3(Flag3 flag3) {
        this.flag3 = flag3;
    }
    
    // Flag3 객체 가져오기
    public Flag3 getFlag3() {
        return flag3;
    }
    
    public boolean getEnemyDieCheck() {
    	return enemyDeadCheck;
    }
    
    public void setEnemyDieCheck(boolean enemyDeadCheck) {
    	this.enemyDeadCheck = enemyDeadCheck;
    }
    
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return this.camera;
    }
    
    public boolean getEnemyN_Touch_enemyG() {
    	return enemyN_Touch_enemyG;
    }
    
    public void setEnemyN_Touch_enemyG(boolean enemyN_Touch_enemyG) {
    	this.enemyN_Touch_enemyG = enemyN_Touch_enemyG;
    }
    
    public boolean getLeftToRight() {
    	return leftToRight;
    }
    
    public void setLeftToRight(boolean leftToRight) {
    	this.leftToRight = leftToRight;
    }
    public boolean getRightToLeft() {
    	return rightToLeft;
    }
    
    public void setRightToLeft(boolean rightToLeft) {
    	this.rightToLeft = rightToLeft;
    }
    
    public boolean getCheckToFire() {
    	return checkToFire;
    }
    
    public void setCheckToFire(boolean checkToFire) {
    	this.checkToFire = checkToFire;
    }
    
    public boolean getNextStage() {
    	return nextStage;
    }
    
    public void setNextStage(boolean nextStage) {
    	this.nextStage = nextStage;
    }
    
    public void clearObject() {
    	gameObjs.clear();
        enemiesG.clear();
        enemiesN.clear();
    }
}
