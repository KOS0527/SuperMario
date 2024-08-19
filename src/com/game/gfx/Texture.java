package com.game.gfx;

import java.awt.image.BufferedImage;

public class Texture {

    private final String PARENT_FOLDER = "/tile";

    private final int MARIO_L_COUNT = 22;
    private final int MARIO_S_COUNT = 15;
    private final int MARIO_F_COUNT = 21;
    
    private final int ENEMY_GUMBA_COUNT = 3;
    private final int ENEMY_NOKONOKO_COUNT = 4;
    
    private final int ITEM_BLOCK_FRAME_COUNT = 4;
    private final int COIN_FRAME_COUNT = 4;
    private final int FLOWER_FRAME_COUNT = 4;
    private final int STAR_FRAME_COUNT = 4;
    
    private final int Tile_1_COUNT = 28;
    private final int Tile_2_COUNT = 33;
    
    private final int CASTLE_FRAME_COUNT = 5;

    private BufferedImageLoader loader;
    private BufferedImage player_sheet, enemy_sheet, npc_sheet, block_sheet, tile_sheet, tile_sheet2,
                          game_over_sheet, intro_sheet;
    public BufferedImage[] mario_l, mario_s, mario_F, mario_SS, mario_sL_1,mario_sL_2, mario_sL_3, mario_sS_1, mario_sS_2, mario_sS_3,
    					   enemyG, enemyN, tile_1, tile_2, tile_3, tile_4, endtile,
                           pipe_1, cloud_1, cloud_2, debris_1, mountain1, mountain2, grass, sea1,
                           flag_1, flag_2, flag_3, endFlag, itemBlocks, coinBlocks, starBlock,
                           castle_1, castle_2, castle_3,castle_4, castle_5,
                           coin, mushroom, lifemushroom, flower, star, fireBall;

