package uk.joshiejack.settlements;

import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Level;
import uk.joshiejack.settlements.client.animation.Animation;
import uk.joshiejack.settlements.entity.ai.action.Action;
import uk.joshiejack.settlements.npcs.status.StatusTracker;
import uk.joshiejack.settlements.quest.Quest;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.events.CollectRegistryEvent;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.events.NewDayEvent;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ResourceLoader;

import java.io.File;
import java.util.Set;

import static uk.joshiejack.settlements.Settlements.MODID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
public class AdventureDataLoader {
    @SubscribeEvent
    public static void onCollectForRegistration(CollectRegistryEvent event) {
        event.add(Action.class, (d, c, s, l) -> Action.register(l, c));
        event.add(Animation.class, ((d, c, s, l) -> Animation.register(l, c)));
    }

    @SubscribeEvent
    public static void preDatabase(DatabaseLoadedEvent.Pre event) {
        //Load in all the javascript files
        File directory = new File(PenguinLib.getDirectory(), "quests");
        if (!directory.exists()) {
            boolean made = directory.mkdir();
        }

        for (File file: FileUtils.listFiles(directory, new String[] { "js" }, true)) {
            new Quest(file, new ResourceLocation(MODID, file.getName().replace(".js", "")));
        }

        Set<ResourceLocation> ignores = ResourceLoader.get().getResourceListInDirectory("quests", ".ignore");
        ResourceLoader.get().getResourceListInDirectory("quests", "js", Sets.newHashSet("templates")).forEach(questLocation -> {
            ResourceLocation registryName = new ResourceLocation(questLocation.getNamespace(), questLocation.getPath().replace("/", "_"));
            Quest quest = new Quest(questLocation, registryName);
            Settlements.logger.log(Level.INFO, "Registered the Quest: " + quest.getRegistryName() + " with the quest script at " + questLocation);
        });
    }

    public static AdventureData get(World world) {
        AdventureData data = (AdventureData) world.loadData(AdventureData.class, AdventureData.DATA_NAME);
        if (data == null) {
            data = new AdventureData(AdventureData.DATA_NAME);
            world.setData(AdventureData.DATA_NAME, data);
            data.markDirty(); //Save the file
        }

        return data;
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        AdventureDataLoader.get(event.player.world).sync(event.player);
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void onNewDay(NewDayEvent event) {
        AdventureData data = AdventureDataLoader.get(event.getWorld());
        data.getStatusTrackers().forEach(StatusTracker::newDay);
        data.getScriptTrackers().forEach(q -> q.onNewDay(event.getWorld()));
        //Sync the daily quests to everyone when they change
        event.getWorld().getMinecraftServer().getPlayerList().getPlayers()
                .forEach(p -> AdventureDataLoader.get(event.getWorld()).syncDailies(p));
        data.getTowns(event.getWorld()).forEach(town -> town.newDay(event.getWorld()));
        data.markDirty();
    }
}
