package joshie.harvest.quests;

import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.util.HFEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;

import static joshie.harvest.quests.QuestHelper.getCurrentQuest;

@HFEvents
public class QuestEvents {
    private boolean isFakePlayer(EntityPlayer player) {
        return player instanceof FakePlayer;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!isFakePlayer(event.getEntityPlayer())) {
            HashSet<Quest> quests = getCurrentQuest(event.getEntityPlayer());
            for (Quest quest : quests) {
                if (quest != null) {
                    quest.onEntityInteract(event.getEntityPlayer(), event.getTarget());
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRightClickGround(PlayerInteractEvent.RightClickBlock event) {
        if (!isFakePlayer(event.getEntityPlayer())) {
            HashSet<Quest> quests = getCurrentQuest(event.getEntityPlayer());
            for (Quest quest : quests) {
                if (quest != null) {
                    quest.onRightClickBlock(event.getEntityPlayer(), event.getPos(), event.getFace());
                }
            }
        }
    }
}