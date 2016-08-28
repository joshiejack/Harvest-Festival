package joshie.harvest.core.util.holder;

public abstract class AbstractDataHolder<C extends AbstractDataHolder> extends AbstractHolder {
    public abstract void merge(C stack);
}
