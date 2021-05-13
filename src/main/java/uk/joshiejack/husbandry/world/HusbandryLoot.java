package uk.joshiejack.husbandry.world;

import uk.joshiejack.husbandry.HusbandryConfig;
import uk.joshiejack.husbandry.item.HusbandryItems;
import uk.joshiejack.husbandry.item.ItemTreat;
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

import java.util.Locale;

import static uk.joshiejack.husbandry.Husbandry.MODID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
public class HusbandryLoot {
    @SubscribeEvent
    public static void onDungeonLootLoad(LootTableLoadEvent event) {
        if (HusbandryConfig.enableLootTreats) {
            if (event.getName().equals(new ResourceLocation("minecraft:chests/simple_dungeon"))) {
                LootPool pool = event.getTable().getPool("main");
                for (ItemTreat.Treat treat: ItemTreat.Treat.values()) {
                    if (treat != ItemTreat.Treat.GENERIC) {
                        pool.addEntry(getEntry("husbandry_treat_" + treat.name().toLowerCase(Locale.ENGLISH), treat, 2, 3));
                    } else pool.addEntry(getEntry("husbandry_treat_" + treat.name().toLowerCase(Locale.ENGLISH), treat, 3, 7));
                }
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private static LootEntryItem getEntry(String name, ItemTreat.Treat treat, int min, int max) {
        LootCondition[] conditions = new LootCondition[0];
        return new LootEntryItem(HusbandryItems.TREAT, 8, 0,
                new LootFunction[]{ new SetMetadata(conditions, new RandomValueRange(treat.ordinal())),
                new SetCount(conditions, new RandomValueRange(min, max))}, conditions, name);
    }
}
