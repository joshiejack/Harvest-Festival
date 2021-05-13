package uk.joshiejack.penguinlib.scripting.wrappers;

import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.data.holder.HolderOre;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.util.helpers.minecraft.InventoryHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemHandlerHelper;

@SuppressWarnings("unused")
public class PlayerJS extends EntityLivingJS<EntityPlayer> {
    public PlayerJS(EntityPlayer player) {
        super(player);
    }

    public TeamJS team() {
        return WrapperRegistry.wrap(PenguinTeams.getTeamForPlayer(penguinScriptingObject));
    }

    public void give(ItemStackJS stack) {
        ItemHandlerHelper.giveItemToPlayer(penguinScriptingObject, stack.penguinScriptingObject);
    }

    public boolean has(String ore, int amount) {
        return InventoryHelper.hasInInventory(penguinScriptingObject, Holder.getFromString(ore), amount);
    }

    public boolean isHolding(String ore, int amount) {
        return InventoryHelper.isHolding(penguinScriptingObject, new HolderOre(ore), amount);
    }

    public long spawnday() {
        NBTTagCompound tag = penguinScriptingObject.getEntityData().getCompoundTag(PenguinLib.MOD_ID);
        return tag.hasKey("Spawnday") ? tag.getLong("Spawnday") : -1;
    }

    public void setBirthday(long birthday) {
        penguinScriptingObject.getEntityData().getCompoundTag(PenguinLib.MOD_ID).setLong("Birthday", birthday);
    }

    public String getUUID() {
        return PlayerHelper.getUUIDForPlayer(penguinScriptingObject).toString();
    }

    public PlayerStatusJS status() {
        return WrapperRegistry.wrap(this);
    }

    public boolean isHungry() {
        return penguinScriptingObject.getFoodStats().needFood();
    }

    public void feed(int hunger, float saturation) {
        penguinScriptingObject.getFoodStats().addStats(hunger, saturation);
    }
}
