package joshie.harvest.npcs.gift.init;

import joshie.harvest.api.core.MatchType;
import joshie.harvest.api.core.Ore;
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
        assignGeneric(Ore.of("gemDiamond"), GEM);
        assignGeneric(Ore.of("ingotIron"), MINERAL);
        assignGeneric(Ore.of("ingotGold"), MINERAL);
        assignGeneric(Ore.of("stickWood"), JUNK);
        assignGeneric(BOWL, JUNK);
        assignGeneric(MUSHROOM_STEW, COOKING);
        assignGeneric(Ore.of("string"), WOOL);
        assignGeneric(Ore.of("feather"), MEAT);
        assignGeneric(Ore.of("gunpowder"), MONSTER);
        assignGeneric(BREAD, COOKING);
        assignGeneric(FLINT, MINERAL);
        assignGeneric(PORKCHOP, MEAT);
        assignGeneric(COOKED_PORKCHOP, MEAT);
        assignGeneric(PAINTING, ART);
        assignGeneric(GOLDEN_APPLE, FRUIT);
        assignGeneric(SIGN, BUILDING);
        assignGeneric(SADDLE, JUNK);
        assignGeneric(Ore.of("dustRedstone"), MINERAL);
        assignGeneric(SNOWBALL, JUNK);
        assignGeneric(Ore.of("leather"), MEAT);
        assignGeneric(MILK_BUCKET, MILK);
        assignGeneric(Ore.of("ingotBrick"), BUILDING);
        assignGeneric(CLAY_BALL, BUILDING);
        assignGeneric(Ore.of("sugarcane"), PLANT);
        assignGeneric(Ore.of("paper"), KNOWLEDGE);
        assignGeneric(BOOK, KNOWLEDGE);
        assignGeneric(Ore.of("slimeball"), MONSTER);
        assignGeneric(Ore.of("egg"), GiftCategory.EGG);
        assignGeneric(COMPASS, KNOWLEDGE);
        assignGeneric(CLOCK, KNOWLEDGE);
        assignGeneric(Ore.of("dustGlowstone"), MINERAL);
        assignGeneric(Ore.of("fish"), GiftCategory.FISH);
        assignGeneric(COOKED_FISH, COOKING);
        assignGeneric(new ItemStack(Items.DYE, 1, 0), KNOWLEDGE); //Ink sac
        assignGeneric(Ore.of("gemLapis"), GEM); //Lapis Lazuli
        assignGeneric(new ItemStack(Items.DYE, 1, 15), MONSTER); //Bonemeal
        assignGeneric(Ore.of("dye").setType(MatchType.PREFIX), ART);
        assignGeneric(Ore.of("bone"), MONSTER);
        assignGeneric(SUGAR, JUNK);
        assignGeneric(CAKE, COOKING);
        assignGeneric(COOKIE, COOKING);
        assignGeneric(MAP, KNOWLEDGE);
        assignGeneric(FILLED_MAP, KNOWLEDGE);
        assignGeneric(MELON, FRUIT);
        assignGeneric(BEEF, MEAT);
        assignGeneric(COOKED_BEEF, MEAT);
        assignGeneric(CHICKEN, MEAT);
        assignGeneric(COOKED_CHICKEN, MEAT);
        assignGeneric(ROTTEN_FLESH, MONSTER);
        assignGeneric(Ore.of("enderpearl"), MONSTER);
        assignGeneric(BLAZE_ROD, MONSTER);
        assignGeneric(GHAST_TEAR, MONSTER);
        assignGeneric(Ore.of("nuggetGold"), MINERAL);
        assignGeneric(Ore.of("cropNetherWart"), MONSTER);
        assignGeneric(POTIONITEM, MAGIC);
        assignGeneric(GLASS_BOTTLE, JUNK);
        assignGeneric(SPIDER_EYE, MONSTER);
        assignGeneric(FERMENTED_SPIDER_EYE, MONSTER);
        assignGeneric(BLAZE_POWDER, MONSTER);
        assignGeneric(MAGMA_CREAM, MONSTER);
        assignGeneric(ENDER_EYE, MONSTER);
        assignGeneric(SPECKLED_MELON, FRUIT);
        assignGeneric(EXPERIENCE_BOTTLE, MAGIC);
        assignGeneric(FIRE_CHARGE, MONSTER);
        assignGeneric(WRITABLE_BOOK, KNOWLEDGE);
        assignGeneric(WRITTEN_BOOK, KNOWLEDGE);
        assignGeneric(Ore.of("gemEmerald"), GEM);
        assignGeneric(FLOWER_POT, FLOWER);
        assignGeneric(BAKED_POTATO, COOKING);
        assignGeneric(POISONOUS_POTATO, JUNK);
        assignGeneric(GOLDEN_CARROT, VEGETABLE);
        assignGeneric(SKULL, MONSTER);
        assignGeneric(CARROT_ON_A_STICK, JUNK);
        assignGeneric(PUMPKIN_PIE, COOKING);
        assignGeneric(FIREWORKS, JUNK);
        assignGeneric(FIREWORK_CHARGE, JUNK);
        assignGeneric(ENCHANTED_BOOK, MAGIC);
        assignGeneric(Ore.of("netherStar"), MAGIC);
        assignGeneric(Ore.of("ingotBrickNether"), BUILDING);
        assignGeneric(Ore.of("gemQuartz"), MINERAL);
        assignGeneric(LEAD, JUNK);
        assignGeneric(NAME_TAG, JUNK);
        assignGeneric(Ore.of("record"), ART);
        assignGeneric(Ore.of("gemPrismarine"), MINERAL);
        assignGeneric(Ore.of("dustPrismarine"), MINERAL);
        assignGeneric(CHORUS_FRUIT, FRUIT);
        assignGeneric(CHORUS_FRUIT_POPPED, FRUIT);
        assignGeneric(BEETROOT_SOUP, COOKING);
        assignGeneric(BANNER, GiftCategory.WOOL);
        assignGeneric(MUTTON, MEAT);
        assignGeneric(COOKED_MUTTON, MEAT);
        assignGeneric(COOKED_RABBIT, MEAT);
        assignGeneric(RABBIT, MEAT);
        assignGeneric(RABBIT_HIDE, MEAT);
        assignGeneric(RABBIT_STEW, COOKING);
        assignGeneric(ITEM_FRAME, ART);
    }
}