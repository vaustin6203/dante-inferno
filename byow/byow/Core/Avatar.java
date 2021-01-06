package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.LinkedList;

public class Avatar {
    private Position position;
    private int HP;
    private int energy;
    private Level level;
    private TETile color;
    private LinkedList<Power> powers;

    private class Power {
        private int energycost;
        private int damage;

        private Power(int cost, int damage) {
            this.energycost = cost;
            this.damage = damage;
        }
    }

    public int getEnergy() {
        return energy;
    }

    public int getHP() {
        return HP;
    }

    public Avatar(Position p, TETile image, Level lvl) {
        this.position = p;
        this.HP = 100;
        this.color = image;
        this.level = lvl;
        this.energy = 75;
        powers = new LinkedList<>();
        powers.add(new Power(2, 8));
        powers.add(new Power(7, 14));
        powers.add(new Power(15, 23));
    }

    public int getabildamage(Character c) {
        return powers.get(Character.getNumericValue(c)).damage;
    }

    public boolean enoughenergy(int s) {
        int x = powers.get(s).energycost;
        if (energy - x < 0) {
            return false;
        }
        return true;
    }

    public int useability(int s) {
        int x = powers.get(s).energycost;
        if (energy - x < 0) {
            return 0;
        } else {
            energy -= x;
        }
        return powers.get(s).damage;
    }

    public TETile getimage() {
        return this.color;
    }

    public void changeHP(int change) {
        this.HP -= change;
    }

    public int getX() {
        return this.position.getX();
    }

    public int getY() {
        return this.position.getY();
    }

    public void move(char c) {
        if (c == 'w') {
            Position newp = new Position(this.position.getX(), this.position.getY() + 1);
            if (!level.inBounds(newp)
                    || level.getWorld()[newp.getX()][newp.getY()] == level.getwall()) {
                return;
            } else {
                energy(newp);
                if (level.getWorld()[newp.getX()][newp.getY()] == Tileset.LOCKED_DOOR) {
                    level.changepointer();
                } else {
                    level.getWorld()[this.getX()][this.getY()] = level.getfloor();
                    this.position = newp;
                    level.getWorld()[newp.getX()][newp.getY()] = color;
                }
            }
        } else if (c == 'a') {
            Position newp = new Position(this.position.getX() - 1, this.position.getY());
            if (!level.inBounds(newp)
                    || level.getWorld()[newp.getX()][newp.getY()] == level.getwall()) {
                return;
            } else {
                energy(newp);
                if (level.getWorld()[newp.getX()][newp.getY()] == Tileset.LOCKED_DOOR) {
                    level.changepointer();
                } else {
                    level.getWorld()[this.getX()][this.getY()] = level.getfloor();
                    this.position = newp;
                    level.getWorld()[newp.getX()][newp.getY()] = color;
                }
            }
        } else if (c == 's') {
            Position newp = new Position(this.position.getX(), this.position.getY() - 1);
            if (!level.inBounds(newp)
                    || level.getWorld()[newp.getX()][newp.getY()] == level.getwall()) {
                return;
            } else {
                energy(newp);
                if (level.getWorld()[newp.getX()][newp.getY()] == Tileset.LOCKED_DOOR) {
                    level.changepointer();
                } else {
                    level.getWorld()[this.getX()][this.getY()] = level.getfloor();
                    this.position = newp;
                    level.getWorld()[newp.getX()][newp.getY()] = color;
                }
            }
        } else if (c == 'd') {
            Position newp = new Position(this.position.getX() + 1, this.position.getY());
            if (!level.inBounds(newp)
                    || level.getWorld()[newp.getX()][newp.getY()] == level.getwall()) {
                return;
            } else {
                energy(newp);
                if (level.getWorld()[newp.getX()][newp.getY()] == Tileset.LOCKED_DOOR) {
                    level.changepointer();
                } else {
                    level.getWorld()[this.getX()][this.getY()] = level.getfloor();
                    this.position = newp;
                    level.getWorld()[newp.getX()][newp.getY()] = color;
                }
            }
        }
    }

    public boolean exhausted() {
        return energy <= 0;
    }

    public boolean isdead() {
        return HP <= 0;
    }

    private void energy(Position newp) {
        if (level.getWorld()[newp.getX()][newp.getY()] == Tileset.ENERGY) {
            this.energy = 75;
        } else {
            energy -= 1;
        }
    }

    public boolean nexttodemon() {
        PositionSet positions = Roomwithsubspace.border(this.position);
        for (Position p: positions) {
            if (level.getWorld()[p.getX()][p.getY()] == level.getdemontile()) {
                return true;
            }
        }
        return false;
    }

    public NPC adjacentdemon() {
        for (Position p : Roomwithsubspace.border(this.position)) {
            if (level.getdemon().getpos() == p) {
                return level.getdemon();
            }
        }
        return new NPC(new Position(0, 0), this.level);
    }
}

