package uk.joshiejack.harvestcore.stage;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import uk.joshiejack.penguinlib.data.database.Row;

public class StageHandlerNumbered extends StageHandler {
    private final IntSet ticks;
    private final int max;

    public StageHandlerNumbered() { this.ticks = new IntOpenHashSet(); this.max = 0;}
    private StageHandlerNumbered(int max, IntSet ticks) {
        this.ticks = ticks;
        this.max = max;
    }

    @Override
    public StageHandler createFromData(Row row) {
        String[] days = row.get("days").toString().replace("\"", "").replace(" ", "").split(",");
        IntSet set = new IntOpenHashSet();
        int maximum = Integer.parseInt(days[days.length - 1]);
        for (String d: days) {
            set.add(Integer.parseInt(d));
        }

        return new StageHandlerNumbered(maximum, set);
    }

    @Override
    public int getMaximumStage() {
        return max;
    }

    @Override
    public boolean grow(int stage) {
        return ticks.contains(stage);
    }
}
