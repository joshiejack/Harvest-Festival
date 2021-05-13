package uk.joshiejack.horticulture.world;

import uk.joshiejack.horticulture.block.BlockSapling;
import uk.joshiejack.horticulture.block.HorticultureBlocks;
import uk.joshiejack.horticulture.HorticultureConfig;
import uk.joshiejack.horticulture.item.HorticultureItems;
import uk.joshiejack.horticulture.item.ItemCrop;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static uk.joshiejack.horticulture.Horticulture.MODID;

@SuppressWarnings("ConstantConditions, unused")
@Mod.EventBusSubscriber(modid = MODID)
public class DungeonLoot {
    @SubscribeEvent
    public static void onDungeonLootLoad(LootTableLoadEvent event) {
        if (HorticultureConfig.enableDungeonChests) {
            if (event.getName().equals(new ResourceLocation("minecraft:chests/simple_dungeon")) ||
                    event.getName().equals(new ResourceLocation("minecraft:chests/village_blacksmith"))) {
                LootPool pool = event.getTable().getPool("main");
                pool.addEntry(getEntry("horticulture_strawberry", ItemCrop.Crops.STRAWBERRY, 2, 3));
                pool.addEntry(getEntry("horticulture_corn", ItemCrop.Crops.CORN, 1, 2));
                pool.addEntry(getEntry("horticulture_sweet_potato", ItemCrop.Crops.SWEET_POTATO, 2, 4));
                pool.addEntry(getEntry("horticulture_orange", BlockSapling.Sapling.ORANGE, 2));
                pool.addEntry(getEntry("horticulture_peach", BlockSapling.Sapling.PEACH, 3));
            }
        }
    }

    private static LootEntryItem getEntry(String name, BlockSapling.Sapling sapling, int max) {
        LootCondition[] conditions = new LootCondition[0];
        return new LootEntryItem(Item.getItemFromBlock(HorticultureBlocks.SAPLING), 6, 0,
                new LootFunction[]{ new SetMetadata(conditions, new RandomValueRange(sapling.ordinal())),
                        new SetCount(conditions, new RandomValueRange(1, max))}, conditions, name);
    }

    private static LootEntryItem getEntry(String name, ItemCrop.Crops crops, int min, int max) {
        LootCondition[] conditions = new LootCondition[0];
        return new LootEntryItem(HorticultureItems.SEEDS, 8, 0,
                new LootFunction[]{ new SetMetadata(conditions, new RandomValueRange(crops.ordinal())),
                new SetCount(conditions, new RandomValueRange(min, max))}, conditions, name);
    }
}
