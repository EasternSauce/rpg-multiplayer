package com.kamilkurp.rpgmultiplayer.client;

import com.esotericsoftware.kryonet.Client;
import com.kamilkurp.rpgmultiplayer.util.Network;
import com.kamilkurp.rpgmultiplayer.character.Character;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;


public class ClientApp extends StateBasedGame {

    static final int START_MENU_STATE = 0;
    static final int GAME_PLAY_STATE = 1;
    static final int MENU_STATE = 2;
    static final int CONNECT_MENU_STATE = 3;

    public static final int WIDTH = 1366;
    public static final int HEIGHT = 768;
    private static final boolean FULLSCREEN = false;

    Client client;
    Character player;

    private StartMenuState startMenuState;
    private GamePlayState gamePlayState;
    private MenuState menuState;
    private ConnectMenuState connectMenuState;


    private ClientApp() {
        super("Insert RPG Title Here");

        client = new Client();
        Network.register(client);

        player = new Character();

        startMenuState = new StartMenuState(START_MENU_STATE, this);
        gamePlayState = new GamePlayState(GAME_PLAY_STATE, this);
        menuState = new MenuState(MENU_STATE, this);
        connectMenuState = new ConnectMenuState(CONNECT_MENU_STATE, this);
    }

    public void initStatesList(GameContainer gc) throws SlickException {
        addState(startMenuState);
        addState(gamePlayState);
        addState(menuState);
        addState(connectMenuState);
        this.enterState(START_MENU_STATE);
    }

    public boolean closeRequested() {
        gamePlayState.onExit();
        return true;
    }

    public Client getClient() {
        return client;
    }

    public Character getPlayer() {
        return player;
    }


    public static void main(String[] args) {
        Input.disableControllers();

        try {
            AppGameContainer app = new AppGameContainer(new ClientApp());
            app.setDisplayMode(WIDTH, HEIGHT, FULLSCREEN);
            app.start();
        } catch(SlickException e) {
            e.printStackTrace();
        }
    }
}