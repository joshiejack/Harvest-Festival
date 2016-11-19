package joshie.harvest.npc.gift.init;

import joshie.harvest.api.npc.gift.GiftCategory;
import joshie.harvest.core.util.annotations.HFLoader;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static net.minecraft.init.Items.*;

@HFLoader(priority = 0)
public class HFGiftsVanillaItems extends HFGiftsAbstract {
    public static void init() {
        assignGeneric(APPLE, FRUIT);
        assignGeneric(COAL, MINERAL);
        assignGeneric(DIAMOND, GEM);
        assignGeneric(IRON_INGOT, MINERAL);
        assignGeneric(GOLD_INGOT, MINERAL);
        assignGeneric(STICK, JUNK);
        assignGeneric(BOWL, JUNK);
        assignGeneric(MUSHROOM_STEW, COOKING);
        assignGeneric(STRING, ANIMAL);
        assignGeneric(FEATHER, ANIMAL);
        assignGeneric(GUNPOWDER, MONSTER);
        assignGeneric(BREAD, COOKING);
        assignGeneric(FLINT, MINERAL);
        assignGeneric(PORKCHOP, MEAT);
        assignGeneric(COOKED_PORKCHOP, MEAT);
        assignGeneric(PAINTING, BUILDING);
        assignGeneric(GOLDEN_APPLE, FRUIT, MINERAL);
        assignGeneric(SIGN, BUILDING);
        assignGeneric(SADDLE, ANIMAL);
        assignGeneric(REDSTONE, MINERAL);
        assignGeneric(SNOWBALL, SWEET);
        assignGeneric(LEATHER, MEAT);
        assignGeneric(MILK_BUCKET, ANIMAL);
        assignGeneric(BRICK, BUILDING);
        assignGeneric(CLAY_BALL, BUILDING);
        assignGeneric(REEDS, PLANT);
        assignGeneric(PAPER, KNOWLEDGE);
        assignGeneric(BOOK, KNOWLEDGE);
        assignGeneric(SLIME_BALL, MONSTER);
        assignGeneric(EGG, ANIMAL);
        assignGeneric(COMPASS, KNOWLEDGE);
        assignGeneric(CLOCK, KNOWLEDGE);
        assignGeneric(GLOWSTONE_DUST, MINERAL);
        assignGeneric(Items.FISH, GiftCategory.FISH);
        assignGeneric(COOKED_FISH, GiftCategory.FISH);
        assignGeneric(new ItemStack(Items.DYE, 1, 0), KNOWLEDGE); //Ink sac
        assignGeneric(new ItemStack(Items.DYE, 1, 1), FLOWER); //Rose Red
        assignGeneric(new ItemStack(Items.DYE, 1, 2), PLANT); //Cactus
        assignGeneric(new ItemStack(Items.DYE, 1, 3), PLANT); //Cocoa Beans
        assignGeneric(new ItemStack(Items.DYE, 1, 4), GEM); //Lapis Lazuli
        assignGeneric(new ItemStack(Items.DYE, 1, 11), FLOWER); //Dandelion
        assignGeneric(new ItemStack(Items.DYE, 1, 15), MONSTER); //Bonemeal
        assignGeneric(BONE, MONSTER);
        assignGeneric(SUGAR, SWEET);
        assignGeneric(CAKE, SWEET, COOKING);
        assignGeneric(COOKIE, SWEET, COOKING);
        assignGeneric(MAP, KNOWLEDGE);
        assignGeneric(FILLED_MAP, KNOWLEDGE);
        assignGeneric(MELON, FRUIT);
        assignGeneric(BEEF, MEAT);
        assignGeneric(COOKED_BEEF, MEAT);
        assignGeneric(CHICKEN, MEAT);
        assignGeneric(COOKED_CHICKEN, MEAT);
        assignGeneric(ROTTEN_FLESH, MONSTER);
        assignGeneric(ENDER_PEARL, MONSTER);
        assignGeneric(BLAZE_ROD, MONSTER);
        assignGeneric(GHAST_TEAR, MONSTER);
        assignGeneric(GOLD_NUGGET, MINERAL);
        assignGeneric(NETHER_WART, PLANT, MONSTER);
        assignGeneric(POTIONITEM, MAGIC);
        assignGeneric(GLASS_BOTTLE, JUNK);
        assignGeneric(SPIDER_EYE, MONSTER);
        assignGeneric(FERMENTED_SPIDER_EYE, MONSTER);
        assignGeneric(BLAZE_POWDER, MONSTER);
        assignGeneric(MAGMA_CREAM, MONSTER);
        assignGeneric(ENDER_EYE, MONSTER);
        assignGeneric(SPECKLED_MELON, FRUIT, MINERAL);
        assignGeneric(EXPERIENCE_BOTTLE, MAGIC);
        assignGeneric(FIRE_CHARGE, MONSTER);
        assignGeneric(WRITABLE_BOOK, KNOWLEDGE);
        assignGeneric(WRITTEN_BOOK, KNOWLEDGE);
        assignGeneric(EMERALD, GEM);
        assignGeneric(FLOWER_POT, FLOWER);
        assignGeneric(BAKED_POTATO, VEGETABLE, COOKING);
        assignGeneric(POISONOUS_POTATO, VEGETABLE, MONSTER);
        assignGeneric(GOLDEN_CARROT, VEGETABLE, MINERAL);
        assignGeneric(SKULL, MONSTER);
        assignGeneric(CARROT_ON_A_STICK, ANIMAL);
        assignGeneric(NETHER_STAR, MONSTER, MAGIC);
        assignGeneric(PUMPKIN_PIE, COOKING);
        assignGeneric(FIREWORKS, MAGIC);
        assignGeneric(FIREWORK_CHARGE, JUNK);
        assignGeneric(ENCHANTED_BOOK, MAGIC);
        assignGeneric(NETHERBRICK, BUILDING);
        assignGeneric(QUARTZ, MINERAL);
        assignGeneric(LEAD, ANIMAL);
        assignGeneric(NAME_TAG, ANIMAL);
        assignGeneric(RECORD_13, GEM);
        assignGeneric(RECORD_CAT, GEM);
        assignGeneric(RECORD_BLOCKS, GEM);
        assignGeneric(RECORD_CHIRP, GEM);
        assignGeneric(RECORD_FAR, GEM);
        assignGeneric(RECORD_MALL, GEM);
        assignGeneric(RECORD_MELLOHI, GEM);
        assignGeneric(RECORD_STAL, GEM);
        assignGeneric(RECORD_STRAD, GEM);
        assignGeneric(RECORD_WARD, GEM);
        assignGeneric(RECORD_11, GEM);
        assignGeneric(RECORD_WAIT, GEM);
        assignGeneric(PRISMARINE_SHARD, MINERAL);
        assignGeneric(PRISMARINE_CRYSTALS, GEM);
        assignGeneric(CHORUS_FRUIT, FRUIT);
        assignGeneric(CHORUS_FRUIT_POPPED, FRUIT);
        assignGeneric(BEETROOT_SOUP, COOKING);
    }
}