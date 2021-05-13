package uk.joshiejack.settlements.network.town.people;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.settlements.world.town.people.Citizenship;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.SERVER)
public class PacketRequestCitizenship extends PenguinPacket {
    private int dimension;
    private int id;

    public PacketRequestCitizenship() {}
    public PacketRequestCitizenship(int dimension, int id) {
        this.dimension = dimension;
        this.id = id;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dimension);
        buf.writeInt(id);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = buf.readInt();
        id = buf.readInt();
    }


    @Override
    public void handlePacket(EntityPlayer player) {
        Town<?> town = AdventureDataLoader.get(player.world).getTownByID(dimension, id);
        if (town.getGovernment().getCitizenship() == Citizenship.APPLICATION) {
            town.getGovernment().addApplication(PlayerHelper.getUUIDForPlayer(player));
            PenguinNetwork.sendToTeam(new PacketSyncApplications(dimension, id, town.getGovernment().getApplications()), player.world, town.getCharter().getTeamID());
            AdventureDataLoader.get(player.world).markDirty();
        }
    }
}
