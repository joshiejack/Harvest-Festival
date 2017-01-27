package joshie.harvest.festivals.tile;

import joshie.harvest.core.base.tile.TileFaceable;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.MCServerHelper;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.festivals.HFFestivals;
import joshie.harvest.festivals.cooking.CookingContestQuest;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.UUID;

public class TileStand extends TileFaceable {
    private UUID owner;
    private ItemStack stack;

    public boolean isEmpty() {
        return stack == null;
    }

    public boolean setContents(@Nullable EntityPlayer player, ItemStack stack) {
        CookingContestQuest quest = TownHelper.getClosestTownToBlockPos(worldObj, pos).getQuests().getAQuest(HFFestivals.COOKING_FESTIVAL.getQuest());
        if (quest == null || quest.isFull() || this.stack != null) return false;
        else {
            if (player != null) quest.addStand(EntityHelper.getPlayerUUID(player), pos);
            this.stack = stack.splitStack(1); //Remove one item
            MCServerHelper.markTileForUpdate(this);
        }

        return true;
    }

    public ItemStack removeContents() {
        CookingContestQuest quest = TownHelper.getClosestTownToBlockPos(worldObj, pos).getQuests().getAQuest(HFFestivals.COOKING_FESTIVAL.getQuest());
        if (stack == null) return null;
        else {
            if (quest != null) quest.removeStand(pos);
            ItemStack stack = this.stack.copy();
            this.stack = null;
            MCServerHelper.markTileForUpdate(this);
            return stack;
        }
    }

    public ItemStack getContents() {
        return stack;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        stack = nbt.hasKey("Stack") ? NBTHelper.readItemStack(nbt.getCompoundTag("Stack")) : null;
        owner = nbt.hasKey("Owner") ? UUID.fromString("Owner") : null;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (stack != null) nbt.setTag("Stack", NBTHelper.writeItemStack(stack, new NBTTagCompound()));
        if (owner != null) nbt.setString("Owner", owner.toString());
        return super.writeToNBT(nbt);
    }
}
