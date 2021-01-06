package byow.Core;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;


/** A class that makes a new file in the current directory given the seed,
the String of keys pressed by the player, and the String name the player 
wants to save the file under, in order to save the current state of the game. 
The string of keys pressed needs to have the "Q" removed before the SaveGame 
class takes it in as an argument. 
@source 
Used source to learn how to make a new text file and write into it.
https://howtodoinjava.com/java/io/how-to-create-a-new-file-in-java/ */
public class SaveGame {
    /**
    String seed;
     */
    String keysPressed;

    public SaveGame(String keys) {
        /**
        seed = Integer.toString(s);
         */
        keysPressed = keys;
    }

    /** Only need to have the name of the file we want in order to make it in
    the current directory . */
    public static void newFile(SaveGame sg) {
        sg.save();
    }

    private void save() {
        File f = new File("filename.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(keysPressed);
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    /** This main method just gives an example of how the class is used.
    We only need the name of the file we want to make if the file is
    going to be saved in the current directory. */
    public static void main(String[] args) throws IOException {
        SaveGame file = new SaveGame("helloworld");
        SaveGame.newFile(file);
    }
}

/** When we reach the "Q" char in the game, a command line should pop up for the 
player that asks them to type the name of the file they want to save the game under. 
This name will be used as the argument for the SaveGame class. This way, a player 
can save multiple game states at a time. */
