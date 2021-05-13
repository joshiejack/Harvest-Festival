package uk.joshiejack.harvestcore.stage;

import uk.joshiejack.penguinlib.data.database.Row;

public class StageHandlerDefault extends StageHandler {
    private final int max;

    public StageHandlerDefault(){ this.max = 0; }
    private StageHandlerDefault(int max) {
        this.max = max;
    }

    @Override
    public StageHandler createFromData(Row row) {
        return new StageHandlerDefault(row.get("days"));
    }

    @Override
    public int getMaximumStage() {
        return max;
    }

    @Override
    public boolean grow(int stage) {
        return true;
    }
}
