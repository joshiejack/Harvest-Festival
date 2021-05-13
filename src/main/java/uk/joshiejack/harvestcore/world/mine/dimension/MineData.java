package uk.joshiejack.harvestcore.world.mine.dimension;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.harvestcore.world.storage.SavedData;

@SuppressWarnings("unused")
public class MineData implements INBTSerializable<NBTTagCompound> {
    private final Int2ObjectMap<Int2ObjectMap<Pair<BlockPos, EnumFacing>>> mineID_floor_pos = new Int2ObjectOpenHashMap<>();
    private final Int2ObjectMap<Int2ObjectMap<Pair<BlockPos, EnumFacing>>> mineID_portal_pos = new Int2ObjectOpenHashMap<>();
    private final Int2IntMap mineID_max_floors = new Int2IntOpenHashMap();

    public boolean hasReachedFloor(int id, int floor) {
        return floor <= mineID_max_floors.get(id);
    }

    public int getMaxFloorForID(int mineID) {
        return mineID_max_floors.get(mineID);
    }

    public void updateMaxFloor(World world, int mineID, int maxFloor) {
        if (mineID_max_floors.get(mineID) < maxFloor) {
            mineID_max_floors.put(mineID, maxFloor);
            SavedData.save(world);
        }
    }

    public Int2ObjectMap<Pair<BlockPos, EnumFacing>> getElevatorsForID(int id) {
        if (!mineID_floor_pos.containsKey(id)) {
            mineID_floor_pos.put(id, new Int2ObjectOpenHashMap<>());
        }

        return mineID_floor_pos.get(id);
    }

    public Int2ObjectMap<Pair<BlockPos, EnumFacing>> getPortalsForID(int id) {
        if (!mineID_portal_pos.containsKey(id)) {
            mineID_portal_pos.put(id, new Int2ObjectOpenHashMap<>());
        }

        return mineID_portal_pos.get(id);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        mineID_max_floors.forEach((id, max) -> {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("ID", id);
            tag.setInteger("Max", max);
            list.appendTag(tag);
        });

        nbt.setTag("MaxFloorReached", list);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("MaxFloorReached", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            int id = tag.getInteger("ID");
            int max = tag.getInteger("Max");
            mineID_max_floors.put(id, max);
        }
    }
}
