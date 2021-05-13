package uk.joshiejack.penguinlib.client.gui;

import java.util.List;

public class CyclingObject<T> {
    private final List<T> stacks;
    private T current;
    private T empty;
    private long ticker = 0;
    protected int id;

    public CyclingObject(List<T> stacks, T empty) {
        this.stacks = stacks;
        this.current = empty;
        this.empty = empty;
    }

    private T getStackFromID(int withOffset) {
        int id = withOffset % stacks.size();
        if (id < 0) id = stacks.size();
        if (id >= stacks.size()) {
            id = 0;
        }

        return stacks.get(id);
    }

    public T getStack(int offset) {
        if (stacks.isEmpty()) return empty;
        if (System.currentTimeMillis() - ticker > 1000) {
            ticker = System.currentTimeMillis();
            id++;

            if (id >= stacks.size()) {
                id = 0; //Reset to 0
            }
        }

        return getStackFromID(id + offset);
    }
}