    public Texture(){
        mario_l = new BufferedImage[MARIO_L_COUNT];
        mario_s = new BufferedImage[MARIO_S_COUNT];
        mario_F = new BufferedImage[MARIO_F_COUNT];
        mario_SS = new BufferedImage[MARIO_S_COUNT];
        mario_sL_1 = new BufferedImage[MARIO_L_COUNT];
        mario_sL_2 = new BufferedImage[MARIO_L_COUNT];
        mario_sL_3 = new BufferedImage[MARIO_L_COUNT];
        mario_sS_1 = new BufferedImage[MARIO_S_COUNT];
        mario_sS_2 = new BufferedImage[MARIO_S_COUNT];
        mario_sS_3 = new BufferedImage[MARIO_S_COUNT];
        enemyG = new BufferedImage[ENEMY_GUMBA_COUNT];
        enemyN = new BufferedImage[ENEMY_NOKONOKO_COUNT];
        tile_1 = new BufferedImage[Tile_1_COUNT + Tile_2_COUNT];
        tile_2 = new BufferedImage[Tile_1_COUNT + Tile_2_COUNT];
        tile_3 = new BufferedImage[Tile_1_COUNT + Tile_2_COUNT];
        tile_4 = new BufferedImage[Tile_1_COUNT + Tile_2_COUNT];
        endtile = new BufferedImage[2];
        pipe_1 = new BufferedImage[6];
        cloud_1 = new BufferedImage[1];
        cloud_2 = new BufferedImage[1];
        mountain1 = new BufferedImage[3];
        mountain2 = new BufferedImage[3];
        grass = new BufferedImage[3];
        sea1 = new BufferedImage[1];
        castle_1 = new BufferedImage[CASTLE_FRAME_COUNT];
        castle_2 = new BufferedImage[CASTLE_FRAME_COUNT];
        castle_3 = new BufferedImage[CASTLE_FRAME_COUNT];
        castle_4 = new BufferedImage[CASTLE_FRAME_COUNT];
        castle_5 = new BufferedImage[CASTLE_FRAME_COUNT];
        flag_1 = new BufferedImage[1];
        flag_2 = new BufferedImage[1];
        flag_3 = new BufferedImage[1];
        endFlag = new BufferedImage[1];
        debris_1 = new BufferedImage[4];
        coin = new BufferedImage[COIN_FRAME_COUNT];
        mushroom = new BufferedImage[1];
        flower = new BufferedImage[FLOWER_FRAME_COUNT];
        star = new BufferedImage[STAR_FRAME_COUNT];
        lifemushroom = new BufferedImage[1];
        itemBlocks = new BufferedImage[ITEM_BLOCK_FRAME_COUNT];
        starBlock = new BufferedImage[ITEM_BLOCK_FRAME_COUNT];
        coinBlocks = new BufferedImage[2];
        fireBall = new BufferedImage[COIN_FRAME_COUNT];

        loader = new BufferedImageLoader();

        try {
            player_sheet = loader.loadImage(PARENT_FOLDER + "/NES - Super Mario Bros - Mario & Luigi.png");
            enemy_sheet = loader.loadImage(PARENT_FOLDER + "/NES - Super Mario Bros - Enemies & Bosses.png");
            npc_sheet = loader.loadImage(PARENT_FOLDER + "/NES - Super Mario Bros - Items Objects and NPCs.png");
            block_sheet = loader.loadImage(PARENT_FOLDER + "/NES - Super Mario Bros - Item and Brick Blocks.png");
            tile_sheet = loader.loadImage(PARENT_FOLDER + "/NES - Super Mario Bros - Tileset.png");
            tile_sheet2 = loader.loadImage(PARENT_FOLDER + "/NES - Super Mario Bros - Tileset2.png");
            game_over_sheet = loader.loadImage(PARENT_FOLDER + "/NES - Super Mario Bros - Time Up Game Over Screens.png");
            intro_sheet = loader.loadImage(PARENT_FOLDER + "/NES - Super Mario Bros Title Screen.png");

        } catch (Exception e){
            e.printStackTrace();
        }

        if(player_sheet != null || tile_sheet != null){
        	// 플레이어 & 적
            getPlayerTexture();
            getEnemyGumba();
            getEnemyNoko();
            
            // 맵오브젝트
            getTileTexture();
            endTile();
            
            getPipeTextures();
            
            getDebrisTextures();
            
            getCloudTextures();
            
            getMountain1();
            getMountain2();
            
            getGrassTexture();
            
            getFlag();
            
            getSeaTexture();
            
            getCastleTexture_1();
            
            // 블록 && 아이템
            getStarBlock();
            getItemBlock();
            loadCoinFrames();
            getCoinBlock();
            getMushroomTexture();
            getLifeMushroomTexture();
            getFlowerTexture();
            getStarTexture();
            getFireBallTextures();
        }else{
            throw new IllegalStateException("Failed to load necessary images");
        }
    }

