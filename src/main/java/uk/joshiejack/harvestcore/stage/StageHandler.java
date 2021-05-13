package uk.joshiejack.harvestcore.stage;

import uk.joshiejack.penguinlib.data.database.Row;

public abstract class StageHandler<E extends StageHandler> {
    public abstract int getMaximumStage();
    public abstract boolean grow(int stage);
    public abstract E createFromData(Row row);
}
