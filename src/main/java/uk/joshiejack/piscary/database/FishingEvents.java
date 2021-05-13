package uk.joshiejack.piscary.database;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import javax.annotation.Nonnull;

import static uk.joshiejack.piscary.Piscary.MODID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
public class FishingEvents {
    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        for (Row row: event.getData("hatchery")) {
            ItemStack stack = StackHelper.getStackFromString(row.get("item"));
            if (!stack.isEmpty()) {
                HatcheryRegistry.register(stack, row.get("days"));
            }
        }
    }

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (event.getName().toString().equals("minecraft:gameplay/fishing")) {
            LootPool pool = event.getTable().getPool("main");
            pool.addEntry(getEntry(MODID + "_fish", "fish", -1, 75));
            pool.addEntry(getEntry(MODID + "_junk", "junk", -2, 8));
            pool.addEntry(getEntry(MODID + "_treasure", "treasure", 10, 1));
        }
    }

    private static LootEntryTable getEntry(String unique, @Nonnull String name, int quality, int weight) {
        return new LootEntryTable(new ResourceLocation(MODID, name), weight, quality, new LootCondition[0], unique);
    }
}
