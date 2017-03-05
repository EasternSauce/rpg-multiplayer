package com.kamilkurp.rpgmultiplayer.client;

import java.awt.Font;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

class StartMenuState extends BasicGameState {

    private static final int NUMBER_OF_CHOICES = 2;
    private static final int START = 0;
    private static final int QUIT = 1;
    private String[] playersOptions = new String[NUMBER_OF_CHOICES];
    private TrueTypeFont playersOptionsTTF;
    private int playersChoice, stateId;
    private Color notChosen = new Color(153, 204, 255);
    private ClientApp app;

    StartMenuState(int id, ClientApp app) {
        stateId = id;
        this.app = app;
    }

    @Override
    public int getID() {
        return stateId;
    }

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        Font menuFont = new Font("Verdana", Font.BOLD, 40);
        playersOptionsTTF = new TrueTypeFont(menuFont, true);
        playersOptions[0] = "Start";
        playersOptions[1] = "Quit";
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gc.getInput();
        if (input.isKeyPressed(Input.KEY_DOWN)) {
            if (playersChoice == (NUMBER_OF_CHOICES - 1)) {
                playersChoice = 0;
            } else {
                playersChoice++;
            }
        }
        if (input.isKeyPressed(Input.KEY_UP)) {
            if (playersChoice == 0) {
                playersChoice = NUMBER_OF_CHOICES - 1;
            } else {
                playersChoice--;
            }
        }
        if (input.isKeyPressed(Input.KEY_ENTER)) {
            switch (playersChoice) {
                case START:
                    sbg.enterState(ClientApp.CONNECT_MENU_STATE);
                    break;
                case QUIT:
                    gc.exit();
                    break;
            }
        }
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        renderPlayersOptions();
    }

    private void renderPlayersOptions() {
        for (int i = 0; i < NUMBER_OF_CHOICES; i++) {
            if (playersChoice == i) {
                playersOptionsTTF.drawString(100, i * 50 + 200, playersOptions[i]);
            } else {
                playersOptionsTTF.drawString(100, i * 50 + 200, playersOptions[i], notChosen);
            }
        }
    }

}