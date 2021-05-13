package uk.joshiejack.penguinlib.data.custom;

public interface ICustom {
    AbstractCustomData.ItemOrBlock<?, ?> getDefaults();
    AbstractCustomData.ItemOrBlock<?, ?>[] getStates();
}
