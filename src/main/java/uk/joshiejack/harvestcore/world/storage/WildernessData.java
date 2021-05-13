package uk.joshiejack.harvestcore.world.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import uk.joshiejack.harvestcore.event.GatherTownCentres;
import uk.joshiejack.harvestcore.world.gen.WildernessGenerator;

import java.util.List;
import java.util.Set;

public class WildernessData implements INBTSerializable<NBTTagCompound> {
    private static final WildernessGenerator generator = new WildernessGenerator();
    private final Int2ObjectMap<Set<WildernessPos>> positions = new Int2ObjectOpenHashMap<>();

    public void onNewDay(World world) {
        generator.onNewDay(world, this);
    }

    public List<BlockPos> getPotentialSpawnLocations(World world) {
        List<BlockPos> spawns = Lists.newArrayList();
        MinecraftForge.EVENT_BUS.post(new GatherTownCentres(world, spawns));
        return spawns;
    }

    public Set<WildernessPos> getLocations(World world) {
        Set<WildernessPos> set = positions.get(world.provider.getDimension());
        if (set == null) {
            set = Sets.newHashSet();
            positions.put(world.provider.getDimension(), set);
        }

        return set;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        positions.forEach((i, p) -> {
            NBTTagCompound tag = new NBTTagCompound();
            NBTTagList list2 = new NBTTagList();
            p.forEach(w -> {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setLong("Pos", w.toLong());
                nbt.setTag("State", w.serializeNBT());
                list2.appendTag(nbt);
            });

            tag.setInteger("ID", i);
            tag.setTag("List", list2);
            list.appendTag(tag);
        });

        compound.setTag("Data", list);
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        NBTTagList list = compound.getTagList("Data", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            int dim = tag.getInteger("ID");
            Set<WildernessPos> data = Sets.newHashSet();
            positions.put(dim, data);
            NBTTagList lists2 = tag.getTagList("List", 10);
            for (int j = 0; j < lists2.tagCount(); j++) {
                NBTTagCompound nbt = lists2.getCompoundTagAt(j);
                WildernessPos pos = new WildernessPos(null, BlockPos.fromLong(nbt.getLong("Pos")));
                pos.deserializeNBT((NBTTagString) nbt.getTag("State"));
                data.add(pos);
            }
        }
    }
}
