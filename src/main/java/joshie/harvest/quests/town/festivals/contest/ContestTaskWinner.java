package joshie.harvest.quests.town.festivals.contest;

import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.npc.task.TaskElement;
import joshie.harvest.quests.town.festivals.Place;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ContestTaskWinner extends TaskElement {
    private Festival festival;

    @SuppressWarnings("WeakerAccess")
    public ContestTaskWinner(Festival festival) {
        this.festival = festival;
    }

    public void execute(EntityAgeable npc) {
        super.execute(npc);
        QuestContest quest = TownHelper.getClosestTownToEntity(npc, false).getQuests().getAQuest(festival.getQuest());
        if (quest != null) {
            ContestEntries entries = quest.getEntries();
            for (Place place: Place.VALUES) {
                quest.reward(npc.worldObj, place);
            }

            entries.complete(npc.worldObj);
            TownHelper.getClosestTownToEntity(npc, false).getQuests().markCompleted(npc.worldObj, null, quest, false);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        festival = Festival.REGISTRY.get(new ResourceLocation(tag.getString("Festival")));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("Festival", festival.getResource().toString());
        return tag;
    }
}