    private void getPlayerTexture() {
        if(player_sheet == null){
            throw new IllegalStateException("player_sheet is null");
        }
        int x_off = 80;
        int y_off = 1;
        int width = 16;
        int height = 32;

        for(int i=0; i<MARIO_L_COUNT; i++){
            mario_l[i] = player_sheet.getSubimage(x_off + i*(width+1), y_off, width, height);
        }

        y_off += height+1;
        height = 16;

        for(int i=0; i<MARIO_S_COUNT; i++){
            mario_s[i] = player_sheet.getSubimage(x_off + i*(width+1), y_off, width, height);
        }
        
        int fy_off = 129;
        height = 32;
        
        for(int i=0; i<MARIO_F_COUNT; i++) {
        	mario_F[i] = player_sheet.getSubimage(x_off + i*(width+1), fy_off, width, height);
        }
        
        // Star large 효과
        int sLx1_off = 80;
        int sLy1_off = 192;
        int sL1_width = 16;
        int sL1_height = 32;
        for(int i=0; i<MARIO_L_COUNT; i++){
            mario_sL_1[i] = player_sheet.getSubimage(sLx1_off + i*(sL1_width+1), sLy1_off, sL1_width, sL1_height);
        }
        
        int sLx2_off = 80;
        int sLy2_off = 255;
        int sL2_width = 16;
        int sL2_height = 32;
        for(int i=0; i<MARIO_L_COUNT; i++){
            mario_sL_2[i] = player_sheet.getSubimage(sLx2_off + i*(sL2_width+1), sLy2_off, sL2_width, sL2_height);
        }
        
        int sLx3_off = 80;
        int sLy3_off = 318;
        int sL3_width = 16;
        int sL3_height = 32;
        for(int i=0; i<MARIO_L_COUNT; i++){
            mario_sL_3[i] = player_sheet.getSubimage(sLx3_off + i*(sL3_width+1), sLy3_off, sL3_width, sL3_height);
        }
        
        // Star small 효과
        int sSx1_off = 80;
        int sSy1_off = 225;
        int sS1_width = 16;
        int sS1_height = 16;
        for(int i=0; i<MARIO_S_COUNT; i++){
            mario_sS_1[i] = player_sheet.getSubimage(sSx1_off + i*(sS1_width+1), sSy1_off, sS1_width, sS1_height);
        }
        
        int sSx2_off = 80;
        int sSy2_off = 288;
        int sS2_width = 16;
        int sS2_height = 16;
        for(int i=0; i<MARIO_S_COUNT; i++){
            mario_sS_2[i] = player_sheet.getSubimage(sSx2_off + i*(sS2_width+1), sSy2_off, sS2_width, sS2_height);
        }
        
        int sSx3_off = 80;
        int sSy3_off = 351;
        int sS3_width = 16;
        int sS3_height = 16;
        for(int i=0; i<MARIO_S_COUNT; i++){
            mario_sS_3[i] = player_sheet.getSubimage(sSx3_off + i*(sS3_width+1), sSy3_off, sS3_width, sS3_height);
        }
    }
    
    private void getEnemyGumba() {
    	if(enemy_sheet == null) {
    		throw new IllegalStateException("enemy_sheet is null");
    	}
    	int x_off = 0;
        int y_off = 16;
        int width = 15;
        int height = 15;
        
	        for(int i=0; i<ENEMY_GUMBA_COUNT; i++) {
	        	enemyG[i] = enemy_sheet.getSubimage(x_off, y_off, width, height);
	        	x_off += 16;
	        }
        }
    
    private void getEnemyNoko() {
    	if(enemy_sheet == null) {
    		throw new IllegalStateException("enemy_sheet is null");
    	}
    	int[] x_off_values = {48, 63, 79, 95};
    	int y_off = 9;
    	int width = 15;
    	int height = 22;

    	for (int i = 0; i < ENEMY_NOKONOKO_COUNT; i++) {
    	    enemyN[i] = enemy_sheet.getSubimage(x_off_values[i], y_off, width, height);
    	}
    }

    private void getTileTexture() {
        if(tile_sheet == null){
            throw new IllegalStateException("tile_sheet is null");
        }

        int x_off = 0;
        int y_off = 0;
        int width = 16;
        int height = 16;

        for(int j=0; j<4; j++){
            for(int i=0; i<Tile_1_COUNT; i++){
                if(j == 0){
                    tile_1[i] = tile_sheet.getSubimage(x_off + i*width, y_off + j*height*2, width, height);
                }else if(j == 1){
                    tile_2[i] = tile_sheet.getSubimage(x_off + i*width, y_off + j*height*2, width, height);
                }else if(j == 2){
                    tile_3[i] = tile_sheet.getSubimage(x_off + i*width, y_off + j*height*2, width, height);
                }else if(j == 3){
                    tile_4[i] = tile_sheet.getSubimage(x_off + i*width, y_off + j*height*2, width, height);
                }
            }

            y_off += height+16;

            for(int i=0; i<Tile_2_COUNT; i++){
                if(j == 0){
                    tile_1[i + Tile_1_COUNT] = tile_sheet.getSubimage(x_off + i*width, y_off + j*height*2, width, height);
                }else if(j == 1){
                    tile_2[i + Tile_1_COUNT] = tile_sheet.getSubimage(x_off + i*width, y_off + j*height*2, width, height);
                }else if(j == 2){
                    tile_3[i + Tile_1_COUNT] = tile_sheet.getSubimage(x_off + i*width, y_off + j*height*2, width, height);
                }else if(j == 3){
                    tile_4[i + Tile_1_COUNT] = tile_sheet.getSubimage(x_off + i*width, y_off + j*height*2, width, height);
                }
            }
        }
    }
    
