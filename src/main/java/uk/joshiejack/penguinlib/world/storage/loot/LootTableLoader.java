package uk.joshiejack.penguinlib.world.storage.loot;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.custom.CustomLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.ReflectionHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
public class LootTableLoader {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onLootTableLoaded(LootTableLoadEvent event) {
        //If we have an override do it, otherwise let's merge
        if (CustomLoader.LOOT_OVERRIDES.containsKey(event.getName())) {
            event.setTable(event.getLootTableManager().getLootTableFromLocation(CustomLoader.LOOT_OVERRIDES.get(event.getName())));
        } else {
            List<LootTable> tables = CustomLoader.LOOT_MERGES.get(event.getName()).stream().map(r -> event.getLootTableManager().getLootTableFromLocation(r)).collect(Collectors.toList());
            if (tables.size() > 0) {
                tables.forEach(table -> merge(event.getTable(), table));
            }
        }
    }
    private static void merge(LootTable primary, LootTable secondary) {
        //Copy all the lootpools from main to secondary, MERGE them if the pools have the same name
        List<LootPool> pools1 = ReflectionHelper.getPrivateValue(LootTable.class, primary, "pools", "field_186466_c");
        List<LootPool> pools2 = ReflectionHelper.getPrivateValue(LootTable.class, secondary, "pools", "field_186466_c");

        for (LootPool pool2: pools2) {
            Optional<LootPool> optional = getEntryWithName(pool2.getName(), pools1);
            if (optional.isPresent()) merge(optional.get(), pool2);
            else pools1.add(pool2);
        }
    }

    private static void merge(@Nullable LootPool pool1, LootPool pool2) {
        //Entries in pool 2 =...
        List<LootEntry> entries2 = ReflectionHelper.getPrivateValue(LootPool.class, pool2, "lootEntries", "field_186453_a");
        if (pool1 != null) {
            entries2.forEach(pool1::addEntry);
        }
    }

    private static Optional<LootPool> getEntryWithName(String name, List<LootPool> pools) {
        return pools.stream().filter(p -> new ResourceLocation(p.getName()).getPath().equals(new ResourceLocation(name).getPath())).findFirst();
    }

}
