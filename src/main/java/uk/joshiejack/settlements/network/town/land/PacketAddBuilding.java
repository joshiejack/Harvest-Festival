package uk.joshiejack.settlements.network.town.land;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.settlements.client.WorldMap;
import uk.joshiejack.settlements.world.town.land.TownBuilding;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketAddBuilding extends PenguinPacket {
    private int dimension;
    private int id;
    private TownBuilding building;

    public PacketAddBuilding() {}
    public PacketAddBuilding(int dimension, int id, TownBuilding building) {
        this.dimension = dimension;
        this.id = id;
        this.building = building;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dimension);
        buf.writeInt(id);
        ByteBufUtils.writeUTF8String(buf, building.getBuilding().getRegistryName().toString());
        buf.writeLong(building.getPosition().toLong());
        buf.writeByte(building.getRotation().ordinal());
        buf.writeBoolean(building.isBuilt());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dimension = buf.readInt();
        id = buf.readInt();
        building = new TownBuilding(Building.REGISTRY.get(new ResourceLocation(ByteBufUtils.readUTF8String(buf))),
                                    BlockPos.fromLong(buf.readLong()),
                                    Rotation.values()[buf.readByte()]);
        if (buf.readBoolean()) building.setBuilt();
    }


    @Override
    public void handlePacket(EntityPlayer player) {
        WorldMap.getTownByID(dimension, id).getLandRegistry().addBuilding(player.world, building);
    }
}
