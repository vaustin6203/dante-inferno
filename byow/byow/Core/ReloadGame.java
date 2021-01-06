package byow.Core;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;
/** A class that takes in a file name that is saved in the current 
directory and reads the stored data so it can be easily accessed 
to reload the state of a previously saved game. 
@source 
Used source to learn how to access information saved in a text file.
https://www.geeksforgeeks.org/different-ways-reading-text-file-java/ */
public class ReloadGame {
    private String keysPressed;
    private boolean fileexists;

    public ReloadGame(String fileName) throws IOException {
        File f = new File("filename.txt");
        if (f.exists()) {
            fileexists = true;
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                keysPressed = (String) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
    }



    public boolean fileexists() {
        return fileexists;
    }

    public String getKeysPressed() {
        return keysPressed;
    }


    /** This main method just gives an example of how the class is used.
    We only need the name of the file we are trying to access if the file
    is saved in the current directory.
     */
    public static void main(String[] args) throws IOException {
        ReloadGame game = new ReloadGame("filename.txt");
        String keys = game.getKeysPressed();
        System.out.println(keys);
    }

}

/** When we see an "L" char in the game, a command line should pop up that asks 
the player to type the name of the file of a particular game they want to reload. 
This way, a player can save multiple game states at a time. */
