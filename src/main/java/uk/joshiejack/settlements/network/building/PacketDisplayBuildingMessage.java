package uk.joshiejack.settlements.network.building;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.settlements.building.BuildingMessage;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketDisplayBuildingMessage extends PenguinPacket {
    private BuildingMessage message;
    private BlockPos pos;
    private Building building;

    public PacketDisplayBuildingMessage() {}
    public PacketDisplayBuildingMessage(Building building, BlockPos pos, BuildingMessage message) {
        this.building = building;
        this.pos = pos;
        this.message = message;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(building == null);
        if (building != null) {
            ByteBufUtils.writeUTF8String(buf, building.getRegistryName().toString());
            buf.writeLong(pos.toLong());
            buf.writeByte(message.ordinal());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        if (!buf.readBoolean()) {
            building = Building.REGISTRY.get(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
            pos = BlockPos.fromLong(buf.readLong());
            message = BuildingMessage.values()[buf.readByte()];
        }
    }


    @Override
    public void handlePacket(EntityPlayer player) {
        message.display(building, pos);
    }
}
