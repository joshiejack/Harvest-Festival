package uk.joshiejack.settlements.npcs.gifts;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.AdventureConfig;
import uk.joshiejack.settlements.npcs.NPC;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;

import java.util.Collection;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Settlements.MODID)
public class GiftRegistry {
    public static final Multimap<NPC, Pair<Holder, GiftQuality>> ITEM_OVERRIDES = HashMultimap.create();
    public static final Map<NPC, Map<GiftCategory, GiftQuality>> CATEGORY_OVERRIDES = Maps.newHashMap();
    public static HolderRegistry<GiftCategory> CATEGORY_REGISTRY;

    public static GiftQuality getQualityForNPC(ItemStack stack, Collection<Pair<Holder, GiftQuality>> itemOverrides, Map<GiftCategory, GiftQuality> categoryOverrides) {
        for (Pair<Holder, GiftQuality> pair : itemOverrides) {
            if (pair.getLeft().matches(stack)) {
                return pair.getRight();
            }
        }

        GiftCategory clazz = GiftRegistry.CATEGORY_REGISTRY.getValue(stack);
        return categoryOverrides.getOrDefault(clazz, clazz.quality());
    }

    public static GiftQuality getQualityForNPC(NPC npc, ItemStack stack) {
        return getQualityForNPC(stack, GiftRegistry.ITEM_OVERRIDES.get(npc), GiftRegistry.CATEGORY_OVERRIDES.get(npc));
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) { //LOW PRIORITY //TODO: Move to Adventure NPCs
        event.table("gift_quality").rows().forEach(row -> new GiftQuality(row.get("name"), row.get("value")));
        event.table("gift_categories").rows().forEach(category -> new GiftCategory(category.get("name"), GiftQuality.get(category.get("quality"))));
        CATEGORY_REGISTRY = new HolderRegistry<>(GiftCategory.get("none")); //Default to no value, should not be removed from the list
        GiftCategory.REGISTRY.values().forEach(theCategory -> event.table(theCategory.name() + "_gift_mappings").rows()
                .forEach(mapping -> {
                    Holder holder = mapping.holder();
                    if (!holder.isEmpty()) {
                        CATEGORY_REGISTRY.register(holder, theCategory);
                        //TODO: CONFIG Adventure.logger.log(Level.INFO, "Registered " + holder + " as the gift category " + theCategory.name());
                    }
        }));

        NPC.all().forEach(npc -> {
            CATEGORY_OVERRIDES.put(npc, Maps.newHashMap()); // Insert the map for this npc on the overrides
            event.table(npc.getRegistryName().getPath() + "_gift_preferences").rows().forEach(row -> {
                GiftQuality quality = GiftQuality.get(row.get("quality"));
                if (quality != null) {
                    //We're going through all the data for this npc now
                    String item = row.get("item");
                    if (item.startsWith("cat#")) {
                        CATEGORY_OVERRIDES.get(npc).put(GiftCategory.get(item.substring(4)), quality);
                        //TODO: CONFIG Adventure.logger.log(Level.INFO, "Registered gift preference for " + npc.getLocalizedName() + ": "
                               // + GiftCategory.get(item.substring(4)).name() + " as " + quality.name());
                    } else {
                        Holder holder = Holder.getFromString(item);
                        ITEM_OVERRIDES.get(npc).add(Pair.of(holder, quality));
                        Settlements.logger.log(Level.INFO, "Registered gift preference for " + npc.getLocalizedName() + ": "
                                + holder + " as : " + quality.name());
                    }
                } else {
                    Settlements.logger.log(Level.INFO, "Failed to register gift preference for " + npc.getLocalizedName() + ": with the item as"
                            + row.get("item") + " because " + quality.name() + " is not a valid quality");
                }
            });
        });
    }

    public static boolean isValidMod(String modid) {
        for (String s : AdventureConfig.enableGiftLoggingForModIDs.split(",")) {
            if (s.equals(modid)) return true;
        }

        return false;
    }
}
