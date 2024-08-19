package com.game.main.util;

import java.awt.image.BufferedImage;

import com.game.gfx.BufferedImageLoader;
import com.game.gfx.SoundManager;
import com.game.object.Block;
import com.game.object.Castle_1;
import com.game.object.Cloud;
import com.game.object.Coin2;
import com.game.object.CoinBlocks;
import com.game.object.EndFlag;
import com.game.object.EndTile;
import com.game.object.EnemyG;
import com.game.object.EnemyN;
import com.game.object.Flag1;
import com.game.object.Flag2;
import com.game.object.Flag3;
import com.game.object.Grass;
import com.game.object.ItemBlocks;
import com.game.object.Mountain1;
import com.game.object.Mountain2;
import com.game.object.Pipe;
import com.game.object.Player;
import com.game.object.Sea;
import com.game.object.TransparentBlock;
import com.game.object.util.Handler;

public class LevelHandler {

	private final String PARENT_FOLDER = "/level";
	
	private BufferedImageLoader loader;
	private BufferedImage levelTiles;
	private Handler handler;
	private int currentLevel = 1;
	
	SoundManager soundManager = SoundManager.getInstance();
	
	public LevelHandler(Handler handler) {
		this.handler = handler;
		loader = new BufferedImageLoader();
	}
	
	public void start() {
		handler.clearObject();
		
		if(currentLevel==1) {
			setLevel(PARENT_FOLDER + "/1_"+currentLevel+"A.png");
			loadCharacters(PARENT_FOLDER + "/1_"+currentLevel+"B.png");
		} else if(currentLevel==2) {
			setLevel(PARENT_FOLDER + "/1_"+currentLevel+"A.png");
			loadCharacters(PARENT_FOLDER + "/1_"+currentLevel+"B.png");
		}
	}
	