    private void endTile() {
    	if(tile_sheet== null){
            throw new IllegalStateException("pipe image is null");
        }
    	
    	int x_off = 0;
        int y_off = 16;
        int width = 16;
        int height = 16;
        
        endtile[0] = tile_sheet.getSubimage(x_off, y_off, width, height);
        endtile[1] = tile_sheet.getSubimage(x_off, y_off+32, width, height);
    }

    private void getPipeTextures(){
        if(tile_sheet == null){
            throw new IllegalStateException("pipe image is null");
        }

        int x_off = 0;
        int y_off = 16*8;
        int width = 32;
        int height = 16;

        pipe_1[0] = tile_sheet.getSubimage(x_off, y_off, width, height);
        pipe_1[1] = tile_sheet.getSubimage(x_off, y_off + height, width, height);
        pipe_1[2] = tile_sheet.getSubimage(x_off + width, y_off, width, height);
        pipe_1[3] = tile_sheet.getSubimage(x_off + width, y_off + height, width, height);
        pipe_1[4] = tile_sheet.getSubimage(x_off, y_off, width, height);
    }
    
    public void getDebrisTextures() {
    	if (block_sheet == null) {
            throw new IllegalStateException("block image is null");
        }
    	int x_off = 304;
    	int y_off = 112;
    	int width = 8;
    	int height = 8;
    	
    	debris_1[0] = block_sheet.getSubimage(x_off, y_off, width, height);
    	debris_1[1] = block_sheet.getSubimage(x_off, y_off, width, height);
    	debris_1[2] = block_sheet.getSubimage(x_off, y_off, width, height);
    	debris_1[3] = block_sheet.getSubimage(x_off, y_off, width, height);
    }
    
    public void getFireBallTextures() {
    	if(npc_sheet == null) {
    		throw new IllegalStateException("fireball image is null");
    	}
//    	int x_off = 96;
//    	int y_off = 144;
//    	int width = 16;
//    	int height = 16;

//    	for (int i = 0; i < COIN_FRAME_COUNT; i++) {
//    	    int x = x_off + (i % 2) * width; // x 좌표는 i의 짝수/홀수에 따라 결정
//    	    int y = y_off + (i / 2) * height; // y 좌표는 i의 홀수/짝수에 따라 결정
//    	    fireBall[i] = player_sheet.getSubimage(x, y, width, height);
//    	}
    	
    	fireBall[0] = npc_sheet.getSubimage(96, 144, 8, 8);
    	fireBall[1] = npc_sheet.getSubimage(104, 144, 8, 8);
    	fireBall[2] = npc_sheet.getSubimage(96, 152, 8, 8);
    	fireBall[3] = npc_sheet.getSubimage(104, 152, 8, 8);
    }
    
    public void getCloudTextures() {
    	if (tile_sheet == null) {
            throw new IllegalStateException("cloud image is null");
        }
    	int x_off = 88;
    	int y_off = 320;
    	int width = 32;
    	int height = 24;
    	
    	cloud_1[0] = tile_sheet.getSubimage(x_off, y_off, width, height);
    	
    	int x_off_cloud2 = 128;
	    int y_off_cloud2 = 320;
	    int width_cloud2 = 48;
	    int height_cloud2 = 16;

	    cloud_2[0] = tile_sheet.getSubimage(x_off_cloud2, y_off_cloud2, width_cloud2, height_cloud2);
    }
    
    public void getMountain1() {
    	if(tile_sheet == null){
            throw new IllegalStateException("maountain image is null");
        }
    	int x_off = 128;
    	int y_off = 128;
    	int width = 15;
    	int height = 15;
    	
    	mountain1[0] = tile_sheet.getSubimage(x_off, y_off, width, height);
    	mountain1[1] = tile_sheet.getSubimage(x_off + 16, y_off, width, height);
    	mountain1[2] = tile_sheet.getSubimage(x_off + 32, y_off, width, height);
    }
    
