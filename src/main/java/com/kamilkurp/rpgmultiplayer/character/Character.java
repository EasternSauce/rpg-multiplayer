package com.kamilkurp.rpgmultiplayer.character;

import com.kamilkurp.rpgmultiplayer.util.Network;
import com.kamilkurp.rpgmultiplayer.util.AnimationType;
import com.kamilkurp.rpgmultiplayer.util.Direction;
import com.kamilkurp.rpgmultiplayer.util.Level;
import com.kamilkurp.rpgmultiplayer.util.Network.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import java.util.Set;

import static com.kamilkurp.rpgmultiplayer.util.AnimationType.*;

public class Character {

    private int id = -1;
    private String name;

    private float positionX = 100;
    private float positionY = 100;
    private float destinationX = positionX;
    private float destinationY = positionY;

    //todo: speed setter
    private float speed = 0.3f;

    private AnimationType currentAnimationType = DOWN;

    //todo: get it from the sprite
    private int spriteWidth = 29;
    private int spriteHeight = 34;

    private long timeLastShot = 0;

    private int health = 100;
    private long respawnTime = Long.MAX_VALUE;

    private boolean dead = false;

    public Character() {

    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AnimationType getCurrentAnimationType() {
        return currentAnimationType;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public float getSpeed() {
        return speed;
    }

    public void setCurrentAnimationType(AnimationType currentAnim) {
        this.currentAnimationType = currentAnim;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public float getDestinationX() {
        return destinationX;
    }

    public float getDestinationY() {
        return destinationY;
    }

    public void setPosition(float x, float y) {
        this.positionX = x;
        this.positionY = y;
    }

    public void setDestination(float x, float y) {
        this.destinationX = x;
        this.destinationY = y;
    }

    public long getTimeLastShot() {
        return timeLastShot;
    }

    public void setTimeLastShot(long timeLastShot) {
        this.timeLastShot = timeLastShot;
    }

    public Projectile shoot() {
        Direction direction = Direction.DOWN;

        if(currentAnimationType == UP || currentAnimationType == MOV_UP) {
            direction = Direction.UP;
        }
        else if(currentAnimationType == DOWN || currentAnimationType == MOV_DOWN) {
            direction = Direction.DOWN;
        }
        else if(currentAnimationType == LEFT || currentAnimationType == MOV_LEFT) {
            direction = Direction.LEFT;
        }
        else if(currentAnimationType == RIGHT || currentAnimationType == MOV_RIGHT) {
            direction = Direction.RIGHT;
        }

        return new Projectile(this.getId(), positionX, positionY, direction);
    }

    public void update(int delta) {
        if(dead && System.currentTimeMillis() > respawnTime) {
            dead = false;
            health = 100;
            positionX = 100;
            positionY = 100;
            destinationX = positionX;
            destinationY = positionY;
        }
        if(dead) return;
            if(new Vector2f(positionX, positionY).distance(new Vector2f(destinationX, destinationY)) > 10.0f) {
                setPosition(destinationX, destinationY);
            }

            else if ((int) positionX != (int) destinationX || (int) positionY != (int) destinationY) {
            if (positionY < destinationY) {
                positionY += delta * speed;
                if (positionY >= destinationY) positionY = destinationY;

            }
            if (positionY > destinationY) {
                positionY -= delta * speed;
                if (positionY <= destinationY) positionY = destinationY;

            }
            if (positionX < destinationX) {
                positionX += delta * speed;
                if (positionX >= destinationX) positionX = destinationX;

            }
            if (positionX > destinationX) {
                positionX -= delta * speed;
                if (positionX <= destinationX) positionX = destinationX;

            }

            setPosition(positionX, positionY);


        }

    }

    public void checkCollisions(Set<Projectile> projectiles) {
        for(Projectile projectile : projectiles) {
            if(!projectile.isVisible()) continue;
            Rectangle characterRect = new Rectangle(positionX, positionY, spriteWidth, spriteHeight);
            Rectangle projectileRect = new Rectangle(projectile.getPositionX(), projectile.getPositionY(), 10, 10);
            if(characterRect.intersects(projectileRect) && projectile.getShooterId() != id) {
                health -= 20;
                if(health <= 0 && !dead) {
                    health = 0;
                    dead = true;
                    respawnTime = System.currentTimeMillis() + 5000;
                }
                projectile.setVisible(false);
            }
        }
    }

    public MoveCharacter handleInput(Network.InputInfo inputInfo, Level level, int delta) {
        float posX = getPositionX();
        float posY = getPositionY();

        float speed = getSpeed();

        float newPosX = posX;
        float newPosY = posY;

        int spriteWidth = getSpriteWidth();
        int spriteHeight = getSpriteHeight();
        AnimationType animationType = getCurrentAnimationType();




        if (inputInfo.isDownPressed()) {
            if (!level.isBlocked(posX + spriteWidth - 4, posY + spriteHeight + delta * speed)
                    && !level.isBlocked(posX + 4, posY + spriteHeight + delta * speed)) {
                newPosY += delta * speed;
            } else {
                float change = delta * speed;
                while (level.isBlocked(posX + spriteWidth - 4, posY + spriteHeight + change)
                        || level.isBlocked(posX + 4, posY + spriteHeight + change)) {
                    change -= 0.1f;
                }
                newPosY += change;
            }
            animationType = MOV_DOWN;
        }

        if (inputInfo.isUpPressed()) {
            if (!level.isBlocked(posX + spriteWidth - 4, posY - delta * speed)
                    && !level.isBlocked(posX + 4, posY - delta * speed)) {
                newPosY -= delta * speed;
            } else {
                float change = delta * speed;
                while (level.isBlocked(posX + spriteWidth - 4, posY - change)
                        || level.isBlocked(posX + 4, posY - change)) {
                    change -= 0.1f;
                }
                newPosY -= change;
            }
            animationType = MOV_UP;
        }
        if (inputInfo.isLeftPressed()) {
            if (!level.isBlocked(posX - delta * speed, posY + 4)
                    && !level.isBlocked(posX - delta * speed, posY + spriteHeight - 4)) {
                newPosX -= delta * speed;
            } else {
                float change = delta * speed;
                while (level.isBlocked(posX - change, posY + 4)
                        || level.isBlocked(posX - change, posY + spriteHeight - 4)) {
                    change -= 0.1f;
                }
                newPosX -= change;
            }
            animationType = MOV_LEFT;

        }
        if (inputInfo.isRightPressed()) {
            if (!level.isBlocked(posX + spriteWidth + delta * speed, posY + spriteHeight - 4)
                    && !level.isBlocked(posX + spriteWidth + delta * speed, posY + 4)) {
                newPosX += delta * speed;
            } else {
                float change = delta * speed;
                while (level.isBlocked(posX + spriteWidth + change, posY + spriteHeight - 4)
                        || level.isBlocked(posX + spriteWidth + change, posY + 4)) {
                    change -= 0.1f;
                }
                newPosX += change;
            }
            animationType = MOV_RIGHT;


        }

        if (!inputInfo.isDownPressed() && !inputInfo.isUpPressed() && !inputInfo.isLeftPressed()
                && !inputInfo.isRightPressed()) {
            switch (animationType) {
                case MOV_DOWN:
                    animationType = DOWN;
                    break;
                case MOV_UP:
                    animationType = UP;
                    break;
                case MOV_LEFT:
                    animationType = LEFT;
                    break;
                case MOV_RIGHT:
                    animationType = RIGHT;
                    break;
            }
        }

        MoveCharacter moveCharacter = new MoveCharacter();
        moveCharacter.id = id;
        moveCharacter.destX = newPosX;
        moveCharacter.destY = newPosY;
        moveCharacter.currentAnimationType = animationType;
        return moveCharacter;
    }

    public int getHealth() {
        return health;
    }

    public long getRespawnTime() {
        return respawnTime;
    }
}

