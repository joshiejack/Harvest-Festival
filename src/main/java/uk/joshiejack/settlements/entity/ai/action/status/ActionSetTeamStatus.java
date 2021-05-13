package uk.joshiejack.settlements.entity.ai.action.status;

import net.minecraft.world.WorldServer;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionMental;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.scripting.wrappers.TeamJS;
import uk.joshiejack.penguinlib.scripting.wrappers.WorldServerJS;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.EnumActionResult;

@PenguinLoader("set_team_status")
public class ActionSetTeamStatus extends ActionMental {
    private String status;
    private int value;

    @Override
    public ActionSetTeamStatus withData(Object... params) {
        this.status = (String) params[0];
        this.value = (Integer) params[1];
        return this;
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        if (player != null) {
            new TeamJS(PenguinTeams.getTeamForPlayer(player)).status().set(new WorldServerJS((WorldServer) npc.world), status, value);
        }

        return EnumActionResult.SUCCESS;
    }
}
