package uk.joshiejack.settlements.world.town;

import uk.joshiejack.settlements.world.town.land.LandRegistry;
import uk.joshiejack.settlements.world.town.people.AbstractCensus;
import uk.joshiejack.settlements.world.town.people.Government;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;
import uk.joshiejack.harvestcore.HCConfig;

import java.util.Objects;

public abstract class Town<C extends AbstractCensus> implements INBTSerializable<NBTTagCompound> {
    private final TownCharter charter = new TownCharter();
    private final Government government = new Government();
    private final LandRegistry landRegistry = new LandRegistry(this);
    protected final C census;
    private final BlockPos centre;
    private final int id;

    public Town(int id, BlockPos centre) {
        this.id = id;
        this.centre = centre;
        this.census = createCensus();
    }

    @SuppressWarnings("unchecked")
    protected abstract C createCensus();

    public TownCharter getCharter() {
        return charter;
    }

    public LandRegistry getLandRegistry() {
         return landRegistry;
    }

    public Government getGovernment() {
        return government;
    }

    public C getCensus() {
        return census;
    }

    public int getID() {
        return id;
    }

    public BlockPos getCentre() {
        return centre;
    }

    public NBTTagCompound getTagForSync() {
        return serializeNBT();
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("Charter", charter.serializeNBT());
        tag.setTag("Government", government.serializeNBT());
        tag.setTag("LandRegistry", landRegistry.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        charter.deserializeNBT(tag.getCompoundTag("Charter"));
        government.deserializeNBT(tag.getCompoundTag("Government"));
        landRegistry.deserializeNBT(tag.getCompoundTag("LandRegistry"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Town<?> town = (Town<?>) o;
        return id == town.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getRadius() {
        return HCConfig.maxWildernessDistance;
    }
}
