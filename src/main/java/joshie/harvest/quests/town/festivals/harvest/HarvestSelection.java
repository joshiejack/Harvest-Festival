package joshie.harvest.quests.town.festivals.harvest;

import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.quests.town.festivals.QuestHarvestFestival;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class HarvestSelection extends Selection<QuestHarvestFestival> {
    private boolean started;

    public HarvestSelection() {
        super("harvestfestival.quest.festival.harvest.ready", "harvestfestival.quest.festival.harvest.yes", "harvestfestival.quest.festival.harvest.no");
    }

    public void setStarted() {
        this.started = true;
    }

    public boolean hasStarted() {
        return started;
    }

    @Override
    public Result onSelected(EntityPlayer player, NPCEntity entity, QuestHarvestFestival quest, int option) {
        if (option == 1) {
            quest.execute(player, entity);
        }

        quest.syncData(player); //Resync to client
        //Option2 = Chat
        return Result.DENY;
    }

    /////////////////////////////// Saving and Loading /////////////////////////////////
    public static HarvestSelection fromNBT(NBTTagCompound tag) {
        HarvestSelection data =  new HarvestSelection();
        data.started = tag.getBoolean("Started");
        return data;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("Started", started);
        return tag;
    }
}
