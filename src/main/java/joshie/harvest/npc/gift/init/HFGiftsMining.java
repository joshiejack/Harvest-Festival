package joshie.harvest.npc.gift.init;

import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.mining.item.ItemMaterial.Material;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.mining.HFMining.MATERIALS;

@HFLoader(priority = 0)
public class HFGiftsMining extends HFGiftsAbstract {
    public static void init() {
        assignGeneric(MATERIALS.getStackFromEnum(Material.JUNK), JUNK);
        assignGeneric(MATERIALS.getStackFromEnum(Material.SILVER), MINERAL);
        assignGeneric(MATERIALS.getStackFromEnum(Material.MYSTRIL), MINERAL);
        assignGeneric(MATERIALS.getStackFromEnum(Material.GOLD), MINERAL);
        assignGeneric(MATERIALS.getStackFromEnum(Material.COPPER), MINERAL);
        assignGeneric(MATERIALS.getStackFromEnum(Material.MYTHIC), MINERAL);
        assignGeneric(MATERIALS.getStackFromEnum(Material.ADAMANTITE), GEM);
        assignGeneric(MATERIALS.getStackFromEnum(Material.AGATE), GEM);
        assignGeneric(MATERIALS.getStackFromEnum(Material.ALEXANDRITE), GEM);
        assignGeneric(MATERIALS.getStackFromEnum(Material.AMETHYST), GEM);
        assignGeneric(MATERIALS.getStackFromEnum(Material.FLUORITE), GEM);
        assignGeneric(MATERIALS.getStackFromEnum(Material.MOON_STONE), GEM);
        assignGeneric(MATERIALS.getStackFromEnum(Material.ORICHALC), GEM);
        assignGeneric(MATERIALS.getStackFromEnum(Material.PERIDOT), GEM);
        assignGeneric(MATERIALS.getStackFromEnum(Material.PINK_DIAMOND), GEM);
        assignGeneric(MATERIALS.getStackFromEnum(Material.RUBY), GEM);
        assignGeneric(MATERIALS.getStackFromEnum(Material.SAND_ROSE), GEM);
        assignGeneric(MATERIALS.getStackFromEnum(Material.TOPAZ), GEM);
    }
}