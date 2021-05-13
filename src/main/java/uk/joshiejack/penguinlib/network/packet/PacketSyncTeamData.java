package uk.joshiejack.penguinlib.network.packet;

import uk.joshiejack.penguinlib.client.PenguinTeamsClient;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncTeamData extends PacketSyncNBTTagCompound {
    public PacketSyncTeamData() {}
    public PacketSyncTeamData(NBTTagCompound data) {
        super(data);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        PenguinTeamsClient.setInstance(tag);
    }
}
