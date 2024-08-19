package com.game.gfx;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager {
	
	private final String SOUND_FOLDER = "/sounds";
	private static SoundManager instance;
	private boolean isPlaying;
	
	private Map<String, Clip> playingSounds;
	
	private SoundManager() {
		playingSounds = new HashMap<>();
    }

	public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
	
	public void playSound(String soundFilePath) {
        try {
        	InputStream inputStream = SoundManager.class.getResourceAsStream(soundFilePath);
            if (inputStream == null) {
                System.out.println("Resource not found: " + soundFilePath);
                return;
            }
        	
            AudioInputStream ais = AudioSystem.getAudioInputStream(inputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
            
            playingSounds.put(soundFilePath, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(soundFilePath + "Error");
        }
    }
	
	public void stopSound(String soundFilePath) {
        Clip clip = playingSounds.get(soundFilePath);
        if (clip != null) {
            clip.stop();
            clip.close();
            playingSounds.remove(soundFilePath);
        }
    }

    public void stopAllSounds() {
        for (Clip clip : playingSounds.values()) {
            if (clip != null) {
                clip.stop();
                clip.close();
            }
        }
        playingSounds.clear();
    }
    
    public boolean isPlaying() {
        for (Clip clip : playingSounds.values()) {
            if (clip.isActive()) {
                return true;
            }
        }
        return false;
    }
	
	// Stage Sound
	public void playStageSound() {
        playSound(SOUND_FOLDER + "/bgm/01.Ground-Theme.wav");
    }

	public void timeLimit() {
    	playSound(SOUND_FOLDER + "/bgm/02.Ground-Theme-Hurry.wav");
    }
	
	public void playStage2Sound() {
        playSound(SOUND_FOLDER + "/bgm/03. Underground BGM.wav");
    }

	public void timeLimit2() {
    	playSound(SOUND_FOLDER + "/bgm/04. Underground Theme (Hurry!).wav");
    }
	
	public void invincibilityTime() {
    	playSound(SOUND_FOLDER + "/bgm/05.Invincibility-Theme.wav");
    }
	
    // Effect Sound
    public void playJumpSound() {
        playSound(SOUND_FOLDER + "/effect/Small Jump.wav");
    }
    
    public void bigMarioJump() {
    	playSound(SOUND_FOLDER + "/effect/Big Jump.wav");
    }
    
    public void blockDestroy() {
    	playSound(SOUND_FOLDER + "/effect/Break.wav");
    }
    
    public void powerUp() {
        playSound(SOUND_FOLDER + "/effect/Powerup.wav");
    }
    
    public void powerDown() {
        playSound(SOUND_FOLDER + "/effect/Powerdown.wav");
    }
    
    public void getCoinSound() {
        playSound(SOUND_FOLDER + "/effect/coin.wav");
    }
    
    public void getMushroomSound() {
    	playSound(SOUND_FOLDER + "/effect/Vine.wav");
    }
    
    public void getLifeMushroomSound() {
    	playSound(SOUND_FOLDER + "/effect/1up.wav");
    }
    
    public void emptyItemBlock() {
    	playSound(SOUND_FOLDER + "/effect/Bump.wav");
    }
    
    public void playDie() {
    	playSound(SOUND_FOLDER + "/effect/Die.wav");
    }
    
    public void playKick() {
    	playSound(SOUND_FOLDER + "/effect/Kick.wav");
    }
    
    public void jumpToEnemy() {
    	playSound(SOUND_FOLDER + "/effect/Squish.wav");
    }
    
    public void isFireBall() {
    	playSound(SOUND_FOLDER + "/effect/Fire Ball.wav");
    }
    
    public void usePipe() {
    	playSound(SOUND_FOLDER + "/effect/Warp.wav");
    }
    
    public void touchFlag() {
    	playSound(SOUND_FOLDER + "/effect/Flagpole.wav");
    }
   
    public void gameClear() {
    	playSound(SOUND_FOLDER + "/effect/StageClear.wav");
    }
    
    public void gamePause() {
    	playSound(SOUND_FOLDER + "/effect/Pause.wav");
    }
}
