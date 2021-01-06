package byow.Core;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Level {
    private Random RANDOM;
    private Roomwithsubspace startroom;
    private LinkedList<NPC> demons;
    private LinkedList<Position> startpos;
    private HashSet<Roomwithsubspace> rooms;
    private LinkedList<Position> doors;
    private LinkedList<Avatar> avatars;
    private int size;
    private ArrayList<TETile[][]> worlds;
    private ArrayList<HashMap<String, Object>> specs;
    private int pointer;
    private TETile nothing = Tileset.NOTHING;
    private int roomsize = 10;
    private double roomlikelihood = 0.9;
    private double doorlikelihood = 0.9;


    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    public Level(long seed) {
        initializespecs();
        RANDOM = new Random(seed);
        worlds = new ArrayList<>();
        startpos = new LinkedList<>();
        demons = new LinkedList<>();
        avatars = new LinkedList<>();
        double loadfactor = RandomUtils.uniform(RANDOM, 0.5, 0.8);
        for (int p = 0; p < 3; p++) {
            pointer = p;
            worlds.add(new TETile[WIDTH][HEIGHT]);
            size = 0;
            rooms = new HashSet<>();
            doors = new LinkedList<>();
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    this.getWorld()[x][y] = getnothing();
                }
            }
            Position start = new Position(RandomUtils.uniform(RANDOM, 20, 60),
                    RandomUtils.uniform(RANDOM, 10, 20), RandomUtils.uniform(RANDOM, 0, 4));
            makeRectangle(start);
            while (this.getsize() / (WIDTH * HEIGHT * 1.0) < loadfactor && getdoors().size() > 0) {
                Position door = getdoors().getFirst();
                if (this.getWorld()[door.getX()][door.getY()] == getwall()) {
                    getdoors().removeFirst();
                } else {
                    makeRectangle(getdoors().removeFirst());
                }
            }
            demons.add(new NPC(new Position(startroom.topleft().getX() + 1,
                    startroom.topleft().getY() - 1), this));
            startpos.add(Position.nextpos(start, 2, start.getDirection()));
            getdoors().add(Position.nextpos(start, 1, start.getDirection()));
            avatars.add(new Avatar(startpos.get(p), Tileset.AVATAR, this));
            this.getWorld()[getAvatar().getX()][getAvatar().getY()] = Tileset.AVATAR;
            this.getWorld()[getdemon().getX()][getdemon().getY()] = getdemontile();
            for (Position d: getdoors()) {
                this.getWorld()[d.getX()][d.getY()] = getwall();
            }
        }
        pointer = 0;
    }

    public TETile getwall() {
        return (TETile) specs.get(pointer).get("Wall");
    }
    public TETile getfloor() {
        return (TETile) specs.get(pointer).get("Floor");
    }
    public TETile getnothing() {
        return (TETile) specs.get(pointer).get("Nothing");
    }
    public TETile getdemontile() {
        return (TETile) specs.get(pointer).get("Demon");
    }

    public int getPointer() {
        return this.pointer;
    }

    public Avatar getAvatar() {
        return avatars.get(pointer);
    }

    public void changepointer() {
        pointer += 1;
    }

    public void changepointer(int s) {
        pointer = s;
    }

    public NPC getdemon() {
        return demons.get(pointer);
    }

    private HashSet<Roomwithsubspace> getRooms() {
        return rooms;
    }

    private LinkedList<Position> getdoors() {
        return doors;
    }

    public TETile[][] getWorld() {
        return worlds.get(pointer);
    }

    private int getsize() {
        return size;
    }

    private void initializespecs() {
        specs = new ArrayList<>();
        HashMap<String, Object> zero = new HashMap<>();
        zero.put("Floor", Tileset.DUNGEONFLOORZERO);
        zero.put("Wall", Tileset.DUNGEONWALLZERO);
        zero.put("Nothing", Tileset.NOTHINGZERO);
        zero.put("Demon", Tileset.DEMONZERO);
        HashMap<String, Object> first = new HashMap<>();
        first.put("Floor", Tileset.DUNGEONFLOORONE);
        first.put("Wall", Tileset.DUNGEONWALLONE);
        first.put("Nothing", Tileset.NOTHINGONE);
        first.put("Demon", Tileset.DEMONONE);
        HashMap<String, Object> second = new HashMap<>();
        second.put("Floor", Tileset.DUNGEONFLOORTWO);
        second.put("Wall", Tileset.DUNGEONWALLTWO);
        second.put("Nothing", Tileset.NOTHINGTWO);
        second.put("Demon", Tileset.DEMONTWO);
        specs.add(zero);
        specs.add(first);
        specs.add(second);

    }

    public Random getRandom() {
        return this.RANDOM;
    }



    /** Given room, generate 0-3 doors. Doors cannot be on corners or on the same side of the door
     * that originally entered the room */
    private List<Position> doorgenerator(Roomwithsubspace r, int direction) {
        List<Integer> directionnodoorscannotface = Position.oppositedirection(direction);
        List<Position> roomdoors = new ArrayList<>();
        for (Integer i: directionnodoorscannotface) {
            if (RandomUtils.uniform(RANDOM, 0.0, 1.1) < doorlikelihood) {
                if (i == 0) {
                    int randomx = RandomUtils.uniform(RANDOM, r.topleft().getX() + 1,
                            r.bottomright().getX());
                    if (possibleroom(new Position(randomx, r.topleft().getY() + 2))) {
                        roomdoors.add(new Position(randomx,
                                r.topleft().getY(), 0));
                    }
                } else if (i == 1) {
                    int randomy = RandomUtils.uniform(RANDOM, r.bottomright().getY() + 1,
                            r.topleft().getY());
                    if (possibleroom(new Position(r.bottomright().getX() + 2, randomy))) {
                        roomdoors.add(new Position(r.bottomright().getX(),
                                randomy, 1));
                    }
                } else if (i == 2) {
                    int randomx = RandomUtils.uniform(RANDOM, r.topleft().getX() + 1,
                            r.bottomright().getX());
                    if (possibleroom(new Position(randomx, r.bottomright().getY() - 2))) {
                        roomdoors.add(new Position(randomx,
                                r.bottomright().getY(), 2));
                    }
                } else {
                    int randomy = RandomUtils.uniform(RANDOM, r.bottomright().getY() + 1,
                            r.topleft().getY());
                    if (possibleroom(new Position(r.topleft().getX() - 2, randomy))) {
                        roomdoors.add(new Position(r.topleft().getX(),
                                randomy, 3));
                    }
                }
            }
        }
        return roomdoors;
    }

    /** Given room and list of doors, draws the room */
    private void boxdrawer(Roomwithsubspace room, List<Position> roomdoors) {
        for (int x = room.topleft().getX(); x <= room.bottomright().getX(); x++) {
            int y = room.topleft().getY();
            int y1 = room.bottomright().getY();
            if (roomdoors.contains(new Position(x, y))) {
                if (RandomUtils.uniform(RANDOM, 0, 10) > 4) {
                    this.getWorld()[x][y] = Tileset.ENERGY;
                } else {
                    this.getWorld()[x][y] = getfloor();
                }

            } else {
                this.getWorld()[x][y] = getwall();
            }
            if (roomdoors.contains(new Position(x, y1))) {
                this.getWorld()[x][y1] = getfloor();
            } else {
                this.getWorld()[x][y1] = getwall();
            }
        }
        for (int y = room.bottomright().getY() + 1; y < room.topleft().getY(); y++) {
            int x = room.topleft().getX();
            int x2 = room.bottomright().getX();
            if (roomdoors.contains(new Position(x, y))) {
                this.getWorld()[x][y] = getfloor();
            } else {
                this.getWorld()[x][y] = getwall();
            }
            if (roomdoors.contains(new Position(x2, y))) {
                this.getWorld()[x2][y] = getfloor();
            } else {
                this.getWorld()[x2][y] = getwall();
            }
        }
        for (int x = room.topleft().getX() + 1; x < room.bottomright().getX(); x++) {
            for (int y = room.bottomright().getY() + 1; y < room.topleft().getY(); y++) {
                this.getWorld()[x][y] = getfloor();
            }
        }
        size += room.area();
    }



    /** Primary room creation method. Given door of last room, draws a new
     * room of random width/height/random set of doors */
    private void makeRectangle(Position p) {
        Position start = Position.nextpos(p, 2);
        if (!possibleroom(start)) {
            getWorld()[p.getX()][p.getY()] = getwall();
            return;
        }
        Position newroomdoor = null;
        if (p.getDirection() == 0) {
            newroomdoor = new Position(p.getX(), p.getY() + 1, p.getDirection());
        } else if (p.getDirection() == 1) {
            newroomdoor = new Position(p.getX() + 1, p.getY(), p.getDirection());
        } else if (p.getDirection() == 2) {
            newroomdoor = new Position(p.getX(), p.getY() - 1, p.getDirection());
        } else {
            newroomdoor = new Position(p.getX() - 1, p.getY(), p.getDirection());
        }
        Roomwithsubspace newroom;
        if (RandomUtils.uniform(RANDOM, 0.0, 1.1) < roomlikelihood) {
            newroom = rectanglerange(newroomdoor);
        } else {
            newroom = hallwaycreator(newroomdoor);
        }
        List<Position> roomdoors = doorgenerator(newroom, p.getDirection());
        for (Position pos : roomdoors) {
            getdoors().add(pos);
        }
        roomdoors.add(newroomdoor);
        boxdrawer(newroom, roomdoors);
        Roomwithsubspace curr = newroom;
        int thing = curr.getQuadrants().size();
        for (int i = 0; i < thing; i++) {
            getRooms().add(curr);
            curr.addquadrant(curr.removequadrant());
        }
        startroom = curr;

    }

    /** given possible CENTER of a 3*3 room, tells us if room creation is viable. */
    public boolean possibleroom(Position p) {
        PositionSet tiles = Roomwithsubspace.border(p);
        // Checks if surrounding tiles are out-of-bounds
        for (Position pos: tiles) {
            if (!inBounds(pos)) {
                return false;
            }
        }
        // Checks if this room overlaps with rooms already created
        Roomwithsubspace curr = new Roomwithsubspace(tiles.get(0), tiles.get(7));
        int thing = curr.getQuadrants().size();
        for (int i = 0; i < thing; i++) {
            if ((getRooms().contains(curr))) {
                return false;
            }
            curr.addquadrant(curr.removequadrant());
        }
        return true;
    }

    /** Given corner and direction, stretches corner in given direction to random length or until it
     * collides with other room */
    private Position potentialcorner(Position topleft, Position bottomright,
                                     int direction, boolean lookingattopleft) {
        int expansiondist = RandomUtils.uniform(RANDOM, 1, roomsize);
        if (lookingattopleft) {
            for (int i = 0; i < expansiondist; i++) {
                Position nextcorner = Position.nextpos(topleft, 1, direction);
                if (!inBounds(nextcorner)) {
                    return topleft;
                }
                Roomwithsubspace curr = new Roomwithsubspace(nextcorner, bottomright);
                LinkedList<Integer> quadrants = new LinkedList<>();
                while (curr.getQuadrants().size() != 0) {
                    if ((getRooms().contains(curr))
                            || this.getWorld()[nextcorner.getX()][nextcorner.getY()] == getwall()) {
                        return topleft;
                    }
                    quadrants.add(curr.removequadrant());
                }
                curr.addquadrant(quadrants);
                topleft = nextcorner;
            }
            return topleft;
        } else {
            for (int i = 0; i < expansiondist; i++) {
                Position nextcorner = Position.nextpos(bottomright, 1, direction);
                if (!inBounds(nextcorner)) {
                    return bottomright;
                }
                Roomwithsubspace curr = new Roomwithsubspace(topleft, nextcorner);
                int thing = curr.getQuadrants().size();
                for (int j = 0; j < thing; j++) {
                    if (getRooms().contains(curr)
                            || getWorld()[nextcorner.getX()][nextcorner.getY()] == getwall()) {
                        return bottomright;
                    }
                    curr.addquadrant(curr.removequadrant());
                }

                bottomright = nextcorner;
            }
            return bottomright;
        }
    }

    /** Method that creates hallways similar to rectanglerange. Unused for now */
    private Roomwithsubspace hallwaycreator(Position p) {
        Roomwithsubspace potentialroom = new Roomwithsubspace(p, p.getDirection());
        if (p.getDirection() == 0 || p.getDirection() == 3) {
            Position topleft = potentialcorner(potentialroom.topleft(),
                    potentialroom.bottomright(), p.getDirection(), true);
            potentialroom = new Roomwithsubspace(topleft, potentialroom.bottomright());
        }
        if (p.getDirection() == 1 || p.getDirection() == 2) {
            Position bottomright = potentialcorner(potentialroom.topleft(),
                    potentialroom.bottomright(), p.getDirection(), false);
            potentialroom = new Roomwithsubspace(potentialroom.topleft(), bottomright);
        }
        return potentialroom;
    }

    /** Given door in new room, return a new room of random width and height */

    public Roomwithsubspace rectanglerange(Position p) {
        Roomwithsubspace potentialroom = new Roomwithsubspace(p, p.getDirection());
        if (p.getDirection() == 0) {
            Position topleft = potentialcorner(potentialroom.topleft(),
                    potentialroom.bottomright(), 3, true);
            topleft = potentialcorner(topleft, potentialroom.bottomright(), 0, true);
            Position bottomright = potentialcorner(potentialroom.topleft(),
                    potentialroom.bottomright(), 1, false);
            potentialroom = new Roomwithsubspace(topleft, bottomright);
        }
        if (p.getDirection() == 1) {
            Position topleft = potentialcorner(potentialroom.topleft(),
                    potentialroom.bottomright(), 0, true);
            Position bottomright = potentialcorner(topleft, potentialroom.bottomright(), 2, false);
            bottomright = potentialcorner(topleft, bottomright, 1, false);
            potentialroom = new Roomwithsubspace(topleft, bottomright);
        }
        if (p.getDirection() == 2) {
            Position topleft = potentialcorner(potentialroom.topleft(),
                    potentialroom.bottomright(), 3, true);
            Position bottomright = potentialcorner(topleft, potentialroom.bottomright(), 1, false);
            bottomright = potentialcorner(topleft, bottomright, 2, false);
            potentialroom = new Roomwithsubspace(topleft, bottomright);
        }
        if (p.getDirection() == 3) {
            Position topleft = potentialcorner(potentialroom.topleft(),
                    potentialroom.bottomright(), 0, true);
            topleft = potentialcorner(topleft, potentialroom.bottomright(), 3, true);
            Position bottomright = potentialcorner(topleft, potentialroom.bottomright(), 2, false);
            potentialroom = new Roomwithsubspace(topleft, bottomright);
        }
        return potentialroom;
    }

    public boolean inBounds(Position p) {
        return p.getX() >= 0 && p.getX() < 80 && p.getY() >= 0 && p.getY() < 30;
    }

    public static void main(String[] args) {
        TETile[][] world = new Level(5).getWorld();
        TERenderer ter = new TERenderer();
        ter.initialize(80, 30);
        ter.renderFrame(world);
    }
}
