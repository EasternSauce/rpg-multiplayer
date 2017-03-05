package com.kamilkurp.rpgmultiplayer.util;

public class Level {
    private int tileWidth;
    private int tileHeight;
    private int width;
    private int height;



    private boolean blocked[][];

    public Level(int tileWidth, int tileHeight, int width, int height) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.width = width;
        this.height = height;
    }

    public int getMapWidth() {
        return width * tileWidth;
    }

    public int getMapHeight() {
        return height * tileHeight;
    }

    public boolean isBlocked(float x, float y) {
        int xBlock = (int) x / tileWidth;
        int yBlock = (int) y / tileHeight;
        return blocked[xBlock][yBlock];
    }

    public void setBlocked(boolean[][] blocked) {
        this.blocked = blocked;
    }


}
