package joshie.harvestmoon.init;

import static joshie.harvestmoon.init.HMConfiguration.vanilla;
import joshie.harvestmoon.core.HMTab;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class HMVanilla {
    public static void init() {
        if (HMConfiguration.vanilla.HOES_HIDDEN) {
            Items.wooden_hoe.setCreativeTab(null);
            Items.stone_hoe.setCreativeTab(null);
            Items.iron_hoe.setCreativeTab(null);
            Items.diamond_hoe.setCreativeTab(null);
            Items.golden_hoe.setCreativeTab(null);
        }

        if (vanilla.CARROT_BLOCK_DISABLE_TICKING) Blocks.carrots.setTickRandomly(false);
        if (vanilla.POTATO_BLOCK_DISABLE_TICKING) Blocks.potatoes.setTickRandomly(false);
        if (vanilla.WHEAT_BLOCK_DISABLE_TICKING) Blocks.wheat.setTickRandomly(false);
        if (vanilla.MOVE_OVERRIDE_TAB) {
            if (vanilla.CARROT_OVERRIDE) Items.carrot.setCreativeTab(HMTab.tabGeneral);
            if (vanilla.POTATO_OVERRIDE) Items.potato.setCreativeTab(HMTab.tabGeneral);
            if (vanilla.WHEAT_OVERRIDE) Items.wheat.setCreativeTab(HMTab.tabGeneral);
            if (vanilla.EGG_OVERRIDE) Items.egg.setCreativeTab(HMTab.tabGeneral).setHasSubtypes(true);
        }
    }
}
