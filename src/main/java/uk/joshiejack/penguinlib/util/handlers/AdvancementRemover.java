package uk.joshiejack.penguinlib.util.handlers;

import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.events.ReloadResourcesEvent;
import uk.joshiejack.penguinlib.events.RemoveAdvancementEvent;
import uk.joshiejack.penguinlib.util.helpers.generic.ReflectionHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
public class AdvancementRemover {
    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if (!event.getWorld().isRemote) {
            reload((WorldServer) event.getWorld());
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(ReloadResourcesEvent event) {
        reload((WorldServer)event.getWorld());
    }

    private static void reload(WorldServer world) {
        AdvancementManager manager = world.getAdvancementManager();
        AdvancementList list = ReflectionHelper.getPrivateValue(AdvancementManager.class, manager, "ADVANCEMENT_LIST", "field_192784_c");
        Map<ResourceLocation, Advancement> advancements = ReflectionHelper.getPrivateValue(AdvancementList.class, list, "advancements", "field_192092_b");
        MinecraftForge.EVENT_BUS.post(new RemoveAdvancementEvent(world, advancements));
    }
}