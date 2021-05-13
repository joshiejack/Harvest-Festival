package uk.joshiejack.settlements.network.building;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.settlements.building.BuildingPlacement;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncPreview extends PenguinPacket {
    private Building building;
    private BlockPos pos;
    private Rotation rotation;
    private boolean demolish;

    public PacketSyncPreview() {}
    public PacketSyncPreview(Building building, BlockPos pos, Rotation rotation, boolean demolish) {
        this.building = building;
        this.pos = pos;
        this.rotation = rotation;
        this.demolish = demolish;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, building.getRegistryName().toString());
        buf.writeLong(pos.toLong());
        buf.writeByte(rotation.ordinal());
        buf.writeBoolean(demolish);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        building = Building.REGISTRY.get(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        pos = BlockPos.fromLong(buf.readLong());
        rotation = Rotation.values()[buf.readByte()];
        demolish = buf.readBoolean();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        BuildingPlacement.set(building, pos, rotation, demolish);
    }
}
