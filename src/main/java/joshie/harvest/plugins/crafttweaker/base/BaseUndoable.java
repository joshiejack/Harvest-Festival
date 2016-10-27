package joshie.harvest.plugins.crafttweaker.base;

import minetweaker.IUndoableAction;

public abstract class BaseUndoable implements IUndoableAction {
    private boolean applied;

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void undo() {}

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

    @Override
    public String describeUndo() {
        return "";
    }

    @Override
    public Object getOverrideKey() {
        return null;
    }
}
