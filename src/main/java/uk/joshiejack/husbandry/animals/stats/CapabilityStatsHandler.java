package uk.joshiejack.husbandry.animals.stats;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityStatsHandler {
    @SuppressWarnings("CanBeFinal")
    @CapabilityInject(AnimalStats.class)
    public static Capability<AnimalStats<?>> ANIMAL_STATS_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(AnimalStats.class, new DefaultAnimalStats<>(), () -> new AnimalStats<>(null, null));
    }

    private static class DefaultAnimalStats<T extends AnimalStats<?>> implements Capability.IStorage<T> {
        @Override
        public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side)  {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
            instance.deserializeNBT((NBTTagCompound)nbt);
        }
    }
}
