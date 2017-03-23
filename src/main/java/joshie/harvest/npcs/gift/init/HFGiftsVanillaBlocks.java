package joshie.harvest.npcs.gift.init;

import joshie.harvest.api.core.MatchType;
import joshie.harvest.api.core.Ore;
import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.core.util.annotations.HFLoader;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static net.minecraft.init.Blocks.*;

@HFLoader(priority = 0)
public class HFGiftsVanillaBlocks extends HFGiftsAbstract {
    public static void init() {
        assignGeneric(Ore.of("stone").setType(MatchType.PREFIX), BUILDING);
        assignGeneric(Ore.of("grass"), JUNK);
        assignGeneric(Ore.of("dirt"), JUNK);
        assignGeneric(Ore.of("cobblestone"), BUILDING);
        assignGeneric(Ore.of("plank").setType(MatchType.PREFIX), BUILDING);
        assignGeneric(Ore.of("sand"), JUNK);
        assignGeneric(Ore.of("gravel"), JUNK);
        assignGeneric(Ore.of("oreGold"), MINERAL);
        assignGeneric(Ore.of("oreIron"), MINERAL);
        assignGeneric(Ore.of("oreCoal"), MINERAL);
        assignGeneric(Ore.of("log").setType(MatchType.PREFIX), BUILDING);
        assignGeneric(Ore.of("tree").setType(MatchType.PREFIX), PLANT);
        assignGeneric(SPONGE, FISH);
        assignGeneric(Ore.of("blockGlass").setType(MatchType.PREFIX), BUILDING);
        assignGeneric(Ore.of("oreLapis"), MINERAL);
        assignGeneric(Ore.of("blockLapis"), GEM);
        assignGeneric(Ore.of("sandstone"), BUILDING);
        assignGeneric(WEB, MONSTER);
        assignGeneric(TALLGRASS, PLANT);
        assignGeneric(DEADBUSH, JUNK);
        assignGeneric(Blocks.WOOL, GiftCategory.WOOL);
        assignGeneric(YELLOW_FLOWER, FLOWER);
        assignGeneric(RED_FLOWER, FLOWER);
        assignGeneric(BROWN_MUSHROOM, MUSHROOM);
        assignGeneric(RED_MUSHROOM, MUSHROOM);
        assignGeneric(Ore.of("blockGold"), MINERAL);
        assignGeneric(Ore.of("blockIron"), MINERAL);
        assignGeneric(STONE_SLAB, BUILDING);
        assignGeneric(BRICK_BLOCK, BUILDING);
        assignGeneric(TNT, MONSTER);
        assignGeneric(BOOKSHELF, BUILDING);
        assignGeneric(MOSSY_COBBLESTONE, BUILDING);
        assignGeneric(Ore.of("obsidian"), BUILDING);
        assignGeneric(Ore.of("torch"), JUNK);
        assignGeneric(Ore.of("stair").setType(MatchType.PREFIX), BUILDING);
        assignGeneric(Ore.of("oreDiamond"), MINERAL);
        assignGeneric(Ore.of("blockDiamond"), GEM);
        assignGeneric(Ore.of("workbench"), BUILDING);
        assignGeneric(LADDER, BUILDING);
        assignGeneric(STONE_STAIRS, BUILDING);
        assignGeneric(LEVER, BUILDING);
        assignGeneric(STONE_PRESSURE_PLATE, BUILDING);
        assignGeneric(WOODEN_PRESSURE_PLATE, BUILDING);
        assignGeneric(Ore.of("oreRedstone"), MINERAL);
        assignGeneric(REDSTONE_TORCH, BUILDING);
        assignGeneric(STONE_BUTTON, BUILDING);
        assignGeneric(ICE, MINERAL);
        assignGeneric(SNOW, BUILDING);
        assignGeneric(Ore.of("blockCactus"), PLANT);
        assignGeneric(CLAY, BUILDING);
        assignGeneric(OAK_FENCE, BUILDING);
        assignGeneric(SPRUCE_FENCE, BUILDING);
        assignGeneric(BIRCH_FENCE, BUILDING);
        assignGeneric(JUNGLE_FENCE, BUILDING);
        assignGeneric(ACACIA_FENCE, BUILDING);
        assignGeneric(DARK_OAK_FENCE, BUILDING);
        assignGeneric(PUMPKIN, VEGETABLE);
        assignGeneric(Ore.of("netherrack"), JUNK);
        assignGeneric(SOUL_SAND, MONSTER);
        assignGeneric(Ore.of("glowstone"), MINERAL);
        assignGeneric(LIT_PUMPKIN, VEGETABLE);
        assignGeneric(TRAPDOOR, BUILDING);
        assignGeneric(STONEBRICK, BUILDING);
        assignGeneric(BROWN_MUSHROOM_BLOCK, PLANT);
        assignGeneric(RED_MUSHROOM_BLOCK, PLANT);
        assignGeneric(IRON_BARS, BUILDING);
        assignGeneric(Ore.of("paneGlass").setType(MatchType.PREFIX), BUILDING);
        assignGeneric(MELON_BLOCK, FRUIT);
        assignGeneric(Ore.of("vine"), PLANT);
        assignGeneric(OAK_FENCE_GATE, BUILDING);
        assignGeneric(SPRUCE_FENCE_GATE, BUILDING);
        assignGeneric(BIRCH_FENCE_GATE, BUILDING);
        assignGeneric(JUNGLE_FENCE_GATE, BUILDING);
        assignGeneric(ACACIA_FENCE_GATE, BUILDING);
        assignGeneric(DARK_OAK_FENCE_GATE, BUILDING);
        assignGeneric(BRICK_STAIRS, BUILDING);
        assignGeneric(STONE_BRICK_STAIRS, BUILDING);
        assignGeneric(MYCELIUM, JUNK);
        assignGeneric(WATERLILY, PLANT);
        assignGeneric(NETHER_BRICK, BUILDING);
        assignGeneric(NETHER_BRICK_FENCE, BUILDING);
        assignGeneric(NETHER_BRICK_STAIRS, BUILDING);
        assignGeneric(Ore.of("endstone"), JUNK);
        assignGeneric(DRAGON_EGG, MONSTER);
        assignGeneric(REDSTONE_LAMP, BUILDING);
        assignGeneric(Ore.of("slab").setType(MatchType.PREFIX), BUILDING);
        assignGeneric(SANDSTONE_STAIRS, BUILDING);
        assignGeneric(Ore.of("oreEmerald"), MINERAL);
        assignGeneric(TRIPWIRE_HOOK, BUILDING);
        assignGeneric(Ore.of("blockEmerald"), GEM);
        assignGeneric(COBBLESTONE_WALL, BUILDING);
        assignGeneric(WOODEN_BUTTON, BUILDING);
        assignGeneric(LIGHT_WEIGHTED_PRESSURE_PLATE, BUILDING);
        assignGeneric(HEAVY_WEIGHTED_PRESSURE_PLATE, BUILDING);
        assignGeneric(DAYLIGHT_DETECTOR, BUILDING);
        assignGeneric(Ore.of("blockRedstone"), MINERAL);
        assignGeneric(Ore.of("oreQuartz"), MINERAL);
        assignGeneric(Ore.of("blockQuartz"), MINERAL);
        assignGeneric(QUARTZ_STAIRS, BUILDING);
        assignGeneric(STAINED_HARDENED_CLAY, BUILDING);
        assignGeneric(HAY_BLOCK, BUILDING);
        assignGeneric(CARPET, GiftCategory.WOOL);
        assignGeneric(HARDENED_CLAY, BUILDING);
        assignGeneric(Ore.of("blockCoal"), MINERAL);
        assignGeneric(PACKED_ICE, BUILDING);
        assignGeneric(new ItemStack(DOUBLE_PLANT, 1, 0), FLOWER); //Sunflower
        assignGeneric(new ItemStack(DOUBLE_PLANT, 1, 1), FLOWER); //Lilac
        assignGeneric(new ItemStack(DOUBLE_PLANT, 1, 2), PLANT); //Tallgrass
        assignGeneric(new ItemStack(DOUBLE_PLANT, 1, 3), PLANT); //Fern
        assignGeneric(new ItemStack(DOUBLE_PLANT, 1, 4), FLOWER); //Rose
        assignGeneric(new ItemStack(DOUBLE_PLANT, 1, 5), FLOWER); //Peony
        assignGeneric(STAINED_GLASS, BUILDING);
        assignGeneric(STAINED_GLASS_PANE, BUILDING);
        assignGeneric(Ore.of("blockPrismarine").setType(MatchType.PREFIX), BUILDING);
        assignGeneric(SEA_LANTERN, BUILDING);
        assignGeneric(RED_SANDSTONE, BUILDING);
        assignGeneric(RED_SANDSTONE_STAIRS, BUILDING);
        assignGeneric(PURPUR_BLOCK, BUILDING);
        assignGeneric(PURPUR_PILLAR, BUILDING);
        assignGeneric(PURPUR_SLAB, BUILDING);
        assignGeneric(PURPUR_STAIRS, BUILDING);
        assignGeneric(END_BRICKS, BUILDING);
        assignGeneric(Ore.of("blockSlime"), JUNK);
    }
}