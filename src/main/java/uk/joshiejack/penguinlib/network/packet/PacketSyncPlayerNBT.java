package uk.joshiejack.penguinlib.network.packet;

import joptsimple.internal.Strings;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public abstract class PacketSyncPlayerNBT extends PacketSyncNBTTagCompound {
    private final String tagName;

    public PacketSyncPlayerNBT(){ this.tagName = Strings.EMPTY; } //Ignored
    public PacketSyncPlayerNBT(String tagName) { this.tagName = tagName; }
    public PacketSyncPlayerNBT(String tagName, NBTTagCompound tag) {
        super(tag);
        this.tagName = tagName;
    }

    @Override
    public void handlePacket(EntityPlayer player) {
       player.getEntityData().setTag(tagName, tag);
    }
}
