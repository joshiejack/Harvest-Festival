package joshie.harvest.core.util.holders;

public abstract class AbstractDataHolder<C extends AbstractDataHolder> extends AbstractHolder {
    public abstract void merge(C stack);
}
