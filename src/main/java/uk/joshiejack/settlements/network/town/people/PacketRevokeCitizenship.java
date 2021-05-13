package uk.joshiejack.settlements.network.town.people;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

@PenguinLoader(side = Side.SERVER)
public class PacketRevokeCitizenship extends PenguinPacket {
    private UUID uuid;
    private int dimension;
    private int id;

    public PacketRevokeCitizenship() {}
    public PacketRevokeCitizenship(int dimension, int id, UUID uuid) {
        this.dimension = dimension;
        this.id = id;
        this.uuid = uuid;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dimension);
        buf.writeInt(id);
        ByteBufUtils.writeUTF8String(buf, uuid.toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = buf.readInt();
        id = buf.readInt();
        uuid = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }


    @Override
    public void handlePacket(EntityPlayer player) {
        Town<?> town = AdventureDataLoader.get(player.world).getTownByID(dimension, id);
        PenguinTeam team = PenguinTeams.getTeamForPlayer(player);
        UUID playerUUID = PlayerHelper.getUUIDForPlayer(player);
        if (team.getID().equals(town.getCharter().getTeamID()) && playerUUID.equals(team.getOwner())) { //Only the owner can kick
            PenguinTeams.get(player.world).changeTeam(player.world, playerUUID, playerUUID); //Kick back to their personal team
            town.getGovernment().getApplications().remove(playerUUID);
            PenguinNetwork.sendToTeam(new PacketSyncApplications(dimension, id, town.getGovernment().getApplications()), player.world, town.getCharter().getTeamID());
        }
    }
}