    public void getMountain2() {
    	if(tile_sheet == null){
            throw new IllegalStateException("maountain image is null");
        }
    	int x_off = 128;
    	int y_off = 144;
    	int width = 15;
    	int height = 15;
    	
    	mountain2[0] = tile_sheet.getSubimage(x_off, y_off, width, height);
    	mountain2[1] = tile_sheet.getSubimage(x_off + 16, y_off, width, height);
    	mountain2[2] = tile_sheet.getSubimage(x_off + 32, y_off, width, height);
    }
    
    public void getGrassTexture() {
    	if(tile_sheet == null){
            throw new IllegalStateException("grass image is null");
        }
    	
    	grass[0] = tile_sheet.getSubimage(304, 128, 47, 15);
    	grass[1] = tile_sheet.getSubimage(176, 144, 47, 15);
    	grass[2] = tile_sheet.getSubimage(304, 144, 47, 15);
    }
    
    public void getSeaTexture() {
    	if(tile_sheet == null){
            throw new IllegalStateException("sea image is null");
        }
    	int x_off = 48;
    	int y_off = 319;
    	int width = 16;
    	int height = 32;
    	
    	sea1[0] = tile_sheet.getSubimage(x_off, y_off, width, height);
    }
    
    public void getCastleTexture_1() {
    	if(tile_sheet2 == null){
            throw new IllegalStateException("castle image is null");
        }
//    	int x_off = 24;
//    	int y_off = 683;
//    	int width = 16;
//    	int height = 16;
//
//    	for(int i=0; i<CASTLE_FRAME_COUNT; i++){
//    		castle_1[i] = tile_sheet2.getSubimage(x_off + width * i, y_off, width, height);
//        }
    	
    	int x_off = 24;
    	int y_off = 683;
    	int width = 80;
    	int height = 80;
    	
		castle_1[0] = tile_sheet2.getSubimage(x_off, y_off, width, height);
    }
    
    public void getCastleTexture_2() {
    	if(tile_sheet2 == null){
            throw new IllegalStateException("castle image is null");
        }
    	int x_off = 24;
    	int y_off = 699;
    	int width = 16;
    	int height = 16;
    	
    	y_off += height;
    	
    	for(int i=0; i<CASTLE_FRAME_COUNT; i++){
            castle_2[i] = tile_sheet2.getSubimage(x_off + width * i, y_off, width, height);
        }
    }
    
    public void getCastleTexture_3() {
    	if(tile_sheet2 == null){
            throw new IllegalStateException("castle image is null");
        }
    	int x_off = 24;
    	int y_off = 684;
    	int width = 16;
    	int height = 16;
    	y_off += height;
    	y_off += height;
    	
    	for(int i=0; i<CASTLE_FRAME_COUNT; i++){
            castle_3[i] = tile_sheet2.getSubimage(x_off + width * i, y_off, width, height);
        }
    }
    
    public void getCastleTexture_4() {
    	if(tile_sheet2 == null){
            throw new IllegalStateException("castle image is null");
        }
    	int x_off = 24;
    	int y_off = 684;
    	int width = 16;
    	int height = 16;
    	
    	y_off += height;
    	y_off += height;
    	y_off += height;
    	
    	for(int i=0; i<CASTLE_FRAME_COUNT; i++){
            castle_4[i] = tile_sheet2.getSubimage(x_off + width * i, y_off, width, height);
        }
    }
    
