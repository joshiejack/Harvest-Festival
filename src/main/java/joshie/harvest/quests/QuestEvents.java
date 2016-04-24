package joshie.harvest.quests;

import joshie.harvest.api.quest.IQuest;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;

import static joshie.harvest.core.helpers.QuestHelper.getCurrentQuest;

public class QuestEvents {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityInteract(EntityInteractEvent event) {
        HashSet<IQuest> quests = getCurrentQuest(event.entityPlayer);
        for (IQuest quest : quests) {
            if (quest != null) {
                quest.onEntityInteract(event.entityPlayer, event.target);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRightClickGround(PlayerInteractEvent event) {
        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            HashSet<IQuest> quests = getCurrentQuest(event.entityPlayer);
            for (IQuest quest : quests) {
                if (quest != null) {
                    quest.onRightClickBlock(event.entityPlayer, event.pos, event.face);
                }
            }
        }
    }
}
