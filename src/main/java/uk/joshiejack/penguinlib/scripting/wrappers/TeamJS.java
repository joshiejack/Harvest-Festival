package uk.joshiejack.penguinlib.scripting.wrappers;

import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;

public class TeamJS extends AbstractJS<PenguinTeam> {
    public TeamJS(PenguinTeam team) {
        super(team);
    }

    public TeamStatusJS status() {
        return WrapperRegistry.wrap(this);
    }

    public int size() {
        return penguinScriptingObject.members().size();
    }
}
