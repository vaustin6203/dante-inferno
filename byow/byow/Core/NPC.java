package byow.Core;

import byow.TileEngine.Tileset;

public class NPC {
    private Position position;
    private int HP = 66;
    private int attackpower = 15;
    private Level lvl;
    private int nextattack;

    public NPC(Position p, Level l) {
        this.position = p;
        this.lvl = l;
    }

    public boolean isdead() {
        return HP <= 0;
    }

    public void death() {
        lvl.getWorld()[this.position.getX()][this.position.getY()] = Tileset.LOCKED_DOOR;
    }

    private void changeHP(int change) {
        this.HP -= change;
    }

    public Position getpos() {
        return this.position;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public int nextattack() {
        nextattack = this.attackpower + RandomUtils.uniform(lvl.getRandom(), 0, 5);
        return nextattack;
    }

    public int getNextattack() {
        return nextattack;
    }

    public void attack(Avatar a) {
        a.changeHP(this.attackpower + nextattack);
    }

    public void getsattacked(int change) {
        changeHP(change);
        if (isdead()) {
            death();
        }
    }

    public String getImageString() {
        int p = this.lvl.getPointer();
        if (p == 0) {
            return "images/demon0.png";
        } else if (p == 1) {
            return "images/demon1.png";
        } else {
            return "images/demon2.png";
        }
    }

    public int getHP() {
        return this.HP;
    }

    public int getDamage() {
        return this.attackpower;
    }

}
