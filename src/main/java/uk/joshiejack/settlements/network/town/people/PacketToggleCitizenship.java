package uk.joshiejack.settlements.network.town.people;

import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.network.town.PacketAbstractTownSync;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;

@PenguinLoader
public class PacketToggleCitizenship extends PacketAbstractTownSync {
    public PacketToggleCitizenship(){}
    public PacketToggleCitizenship(int dimension, int town) {
        super(dimension, town);
    }

    @Override
    protected void handlePacket(EntityPlayer player, Town<?> town) {
        if (!player.world.isRemote) {
            PenguinTeam team = PenguinTeams.getTeamForPlayer(player);
            if (town.getCharter().getTeamID().equals(team.getID())) {
                town.getGovernment().toggleCitizenship();
                AdventureDataLoader.get(player.world).markDirty();
                PenguinNetwork.sendToEveryone(new PacketToggleCitizenship(dimension, id));
            }
        } else town.getGovernment().toggleCitizenship();
    }
}
