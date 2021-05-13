package uk.joshiejack.harvestcore.network.mine;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.harvestcore.world.mine.Mine;
import uk.joshiejack.harvestcore.world.mine.MineHelper;
import uk.joshiejack.harvestcore.world.mine.dimension.MineData;
import uk.joshiejack.harvestcore.world.storage.SavedData;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.SERVER)
public class PacketElevator extends PenguinPacket {
    private int floor;

    public PacketElevator() {}

    public PacketElevator(int floor) {
        this.floor = floor;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(floor);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        floor = buf.readInt();
    }


    @Override
    public void handlePacket(EntityPlayer player) {
        int id = MineHelper.getMineIDFromEntity(player);
        MineData data = SavedData.getMineData(player.world, player.world.provider.getDimension());
        if (data.hasReachedFloor(id, floor)) {
            MineHelper.teleportToFloor(player, Mine.BY_ID.get(player.world.provider.getDimension()), id, floor, false);
        }
    }
}
