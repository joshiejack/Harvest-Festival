package joshie.harvest.town.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.town.data.TownData;
import joshie.harvest.town.data.TownDataClient;
import joshie.harvest.town.tracker.TownTrackerClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.LinkedList;
import java.util.UUID;

@Packet(Side.CLIENT)
public class PacketSyncBuilding extends PenguinPacket {
    private UUID town;
    private LinkedList<BuildingStage> building;

    @SuppressWarnings("unused")
    public PacketSyncBuilding(){}
    public PacketSyncBuilding(UUID town, LinkedList<BuildingStage> building) {
        this.town = town;
        this.building = building;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, town.toString());
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for (BuildingStage stage: building) {
            NBTTagCompound compound = new NBTTagCompound();
            stage.writeToNBT(compound);
            list.appendTag(compound);
        }

        tag.setTag("Building", list);
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        town = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        building = new LinkedList<>();
        NBTTagCompound tag = ByteBufUtils.readTag(buf);
        NBTTagList list = tag.getTagList("Building", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            building.add(BuildingStage.readFromNBT(list.getCompoundTagAt(i)));
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TownData townData = HFTrackers.<TownTrackerClient>getTownTracker(player.worldObj).getTownByID(town);
        if (townData != null) {
            ((TownDataClient)townData).setBuilding(building);
        }
    }
}
