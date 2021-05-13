package uk.joshiejack.settlements.network.town.people;

import com.google.common.collect.Sets;
import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.client.WorldMap;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Set;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncResidents extends PenguinPacket {
    private Set<ResourceLocation> invitable = Sets.newHashSet();
    private int townID;

    public PacketSyncResidents() {}
    public PacketSyncResidents(int townID, Set<ResourceLocation> invitable) {
        this.townID = townID;
        this.invitable = invitable;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(townID);
        buf.writeInt(invitable.size());
        invitable.forEach(npc -> ByteBufUtils.writeUTF8String(buf, npc.toString()));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        townID = buf.readInt();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            invitable.add(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        WorldMap.getTownByID(player.world.provider.getDimension(), townID).getCensus().setInvitableList(invitable);
    }
}
