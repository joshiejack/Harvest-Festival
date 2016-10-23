package joshie.harvest.animals.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PacketHandler;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet
public class PacketSyncAnimal extends AbstractSyncAnimal {
    protected NBTTagCompound tag;

    public PacketSyncAnimal() {}
    public PacketSyncAnimal(int id) {
        super(id);
    }

    public PacketSyncAnimal(int id, AnimalStats stats) {
        super(id);
        this.tag = (NBTTagCompound) stats.serializeNBT();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(tag != null);
        if (tag != null) ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        if (buf.readBoolean()) {
            this.tag = ByteBufUtils.readTag(buf);
        }
    }

    @Override
    @SuppressWarnings("unchecked, ConstantConditions")
    public void handlePacket(EntityPlayer player) {
        EntityAnimal animal = (EntityAnimal) MCClientHelper.getWorld().getEntityByID(id);
        if (animal != null) {
            AnimalStats stats = EntityHelper.getStats(animal);
            if (stats != null) {
                if (player.worldObj.isRemote) stats.setEntity(animal).deserializeNBT(tag);
                else PacketHandler.sendToClient(new PacketSyncAnimal(id, stats), player);
            }
        }
    }

    private EntityAnimal getEntityAsAnimal() {
        return (EntityAnimal) MCClientHelper.getWorld().getEntityByID(id);
    }
}