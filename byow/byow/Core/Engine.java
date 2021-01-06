package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;

import java.util.Random;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private StringBuilder keyspressed;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */

    public void interactWithKeyboard() throws IOException {
        canvascreator();
        ReloadGame rg = new ReloadGame("filename.txt");
        mainmenu(rg);
        // play main menu song
        ArrayList<Character> choices = new ArrayList<>();
        choices.add('n');
        choices.add('N');
        choices.add('q');
        choices.add('Q');
        if (rg.fileexists()) {
            choices.add('l');
            choices.add('L');
        }
        keyspressed = new StringBuilder();
        char m;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                m = StdDraw.nextKeyTyped();
                if (choices.contains(m)) {
                    break;
                }
            } else {
                pausegame(500);
            }
        }
        TETile[][] world;
        Level lvl;
        if (m == 'q' || m == 'Q') {
            StdDraw.clear(Color.black);
            StdDraw.text(WIDTH / 2, HEIGHT / 2, "Thanks for playing.");
            StdDraw.show();
            return;
        } else if (m == 'n' || m == 'N') {
            StdDraw.clear(Color.black);
            edu.princeton.cs.introcs.StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
            edu.princeton.cs.introcs.StdDraw.setPenColor(Color.RED);
            edu.princeton.cs.introcs.StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "NEW GAME STARTED."
                    + " PLEASE TYPE IN AN INTEGER, FOLLOWED BY AN S.");
            StdDraw.show();
            keyspressed.append("N");
            long seed = 0;
            pausegame(1000);
            while (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 's' || c == 'S') {
                    keyspressed.append(c);
                    break;
                }
                keyspressed.append(c);
                seed = seed * 10 + Character.getNumericValue(c);
                pausegame(1000);
            }
            lvl = new Level(seed);
            world = lvl.getWorld();
            instructionscreen();

        } else {
            keyspressed.append(rg.getKeysPressed());
            lvl = (Level) Engine.levelfromstring(rg.getKeysPressed()).get("Level");
            world = lvl.getWorld();
        }

        TERenderer mer = new TERenderer();
        mer.initialize(80, 32);
        mer.renderFrame(world);
        interact2(lvl);
    }

    private void interact2(Level lvl) throws IOException {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c != ':') {
                    lvl.getAvatar().move(c);
                    ter.renderFrame(lvl.getWorld());
                    if (lvl.getAvatar().nexttodemon()) {
                        combat(lvl);
                        canvascreator();
                        ter.initialize(80, 32);
                        if (lvl.getAvatar().isdead()) {
                            SaveGame file = new SaveGame(keyspressed.toString());
                            SaveGame.newFile(file);
                            return;
                        } else if (lvl.getdemon().isdead()) {
                            if (lvl.getPointer() == 2) {
                                victoryscreen();
                                SaveGame file = new SaveGame(keyspressed.toString());
                                SaveGame.newFile(file);
                                return;
                            } else {
                                lvl.getdemon().death();
                            }
                        }
                    } else {
                        if (lvl.getAvatar().exhausted()) {
                            death();
                            SaveGame file = new SaveGame(keyspressed.toString());
                            SaveGame.newFile(file);
                            return;
                        }
                    }
                    keyspressed.append(c);
                    ter.renderFrame(lvl.getWorld());
                } else {
                    pausegamewithmouse(100, lvl);
                    if (StdDraw.nextKeyTyped() == 'q') {
                        SaveGame file = new SaveGame(keyspressed.toString());
                        SaveGame.newFile(file);
                        StdDraw.clear(Color.black);
                        StdDraw.text(WIDTH / 2, HEIGHT / 2, "GAME SAVED. THANKS FOR PLAYING.");
                        StdDraw.show();
                        return;
                    }
                }
            }
            pausegamewithmouse(50, lvl);
        }
    }





    private void pausegame(int s) {
        while (!StdDraw.hasNextKeyTyped()) {
            StdDraw.pause(s);
        }
    }

    private void pausegamewithmouse(int s, Level lvl) {
        edu.princeton.cs.introcs.StdDraw.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        edu.princeton.cs.introcs.StdDraw.setPenColor(Color.WHITE);
        while (!StdDraw.hasNextKeyTyped()) {
            if (lvl.inBounds(new Position((int) StdDraw.mouseX(), (int) StdDraw.mouseY()))) {
                StdDraw.clear();
                TETile tile = lvl.getWorld()[(int) StdDraw.mouseX()][(int) StdDraw.mouseY()];
                TETile[][] world = lvl.getWorld();
                TERenderer mfe = new TERenderer();
                mfe.renderFrame(world);
                StdDraw.text(3, 31, tile.description());
                StdDraw.text(70, 31, "ENERGY: " + lvl.getAvatar().getEnergy() + "  HP: "
                        + lvl.getAvatar().getHP());
                StdDraw.show();
                StdDraw.pause(100);
            }
        }
    }


    private void canvascreator() {
        edu.princeton.cs.introcs.StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        edu.princeton.cs.introcs.StdDraw.setFont(font);
        edu.princeton.cs.introcs.StdDraw.setXscale(0, WIDTH);
        edu.princeton.cs.introcs.StdDraw.setYscale(0, HEIGHT);
        edu.princeton.cs.introcs.StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    private void death() {
        StdDraw.clear(Color.black);
        edu.princeton.cs.introcs.StdDraw.setFont(new Font("Crimson Text", Font.PLAIN, 30));
        edu.princeton.cs.introcs.StdDraw.setPenColor(Color.RED);
        edu.princeton.cs.introcs.StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "EXHAUSTED");
        edu.princeton.cs.introcs.StdDraw.setFont(new Font("Crimson Text", Font.PLAIN, 20));
        edu.princeton.cs.introcs.StdDraw.text(WIDTH / 2, HEIGHT / 2 + 3, "You are dead :(");
        StdDraw.show();
    }

    private void mainmenu(ReloadGame rg) {
        edu.princeton.cs.introcs.StdDraw.setFont(new Font("Crimson Text", Font.PLAIN, 30));
        edu.princeton.cs.introcs.StdDraw.setPenColor(Color.RED);
        edu.princeton.cs.introcs.StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "INFERNO");
        edu.princeton.cs.introcs.StdDraw.setFont(new Font("Crimson Text", Font.PLAIN, 20));
        edu.princeton.cs.introcs.StdDraw.text(WIDTH / 2, HEIGHT / 2 + 3, "New Game (N)");
        edu.princeton.cs.introcs.StdDraw.text(WIDTH / 2, HEIGHT / 2 + 0, "Load Game (L)");
        edu.princeton.cs.introcs.StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, "Quit (Q)");
        edu.princeton.cs.introcs.StdDraw.show();
    }

    private static HashMap<String, Object> levelfromstring(String input) {
        // to-do: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types. */
        StringBuilder results = new StringBuilder();
        StringBuilder stringseed = new StringBuilder();
        StringBuilder inputbuilder = new StringBuilder(input);
        if (inputbuilder.charAt(0) == 'L' || inputbuilder.charAt(0) == 'l') {
            try {
                ReloadGame rg = new ReloadGame("filename.txt");
                inputbuilder.deleteCharAt(0);
                inputbuilder.insert(0, rg.getKeysPressed());
            } catch (IOException e) {
                System.out.println("Filename does not exist");
            }
        }
        int i = 1;
        Level lvl = null;
        if (inputbuilder.charAt(0) == 'n' || inputbuilder.charAt(0) == 'N') {
            while (i < inputbuilder.length()) {
                if (inputbuilder.charAt(i) == 's' || inputbuilder.charAt(i) == 'S') {
                    break;
                }
                stringseed = stringseed.append(inputbuilder.charAt(i));
                i++;
            }
            long seed = Long.parseLong(stringseed.toString());
            lvl = new Level(seed);
            results.append('N');
            results.append(stringseed);
            results.append('S');
            i++;
        }
        while (i < inputbuilder.length()) {
            char character = inputbuilder.charAt(i);
            if (character == ':') {
                if (i + 1 < inputbuilder.length() && (inputbuilder.charAt(i + 1) == 'q'
                        || inputbuilder.charAt(i + 1) == 'Q')) {
                    break;
                }
            } else if (character == '1' || character == '2' || character == '0') {
                lvl.getdemon().getsattacked(lvl.getAvatar().useability(
                        Character.getNumericValue(character)));
                if (lvl.getdemon().isdead()) {
                    lvl.getdemon().death();
                }
                results.append(character);
                i++;
            } else {
                lvl.getAvatar().move(character);
                results.append(character);
                i++;
            }
        }
        HashMap<String, Object> thing = new HashMap<>();
        thing.put("Level", lvl);
        thing.put("Filestring", results);
        return thing;
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */

    public static TETile[][] interactWithInputString(String input) {
        // to-do: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types. */
        HashMap<String, Object> items = Engine.levelfromstring(input);
        SaveGame file = new SaveGame(items.get("Filestring").toString());
        SaveGame.newFile(file);
        Level lvl = (Level) items.get("Level");
        TETile[][] finalWorldFrame = lvl.getWorld();
        return finalWorldFrame;

    }

    public void avatarpunch(Level lvl) {
        NPC demon = lvl.getdemon();
        String enemy = demon.getImageString();
        combatcanvascreator();
        StdDraw.setPenColor(Color.red);
        StdDraw.picture(10, 25, enemy);
        StdDraw.text(25, 26, "Demon HP: " + demon.getHP());
        StdDraw.text(25, 23, "Next hit: " + demon.nextattack());
        StdDraw.text(40, 26, "Your HP: " + lvl.getAvatar().getHP());
        StdDraw.text(40, 23, "Your Energy: " + lvl.getAvatar().getEnergy());
        StdDraw.setPenColor(Color.white);
        StdDraw.text(24, 19, "Please select your combat below by typing its key.");
        StdDraw.setPenColor(Color.yellow);
        StdDraw.text(6.5, 14, "'0'");
        StdDraw.text(7, 11, "Energy Cost: 2");
        StdDraw.text(7, 6, "Damage: 8");
        StdDraw.setPenColor(Color.cyan);
        StdDraw.text(22.5, 14, "'1'");
        StdDraw.text(23, 11, "Energy Cost: 7");
        StdDraw.text(23, 6, "Damage: 14");
        StdDraw.setPenColor(Color.green);
        StdDraw.text(39, 14, "'2'");
        StdDraw.text(39.5, 11, "Energy Cost: 15");
        StdDraw.text(39.5, 6, "Damage: 23");
        StdDraw.show();
    }

    public void victoryscreen() {
        combatcanvascreator();
        Random rand = new Random(2);
        Font font = new Font("Monaco", Font.BOLD, 85);
        edu.princeton.cs.introcs.StdDraw.setFont(font);
        StdDraw.setPenColor(Color.red);
        int j = 1;
        for (int i = 1; i < 150; i++) {
            StdDraw.clear(Color.BLACK);
            StdDraw.text(23, 15, "YOU WIN!!!");
            int x0 = rand.nextInt(45);
            int y0 = rand.nextInt(25);
            StdDraw.picture(x0, y0, "images/avatar.png");
            int x1 = rand.nextInt(45);
            int y1 = rand.nextInt(25);
            StdDraw.picture(x1, y1, "images/demon0.png");
            int x2 = rand.nextInt(45);
            int y2 = rand.nextInt(25);
            StdDraw.picture(x2, y2, "images/demon1.png");
            int x3 = rand.nextInt(45);
            int y3 = rand.nextInt(25);
            StdDraw.picture(x3, y3, "images/demon2.png");
            StdDraw.show();
            StdDraw.pause(800);
        }
    }

    public void instructionscreen() {
        combatcanvascreator();
        StdDraw.clear(Color.DARK_GRAY);
        StdDraw.picture(30.5 * .75, 28 * .65, "images/avatar.png");
        StdDraw.setPenColor(Color.white);
        StdDraw.text(23, 12.5, "This is your avatar.");
        StdDraw.text(22, 26, "INSTRUCTIONS");
        StdDraw.show();
        StdDraw.pause(4400);
        StdDraw.clear(Color.DARK_GRAY);
        StdDraw.text(21, 26, "INSTRUCTIONS");
        StdDraw.text(21, 22, "To move your avatar up press 'w'.");
        StdDraw.text(21, 18, "To move your avatar down press 's'.");
        StdDraw.text(21, 14, "To move yur avatar left press 'a'.");
        StdDraw.text(21, 9, "To move your avatar right press 'd'.");
        StdDraw.text(22, 4, "Each step you take will cost you 1 Energy.");
        StdDraw.show();
        StdDraw.pause(9000);
        StdDraw.clear(Color.DARK_GRAY);
        StdDraw.text(22, 26, "INSTRUCTIONS");
        StdDraw.picture(29.5 * .75, 28 * .65, "images/energy_good.png");
        StdDraw.text(23, 9, "This is an Energy tile.");
        StdDraw.text(24, 6, "Stepping on this tile replenishes your Energy to 75.");
        StdDraw.show();
        StdDraw.pause(6500);
        StdDraw.clear(Color.DARK_GRAY);
        StdDraw.text(22.5, 26, "INSTRUCTIONS");
        StdDraw.picture(30 * .75, 30 * .65,  "images/demon0.png");
        StdDraw.text(22.5, 11, "A demon will be located in each ring of Hell.");
        StdDraw.text(24.5, 7, "You must defeat the demon, to move to the next level.");
        StdDraw.text(22, 3, "To fight a demon, you must be close to it.");
        StdDraw.show();
        StdDraw.pause(7500);
        StdDraw.clear(Color.DARK_GRAY);
        StdDraw.text(23, 26, "INSTRUCTIONS");
        StdDraw.picture(31 * .75, 30 * .65, "images/door_good.png");
        StdDraw.text(23, 10, "This portal will appear after defeating a demon.");
        StdDraw.text(24, 6, "Stepping on it will teleport you to the next level.");
        StdDraw.show();
        StdDraw.pause(7000);
        StdDraw.clear(Color.DARK_GRAY);
        StdDraw.text(23, 26, "WINNING THE GAME");
        StdDraw.picture(31 * .75, 30 * .65,  "images/demon2.png");
        StdDraw.text(24, 11, "Defeat the demon on the third level to win the game.");
        StdDraw.text(22, 7, "If your Energy reaches 0, you lose.");
        StdDraw.show();
    }


    private void youpunch(Level lvl, int damage) {
        String demon = lvl.getdemon().getImageString();
        for (int i = 0; i < 40; i++) {
            StdDraw.clear(Color.black);
            StdDraw.picture(50 * .75, 30 * .75, demon);
            StdDraw.picture(50 * .15 - i * 0.05, 30 * .45, "images/avatar.png");
            StdDraw.setPenColor(Color.white);
            StdDraw.show();
        }
        StdDraw.pause(750);
        for (int i = 0; i < 50; i++) {
            StdDraw.clear(Color.black);
            StdDraw.picture(39, 30 * .75, demon);
            StdDraw.picture(5.5 + i * .2, 30 * .45, "images/avatar.png");
            StdDraw.show();
        }
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 90));
        StdDraw.setPenColor(Color.red);
        StdDraw.text(39, 30 * .75, "-" + damage);
        StdDraw.show();
        StdDraw.pause(1000);
    }

    public void demonpunch(Level lvl, int damage) {
        String demon = lvl.getdemon().getImageString();
        for (int i = 0; i < 150; i++) {
            StdDraw.clear(Color.black);
            StdDraw.picture(50 * .75 + i * .01, 30 * .75, demon);
            StdDraw.picture(50 * .15, 30 * .45, "images/avatar.png");
            StdDraw.show();
        }
        StdDraw.pause(750);
        for (int i = 0; i < 62.5; i++) {
            StdDraw.clear(Color.black);
            StdDraw.picture(39 - i * .2, 30 * .75, demon);
            StdDraw.picture(50 * .15, 30 * .45, "images/avatar.png");
            StdDraw.show();
        }
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 90));
        StdDraw.setPenColor(Color.red);
        StdDraw.text(50 * .15, 30 * .45, "-" + damage);
        StdDraw.show();
        StdDraw.pause(1000);
    }

    private void combatcanvascreator() {
        edu.princeton.cs.introcs.StdDraw.setCanvasSize(1000, 600);
        Font font = new Font("Monaco", Font.BOLD, 30);
        edu.princeton.cs.introcs.StdDraw.setFont(font);
        edu.princeton.cs.introcs.StdDraw.setXscale(0, 50);
        edu.princeton.cs.introcs.StdDraw.setYscale(0, 30);
        edu.princeton.cs.introcs.StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }


    public void combat(Level lvl) {
        combatcanvascreator();
        // This^ creates different sized canvas than normal dungeon canvas
        NPC demon = lvl.getdemon();
        Avatar you = lvl.getAvatar();
        StringBuilder x = new StringBuilder();
        while (true) {
            StdDraw.clear(Color.red);
            avatarpunch(lvl);
            StdDraw.show();
            pausegame(100);
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == '1' || c == '2' || c == '0') {
                    if (you.exhausted() || !you.enoughenergy(0)) {
                        death();
                        return;
                    } else if (!you.enoughenergy(Character.getNumericValue(c))) {
                        StdDraw.clear(Color.black);
                        StdDraw.setPenColor(Color.red);
                        StdDraw.text(50 / 2, 30 / 2, "Not enough energy. Pick another option");
                        StdDraw.show();

                    } else {
                        int damagetodemon = you.useability(Character.getNumericValue(c));
                        demon.getsattacked(damagetodemon);
                        youpunch(lvl, you.getabildamage(c));
                        StdDraw.pause(1000);
                        if (lvl.getdemon().isdead()) {
                            StdDraw.clear(Color.black);
                            StdDraw.setPenColor(Color.red);
                            StdDraw.text(50 / 2, 30 / 2, "You beat the demon!");
                            StdDraw.show();
                            StdDraw.pause(3000);
                            x.append(c);
                            keyspressed.append(x);
                            return;
                        } else {
                            int demondamage = demon.getNextattack();
                            demon.attack(you);
                            demonpunch(lvl, demondamage);
                            if (you.isdead()) {
                                StdDraw.clear(Color.black);
                                StdDraw.setPenColor(Color.red);
                                StdDraw.text(50 / 2, 30 / 2, "You are dead.");
                                StdDraw.show();
                                StdDraw.pause(3000);
                                return;
                            }
                        }
                        x.append(c);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        /**
        TETile[][] world = Engine.interactWithInputString("n1234sssssssdds:");
        TERenderer ter = new TERenderer();
        ter.initialize(80, 32);
        ter.renderFrame(world);
         */
        Engine e = new Engine();
        e.victoryscreen();
    }
}