    public void getCastleTexture_5() {
    	if(tile_sheet2 == null){
            throw new IllegalStateException("castle image is null");
        }
    	int x_off = 24;
    	int y_off = 684;
    	int width = 16;
    	int height = 16;
    	y_off += height;
    	y_off += height;
    	y_off += height;
    	y_off += height;
    	
    	for(int i=0; i<CASTLE_FRAME_COUNT; i++){
            castle_5[i] = tile_sheet2.getSubimage(x_off + width * i, y_off, width, height);
        }
    }
    public void getFlag() {
    	if(tile_sheet == null) {
    		throw new IllegalStateException("flag image is null");
    	}
	    	int x1_off = 224;
	    	int y1_off = 112;
	    	int width1 = 16;
	    	int height1 = 16;
	    	
	    	flag_1[0] = tile_sheet.getSubimage(x1_off, y1_off, width1, height1);

	    	int x2_off = 256;
	    	int y2_off = 144;
	    	int width2 = 16;
	    	int height2 = 16;
	    	
	    	flag_2[0] = tile_sheet.getSubimage(x2_off, y2_off, width2, height2); 

	    	int x3_off = 128;
	    	int y3_off = 16;
	    	int width3 = 16;
	    	int height3 = 16;
	    	
	    	flag_3[0] = npc_sheet.getSubimage(x3_off, y3_off, width3, height3);
	    	
	    	int x4_off = 128;
	    	int y4_off = 0;
	    	int width4 = 16;
	    	int height4 = 16;
	    	endFlag[0] = npc_sheet.getSubimage(x4_off, y4_off, width4, height4);
    }
    
    public void getMushroomTexture() {
    	if (npc_sheet == null) {
    		throw new IllegalStateException("mushroom image is null");
    	}
    	
    	int x_off = 0;
    	int y_off = 0;
    	int width = 16;
    	int height = 16;
    	
		mushroom[0]=npc_sheet.getSubimage(x_off, y_off, width, height);
    }
    
    public void getLifeMushroomTexture() {
    	if (npc_sheet == null) {
    		throw new IllegalStateException("lifemushroom image is null");
    	}
    	
    	int x_off = 16;
    	int y_off = 0;
    	int width = 16;
    	int height = 16;
    	
    	lifemushroom[0]=npc_sheet.getSubimage(x_off, y_off, width, height);
    }
    
    public void getFlowerTexture() {
    	if (npc_sheet == null) {
    		throw new IllegalStateException("flower image is null");
    	}
    	
    	int x_off = 0;
    	int y_off = 32;
    	int width = 16;
    	int height = 16;
    	
    	for (int i = 0; i < FLOWER_FRAME_COUNT; i++) {
    		flower[i] = npc_sheet.getSubimage(x_off + i * 16, y_off, width, height);
        }
    }
    
    public void getStarTexture() {
    	if (npc_sheet == null) {
    		throw new IllegalStateException("flower image is null");
    	}
    	
    	int x_off = 0;
    	int y_off = 48;
    	int width = 16;
    	int height = 16;
    	
    	for (int i = 0; i < STAR_FRAME_COUNT; i++) {
    		star[i] = npc_sheet.getSubimage(x_off + i * 16, y_off, width, height);
        }
    }

    public void loadCoinFrames() {
    	if (npc_sheet == null) {
            throw new IllegalStateException("coin image is null");
        }
    	
    	int x_off = 0;
    	int y_off = 96;
    	int width = 16;
    	int height = 16;
    	
    	for (int i = 0; i < COIN_FRAME_COUNT; i++) {
    		coin[i] = npc_sheet.getSubimage(x_off + i * 16, y_off, width, height);
        }
    }
    
    public void getItemBlock() {
    	if(block_sheet == null) {
    		throw new IllegalStateException("itemBlock image is null");
    	}
    	
    	int x_off = 80;
    	int y_off = 112;
    	int width = 16;
    	int height = 16;
    	
    	for (int i = 0; i < ITEM_BLOCK_FRAME_COUNT; i++) {
    		itemBlocks[i] = block_sheet.getSubimage(x_off + i * 16, y_off, width, height);
    	}
    }
    
    public void getStarBlock() {
    	if(block_sheet == null) {
    		throw new IllegalStateException("itemBlock image is null");
    	}
    	
    	int x_off = 80;
    	int y_off = 112;
    	int width = 16;
    	int height = 16;
    	
    	for (int i = 0; i < ITEM_BLOCK_FRAME_COUNT; i++) {
    		starBlock[i] = block_sheet.getSubimage(x_off + i * 16, y_off, width, height);
    	}
    }
    
