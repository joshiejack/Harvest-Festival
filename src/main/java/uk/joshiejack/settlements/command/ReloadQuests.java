package uk.joshiejack.settlements.command;

import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.quest.Quest;
import uk.joshiejack.penguinlib.events.PenguinReloadEvent;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;

@Mod.EventBusSubscriber(modid = Settlements.MODID)
public class ReloadQuests {
    @SubscribeEvent
    public static void onReload(PenguinReloadEvent event) {
        //Temporary, Move to settlements mod instead
        Sets.newHashSet(Quest.REGISTRY.keySet()).forEach(key -> Quest.REGISTRY.put(key, Quest.REGISTRY.get(key).copy()));
        World world = event.getWorld();
        MinecraftServer server = world.getMinecraftServer();
        if (server != null) {
            AdventureDataLoader.get(world).getScriptTrackers().forEach(tracker -> tracker.reload(AdventureDataLoader.get(world).getDay()));
            for (EntityPlayer player : server.getPlayerList().getPlayers()) {
                AdventureDataLoader.get(world).clearCache(PlayerHelper.getUUIDForPlayer(player));
            }
        }
    }
}
