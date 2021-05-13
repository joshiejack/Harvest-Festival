package uk.joshiejack.harvestcore.database;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.water.WaterHandler;
import uk.joshiejack.harvestcore.world.storage.loot.conditions.ConditionQuality;
import uk.joshiejack.harvestcore.world.storage.loot.conditions.ConditionWaterHandler;
import uk.joshiejack.harvestcore.world.storage.loot.functions.ModifyQuality;
import uk.joshiejack.penguinlib.scripting.DataScripting;
import uk.joshiejack.penguinlib.events.CollectRegistryEvent;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.generic.ReflectionHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;


@SuppressWarnings("unused")
//TODO: Add support for adding conditions to fishies
//Remove the piscary fish table and add tables with conditions of "season and watertype"
@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class SeasonalFishing { //TableName,EntryName,CatchHandler
    @SubscribeEvent
    public static void onCollection(CollectRegistryEvent event) {
        event.add(WaterHandler.Builder.class, (d, c, s, l) -> WaterHandler.BUILDERS.put(l, c.newInstance()));
    }

    @SubscribeEvent
    public static void preDatabase(DatabaseLoadedEvent.Pre event) {
        LootConditionManager.registerCondition(new ConditionQuality.Serializer());
        LootConditionManager.registerCondition(new ConditionWaterHandler.Serializer());
        LootFunctionManager.registerFunction(new ModifyQuality.Serializer());
    }

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("water_handlers").rows().forEach(row -> WaterHandler.BUILDERS.get(row.get("type").toString()).build(row.name(), row.get("data")));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLootTableLoaded(LootTableLoadEvent event) {
        //We take the existing fish loot table and then we fuck with it!
        if (event.getName().toString().equals("minecraft:gameplay/fishing")) {
            //Add quality to all of the fish
            List<LootPool> pools = ReflectionHelper.getPrivateValue(LootTable.class, event.getTable(), "pools", "field_186466_c");
            pools.forEach(pool -> {
                List<LootEntry> entries = ReflectionHelper.getPrivateValue(LootPool.class, pool, "lootEntries", "field_186453_a");
                entries.stream().filter(e -> e instanceof LootEntryItem && isFish((LootEntryItem) e))
                        .forEach(item -> addQualityFunctionToFish((LootEntryItem) item));
            });
        }
    }

    private static boolean isFish(LootEntryItem entry) {
        return DataScripting.isInList("fishing", new ItemStack(entry.item, 1, OreDictionary.WILDCARD_VALUE));
    }

    private static void addQualityFunctionToFish(LootEntryItem entry) {
        LootFunction[] functions = entry.functions;
        LootFunction[] newFunctions = new LootFunction[functions.length + 1];
        System.arraycopy(functions, 0, newFunctions, 0, functions.length);
        newFunctions[functions.length] = new ModifyQuality(new LootCondition[0]);
        ReflectionHelper.setPrivateFinalValue(LootEntryItem.class, entry, newFunctions, "functions", "field_186369_b");
    }
}
