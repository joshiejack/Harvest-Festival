package uk.joshiejack.settlements.entity.ai.action;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Objects;

public abstract class Action implements INBTSerializable<NBTTagCompound> {
    private static final BiMap<String, Class<? extends Action>> ACTIONS = HashBiMap.create();
    protected EntityPlayer player;
    public ResourceLocation registryName;
    public boolean isQuest;

    private boolean memorable;

    public static void register(String type, Class<? extends Action> clazz) {
        ACTIONS.put(type, clazz);
    }

    public static Action createOfType(String type) {
        if (!ACTIONS.containsKey(type)) {
            Settlements.logger.error("Attempted to create an action of type '" + type + "' that does not exist. Typo?");
            return ActionError.INSTANCE;
        }

        try {
            return ACTIONS.get(type).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return ActionError.INSTANCE;
        }
    }

    public abstract boolean isPhysical();

    public boolean has(String... data) {
        return false;
    }

    public Action withPlayer(EntityPlayer player) { this.player = player; return this; }
    public Action withScript(ResourceLocation registryName, boolean isQuest) { this.registryName = registryName; this.isQuest = isQuest; return this; }
    public Action withData(Object... params) { return this; }

    public String getType() {
        return ACTIONS.inverse().get(this.getClass());
    }

    public void setMemorable() {
        this.memorable = true;
    }

    public boolean isMemorable() {
        return memorable;
    }

    /** @return EnumActionResult.SUCCESS to go to the next queued item, "succeeded in finishing"
     *          EnumActionResult.FAIL to cancel the rest of the queue,  "there was an error"
     *          EnumActionResult.PASS to continue executing,            "pass to the next step of this action"  */
    public abstract EnumActionResult execute(EntityNPC npc);

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return Objects.equals(getType(), action.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType());
    }
}
