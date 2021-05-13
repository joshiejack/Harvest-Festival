package uk.joshiejack.settlements.network.town.people;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.client.town.CustomNPC;
import uk.joshiejack.settlements.client.town.TownClient;
import uk.joshiejack.settlements.data.database.NPCLoader;
import uk.joshiejack.settlements.network.town.PacketAbstractTownSync;
import uk.joshiejack.settlements.npcs.NPC;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncCustomNPCs extends PacketAbstractTownSync {
    private Collection<Pair<ResourceLocation, NBTTagCompound>> customDataServer;
    private Collection<CustomNPC> customDataClient;

    public PacketSyncCustomNPCs() {}
    public PacketSyncCustomNPCs(int dim, int townID, Collection<Pair<ResourceLocation, NBTTagCompound>> custom) {
        super(dim, townID);
        this.customDataServer = custom;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(customDataServer.size());
        for (Pair<ResourceLocation, NBTTagCompound> pair: customDataServer) {
            NBTTagCompound data = pair.getRight();
            ByteBufUtils.writeUTF8String(buf, pair.getLeft().toString());
            buf.writeBoolean(data.hasKey("UniqueID"));
            buf.writeBoolean(data.hasKey("Class"));
            buf.writeBoolean(data.hasKey("Name"));
            buf.writeBoolean(data.hasKey("PlayerSkin"));
            buf.writeBoolean(data.hasKey("ResourceSkin"));
            if (data.hasKey("UniqueID")) ByteBufUtils.writeUTF8String(buf, data.getString("UniqueID"));
            if (data.hasKey("Class")) ByteBufUtils.writeUTF8String(buf, data.getString("Class"));
            if (data.hasKey("Name")) ByteBufUtils.writeUTF8String(buf, data.getString("Name"));
            if (data.hasKey("PlayerSkin")) ByteBufUtils.writeUTF8String(buf, data.getString("PlayerSkin"));
            if (data.hasKey("ResourceSkin")) ByteBufUtils.writeUTF8String(buf, data.getString("ResourceSkin"));
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        customDataClient = Lists.newArrayList();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            NPC base = NPC.getNPCFromRegistry(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
            boolean hasUniqueID = buf.readBoolean();
            boolean hasClass = buf.readBoolean();
            boolean hasName = buf.readBoolean();
            boolean hasPlayerSkin = buf.readBoolean();
            boolean hasResourceSkin = buf.readBoolean();
            CustomNPC npc = new CustomNPC(base,
                    hasUniqueID ? new ResourceLocation(ByteBufUtils.readUTF8String(buf)) : null,
                    hasClass ? NPCLoader.NPCClass.REGISTRY.get(ByteBufUtils.readUTF8String(buf)) : null,
                    hasName ? ByteBufUtils.readUTF8String(buf) : null,
                    hasPlayerSkin ? ByteBufUtils.readUTF8String(buf) : null,
                    hasResourceSkin ? ByteBufUtils.readUTF8String(buf) : null);
            customDataClient.add(npc);
        }
    }

    @Override
    protected void handlePacket(EntityPlayer player, Town<?> town) {
        ((TownClient)town).getCensus().setCustomNPCs(customDataClient);
    }
}
