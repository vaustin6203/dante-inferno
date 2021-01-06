package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.List;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    private static class Position {
        int x;
        int y;

        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /** adds a hexagon to the world. */
    public  static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        for (int i = 0; i < 2 * s; i += 1) {
            int rowY = p.y + i;
            int rowXStart = p.x + hexRowOffset(s, i);
            Position startP = new Position(rowXStart, rowY);
            int rowWidth = hexRowWidth(s, i);
            addRow(world, startP, rowWidth, t);
        }
    }

    /** Computes relative x coordinate of the leftmost tile in the ith
     * row of a hexagon, assuming that the bottom row has an x-coordinate
     * of zero. For example, if s = 3, and i = 2, this function
     * returns -2, because the row 2 up from the bottom starts 2 to the left
     * of the start position, e.g. */
    public static int hexRowOffset(int s, int i) {
        int effectI = i;
        if (i >= s) {
            effectI = 2 * s - 1 - effectI;
        }
        return -1 * effectI;
    }

    /** Computes the width of row i for a size s hexagon. */
    public static int hexRowWidth(int s , int i) {
        int effectI = i;
        if (i >= s) {
            effectI = 2 * s - 1 - effectI;
        }

        return s + 2 * effectI;
    }

    /** adds a row of the same tile. */
    public static void addRow(TETile[][] world, Position p, int width, TETile t) {
        for (int xi = 0; xi < width; xi += 1) {
            int xCoord = p.x + xi;
            int yCoord = p.y;
            world[xCoord][yCoord] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
        }
    }

    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being empty space.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.NOTHING;
            default: return Tileset.NOTHING;
        }
    }

    /** draws a column of N hexegons, each with a random biome. */
    public static void drawRandomVerticalHexes(int n, int s, Position p, TETile[][] world, boolean b) {
        if (n > 0) {
            addHexagon(world, p, s, randomTile());
            int y = p.y + s * 2;
            if (b) {
                y = p.y - s * 2;
            }
            drawRandomVerticalHexes(n - 1, s, new Position(p.x, y), world, b);
        } else {
            drawHex(world);
        }
    }

    public static Position startTop(Position p, int s, int n) {
        int x = p.x + s;
        int y = p.y + s * 2 * n;
        return new Position(x, y);
    }

    public static Position startBottom(Position p, int s, int n) {
        int x = p.x + s + 2;
        int y = p.y - s * 2 * n;
        return new Position(x, y);
    }

    public static void drawHex(TETile[][] world) {
        for (TETile[] row : world) {
            for (TETile col : row) {
                if (col == null) {
                    System.out.print(" ");
                } else {
                    System.out.print("a");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        List<Integer> numHexes = List.of(3, 4, 5, 4, 3);
        Position p = new Position(0, 0);
        TETile[][] world = new TETile[50][50];
        boolean b = true;
        for (Integer n : numHexes) {
            if (b) {
                p = startTop(p, 3, n);
            } else {
                p = startBottom(p, 3, n);
            }
            drawRandomVerticalHexes(n, 3, p, world, b);
            if (b) {
                b = false;
            } else {
                b = true;
            }
        }
    }

}
