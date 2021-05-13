package uk.joshiejack.settlements.quest;

import uk.joshiejack.settlements.AdventureData;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.event.NPCEvent;
import uk.joshiejack.settlements.quest.data.QuestData;
import uk.joshiejack.settlements.quest.data.QuestTracker;
import uk.joshiejack.settlements.util.QuestHelper;
import uk.joshiejack.penguinlib.scripting.event.ScriptingTriggerFired;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

import static uk.joshiejack.settlements.Settlements.MODID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
public class QuestingEvents {


    @SubscribeEvent
    public static void onPlayedLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        int day = TimeHelper.getElapsedDays(event.player.world);
        AdventureData data = AdventureDataLoader.get(event.player.world);
        data.setDay(day);
        data.getTrackers(event.player)
                .forEach(tracker -> tracker.setupOrRefresh(day));
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.world.provider.getDimension() == 0 && event.phase == TickEvent.Phase.END && event.world.getWorldTime() %TimeHelper.TICKS_PER_DAY == 1) {
            int day = TimeHelper.getElapsedDays(event.world);
            AdventureDataLoader.get(event.world).setDay(day); //Set the day for refresh on load purposes
        }
    }

    @SubscribeEvent
    public static void onNPCRightClicked(NPCEvent.NPCRightClickedEvent event) {
        MinecraftForge.EVENT_BUS.post(new ScriptingTriggerFired("onRightClickedNPC", event.getEntityPlayer(), event.getNPCEntity(), event.getHand()));
    }

    @SubscribeEvent
    public static void onNPCFinishedTalking(NPCEvent.NPCFinishedSpeakingEvent event) {
        runOnStorage("onNPCFinishedTalking", event.getScript(), event.getEntityPlayer(), event.getNPCEntity());
    }

    @SubscribeEvent
    public static void onTriggerFired(ScriptingTriggerFired event) {
        //Only fire these when we have a player assigned
        EntityPlayer player = event.getPlayer();
        if (player != null) {
            fireTrigger(event.getMethod(), player);
            QuestHelper.getActive(player, event.getMethod())
                    .forEach(script -> script.callFunction(event.getMethod(), event.getObjects()));
            AdventureDataLoader.get(player.world).markDirty();
        }
    }

    public static void runOnStorage(String function, Quest script, EntityPlayer player, Object... args) {
        fireTrigger(function, player);
        QuestData data = QuestHelper.getData(player, script);

        if (data != null) {
            Object[] args2 = new Object[args.length + 1];
            System.arraycopy(args, 0, args2, 1, args.length);
            args2[0] = player; //Set the first param to the player
            data.getInterpreter().callFunction(function, args2);
        }

        AdventureDataLoader.get(player.world).markDirty();
    }

    private static void fireTrigger(String method, EntityPlayer player) {
        //Get All quests for Player
        List<QuestTracker> trackers = AdventureDataLoader.get(player.world).getTrackers(player);
        trackers.forEach((t) -> {
            //We have all the trackers, now we need to call the fire method
            t.fire(method, player);
        });
    }
}
