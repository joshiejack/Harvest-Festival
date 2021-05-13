package uk.joshiejack.harvestcore.stage;

import uk.joshiejack.penguinlib.data.database.Database;
import uk.joshiejack.penguinlib.data.database.Row;

public class StageHandlerPatterned extends StageHandler {

    private final boolean[] ticks;
    private final int max;

    public StageHandlerPatterned() { this.ticks = new boolean[1]; this.max = 0;}
    private StageHandlerPatterned(boolean[] ticks, int max) {
        this.ticks = ticks;
        this.max = max;
    }

    @Override
    public StageHandler createFromData(Row row) {
        boolean[] array = Database.toBooleanArray(row.get("days"));
        return new StageHandlerPatterned(array, array.length - 1);
    }

    @Override
    public int getMaximumStage() {
        return max;
    }

    @Override
    public boolean grow(int stage) {
        return ticks[stage];
    }
}
