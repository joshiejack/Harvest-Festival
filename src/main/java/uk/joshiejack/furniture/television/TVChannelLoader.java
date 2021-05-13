package uk.joshiejack.furniture.television;

import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Furniture.MODID)
public class TVChannelLoader {
    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("tv_channels").rows().forEach(row -> {
            TVChannel channel = TVChannel.create(new ResourceLocation(row.get("id")));
            if (!row.isEmpty("script")) channel.setScript(row.getScript());
            if (!row.isEmpty("screenshot")) channel.setScreenshot(new ResourceLocation(row.get("screenshot")), row.get("screenshotx"), row.get("screenshoty"));
            if (!row.isEmpty("hidden")) {
                if (!row.isTrue("hidden")) channel.setSelectable();
            }
        });

        event.table("tv_programs").rows().forEach(row -> TVChannel.create(new ResourceLocation(row.get("id"))));
    }
}
