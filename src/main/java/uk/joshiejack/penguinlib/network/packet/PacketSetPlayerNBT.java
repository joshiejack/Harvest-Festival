package uk.joshiejack.penguinlib.network.packet;

import joptsimple.internal.Strings;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public abstract class PacketSetPlayerNBT extends PenguinPacket {
    private final String tagName;

    public PacketSetPlayerNBT() { this.tagName = Strings.EMPTY; }
    public PacketSetPlayerNBT(String tagName) { this.tagName = tagName; }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (!player.getEntityData().hasKey(tagName)) {
            player.getEntityData().setTag(tagName, new NBTTagCompound());
        }

       setData(player.getEntityData().getCompoundTag(tagName));
    }

    public abstract void setData(NBTTagCompound tag);
}
