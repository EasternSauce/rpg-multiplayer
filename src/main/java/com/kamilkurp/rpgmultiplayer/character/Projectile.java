package com.kamilkurp.rpgmultiplayer.character;

import com.kamilkurp.rpgmultiplayer.util.Direction;
import com.kamilkurp.rpgmultiplayer.util.Level;


/**
 * Created by kamil on 22.02.2017.
 */
public class Projectile {
    private int id;
    private float positionX;
    private float positionY;
    private Direction direction;
    private int shooterId;
    private boolean visible = true;
    private float speed = 0.6f;

    public Projectile() {

    }

    public Projectile(int shooterId, float positionX, float positionY, Direction direction) {
        this.shooterId = shooterId;
        this.positionX = positionX;
        this.positionY = positionY;
        this.direction = direction;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getShooterId() {
        return shooterId;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setPosition(float positionX, float positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void update(int delta, Level level) {
        if (!visible) return;

        float newPosX = positionX;
        float newPosY = positionY;

        switch (getDirection()) {
            case UP:
                newPosY -= delta * speed;
                break;
            case DOWN:
                newPosY += delta * speed;
                break;
            case LEFT:
                newPosX -= delta * speed;
                break;
            case RIGHT:
                newPosX += delta * speed;
                break;
        }
        setPosition(newPosX, newPosY);

        if (level.isBlocked(newPosX, newPosY)) {
            visible = false;
        }
    }



    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}