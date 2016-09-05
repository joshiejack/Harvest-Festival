package joshie.harvest.quests;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.util.HFEvents;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

import static joshie.harvest.api.quests.Quest.EventType.ENTITY_INTERACT;
import static joshie.harvest.api.quests.Quest.EventType.RIGHT_CLICK_BLOCK;

@HFEvents
public class QuestEvents {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Set<Quest> quests = HFApi.quests.getHandledQuests(event.getEntityPlayer(), ENTITY_INTERACT);
        for (Quest quest : quests) {
            quest.onEntityInteract(event.getEntityPlayer(), event.getItemStack(), event.getHand(), event.getTarget());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRightClickGround(PlayerInteractEvent.RightClickBlock event) {
        Set<Quest> quests = HFApi.quests.getHandledQuests(event.getEntityPlayer(), RIGHT_CLICK_BLOCK);
        for (Quest quest : quests) {
            quest.onRightClickBlock(event.getEntityPlayer(), event.getPos(), event.getFace());
        }
    }
}