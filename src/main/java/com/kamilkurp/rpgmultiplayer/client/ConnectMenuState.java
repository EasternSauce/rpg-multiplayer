package com.kamilkurp.rpgmultiplayer.client;

import com.esotericsoftware.kryonet.Client;
import com.kamilkurp.rpgmultiplayer.character.Character;
import com.kamilkurp.rpgmultiplayer.util.FileIO;
import org.newdawn.slick.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.gui.TextField;

import java.awt.Font;
import java.io.IOException;


public class ConnectMenuState extends BasicGameState {

    private static final int NUMBER_OF_CHOICES = 4;
    private static final int NAME = 0;
    private static final int IP = 1;
    private static final int CONNECT = 2;
    private static final int BACK = 3;
    private String[] playersOptions = new String[NUMBER_OF_CHOICES];
    private TrueTypeFont playersOptionsTTF;
    private int playersChoice, stateId;
    private org.newdawn.slick.Color notChosen = new org.newdawn.slick.Color(153, 204, 255);
    private FileIO fileio = new FileIO();
    private TextField ipField;
    private TextField nameField;
    private ClientApp app;

    public ConnectMenuState(int id, ClientApp app) {
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
        playersOptions[0] = "Name:";
        playersOptions[1] = "IP:";
        playersOptions[2] = "Connect";
        playersOptions[3] = "Back";

        nameField = new TextField(gc, playersOptionsTTF, 260, 195, 430, 50);
        nameField.setBorderColor(new Color(24, 241, 123));
        nameField.setBackgroundColor(new Color (34, 80, 42));
        nameField.setText(fileio.loadName());

        ipField = new TextField(gc, playersOptionsTTF, 260, 250, 430, 50);
        ipField.setBorderColor(new Color(24, 241, 123));
        ipField.setBackgroundColor(new Color (34, 80, 42));
        ipField.setText(fileio.loadIP());

        onChoiceChange(playersChoice);


    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        Input input = gc.getInput();
        if (input.isKeyPressed(Input.KEY_DOWN)) {
            if (playersChoice == (NUMBER_OF_CHOICES - 1)) {
                playersChoice = 0;
            } else {
                playersChoice++;

            }
            onChoiceChange(playersChoice);
        }
        if (input.isKeyPressed(Input.KEY_UP)) {
            if (playersChoice == 0) {
                playersChoice = NUMBER_OF_CHOICES - 1;
            } else {
                playersChoice--;
            }
            onChoiceChange(playersChoice);
        }
        if (input.isKeyPressed(Input.KEY_ENTER)) {
            if(playersChoice == NAME || playersChoice == IP || playersChoice == CONNECT) {
                String ip = ipField.getText();
                String name = nameField.getText();


                Client client = app.getClient();
                Character player = app.getPlayer();
                client.start();
                try {
                    client.connect(5000, ip, 54555, 54777);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                player.setName(name);

                client.sendTCP(player);


                fileio.saveName(name);
                fileio.saveIP(ip);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                sbg.enterState(ClientApp.GAME_PLAY_STATE);
            }
            else if(playersChoice == BACK) {
                sbg.enterState(ClientApp.START_MENU_STATE);
            }
        }

    }

    public void onChoiceChange(int choice) {
        if(choice != 0) nameField.setFocus(false);
        else {
            nameField.setFocus(true);
            nameField.setCursorPos(nameField.getText().length());
        }
        if(choice != 1) ipField.setFocus(false);
        else {
            ipField.setFocus(true);
            ipField.setCursorPos(ipField.getText().length());
        }
    }

    public void render(GameContainer gc, StateBasedGame sbg, org.newdawn.slick.Graphics g) throws SlickException {
        renderPlayersOptions();
        ipField.render(gc, g);
        nameField.render(gc, g);
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
