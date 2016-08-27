package joshie.harvest.quests;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.util.HFEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

import static joshie.harvest.api.quests.Quest.EventsHandled.ENTITYINTERACT;
import static joshie.harvest.api.quests.Quest.EventsHandled.RIGHTCLICK;

@HFEvents
public class QuestEvents {
    private boolean isFakePlayer(EntityPlayer player) {
        return player instanceof FakePlayer;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!isFakePlayer(event.getEntityPlayer())) {
            Set<Quest> quests = QuestHelper.getHandledQuests(event.getEntityPlayer(), ENTITYINTERACT);
            for (Quest quest : quests) {
                quest.onEntityInteract(event.getEntityPlayer(), event.getTarget());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRightClickGround(PlayerInteractEvent.RightClickBlock event) {
        if (!isFakePlayer(event.getEntityPlayer())) {
            Set<Quest> quests = QuestHelper.getHandledQuests(event.getEntityPlayer(), RIGHTCLICK);
            for (Quest quest : quests) {
                quest.onRightClickBlock(event.getEntityPlayer(), event.getPos(), event.getFace());
            }
        }
    }
}