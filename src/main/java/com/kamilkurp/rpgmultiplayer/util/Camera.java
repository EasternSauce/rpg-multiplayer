package com.kamilkurp.rpgmultiplayer.util;

import com.kamilkurp.rpgmultiplayer.character.Character;
import com.kamilkurp.rpgmultiplayer.client.ClientApp;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Camera {

    private int x, y;
    private int mapWidth, mapHeight;
    private Rectangle viewPort;

    public Camera(int mapWidth, int mapHeight) {
        x = 0;
        y = 0;
        viewPort = new Rectangle(0, 0, ClientApp.WIDTH, ClientApp.HEIGHT);
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public void translate(Graphics g, Character hero) {

        if (hero.getPositionX() - ClientApp.WIDTH / 2 + 16 < 0) {
            x = 0;
        } else if (hero.getPositionX() + ClientApp.WIDTH / 2 + 16 > mapWidth) {
            x = -mapWidth + ClientApp.WIDTH;
        } else {
            x = (int) -hero.getPositionX() + ClientApp.WIDTH / 2 - 16;
        }

        if (hero.getPositionY() - ClientApp.HEIGHT / 2 + 16 < 0) {
            y = 0;
        } else if (hero.getPositionY() + ClientApp.HEIGHT / 2 + 16 > mapHeight) {
            y = -mapHeight + ClientApp.HEIGHT;
        } else {
            y = (int) -hero.getPositionY() + ClientApp.HEIGHT / 2 - 16;
        }
        g.translate(x, y);
        viewPort.setX(-x);
        viewPort.setY(-y);
    }

    public float getViewportX() {
        return viewPort.getX();
    }

    public float getViewportY() {
        return viewPort.getY();
    }
}