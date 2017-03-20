package com.kamilkurp.rpgmultiplayer.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.kamilkurp.rpgmultiplayer.character.*;
import com.kamilkurp.rpgmultiplayer.character.Character;
import com.kamilkurp.rpgmultiplayer.util.*;
import com.kamilkurp.rpgmultiplayer.util.Network.*;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;


import java.io.IOException;
import java.util.*;



public class ServerApp extends BasicGame {
    private Server server;
    private int maxID = 0;
    private Level level;
    private Map<Integer, Character> characters = new HashMap<Integer, Character>();
    private HashSet<Projectile> projectiles = new HashSet<Projectile>();
    private String mapPath = "tiledmaps/IceAdventureMap.tmx";
    private int delta = 20;

    private ServerApp(String title) throws SlickException {
        super(title);

        server = new Server();
        Network.register(server);

        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof Character) {
                    int assignedId = maxID++;
                    connection.sendTCP(new IdAssignment(assignedId));

                    for(Map.Entry<Integer, Character> characterEntry : characters.entrySet()) {
                        Character character = characterEntry.getValue();
                        connection.sendTCP(character);

                    }

                    Character character = (Character) object;
                    character.setId(assignedId);
                    characters.put(assignedId, character);

                    server.sendToAllExceptTCP(connection.getID(), character);

                }

                if(object instanceof InputInfo) {
                    InputInfo inputInfo = (InputInfo) object;
                    Character player = characters.get(inputInfo.getId());

                    MoveCharacter moveCharacter = player.handleInput(inputInfo, level, delta);
                    player.setDestination(moveCharacter.destX, moveCharacter.destY);
                    player.setCurrentAnimationType(moveCharacter.currentAnimationType);

                    if(inputInfo.isSpacePressed()) {
                        if(Math.abs(System.currentTimeMillis() - player.getTimeLastShot()) > 1500) {
                            Projectile projectile = characters.get(inputInfo.getId()).shoot();
                            projectiles.add(projectile);
                            server.sendToAllTCP(projectile);
                            player.setTimeLastShot(System.currentTimeMillis());
                        }
                    }


                    server.sendToAllTCP(moveCharacter);
                }
                if(object instanceof PlayerLeftInfo) {
                    PlayerLeftInfo playerLeftInfo = (PlayerLeftInfo) object;
                    characters.remove(playerLeftInfo.getId());
                    server.sendToAllExceptTCP(connection.getID(), playerLeftInfo);
                }
            }
        });
    }



    public void init(GameContainer gameContainer) throws SlickException {
        gameContainer.setShowFPS(false);

        TiledMap map = new TiledMap(mapPath);

        level = new Level(map.getTileWidth(), map.getTileHeight(), map.getWidth(), map.getHeight());
        level.setBlocked(getBlocked(map));

        server.start();

        try {
            server.bind(54555, 54777);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(GameContainer gameContainer, int delta) throws SlickException {
        for(Map.Entry<Integer, Character> characterEntry : characters.entrySet()) {
            Character character = characterEntry.getValue();
            character.update(delta);
            character.checkCollisions(projectiles);
        }

        for(Projectile projectile : projectiles) {
            projectile.update(delta, level);
        }
    }

    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        graphics.drawString("Server is up", 0, 0);

    }

    private boolean[][] getBlocked(TiledMap map) {
        boolean blocked[][] = new boolean[map.getWidth()][map.getHeight()];
        for (int l = 0; l < map.getLayerCount(); l++) {
            String layerValue = map.getLayerProperty(l, "blocked", "false");
            if (layerValue.equals("true")) {
                for (int c = 0; c < map.getWidth(); c++) {
                    for (int r = 0; r < map.getHeight(); r++) {
                        if (map.getTileId(c, r, l) != 0) {
                            blocked[c][r] = true;
                        }
                    }
                }
            }
        }
        return blocked;
    }

    public static void main(String[] args) {
        Input.disableControllers();

        try {
            AppGameContainer appgc = new AppGameContainer(new ServerApp("RPGMultiplayer Server"));
            appgc.setDisplayMode(640, 480, false);
            appgc.setMinimumLogicUpdateInterval(20);
            appgc.start();
        } catch(SlickException e) {
            e.printStackTrace();
        }


    }
}

