package byow.Core;

import java.util.List;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.Assert;

public class HelperMethodTests {
    @Test
    public void nextpostests() {
        List<Position> positions = positioncreator();
        Position goupfive = Position.nextpos(positions.remove(0), 5, 0);
        Position gobackdown = Position.nextpos(goupfive, 5, 2);
        Position dooratoneonegoingright = positions.remove(0);
        Position gorighttwice = Position.nextpos(dooratoneonegoingright, 2);
        Position goleft = Position.nextpos(dooratoneonegoingright, 1, 3);
        Assert.assertEquals(goupfive, new Position(0, 5));
        Assert.assertEquals(gobackdown, new Position(0, 0));
        Assert.assertEquals(gorighttwice, new Position(3, 1));
        Assert.assertEquals(goleft, new Position(0, 1));
        Assert.assertEquals(Position.moreLeft(new Position(0, 0), dooratoneonegoingright),
                new Position(0, 0));
    }

    @Test
    public void possibleroomtest() {
        Level lvl = new Level(125);
        Assert.assertFalse(lvl.possibleroom(new Position(20, 26)));
        Assert.assertTrue(lvl.possibleroom(new Position(1, 1, 1)));
        Assert.assertFalse(lvl.possibleroom(Position.nextpos(new Position(1, 1, 3), 1)));
    }

    /**
    @Test
    public void rectangerangetest() {
        TETile[][] world = new TETile[50][50];
        TERenderer ter = new TERenderer();
        ter.initialize(50, 50);
        for (int x = 0; x < 50; x += 1) {
            for (int y = 0; y < 50; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        Level lvl = new Level(129);
        Iterator<Roomwithsubspace>  rmsiter = lvl.getRooms().iterator();
        while (rmsiter.hasNext()) {
            Roomwithsubspace r = rmsiter.next();
            for (int x = r.topleft().getX(); x <= r.bottomright().getX(); x++) {
                for (int y = r.bottomright().getY(); y <= r.topleft().getY(); y++) {
                    world[x][y] = Tileset.GRASS;
                }
            }

        }
        ter.renderFrame(world);


    }
*/


    private List<Position> positioncreator() {
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(0, 0));
        positions.add(new Position(1, 1, 1));
        positions.add(new Position(0, 1));
        positions.add(new Position(10, 10));
        return positions;
    }

}
