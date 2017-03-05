package com.kamilkurp.rpgmultiplayer.character;

import com.kamilkurp.rpgmultiplayer.util.AnimationType;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class AnimationLoader {


    private Animation movementUp, movementDown, movementLeft, movementRight, stillUp, stillDown, stillLeft, stillRight, projectile;
    private static final int ANIMATION_SPEED = 500;

    public AnimationLoader() throws SlickException {
        Image[] animationUp = {new Image("images/hero20.png"), new Image("images/hero22.png")};
        Image[] animationDown = {new Image("images/hero00.png"), new Image("images/hero02.png")};
        Image[] animationLeft = {new Image("images/hero30.png"), new Image("images/hero32.png")};
        Image[] animationRight = {new Image("images/hero10.png"), new Image("images/hero12.png")};
        Image[] up = {new Image("images/hero21.png")};
        Image[] down = {new Image("images/hero01.png")};
        Image[] left = {new Image("images/hero31.png")};
        Image[] right = {new Image("images/hero11.png")};
        Image[] projectileImage = {new Image("images/projectile.png")};

        stillUp = new Animation(up, ANIMATION_SPEED);
        stillDown = new Animation(down, ANIMATION_SPEED);
        stillLeft = new Animation(left, ANIMATION_SPEED);
        stillRight = new Animation(right, ANIMATION_SPEED);
        movementUp = new Animation(animationUp, ANIMATION_SPEED);
        movementDown = new Animation(animationDown, ANIMATION_SPEED);
        movementLeft = new Animation(animationLeft, ANIMATION_SPEED);
        movementRight = new Animation(animationRight, ANIMATION_SPEED);
        projectile = new Animation(projectileImage, ANIMATION_SPEED);

    }

    public Animation getAnimation(AnimationType animationType) {
        switch(animationType) {
            case UP:
                return stillUp;
            case DOWN:
                return stillDown;
            case LEFT:
                return stillLeft;
            case RIGHT:
                return stillRight;
            case MOV_UP:
                return movementUp;
            case MOV_DOWN:
                return movementDown;
            case MOV_LEFT:
                return movementLeft;
            case MOV_RIGHT:
                return movementRight;
            case PROJECTILE:
                return projectile;
            default:
                return stillDown;
        }
    }

}
