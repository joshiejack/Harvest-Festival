package uk.joshiejack.piscary.loot;

import uk.joshiejack.piscary.loot.condition.ConditionBiomeType;
import uk.joshiejack.piscary.loot.condition.ConditionTemp;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;

import static uk.joshiejack.piscary.Piscary.MODID;

public class PiscaryLootTables {
    public static final ResourceLocation FISH = new ResourceLocation(MODID, "fish");
    public static final ResourceLocation JUNK = new ResourceLocation(MODID, "junk");
    public static final ResourceLocation TREASURE = new ResourceLocation(MODID, "treasure");

    public static void init() {
        LootConditionManager.registerCondition(new ConditionBiomeType.Serializer());
        LootConditionManager.registerCondition(new ConditionTemp.Serializer());
        LootTableList.register(FISH);
        LootTableList.register(JUNK);
        LootTableList.register(TREASURE);
    }
}
