package uk.joshiejack.harvestcore.tweaks;

import com.google.common.collect.Lists;
import net.minecraft.advancements.Advancement;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.penguinlib.events.RemoveAdvancementEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class AdvancementRemover {
    public static final List<String> BLACKLIST = Lists.newArrayList();

    @SubscribeEvent
    public static void onWorldLoad(RemoveAdvancementEvent event) {
        //event.getAdvancementMap().entrySet().removeIf(entry ->
                //entry.getKey().toString().startsWith("minecraft:recipes")
                        //&& !isInBlacklist(entry.getValue()));
    }

    private static boolean isInBlacklist(Advancement advancement) {
        //ResourceLocation[] recipes = ReflectionHelper.getPrivateValue(AdvancementRewards.class, advancement.getRewards(), "recipes");
        //for (ResourceLocation r: recipes) {
            //i//f (BLACKLIST.contains(r.getPath())) return true;
       // }

        return false;
    }
}
