package byow.Core;

import java.util.Iterator;
import java.util.List;

public class PositionSet implements Iterable<Position> {
    private List<Position> items;
    private int size;

    public PositionSet(List<Position> items) {
        this.items = items;
        size = items.size();
    }

    public Iterator<Position> iterator() {
        return new PositionSetIter();
    }

    public Position get(int x) {
        return items.get(x);
    }

    private class PositionSetIter implements Iterator<Position> {
        private int index;
        private PositionSetIter() {
            index = 0;
        }
        public boolean hasNext() {
            return index < size;
        }
        public Position next() {
            Position nextitem = items.get(index);
            index += 1;
            return nextitem;
        }


    }

}
