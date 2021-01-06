package byow.Core;

import java.util.List;
import java.util.ArrayList;

public class Position {
    private int x;
    private int y;
    private boolean isDoor;
    private int direction;
    /** Directions -- 0: North, 1: East, 2: South, 3: West
     * isDoor: returns true if tile has door on any of its sides.
     * direction: returns which side door is on,
     * e.g. if door is on north side of a tile, p.isDoor would return true,
     * p.direction would return 0
     */

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.isDoor = false;
    }

    public Position(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.isDoor = true;
        this.direction = direction;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public static List<Integer> oppositedirection(int direction) {
        List<Integer> directions = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            directions.add(i);
        }
        if (direction == 0 || direction == 1) {
            directions.remove(direction + 2);
        } else {
            directions.remove(direction - 2);
        }
        return directions;

    }

    public boolean isDoor() {
        return this.isDoor;
    }

    public int getDirection() {
        if (!isDoor) {
            return -1;
        }
        return this.direction;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Position otherPos = (Position) other;
        return this.getX() == otherPos.getX() && this.getY() == otherPos.getY();
    }

    @Override
    public int hashCode() {
        return this.getX() + (31 * this.getY());
    }


    public static Position moreLeft(Position a, Position b) {
        if (a.getX() < b.getX()) {
            return a;
        }
        return b;
    }

    public static Position moreRight(Position a, Position b) {
        if (a.getX() > b.getX()) {
            return a;
        }
        return b;
    }

    public static Position moreUp(Position a, Position b) {
        if (a.getY() > b.getY()) {
            return a;
        }
        return b;
    }

    public static Position moreDown(Position a, Position b) {
        if (a.getY() < b.getY()) {
            return a;
        }
        return b;
    }

    public static Position nextpos(Position p, int length) {
        int n = p.getDirection();
        if (n == 0) {
            return new Position(p.getX(), p.getY() + length);
        }
        if (n == 1) {
            return new Position(p.getX() + length, p.getY());
        }
        if (n == 2) {
            return new Position(p.getX(), p.getY() - length);
        }
        if (n == 3) {
            return new Position(p.getX() - length, p.getY());
        }
        return p;
    }

    public static Position nextpos(Position p, int length, int direction) {
        int n = direction;
        if (n == 0) {
            return new Position(p.getX(), p.getY() + length);
        }
        if (n == 1) {
            return new Position(p.getX() + length, p.getY());
        }
        if (n == 2) {
            return new Position(p.getX(), p.getY() - length);
        }
        if (n == 3) {
            return new Position(p.getX() - length, p.getY());
        }
        return p;
    }

    @Override
    public String toString() {
        return String.format("X: " + x + " Y: " + y + " Direction: " + getDirection());
    }
}
