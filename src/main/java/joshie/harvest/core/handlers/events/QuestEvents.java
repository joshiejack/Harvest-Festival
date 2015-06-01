package joshie.harvest.core.handlers.events;

import static joshie.harvest.core.helpers.QuestHelper.getCurrentQuest;

import java.util.HashSet;

import joshie.harvest.api.quest.IQuest;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

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
                    quest.onRightClickBlock(event.entityPlayer, event.world, event.x, event.y, event.z, event.face);
                }
            }
        }
    }
}
