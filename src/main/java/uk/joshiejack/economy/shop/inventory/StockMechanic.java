package uk.joshiejack.economy.shop.inventory;

public class StockMechanic {
    private final int maximum;
    private final int increase;

    public StockMechanic(int maximum, int increase) {
        this.maximum = maximum;
        this.increase = increase;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getIncrease() {
        return increase;
    }
}
