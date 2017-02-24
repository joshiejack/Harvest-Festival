package joshie.harvest.quests.data;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.Quest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class QuestDataClient extends QuestData {
    public void addAsCurrent(Quest quest) {
        current.add(quest);
    }
    
    //Removes the quest from the current and available lists
    @Override
    public void markCompleted(@Nonnull World world, @Nullable EntityPlayer player, Quest quest, boolean rewards) {
        Quest aQuest = getAQuest(quest);
        if (aQuest != null && rewards && player != null) {
            aQuest.onQuestCompleted(player); //Complete quest then add notes
            aQuest.getNotes().stream().forEach(note -> HFApi.player.getTrackingForPlayer(player).learnNote(note));
        }

        current.remove(quest);
        finished.add(quest);
    }

    @Override
    public void removeAsCurrent(@Nonnull World world, Quest quest) {
        current.remove(quest);
        finished.remove(quest);
    }
}
