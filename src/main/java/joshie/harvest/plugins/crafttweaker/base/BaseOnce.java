package joshie.harvest.plugins.crafttweaker.base;

import crafttweaker.IAction;

public abstract class BaseOnce implements IAction {
    private boolean applied;

    public abstract String getDescription();

    @Override
    public void apply() {
        if (!isApplied()) {
            applied = true;
            applyOnce();
        }
    }

    public boolean isApplied() {
        return applied;
    }

    public abstract void applyOnce();

    @Override
    public String describe() {
        return "[Harvest Festival] " + getDescription();
    }
}
