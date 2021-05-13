package uk.joshiejack.furniture.scripting;

import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.furniture.scripting.wrappers.TelevisionJS;
import uk.joshiejack.furniture.tile.TileTelevision;
import uk.joshiejack.penguinlib.events.CollectScriptingWrappers;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Furniture.MODID)
public class FurnitureScripting {
    @SubscribeEvent
    public static void onCollectWrappers(CollectScriptingWrappers event) {
        event.register(TelevisionJS.class, TileTelevision.class);
    }
}
