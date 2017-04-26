package joshie.harvest.api.mining;

public class MiningContext {
    protected final int floor;

    public MiningContext(int floor) {
        this.floor = floor;
    }

    /** Checks if the floor is valid for this block to generate
     * @param floor     the floor number
     * @return true if this floor is valid for generation */
    public boolean isValidFloor(int floor) {
        return floor >= this.floor;
    }
}