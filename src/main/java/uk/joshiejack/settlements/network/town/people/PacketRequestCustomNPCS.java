package uk.joshiejack.settlements.network.town.people;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.SERVER)
public class PacketRequestCustomNPCS extends PenguinPacket {
    private int dimension;

    public PacketRequestCustomNPCS() {}
    public PacketRequestCustomNPCS(int dimension) {
        this.dimension = dimension;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dimension);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = buf.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        World world = DimensionManager.getWorld(dimension);
        if (world != null) {
            for (TownServer town : AdventureDataLoader.get(player.world).getTowns(world)) {
                PenguinNetwork.sendToClient(new PacketSyncCustomNPCs(dimension, town.getID(), town.getCensus().getCustomNPCs()), player);
            }
        }
    }
}
