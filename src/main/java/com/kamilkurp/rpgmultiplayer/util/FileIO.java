package com.kamilkurp.rpgmultiplayer.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import org.newdawn.slick.geom.Vector2f;

public class FileIO {

    private Vector2f coordinates = new Vector2f(0, 0);

    public FileIO() {}

    public Vector2f loadSave() {
        try {
            Scanner scanner = new Scanner(new File("saves/game.sav"));
            coordinates.x = Float.parseFloat(scanner.nextLine());
            coordinates.y = Float.parseFloat(scanner.nextLine());
        } catch (FileNotFoundException e) {
            System.out.println("Error: saved game does not exist!");
        } catch (NumberFormatException e) {
            System.out.println("Error: could not read the file!");
        }
        return coordinates;
    }

    public void save(Vector2f vector) {
        try {
            PrintWriter writer = new PrintWriter(new File("saves/game.sav"));
            writer.println(vector.x);
            writer.println(vector.y);
            writer.close();
        } catch (Exception e) {
            System.out.println("Error: could not save the file!");
        }
    }

    public String loadIP(){
        String loadedIP = "";
        try {
            Scanner scanner = new Scanner(new File("saves/ip.sav"));
            loadedIP = scanner.nextLine();
        } catch (FileNotFoundException e) {
            System.out.println("Error: saved ip does not exist!");
        } catch (NumberFormatException e) {
            System.out.println("Error: could not read the ip file!");
        }
        return loadedIP;
    }

    public void saveIP(String ip) {
        try {
            PrintWriter writer = new PrintWriter(new File("saves/ip.sav"));
            writer.println(ip);
            writer.close();
        } catch (Exception e) {
            System.out.println("Error: could not save the ip file!");
        }
    }

    public String loadName() {
        String loadedIP = "";
        try {
            Scanner scanner = new Scanner(new File("saves/name.sav"));
            loadedIP = scanner.nextLine();
        } catch (FileNotFoundException e) {
            System.out.println("Error: saved name does not exist!");
        } catch (NumberFormatException e) {
            System.out.println("Error: could not read the name file!");
        }
        return loadedIP;
    }

    public void saveName(String name) {
        try {
            PrintWriter writer = new PrintWriter(new File("saves/name.sav"));
            writer.println(name);
            writer.close();
        } catch (Exception e) {
            System.out.println("Error: could not save the name file!");
        }
    }
}