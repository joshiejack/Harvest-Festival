package uk.joshiejack.penguinlib.network.packet;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncPlayerStatuses extends PacketSyncPlayerNBT {
    public PacketSyncPlayerStatuses() { super("PenguinStatuses"); }
    public PacketSyncPlayerStatuses(NBTTagCompound tag) {
        super("PenguinStatuses", tag);
    }
}
