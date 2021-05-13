package uk.joshiejack.settlements.network.building;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.building.BuildingPlacement;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketClearPreview extends PenguinPacket {
    public PacketClearPreview() {}

    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public void fromBytes(ByteBuf buf) { }


    @Override
    public void handlePacket(EntityPlayer player) {
        BuildingPlacement.clear();
    }
}
