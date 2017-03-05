package com.kamilkurp.rpgmultiplayer.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.kamilkurp.rpgmultiplayer.character.Character;
import com.kamilkurp.rpgmultiplayer.character.Projectile;
import com.kamilkurp.rpgmultiplayer.util.AnimationType;
import com.kamilkurp.rpgmultiplayer.util.Direction;
import com.kamilkurp.rpgmultiplayer.util.Level;
import org.newdawn.slick.geom.Vector2f;

/**
 * Created by kamil on 25.02.2017.
 */
public class Network {
    static public final int port = 54555;

    public static void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        kryo.register(Character.class);
        kryo.register(PositionInfo.class);
        kryo.register(Vector2f.class);
        kryo.register(IdAssignment.class);
        kryo.register(PlayerLeftInfo.class);
        kryo.register(Level.class);
        kryo.register(AnimationType.class);
        kryo.register(InputInfo.class);
        kryo.register(Projectile.class);
        kryo.register(Direction.class);
        kryo.register(MoveCharacter.class);
    }

    public static class IdAssignment {
        private int id;

        public int getId() {
            return id;
        }

        public IdAssignment() {

        }

        public IdAssignment(int id) {
            this.id = id;
        }
    }

    public static class InputInfo {
        int id;
        boolean upPressed;
        boolean downPressed;
        boolean leftPressed;
        boolean rightPressed;
        boolean spacePressed;

        public InputInfo() {

        }

        public InputInfo(int id, boolean upPressed, boolean downPressed, boolean leftPressed, boolean rightPressed, boolean spacePressed) {
            this.id = id;
            this.upPressed = upPressed;
            this.downPressed = downPressed;
            this.leftPressed = leftPressed;
            this.rightPressed = rightPressed;
            this.spacePressed = spacePressed;
        }

        public int getId() {
            return id;
        }

        public boolean isUpPressed() {
            return upPressed;
        }

        public boolean isDownPressed() {
            return downPressed;
        }

        public boolean isLeftPressed() {
            return leftPressed;
        }

        public boolean isRightPressed() {
            return rightPressed;
        }

        public boolean isSpacePressed() {
            return spacePressed;
        }
    }

    public static class PlayerLeftInfo {
        private int id;

        public PlayerLeftInfo() {

        }

        public PlayerLeftInfo(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }


    public static class PositionInfo {
        private int characterId;
        private Vector2f pos;
        private AnimationType currentAnimationType;

        public PositionInfo(){

        }

        public PositionInfo(int characterId, Vector2f pos, AnimationType currentAnimationType) {
            this.characterId = characterId;
            this.pos = pos;
            this.currentAnimationType = currentAnimationType;
        }

        public Vector2f getPos() {
            return pos;
        }

        public int getCharacterId() {
            return characterId;
        }

        public AnimationType getCurrentAnimationType() {
            return currentAnimationType;
        }

    }

    public static class MoveCharacter {
        public int id;
        public float destX, destY;
        public AnimationType currentAnimationType;
    }

}