	public void setLevel(String levelTilesPath) {
		System.out.println("Loading level from: " + levelTilesPath);
		this.levelTiles = loader.loadImage(levelTilesPath);
		
		int width = levelTiles.getWidth();
		int height = levelTiles.getHeight();
		
		if(currentLevel==1) {
			for(int i = 0; i < height; i++) {
				
			    for(int j = 0; j < width; j++) {
			        // 여기서 getRGB() 메서드 사용
			        int pixel = levelTiles.getRGB(j, i); // j는 x 좌표, i는 y 좌표
			        int red = (pixel >> 16) & 0xff;
			        int green = (pixel >> 8) & 0xff;
			        int blue = pixel & 0xff; // 수정: blue는 마스크가 필요 없습니다.
	
			        // RGB 값에 따라 처리
			        if(red == 255 && green == 255 && blue == 255) {
			            continue;
			        }
			        
			        // 블록
			        if (red == green && red == blue) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 0, 3, 1, soundManager)); // 예시로 기존의 블록 추가
		            } else if (blue == 10 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 1, 3, 1, soundManager)); // 예시로 추가할 블록 1
		            } else if (blue == 15 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 2, 3, 1, soundManager)); // 예시로 추가할 블록 2
		            } else if (blue == 20 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 3, 3,1, soundManager)); // 추가할 블록 3
		            } else if (blue == 25 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 4, 3,1, soundManager)); // 추가할 블록 4
		            } else if (blue == 30 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 5, 3,1, soundManager)); // 추가할 블록 5
		            } else if (blue == 35 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 6, 3,1, soundManager)); // 추가할 블록 6
		            } else if (blue == 40 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 7, 3,1, soundManager)); // 추가할 블록 7
		            } else if (blue == 45 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 28, 3,1, soundManager)); // 추가할 블록 8
		            } else if (blue == 50 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 30, 3,1, soundManager)); // 추가할 블록 8
		            }  else if (blue == 0 && green == 0 && red == 5) {
		            	handler.addObj(new EndTile(j * 16, i * 16, 16, 16, 0, 3,1, soundManager)); // 추가할 블록 8
		            } else if (blue == 45 && green == 0 && red == 5) {
		            	handler.addObj(new EndTile(j * 16, i * 16, 16, 16, 1, 3,1, soundManager)); // 추가할 블록 8
		            }
			        
			        // 파이프
			        else if(blue == 0 && green == 0 && red == 6) {
			            handler.addObj(new Pipe(j * 16, i * 16, 32, 16, 0, 3,2, false, handler));
			        }else if(blue == 0 && green == 0 && red == 10) {
			            handler.addObj(new Pipe(j * 16, i * 16, 32, 16, 1, 3,2, false, handler));
			        }else if(blue == 0 && green == 0 && red == 15) {
			            handler.addObj(new Pipe(j * 16, i * 16, 32, 16, 2, 3,2, false, handler));
			        }else if(blue == 0 && green == 0 && red == 20) {
			            handler.addObj(new Pipe(j * 16, i * 16, 32, 16, 3, 3,2, false, handler));
			        }else if(blue == 0 && green == 0 && red == 25) {
			            handler.addObj(new Pipe(j * 16, i * 16, 32, 16, 0, 3,2, true, handler));
			        }else if(blue == 0 && green == 0 && red == 30) {
			            handler.addObj(new Pipe(j * 16, i * 16, 32, 16, 2, 3,2, true, handler));
			        }
			        
			        // 구름
			        else if(blue == 5 && green == 0 && red == 0) {
			        	handler.addObj(new Cloud(j * 16, i * 16, 30, 23, 0, 4, 1));
			        }
			        
			        // 바다
			        else if(blue == 50 && green == 0 && red == 0) {
			        	handler.addObj(new Sea(j * 16, i * 16, 11, 23, 0, 4, 1, false));
			        }
			        
			        // 산
			        else if(blue == 6 && green == 0 && red == 0 ) {
			        	handler.addObj(new Mountain1(j * 16, i * 16, 16, 16, 0, 3, 1)); // 예시로 기존의 블록 추가
			        }else if(blue == 6 && green == 1 && red == 0 ) {
			        	handler.addObj(new Mountain1(j * 16, i * 16, 16, 16, 1, 3, 1)); // 예시로 기존의 블록 추가
			        }
			        else if(blue == 6 && green == 2 && red == 0 ) {
			        	handler.addObj(new Mountain1(j * 16, i * 16, 16, 16, 2, 3, 1)); // 예시로 기존의 블록 추가
			        }
			        else if(blue == 7 && green == 0 && red == 0 ) {
			        	handler.addObj(new Mountain2(j * 16, i * 16, 16, 16, 0, 3, 1)); // 예시로 기존의 블록 추가
			        }else if(blue == 7 && green == 1 && red == 0 ) {
			        	handler.addObj(new Mountain2(j * 16, i * 16, 16, 16, 1, 3, 1)); // 예시로 기존의 블록 추가
			        }
			        else if(blue == 7 && green == 2 && red == 0 ) {
			        	handler.addObj(new Mountain2(j * 16, i * 16, 16, 16, 2, 3, 1)); // 예시로 기존의 블록 추가
			        }
			        
			        // 풀
			        else if(blue == 0 && green == 21 && red == 22 ) {
			        	handler.addObj(new Grass(j * 16, i * 16, 32, 16, 0, 3, 1)); // 예시로 기존의 블록 추가
			        }else if(blue == 0 && green == 21 && red == 23 ) {
			        	handler.addObj(new Grass(j * 16, i * 16, 64, 16, 1, 3, 1)); // 예시로 기존의 블록 추가
			        }else if(blue == 0 && green == 21 && red == 24 ) {
			        	handler.addObj(new Grass(j * 16, i * 16, 64, 16, 2, 3, 1)); // 예시로 기존의 블록 추가
			        }
			        
			        // 성
			        else if(blue == 5 && green == 0 && red == 1) {
			        	handler.addObj(new Castle_1(j * 16, i * 16, 120, 80, 0, 3, 2,handler)); // 예시로 기존의 블록 추가
			        }else if(blue == 5 && green == 0 && red == 2) {
			        	handler.addObj(new Castle_1(j * 16, i * 16, 16, 16, 1, 3, 2, handler)); // 예시로 기존의 블록 추가
			        }else if(blue == 5 && green == 0 && red == 3) {
			        	handler.addObj(new Castle_1(j * 16, i * 16, 16, 16, 2, 3, 2,handler)); // 예시로 기존의 블록 추가
			        }else if(blue == 5 && green == 0 && red == 4) {
			        	handler.addObj(new Castle_1(j * 16, i * 16, 16, 16, 3, 3, 2,handler)); // 예시로 기존의 블록 추가
			        }else if(blue == 5 && green == 0 && red == 5) {
			        	handler.addObj(new Castle_1(j * 16, i * 16, 16, 16, 4, 3, 2,handler)); // 예시로 기존의 블록 추가
			        }
			        
			        // Flag
			        else if(blue == 0 && green == 1 && red == 0) {
			        	handler.addObj(new Flag1(j * 16, i * 16, 16, 16, 1, 3, 1,soundManager));
			        }else if(blue == 0 && green == 2 && red == 0) {
			        	handler.addObj(new Flag2(j * 16, i * 16, 16, 240, 2, 3, 1,soundManager, handler));
			        }else if(blue == 0 && green == 3 && red == 0) {
			        	handler.addObj(new Flag3((j * 16)+9, i * 16, 16, 16, 3, 3, 1,handler));
			        }else if(blue == 0 && green == 4 && red == 0) {
			        	handler.addObj(new EndFlag((j * 16)+9, i * 16, 16, 16, 0, 3, 1,soundManager, handler));
			        }
			        
			        // MushroomBlocks & ItemBlocks
			        else if (blue == 0 && green == 2 && red == 3) {
			            handler.addObj(new ItemBlocks(j * 16, i * 16, 16, 16, 0, 3, true, false, false, false, 1,soundManager, handler)); // 코인이 있는 블록
			        } else if (blue == 0 && green == 2 && red == 4) {
			            handler.addObj(new ItemBlocks(j * 16, i * 16, 16, 16, 0, 3, false, true, false, false, 1,soundManager, handler)); // 버섯이 있는 블록
			        } else if (blue == 0 && green == 2 && red == 5) {
			            handler.addObj(new ItemBlocks(j * 16, i * 16, 16, 16, 0, 3, false, false, true, false, 1,soundManager, handler)); // 버섯이 있는 블록
			        } else if (blue == 0 && green == 2 && red == 6) {
			            handler.addObj(new ItemBlocks(j * 16, i * 16, 16, 16, 0, 3, false, false, false, true, 1,soundManager, handler)); // 버섯이 있는 블록
			        }
			        // ItemBlocks
			        else if (blue == 2 && green == 1 && red == 3) {
			            handler.addObj(new CoinBlocks(j * 16, i * 16, 16, 16, 0, 3, 1, true, false, soundManager, handler)); // 블록모양 코인블록
			        } else if (blue == 2 && green == 1 && red == 4) {
			            handler.addObj(new CoinBlocks(j * 16, i * 16, 16, 16, 0, 3, 1, false, true, soundManager, handler)); // 블록모양 코인블록
			        }
			        // 투명 아이템블록
				    else if (blue == 1 && green == 0 && red == 0) {
			            handler.addObj(new TransparentBlock(j * 16, i * 16, 16, 16, 0, 3, 1,true, true, soundManager, handler));
			        }
			        
			        // 코인 이미지
				    else if (blue == 132 && green == 133 && red == 134) {
			            handler.addObj(new Coin2(j * 16, i * 16, 16, 16, 0, 3, 1, soundManager, handler));
			        }
			    }
			}
		} else if(currentLevel==2) {
for(int i = 0; i < height; i++) {
				
			    for(int j = 0; j < width; j++) {
			        // 여기서 getRGB() 메서드 사용
			        int pixel = levelTiles.getRGB(j, i); // j는 x 좌표, i는 y 좌표
			        int red = (pixel >> 16) & 0xff;
			        int green = (pixel >> 8) & 0xff;
			        int blue = pixel & 0xff; // 수정: blue는 마스크가 필요 없습니다.
	
			        // RGB 값에 따라 처리
			        if(red == 255 && green == 255 && blue == 255) {
			            continue;
			        }
			        
			        // 블록
			        if (red == green && red == blue) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 0, 3, 1, soundManager)); // 예시로 기존의 블록 추가
		            } else if (blue == 10 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 1, 3, 1, soundManager)); // 예시로 추가할 블록 1
		            } else if (blue == 15 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 2, 3, 1, soundManager)); // 예시로 추가할 블록 2
		            } else if (blue == 20 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 3, 3,1, soundManager)); // 추가할 블록 3
		            } else if (blue == 25 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 4, 3,1, soundManager)); // 추가할 블록 4
		            } else if (blue == 30 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 5, 3,1, soundManager)); // 추가할 블록 5
		            } else if (blue == 35 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 6, 3,1, soundManager)); // 추가할 블록 6
		            } else if (blue == 40 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 7, 3,1, soundManager)); // 추가할 블록 7
		            } else if (blue == 45 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 28, 3,1, soundManager)); // 추가할 블록 8
		            } else if (blue == 50 && green == 0 && red == 0) {
		                handler.addObj(new Block(j * 16, i * 16, 16, 16, 30, 3,1, soundManager)); // 추가할 블록 8
		            }
		            // endTile
		            else if (blue == 0 && green == 0 && red == 5) {
		            	handler.addObj(new EndTile(j * 16, i * 16, 16, 16, 0, 3,1, soundManager)); // 추가할 블록 8
		            }else if (blue == 45 && green == 0 && red == 5) {
		            	handler.addObj(new EndTile(j * 16, i * 16, 16, 16, 1, 3,1, soundManager)); // 추가할 블록 8
		            }
			        
			        // 파이프
			        else if(blue == 0 && green == 0 && red == 6) {
			            handler.addObj(new Pipe(j * 16, i * 16, 32, 16, 0, 3,2, false, handler));
			        }else if(blue == 0 && green == 0 && red == 10) {
			            handler.addObj(new Pipe(j * 16, i * 16, 32, 16, 1, 3,2, false, handler));
			        }else if(blue == 0 && green == 0 && red == 15) {
			            handler.addObj(new Pipe(j * 16, i * 16, 32, 16, 2, 3,2, false, handler));
			        }else if(blue == 0 && green == 0 && red == 20) {
			            handler.addObj(new Pipe(j * 16, i * 16, 32, 16, 3, 3,2, false, handler));
			        }else if(blue == 0 && green == 0 && red == 25) {
			            handler.addObj(new Pipe(j * 16, i * 16, 32, 16, 0, 3,2, true, handler));
			        }else if(blue == 0 && green == 0 && red == 30) {
			            handler.addObj(new Pipe(j * 16, i * 16, 32, 16, 2, 3,2, true, handler));
			        }
			        
			        // 구름
			        else if(blue == 5 && green == 0 && red == 0) {
			        	handler.addObj(new Cloud(j * 16, i * 16, 30, 23, 0, 4, 1));
			        }
			        
			        // 바다
			        else if(blue == 50 && green == 0 && red == 0) {
			        	handler.addObj(new Sea(j * 16, i * 16, 11, 23, 0, 4, 1, false));
			        }
			        
			        // 산
			        else if(blue == 6 && green == 0 && red == 0 ) {
			        	handler.addObj(new Mountain1(j * 16, i * 16, 16, 16, 0, 3, 1)); // 예시로 기존의 블록 추가
			        }else if(blue == 6 && green == 1 && red == 0 ) {
			        	handler.addObj(new Mountain1(j * 16, i * 16, 16, 16, 1, 3, 1)); // 예시로 기존의 블록 추가
			        }
			        else if(blue == 6 && green == 2 && red == 0 ) {
			        	handler.addObj(new Mountain1(j * 16, i * 16, 16, 16, 2, 3, 1)); // 예시로 기존의 블록 추가
			        }
			        else if(blue == 7 && green == 0 && red == 0 ) {
			        	handler.addObj(new Mountain2(j * 16, i * 16, 16, 16, 0, 3, 1)); // 예시로 기존의 블록 추가
			        }else if(blue == 7 && green == 1 && red == 0 ) {
			        	handler.addObj(new Mountain2(j * 16, i * 16, 16, 16, 1, 3, 1)); // 예시로 기존의 블록 추가
			        }
			        else if(blue == 7 && green == 2 && red == 0 ) {
			        	handler.addObj(new Mountain2(j * 16, i * 16, 16, 16, 2, 3, 1)); // 예시로 기존의 블록 추가
			        }
			        
			        // 풀
			        else if(blue == 0 && green == 21 && red == 22 ) {
			        	handler.addObj(new Grass(j * 16, i * 16, 32, 16, 0, 3, 1)); // 예시로 기존의 블록 추가
			        }else if(blue == 0 && green == 21 && red == 23 ) {
			        	handler.addObj(new Grass(j * 16, i * 16, 64, 16, 1, 3, 1)); // 예시로 기존의 블록 추가
			        }else if(blue == 0 && green == 21 && red == 24 ) {
			        	handler.addObj(new Grass(j * 16, i * 16, 64, 16, 2, 3, 1)); // 예시로 기존의 블록 추가
			        }
			        
			        // 성
			        else if(blue == 5 && green == 0 && red == 1) {
			        	handler.addObj(new Castle_1(j * 16, i * 16, 120, 80, 0, 3, 2,handler)); // 예시로 기존의 블록 추가
			        }else if(blue == 5 && green == 0 && red == 2) {
			        	handler.addObj(new Castle_1(j * 16, i * 16, 16, 16, 1, 3, 2, handler)); // 예시로 기존의 블록 추가
			        }else if(blue == 5 && green == 0 && red == 3) {
			        	handler.addObj(new Castle_1(j * 16, i * 16, 16, 16, 2, 3, 2,handler)); // 예시로 기존의 블록 추가
			        }else if(blue == 5 && green == 0 && red == 4) {
			        	handler.addObj(new Castle_1(j * 16, i * 16, 16, 16, 3, 3, 2,handler)); // 예시로 기존의 블록 추가
			        }else if(blue == 5 && green == 0 && red == 5) {
			        	handler.addObj(new Castle_1(j * 16, i * 16, 16, 16, 4, 3, 2,handler)); // 예시로 기존의 블록 추가
			        }
			        
			        // Flag
			        else if(blue == 0 && green == 1 && red == 0) {
			        	handler.addObj(new Flag1(j * 16, i * 16, 16, 16, 1, 3, 1,soundManager));
			        }else if(blue == 0 && green == 2 && red == 0) {
			        	handler.addObj(new Flag2(j * 16, i * 16, 16, 240, 2, 3, 1,soundManager, handler));
			        }else if(blue == 0 && green == 3 && red == 0) {
			        	handler.addObj(new Flag3((j * 16)+9, i * 16, 16, 16, 3, 3, 1,handler));
			        }else if(blue == 0 && green == 4 && red == 0) {
			        	handler.addObj(new EndFlag((j * 16)+9, i * 16, 16, 16, 0, 3, 1,soundManager, handler));
			        }
			        
			        // MushroomBlocks & ItemBlocks
			        else if (blue == 0 && green == 2 && red == 3) {
			            handler.addObj(new ItemBlocks(j * 16, i * 16, 16, 16, 0, 3, true, false, false, false, 1,soundManager, handler)); // 코인이 있는 블록
			        } else if (blue == 0 && green == 2 && red == 4) {
			            handler.addObj(new ItemBlocks(j * 16, i * 16, 16, 16, 0, 3, false, true, false, false, 1,soundManager, handler)); // 버섯이 있는 블록
			        } else if (blue == 0 && green == 2 && red == 5) {
			            handler.addObj(new ItemBlocks(j * 16, i * 16, 16, 16, 0, 3, false, false, true, false, 1,soundManager, handler)); // 버섯이 있는 블록
			        } else if (blue == 0 && green == 2 && red == 6) {
			            handler.addObj(new ItemBlocks(j * 16, i * 16, 16, 16, 0, 3, false, false, false, true, 1,soundManager, handler)); // 버섯이 있는 블록
			        }
			        // ItemBlocks
			        else if (blue == 2 && green == 1 && red == 3) {
			            handler.addObj(new CoinBlocks(j * 16, i * 16, 16, 16, 0, 3, 1, true, false, soundManager, handler)); // 블록모양 코인블록
			        } else if (blue == 2 && green == 1 && red == 4) {
			            handler.addObj(new CoinBlocks(j * 16, i * 16, 16, 16, 0, 3, 1, false, true, soundManager, handler)); // 블록모양 코인블록
			        }
			        // 투명 아이템블록
				    else if (blue == 1 && green == 0 && red == 0) {
			            handler.addObj(new TransparentBlock(j * 16, i * 16, 16, 16, 0, 3, 1,true, true, soundManager, handler));
			        }
			        
			        // 코인 이미지
				    else if (blue == 132 && green == 133 && red == 134) {
			            handler.addObj(new Coin2(j * 16, i * 16, 16, 16, 0, 3, 1, soundManager, handler));
			        }
			    }
			}
		}
	}
	
	 void loadCharacters(String levelCharacterPath) {
	    System.out.println("Loading characters from: " + levelCharacterPath);
	    this.levelTiles = loader.loadImage(levelCharacterPath);

	    int width = levelTiles.getWidth();
	    int height = levelTiles.getHeight();
	    
	    if(currentLevel==1) {
	    	for(int i = 0; i < height; i++) {
		        for(int j = 0; j < width; j++) {
		            int pixel = levelTiles.getRGB(j, i); // j는 x 좌표, i는 y 좌표
		            int red = (pixel >> 16) & 0xff;
		            int green = (pixel >> 8) & 0xff;
		            int blue = pixel & 0xff;

		            if(red == 255 && green == 255 && blue == 255) {
		                continue; // 흰색 픽셀은 건너뜁니다.
		            }
		            
		            if(red == 10 && green == 10 && blue == 10) {
		            	handler.setPlayer(new Player(j * 16, i * 16, 3, 3, handler, null));
		            }
		            // Enemy
		            else if(red == 20 && green == 20 && blue == 20) {
	                    handler.addEnemyG(new EnemyG(j * 16, i * 16, 3, 2, handler, soundManager));
	                }else if(red == 25 && green == 25 && blue == 25) {
	                	handler.addEnemyN(new EnemyN(j * 16, i * 16, 3, 2, handler, soundManager));
	                }
		        }
		    }
	    } else if(currentLevel==2) {
	    	for(int i = 0; i < height; i++) {
		        for(int j = 0; j < width; j++) {
		            int pixel = levelTiles.getRGB(j, i); // j는 x 좌표, i는 y 좌표
		            int red = (pixel >> 16) & 0xff;
		            int green = (pixel >> 8) & 0xff;
		            int blue = pixel & 0xff;

		            if(red == 255 && green == 255 && blue == 255) {
		                continue; // 흰색 픽셀은 건너뜁니다.
		            }
		            
		            if(red == 10 && green == 10 && blue == 10) {
		            	handler.setPlayer(new Player(j * 16, i * 16, 3, 3, handler, null));
		            }
		            // Enemy
		            else if(red == 20 && green == 20 && blue == 20) {
	                    handler.addEnemyG(new EnemyG(j * 16, i * 16, 3, 2, handler, soundManager));
	                }else if(red == 25 && green == 25 && blue == 25) {
	                	handler.addEnemyN(new EnemyN(j * 16, i * 16, 3, 2, handler, soundManager));
	                }
		        }
		    }
	    }
	}
	 
	 public int getCurrentLevel() {
		 if(handler.getNextStage()) {
			 setCurrentLevel(2);
			 System.out.println("currentLevel : "+currentLevel);
		 }
		 return currentLevel;
	 }
	 
	 public void setCurrentLevel(int currentLevel) {
		 this.currentLevel = currentLevel;
	 }
	 
	 public void nextLevel() {
	        currentLevel++;
	        start(); // 다음 레벨을 로드합니다.
	 }
}
