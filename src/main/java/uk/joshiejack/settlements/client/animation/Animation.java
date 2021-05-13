package uk.joshiejack.settlements.client.animation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import uk.joshiejack.settlements.entity.EntityNPC;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class Animation implements INBTSerializable<NBTTagCompound> {
    private static final BiMap<String, Class<? extends Animation>> ANIMATIONS = HashBiMap.create();
    public static void register(String type, Class<? extends Animation> clazz) {
        ANIMATIONS.put(type, clazz);
    }

    public abstract void play(EntityNPC entityNPC);

    public static Animation create(String animation) throws IllegalAccessException, InstantiationException {
        return ANIMATIONS.get(animation).newInstance();
    }

    public String getID() {
        return ANIMATIONS.inverse().get(this.getClass());
    }

    public Animation withData(Object... args) {
        return this;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {}

    public boolean renderLiving(EntityNPC npc, double x, double y, double z) {
        return false;
    }

    public boolean applyRotation(EntityNPC npc) {
        return false;
    }
}
