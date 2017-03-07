package joshie.harvest.quests.town.festivals.harvest;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.quests.town.festivals.QuestHarvestFestival;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class HarvestSelection extends Selection<QuestHarvestFestival> {
    private ItemStack inserted;
    private boolean removed;

    public HarvestSelection() {
        super("Do you want to insert your held item?", "@Yes", "@No");
    }

    public boolean hasRemoved() {
        if (!removed) {
            removed = true;
            return false;
        }

        return removed;
    }

    public ItemStack getInserted() {
        return inserted;
    }

    public String getLocalizedScript(EntityPlayer player, NPC npc) {
        if (getInserted() == null) return "Welcome! We're about to start. We just need you to add something to the pot.";
        else return "Thanks for that";
    }

    @Override
    public Result onSelected(EntityPlayer player, NPCEntity entity, QuestHarvestFestival quest, int option) {
        //Option1 = Invite to Starry Night
        if (option == 1) {
            inserted = player.getHeldItemMainhand().copy();
            quest.execute(player, entity);
        } else return Result.DENY;

        quest.syncData(player); //Resync to client
        //Option2 = Chat
        return Result.ALLOW;
    }

    /////////////////////////////// Saving and Loading /////////////////////////////////
    public static HarvestSelection fromNBT(NBTTagCompound tag) {
        HarvestSelection data =  new HarvestSelection();
        if (tag.hasKey("Inserted")) data.inserted = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Inserted"));
        data.removed = tag.getBoolean("Removed");
        return data;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        if (inserted != null) tag.setTag("Inserted", inserted.writeToNBT(new NBTTagCompound()));
        tag.setBoolean("Removed", removed);
        return tag;
    }
}
