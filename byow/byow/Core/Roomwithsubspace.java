package byow.Core;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class Roomwithsubspace {
    private Position topleft; // topleft
    private Position bottomright; // bottomright
    private List<Position> doors;
    private LinkedList<Integer> allquadrants;

    public Roomwithsubspace(Position l1, Position r2) {
        this.topleft = l1;
        this.bottomright = r2;
        this.allquadrants = findQuadrants(l1, r2);
    }

    public int removequadrant() {
        return this.allquadrants.removeFirst();
    }

    public void addquadrant(LinkedList<Integer> list) {
        this.allquadrants = list;
    }

    public void addquadrant(int x) {
        this.allquadrants.addLast(x);
    }

    public int area() {
        int height = topleft.getY() - bottomright.getY() + 1;
        int width = bottomright.getX() - topleft.getX() + 1;
        return height * width;
    }



    public LinkedList<Integer> findQuadrants(Position l1, Position r2) {
        LinkedList<Integer> results = new LinkedList<>();
        if (l1.getX() >= 0 && l1.getX() < 40 && l1.getY() >= 15 && l1.getY() < 30) {
            results.add(1);
        }
        if (r2.getX() >= 40 && r2.getX() < 80 && l1.getY() >= 15 && l1.getY() < 30) {
            results.add(2);
        }
        if (r2.getX() >= 40 && r2.getX() < 80 && r2.getY() >= 0 && r2.getY() < 15) {
            results.add(3);
        }
        if (l1.getX() >= 0 && l1.getX() < 40 && r2.getY() >= 0 && r2.getY() < 15) {
            results.add(4);
        }
        return results;
    }

    public boolean hasotherquad() {
        return allquadrants.size() > 1;
    }

    public List<Integer> getQuadrants() {
        return allquadrants;
    }

    public Position topleft() {
        return this.topleft;
    }

    public Position bottomright() {
        return this.bottomright;
    }

    public void changetopleft(Position p) {
        this.topleft = p;
    }

    public void changebottomright(Position p) {
        this.bottomright = p;
    }

    /** the GOD method: returns true if r1 and r2 overlap. @source: modified version of
     * https://www.geeksforgeeks.org/find-two-rectangles-overlap/
     * */
    public static boolean overlapping(Roomwithsubspace r1, Roomwithsubspace r2) {
        if (r1.topleft().getX() > r2.bottomright().getX()
                || r2.topleft().getX() > r1.bottomright().getX()) {
            return false;
        }
        // If one rectangle is above other
        if (r1.topleft().getY() < r2.bottomright().getY()
                || r2.topleft().getY() < r1.bottomright().getY()) {
            return false;
        }
        return true;
    }

    /** returns true if object overlaps with rect */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Roomwithsubspace otherPos = (Roomwithsubspace) other;
        return Roomwithsubspace.overlapping(this, otherPos);
    }

    /** don't actually know if this hashcode is good */
    @Override
    public int hashCode() {
        return allquadrants.get(0);
    }

    /** given start door and direction, returns a starter 3*3 room */
    public Roomwithsubspace(Position door, int direction) {
        if (direction == 0) {
            topleft = Position.nextpos(Position.nextpos(door, 1, 3), 2, 0);
            bottomright = Position.nextpos(door, 1, 1);
        } else if (direction == 1) {
            topleft = Position.nextpos(door, 1, 0);
            bottomright = Position.nextpos(Position.nextpos(door, 1, 2), 2, 1);
        } else if (direction == 2) {
            topleft = Position.nextpos(door, 1, 3);
            bottomright = Position.nextpos(Position.nextpos(door, 1, 1), 2, 2);
        } else {
            topleft = Position.nextpos(Position.nextpos(door, 1, 0), 2, 3);
            bottomright = Position.nextpos(door, 1, 2);
        }
    }

    /** given position, returns surrounding squares(imagine
     * p is center of 3*3 room; returns other 8 positions) */
    public static PositionSet border(Position p) {
        List<Position> results = new ArrayList<>();
        results.add(new Position(p.getX() - 1, p.getY() + 1));
        results.add(new Position(p.getX(), p.getY() + 1));
        results.add(new Position(p.getX() + 1, p.getY() + 1));
        results.add(new Position(p.getX() - 1, p.getY()));
        results.add(new Position(p.getX() + 1, p.getY()));
        results.add(new Position(p.getX() - 1, p.getY() - 1));
        results.add(new Position(p.getX(), p.getY() - 1));
        results.add(new Position(p.getX() + 1, p.getY() - 1));
        return new PositionSet(results);
    }

}
