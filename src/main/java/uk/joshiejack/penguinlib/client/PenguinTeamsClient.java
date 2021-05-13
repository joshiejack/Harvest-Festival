package uk.joshiejack.penguinlib.client;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class PenguinTeamsClient {
    private static final Multimap<UUID, UUID> teamMembers = HashMultimap.create();
    private static PenguinTeam INSTANCE;

    public static PenguinTeam getInstance() {
        return INSTANCE;
    }

    public static void setInstance(NBTTagCompound data) {
        INSTANCE = new PenguinTeam(data);
        INSTANCE.setClient();//Yes bitch
    }

    public static void setMembers(Map<UUID, UUID> memberOf) {
        //We need to reverse this basically
        memberOf.forEach((key, value) -> teamMembers.get(value).add(key));
    }

    public static void changeTeam(UUID player, UUID oldTeam, UUID newTeam) {
        teamMembers.get(oldTeam).remove(player);
        teamMembers.get(newTeam).add(player);
    }

    public static Collection<UUID> members(UUID teamID) {
        return teamMembers.get(teamID);
    }
}
