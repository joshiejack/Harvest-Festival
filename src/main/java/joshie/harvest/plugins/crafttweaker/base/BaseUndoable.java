package joshie.harvest.plugins.crafttweaker.base;

import crafttweaker.IAction;

public abstract class BaseUndoable implements IAction {
    public abstract String getDescription();

    @Override
    public String describe() {
        return "[Harvest Festival] " + getDescription();
    }
}
