package uk.joshiejack.penguinlib.scripting.wrappers;

import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import net.minecraft.nbt.NBTTagCompound;

public class TeamStatusJS extends AbstractJS<TeamJS> {
    public TeamStatusJS(TeamJS player) {
        super(player);
    }

    public int get(String status) {
        NBTTagCompound data = penguinScriptingObject.penguinScriptingObject.getData();
        return data.hasKey("PenguinStatuses") ? data.getCompoundTag("PenguinStatuses").getInteger(status) : 0;
    }

    public void set(WorldJS<?> worldJS, String status, int value) {
        PenguinTeam team = penguinScriptingObject.penguinScriptingObject;
        NBTTagCompound data = team.getData();
        if (!data.hasKey("PenguinStatuses")) data.setTag("PenguinStatuses", new NBTTagCompound());
        if (value == 0) data.getCompoundTag("PenguinStatuses").removeTag(status);
        else data.getCompoundTag("PenguinStatuses").setInteger(status, value);
        if (!team.isClient()) {
            //Mark the data as dirty?
            PenguinTeams.get(worldJS.penguinScriptingObject).markDirty();
            team.syncToTeam(worldJS.penguinScriptingObject); //Sync the data to the client
        }
    }

    public void adjust(WorldJS<?> worldJS, String status, int value) {
        set(worldJS, status, get(status) + value);
    }

    public void adjustWithRange(WorldJS<?> worldJS, String status, int value, int min, int max) {
        set(worldJS, status, Math.min(max, Math.max(min, get(status) + value)));
    }
}
