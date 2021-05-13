package uk.joshiejack.settlements.entity.ai.action;

public abstract class ActionMental extends Action {
    @Override
    public boolean isPhysical() {
        return false;
    }
}
