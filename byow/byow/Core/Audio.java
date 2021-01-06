package byow.Core;

import edu.princeton.cs.introcs.StdAudio;

public class Audio {

    public static void playMenu() {
        StdAudio.loop("song_menu.wav");
    }

    public static void playLevel0() {

        StdAudio.loop("song_level0.wav");
    }

    public static void playLevel1() {
        StdAudio.loop("song_level1.wav");
    }

    public static void playLevel2() {

        StdAudio.loop("song_level2.wav");
    }

    public static void quitAudio() {
        StdAudio.close();
    }

    public static void main(String[] args) {
        playLevel2();
    }
}
