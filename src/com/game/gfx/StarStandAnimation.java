package com.game.gfx;

import java.awt.image.BufferedImage;

public class StarStandAnimation {
    private BufferedImage[][] frames; // 각 애니메이션의 프레임을 저장하는 2D 배열
    private int numFrames; // 각 애니메이션의 프레임 수
    private int currentFrame; // 현재 프레임
    private int delay; // 애니메이션 업데이트 지연 시간
    private int time; // 타이머
    private boolean isIdle; // 대기 상태 플래그
    
    public StarStandAnimation(BufferedImage[] anim1, BufferedImage[] anim2, BufferedImage[] anim3, int delay, int currentFrame) {
        frames = new BufferedImage[][] { anim1, anim2, anim3 };
        numFrames = anim1.length; // 모든 애니메이션의 프레임 수는 동일하다고 가정
        this.delay = delay;
        this.time = 0;
        this.currentFrame = currentFrame;
        this.isIdle = false;
    }

    public void setIdle(boolean isIdle) {
        this.isIdle = isIdle;
    }

    public void tick() {
        if (isIdle) {
            time++;
            if (time >= delay) {
                time = 0;
                currentFrame++;
                if (currentFrame >= frames[0].length) {
                    currentFrame = 0; // Loop the idle animation
                }
            }
        } else {
            time++;
            if (time >= delay) {
                time = 0;
                currentFrame++;
                if (currentFrame >= numFrames) {
                    currentFrame = 0; // Loop the animation
                }
            }
        }
    }

    public BufferedImage getCurrentFrame() {
        if (isIdle) {
            return frames[currentFrame % frames.length][0]; // 대기 모션일 때는 항상 0번 프레임
        } else {
            int animIndex = currentFrame % frames.length;
            return frames[animIndex][currentFrame];
        }
    }
}