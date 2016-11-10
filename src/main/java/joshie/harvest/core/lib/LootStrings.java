package joshie.harvest.core.lib;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class LootStrings {
    public static final ResourceLocation JADE_CHEST = register("chests/carpenter_jade");
    public static final ResourceLocation YULIF_CHEST = register("chests/carpenter_yulif");
    public static final ResourceLocation CARPENTER_FRAME = register("frames/carpenter");
    public static final ResourceLocation CLOCKMAKER_CHEST = register("chests/clockworker_bedroom");
    public static final ResourceLocation CLOCKMAKER_FRAME = register("frames/clockworker");
    public static final ResourceLocation MARKET_BASEMENT_CHESTS = register("chests/market_basement");
    public static final ResourceLocation MARKET_BEDROOM_CHESTS = register("chests/market_bedroom");
    public static final ResourceLocation MARKET_BEDROOM_FRAME = register("frames/market_bedroom");
    public static final ResourceLocation MARKET_ENTRY_FRAME = register("frames/market_entry");
    public static final ResourceLocation TOWNHALL_HALL_FRAME = register("frames/townhall_entry");
    public static final ResourceLocation TOWNHALL_MASTER_PRIEST_FRAME = register("frames/townhall_priest");
    public static final ResourceLocation TOWNHALL_MASTER_MAYOR_FRAME = register("frames/townhall_mayor");
    public static final ResourceLocation TOWNHALL_CHILD_FRAME = register("frames/townhall_child");
    public static final ResourceLocation TOWNHALL_TEENAGER_FRAME = register("frames/townhall_teenager");
    public static final ResourceLocation TOWNHALL_PASSAGE_CHEST = register("frames/townhall_passage");
    public static final ResourceLocation TOWNHALL_TEENAGER_CHEST = register("chests/townhall_teenager");
    public static final ResourceLocation CAFE_CHEST = register("chests/cafe");
    public static final ResourceLocation CAFE_FRAME = register("frames/cafe");
    public static final ResourceLocation MINING_CHEST = register("chests/miner");
    public static final ResourceLocation MINING_FRAME = register("frames/miner");
    public static final ResourceLocation POULTRY_CHEST = register("chests/poultry");
    public static final ResourceLocation POULTRY_FRAME = register("frames/poultry");
    public static final ResourceLocation FISHING_CHEST = register("chests/fishing");
    public static final ResourceLocation FISHING_FRAME = register("frames/fishing");
    public static final ResourceLocation CHURCH_FRAME = register("frames/church");
    public static final ResourceLocation BLACKSMITH_CHEST = register("chests/blacksmith");
    public static final ResourceLocation BLACKSMITH_FRAME = register("frames/blacksmith");
    public static final ResourceLocation BARN_FRAME = register("frames/barn");
    public static final ResourceLocation MINING = register("gameplay/mining/junk");
    public static final ResourceLocation MINING_GEMS = register("gameplay/mining/gems");
    public static final ResourceLocation TRAP_JUNK = register("gameplay/fishing/trap_junk");

    private static ResourceLocation register(String id) {
        return LootTableList.register(new ResourceLocation(HFModInfo.MODID, id));
    }
}