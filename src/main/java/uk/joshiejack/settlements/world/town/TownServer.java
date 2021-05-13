package uk.joshiejack.settlements.world.town;

import uk.joshiejack.settlements.world.town.people.CensusServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TownServer extends Town<CensusServer> {
    public static final TownServer NULL = new TownServer(0, BlockPos.ORIGIN);
    public TownServer(int id, BlockPos centre) {
        super(id, centre);
    }

    @Override
    protected CensusServer createCensus() {
        return new CensusServer(this);
    }

    public void newDay(World world) {
        getLandRegistry().onNewDay(world);
        census.onNewDay(world);
    }

    @Override
    public NBTTagCompound getTagForSync() {
        return super.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        super.deserializeNBT(tag);
        census.deserializeNBT(tag.getCompoundTag("Census"));
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound data = super.serializeNBT();
        data.setTag("Census", census.serializeNBT());
        return data;
    }
}
