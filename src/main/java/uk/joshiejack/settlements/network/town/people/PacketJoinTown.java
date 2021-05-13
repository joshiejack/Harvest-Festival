package uk.joshiejack.settlements.network.town.people;

import uk.joshiejack.settlements.network.town.PacketAbstractTownSync;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.settlements.world.town.people.Citizenship;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.SERVER)
public class PacketJoinTown extends PacketAbstractTownSync {
    public PacketJoinTown() {}
    public PacketJoinTown(int dimension, int id) {
        super(dimension, id);
    }

    @Override
    protected void handlePacket(EntityPlayer player, Town<?> town) {
        if (town.getGovernment().getCitizenship() == Citizenship.OPEN) {
            PenguinTeam owner = PenguinTeams.getTeamFromID(player.world, town.getCharter().getTeamID());
            PenguinTeams.get(player.world).changeTeam(player.world, PlayerHelper.getUUIDForPlayer(player), owner.getID());
        }
    }
}
