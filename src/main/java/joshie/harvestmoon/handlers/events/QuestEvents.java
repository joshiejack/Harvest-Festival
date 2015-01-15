package joshie.harvestmoon.handlers.events;

import static joshie.harvestmoon.helpers.QuestHelper.*;

import java.util.HashSet;

import joshie.harvestmoon.helpers.SizeableHelper;
import joshie.harvestmoon.lib.SizeableMeta;
import joshie.harvestmoon.lib.SizeableMeta.Size;
import joshie.harvestmoon.quests.Quest;
import joshie.lib.helpers.ItemHelper;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import static joshie.harvestmoon.HarvestMoon.handler;

public class QuestEvents {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onEntityInteract(EntityInteractEvent event) {
        HashSet<Quest> quests = getCurrentQuest(event.entityPlayer);
        for (Quest quest : quests) {
            if (quest != null) {
                quest.onEntityInteract(event.entityPlayer, event.target);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRightClickGround(PlayerInteractEvent event) {
        HashSet<Quest> quests = getCurrentQuest(event.entityPlayer);
        for (Quest quest : quests) {
            if (quest != null) {
                if (event.action == Action.RIGHT_CLICK_BLOCK) {
                    quest.onRightClickBlock(event.entityPlayer, event.world, event.x, event.y, event.z, event.face);
                }
            }
        }
    }
}
