package joshie.harvest.npcs.gift.init;

import joshie.harvest.core.util.annotations.HFLoader;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static net.minecraft.init.Blocks.*;

@HFLoader(priority = 0)
public class HFGiftsVanillaBlocks extends HFGiftsAbstract {
    public static void init() {
        assignGeneric(STONE, BUILDING);
        assignGeneric(GRASS, JUNK, PLANT);
        assignGeneric(DIRT, JUNK);
        assignGeneric(COBBLESTONE, JUNK, BUILDING);
        assignGeneric(PLANKS, JUNK, BUILDING);
        assignGeneric(SAPLING, JUNK, PLANT);
        assignGeneric(SAND, JUNK);
        assignGeneric(GRAVEL, JUNK);
        assignGeneric(GOLD_ORE, MINERAL);
        assignGeneric(IRON_ORE, MINERAL);
        assignGeneric(COAL_ORE, MINERAL);
        assignGeneric(LOG, BUILDING);
        assignGeneric(LOG2, BUILDING);
        assignGeneric(LEAVES, PLANT);
        assignGeneric(LEAVES2, PLANT);
        assignGeneric(SPONGE, ANIMAL);
        assignGeneric(GLASS, BUILDING);
        assignGeneric(LAPIS_ORE, MINERAL);
        assignGeneric(LAPIS_BLOCK, GEM);
        assignGeneric(SANDSTONE, JUNK, BUILDING);
        assignGeneric(WEB, MONSTER);
        assignGeneric(TALLGRASS, JUNK, PLANT);
        assignGeneric(DEADBUSH, JUNK);
        assignGeneric(WOOL, ANIMAL);
        assignGeneric(YELLOW_FLOWER, FLOWER);
        assignGeneric(RED_FLOWER, FLOWER);
        assignGeneric(BROWN_MUSHROOM, COOKING);
        assignGeneric(RED_MUSHROOM, COOKING);
        assignGeneric(GOLD_BLOCK, MINERAL);
        assignGeneric(IRON_BLOCK, MINERAL);
        assignGeneric(STONE_SLAB, JUNK, BUILDING);
        assignGeneric(BRICK_BLOCK, JUNK, BUILDING);
        assignGeneric(TNT, MONSTER);
        assignGeneric(BOOKSHELF, KNOWLEDGE);
        assignGeneric(MOSSY_COBBLESTONE, JUNK, BUILDING);
        assignGeneric(OBSIDIAN, JUNK, BUILDING);
        assignGeneric(TORCH, KNOWLEDGE);
        assignGeneric(OAK_STAIRS, JUNK, BUILDING);
        assignGeneric(CHEST, JUNK, BUILDING);
        assignGeneric(DIAMOND_ORE, MINERAL);
        assignGeneric(DIAMOND_BLOCK, GEM);
        assignGeneric(CRAFTING_TABLE, BUILDING);
        assignGeneric(LADDER, BUILDING);
        assignGeneric(STONE_STAIRS, BUILDING);
        assignGeneric(LEVER, BUILDING);
        assignGeneric(STONE_PRESSURE_PLATE, BUILDING);
        assignGeneric(WOODEN_PRESSURE_PLATE, BUILDING);
        assignGeneric(REDSTONE_ORE, MINERAL);
        assignGeneric(REDSTONE_TORCH, BUILDING);
        assignGeneric(STONE_BUTTON, BUILDING);
        assignGeneric(ICE, MINERAL);
        assignGeneric(SNOW, SWEET);
        assignGeneric(CACTUS, PLANT);
        assignGeneric(CLAY, BUILDING);
        assignGeneric(OAK_FENCE, BUILDING);
        assignGeneric(SPRUCE_FENCE, BUILDING);
        assignGeneric(BIRCH_FENCE, BUILDING);
        assignGeneric(JUNGLE_FENCE, BUILDING);
        assignGeneric(ACACIA_FENCE, BUILDING);
        assignGeneric(DARK_OAK_FENCE, BUILDING);
        assignGeneric(PUMPKIN, VEGETABLE);
        assignGeneric(NETHERRACK, MONSTER);
        assignGeneric(SOUL_SAND, MONSTER);
        assignGeneric(GLOWSTONE, MINERAL);
        assignGeneric(LIT_PUMPKIN, VEGETABLE);
        assignGeneric(TRAPDOOR, BUILDING);
        assignGeneric(STONEBRICK, BUILDING);
        assignGeneric(BROWN_MUSHROOM_BLOCK, PLANT);
        assignGeneric(RED_MUSHROOM_BLOCK, PLANT);
        assignGeneric(IRON_BARS, BUILDING);
        assignGeneric(GLASS_PANE, BUILDING);
        assignGeneric(MELON_BLOCK, FRUIT);
        assignGeneric(VINE, PLANT);
        assignGeneric(OAK_FENCE_GATE, BUILDING);
        assignGeneric(SPRUCE_FENCE_GATE, BUILDING);
        assignGeneric(BIRCH_FENCE_GATE, BUILDING);
        assignGeneric(JUNGLE_FENCE_GATE, BUILDING);
        assignGeneric(ACACIA_FENCE_GATE, BUILDING);
        assignGeneric(DARK_OAK_FENCE_GATE, BUILDING);
        assignGeneric(BRICK_STAIRS, BUILDING);
        assignGeneric(STONE_BRICK_STAIRS, BUILDING);
        assignGeneric(MYCELIUM, PLANT);
        assignGeneric(WATERLILY, PLANT);
        assignGeneric(NETHER_BRICK, BUILDING);
        assignGeneric(NETHER_BRICK_FENCE, BUILDING);
        assignGeneric(NETHER_BRICK_STAIRS, BUILDING);
        assignGeneric(END_STONE, MONSTER);
        assignGeneric(DRAGON_EGG, MONSTER);
        assignGeneric(REDSTONE_LAMP, BUILDING);
        assignGeneric(WOODEN_SLAB, BUILDING);
        assignGeneric(SANDSTONE_STAIRS, BUILDING);
        assignGeneric(EMERALD_ORE, MINERAL);
        assignGeneric(ENDER_CHEST, BUILDING);
        assignGeneric(TRIPWIRE_HOOK, BUILDING);
        assignGeneric(EMERALD_BLOCK, GEM);
        assignGeneric(SPRUCE_STAIRS, BUILDING);
        assignGeneric(BIRCH_STAIRS, BUILDING);
        assignGeneric(JUNGLE_STAIRS, BUILDING);
        assignGeneric(COBBLESTONE_WALL, BUILDING);
        assignGeneric(WOODEN_BUTTON, BUILDING);
        assignGeneric(LIGHT_WEIGHTED_PRESSURE_PLATE, BUILDING);
        assignGeneric(HEAVY_WEIGHTED_PRESSURE_PLATE, BUILDING);
        assignGeneric(DAYLIGHT_DETECTOR, BUILDING);
        assignGeneric(REDSTONE_BLOCK, MINERAL);
        assignGeneric(QUARTZ_ORE, MINERAL);
        assignGeneric(QUARTZ_BLOCK, MINERAL, BUILDING);
        assignGeneric(QUARTZ_STAIRS, BUILDING);
        assignGeneric(STAINED_HARDENED_CLAY, BUILDING);
        assignGeneric(HAY_BLOCK, ANIMAL);
        assignGeneric(CARPET, BUILDING);
        assignGeneric(HARDENED_CLAY, BUILDING);
        assignGeneric(COAL_BLOCK, BUILDING);
        assignGeneric(PACKED_ICE, SWEET);
        assignGeneric(ACACIA_STAIRS, BUILDING);
        assignGeneric(DARK_OAK_STAIRS, BUILDING);
        assignGeneric(new ItemStack(DOUBLE_PLANT, 1, 0), FLOWER); //Sunflower
        assignGeneric(new ItemStack(DOUBLE_PLANT, 1, 1), FLOWER); //Lilac
        assignGeneric(new ItemStack(DOUBLE_PLANT, 1, 2), PLANT); //Tallgrass
        assignGeneric(new ItemStack(DOUBLE_PLANT, 1, 3), PLANT); //Fern
        assignGeneric(new ItemStack(DOUBLE_PLANT, 1, 4), FLOWER); //Rose
        assignGeneric(new ItemStack(DOUBLE_PLANT, 1, 5), FLOWER); //Peony
        assignGeneric(STAINED_GLASS, BUILDING);
        assignGeneric(STAINED_GLASS_PANE, BUILDING);
        assignGeneric(PRISMARINE, BUILDING);
        assignGeneric(SEA_LANTERN, BUILDING);
        assignGeneric(RED_SANDSTONE, BUILDING);
        assignGeneric(RED_SANDSTONE_STAIRS, BUILDING);
        assignGeneric(PURPUR_BLOCK, BUILDING);
        assignGeneric(PURPUR_PILLAR, BUILDING);
        assignGeneric(PURPUR_SLAB, BUILDING);
        assignGeneric(PURPUR_STAIRS, BUILDING);
        assignGeneric(END_BRICKS, BUILDING, MONSTER);
    }
}