    public void getCoinBlock() {
    	if(block_sheet == null) {
    		throw new IllegalStateException("coinBlock image is null");
    	}
    	
    	int x_off = 272;
    	int y_off = 192;
    	int width = 16;
    	int height = 16;
    	
    	for (int i = 0; i < 2; i++) {
    		coinBlocks[i] = block_sheet.getSubimage(x_off + i * 16, y_off, width, height);
    	}
    }

    public BufferedImage[] getMarioL() {
        return mario_l;
    }

    public BufferedImage[] getMarioS() {
        return mario_s;
    }
    
    public BufferedImage[] getMarioF() {
    	return mario_F;
    }
    
    public BufferedImage[] getMario_sL_1() {
    	return mario_sL_1;
    }
    
    public BufferedImage[] getMario_sL_2() {
    	return mario_sL_2;
    }
    
    public BufferedImage[] getMario_sL_3() {
    	return mario_sL_3;
    }
    
    public BufferedImage[] getMario_sS_1() {
    	return mario_sS_1;
    }
    
    public BufferedImage[] getMario_sS_2() {
    	return mario_sS_2;
    }
    
    public BufferedImage[] getMario_sS_3() {
    	return mario_sS_3;
    }
    
    public BufferedImage[] getEnemyG() {
        return enemyG;
    }
    
    public BufferedImage[] getEnemyN() {
        return enemyN;
    }

    public BufferedImage[] getTile1() {
        return tile_1;
    }

    public BufferedImage[] getTile2() {
        return tile_2;
    }

    public BufferedImage[] getTile3() {
        return tile_3;
    }

    public BufferedImage[] getTile4() {
        return tile_4;
    }
    
    public BufferedImage[] getEndTile() {
        return endtile;
    }
    public BufferedImage[] getPipe_1() {
        return pipe_1;
    }

    public BufferedImage[] getFlag_1() {
    	return flag_1;
    }
 
    public BufferedImage[] getFlag_2() {
    	return flag_2;
    }
    
    public BufferedImage[] getFlag_3() {
    	return flag_3;
    }
    
    public BufferedImage[] getEndFlag() {
    	return endFlag;
    }

    public BufferedImage[] getDebris1() {
    	return debris_1;
    }
    
    public BufferedImage[] getCloud_1() {
    	return cloud_1;
    }
    
    public BufferedImage[] getCloud_2() {
    	return cloud_2;
    }
    
    public BufferedImage[] getMountain_1() {
    	return mountain1;
    }
    
    public BufferedImage[] getMountain_2() {
    	return mountain2;
    }
    
    public BufferedImage[] getGrass() {
    	return grass;
    }
    
    public BufferedImage[] getSea1() {
    	return sea1;
    }
    
    public BufferedImage[] getCastle_1() {
    	return castle_1;
    }
    
    public BufferedImage[] getCastle_2() {
    	return castle_2;
    }
    
    public BufferedImage[] getCastle_3() {
    	return castle_3;
    }
    
    public BufferedImage[] getCastle_4() {
    	return castle_4;
    }
    
    public BufferedImage[] getCastle_5() {
    	return castle_5;
    }

	public BufferedImage[] getCoinAnimation() {
		return coin;
	}
	
	public BufferedImage[] getItemBlocks() {
		return itemBlocks;
	}
	
	public BufferedImage[] getStarBlocks() {
		return itemBlocks;
	}
	
	public BufferedImage[] getCoinBlocks() {
		return coinBlocks;
	}
	
	public BufferedImage[] getMushroom() {
		return mushroom;
	}
	
	public BufferedImage[] getLifeMushroom() {
		return lifemushroom;
	}
	
	public BufferedImage[] getFlower() {
		return flower;
	}
	
	public BufferedImage[] getStar() {
		return star;
	}
	
	public BufferedImage[] getFireBall() {
		return fireBall;
	}
}
