package uk.joshiejack.harvestcore.network.fertilizer;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.harvestcore.client.renderer.fertilizer.FertilizerRender;
import uk.joshiejack.harvestcore.registry.Fertilizer;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketRemoveFertilizer extends PenguinPacket {
    private Fertilizer fertilizer;
    private BlockPos pos;

    public PacketRemoveFertilizer(){}
    public PacketRemoveFertilizer(Fertilizer fertilizer, BlockPos pos) {
        this.fertilizer = fertilizer;
        this.pos = pos;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, fertilizer.getRegistryName().toString());
        buf.writeLong(pos.toLong());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        fertilizer = Fertilizer.REGISTRY.get(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        FertilizerRender.removeBlock(fertilizer, pos);
        FertilizerRender.CACHE.invalidate(fertilizer);
    }
}
