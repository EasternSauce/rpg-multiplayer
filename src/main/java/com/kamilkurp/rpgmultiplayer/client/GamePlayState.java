package com.kamilkurp.rpgmultiplayer.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.kamilkurp.rpgmultiplayer.character.AnimationLoader;
import com.kamilkurp.rpgmultiplayer.character.Character;
import com.kamilkurp.rpgmultiplayer.character.Projectile;
import com.kamilkurp.rpgmultiplayer.util.Network;
import com.kamilkurp.rpgmultiplayer.util.*;
import com.kamilkurp.rpgmultiplayer.util.Network.*;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import java.util.*;

import static com.kamilkurp.rpgmultiplayer.util.AnimationType.*;


public class GamePlayState extends BasicGameState {

    private static boolean showFPS = true;

    private Map<Integer, Character> characters;
    private HashSet<Projectile> projectiles;
    private Camera camera;
    private int id;
    private TrueTypeFont nameFont;
    private TrueTypeFont respawnFont;
    private AnimationLoader animationLoader;
    private FileIO fileio;
    private Level level;
    private TiledMap map;
    private int upKey = Input.KEY_UP;
    private int downKey = Input.KEY_DOWN;
    private int leftKey = Input.KEY_LEFT;
    private int rightKey = Input.KEY_RIGHT;
    private int spaceKey = Input.KEY_SPACE;
    private ClientApp app;

    GamePlayState(int id, ClientApp app) {
        this.id = id;
        this.app = app;
    }

    @Override
    public int getID() {
        return id;
    }


    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

        animationLoader = new AnimationLoader();

        characters = new HashMap<Integer, Character>();
        projectiles = new HashSet<Projectile>();

        final Client client = app.getClient();
        final Character player = app.getPlayer();


        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof Network.IdAssignment) {
                    Network.IdAssignment idAssignment = (Network.IdAssignment) object;
                    player.setId(idAssignment.getId());
                    characters.put(player.getId(), player);
                }
                if (object instanceof Character) {
                    Character character = (Character) object;
                    characters.put(character.getId(), character);
                }
                if(object instanceof PositionInfo) {
                    PositionInfo position = (PositionInfo) object;
                    Character character = characters.get(position.getCharacterId());
                    if(character != null && character.getId() != player.getId()) {
                        character.setDestination(position.getPos().x, position.getPos().y);
                        character.setCurrentAnimationType(position.getCurrentAnimationType());
                    }
                }
                if(object instanceof PlayerLeftInfo) {
                    PlayerLeftInfo playerLeftInfo = (PlayerLeftInfo) object;
                    characters.remove(playerLeftInfo.getId());
                }
                if(object instanceof Projectile) {
                    Projectile projectile = (Projectile) object;
                    projectiles.add(projectile);
                }
                if(object instanceof MoveCharacter) {
                    MoveCharacter moveCharacter = (MoveCharacter) object;
                    characters.get(moveCharacter.id).setDestination(moveCharacter.destX, moveCharacter.destY);
                    characters.get(moveCharacter.id).setCurrentAnimationType(moveCharacter.currentAnimationType);
                }

            }
        });

        java.awt.Font font = new java.awt.Font("Verdana", java.awt.Font.BOLD, 14);

        nameFont = new TrueTypeFont(font, true);

        java.awt.Font biggerFont = new java.awt.Font("Verdana", java.awt.Font.BOLD, 36);

        respawnFont = new TrueTypeFont(biggerFont, true);


        map = new TiledMap("tiledmaps/IceAdventureMap.tmx");

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

        level = new Level(map.getTileWidth(), map.getTileHeight(), map.getWidth(), map.getHeight());
        level.setBlocked(blocked);

        camera = new Camera(level.getMapWidth(), level.getMapHeight());

        gc.setAlwaysRender(true);
        gc.setShowFPS(showFPS);
        gc.setVSync(true);
        fileio = new FileIO();

    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gc.getInput();
        Character player = app.getPlayer();
        Client client = app.getClient();


        System.out.println("pos " + player.getPositionX() + "\t" + player.getPositionY());
        System.out.println("des " + player.getDestinationX() + "\t" + player.getDestinationY());


        InputInfo inputInfo = new InputInfo(player.getId(), input.isKeyDown(upKey), input.isKeyDown(downKey),
                input.isKeyDown(leftKey), input.isKeyDown(rightKey), input.isKeyDown(spaceKey));
        client.sendTCP(inputInfo);

        for(Map.Entry<Integer, Character> characterEntry : characters.entrySet()) {
            Character character = characterEntry.getValue();
            character.update(delta);
            character.checkCollisions(projectiles);

        }

        for(Projectile projectile : projectiles) {
            projectile.update(delta, level);
        }

        if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            sbg.enterState(ClientApp.MENU_STATE);
        }
    }



    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        Character player = app.getPlayer();


        camera.translate(g, app.getPlayer());
        map.render(0, 0);
        for (Map.Entry<Integer, Character> characterEntry : characters.entrySet()) {
            Character character = characterEntry.getValue();

            animationLoader.getAnimation(character.getCurrentAnimationType()).draw(character.getPositionX(),
                    character.getPositionY());
            if (character.getName() != null)
                nameFont.drawString(character.getPositionX(), character.getPositionY() - 30, character.getName(),
                        new Color(24, 24, 24));
            g.setColor(new Color(150, 150, 150));
            g.fillRect(character.getPositionX(), character.getPositionY() - 40, 50, 8);
            g.setColor(new Color(0, 255, 0));
            g.fillRect(character.getPositionX(), character.getPositionY() - 40,
                    (int) (50 * (float) character.getHealth() / 100f), 8);

        }

        for (Projectile projectile : projectiles) {
            float x = projectile.getPositionX();
            float y = projectile.getPositionY();
            if (projectile.isVisible()) animationLoader.getAnimation(PROJECTILE).draw(x, y);

        }

        if (player.isDead()) {
            respawnFont.drawString(camera.getViewportX() + 250, camera.getViewportY() + 250, "Respawning in "
                    + Double.toString((player.getRespawnTime() - System.currentTimeMillis()) / 1000.0),
                    new Color(42, 241, 242));
        }

    }

    public void onExit() {
        app.getClient().stop();
    }

}