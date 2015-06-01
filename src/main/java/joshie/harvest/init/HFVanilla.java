package joshie.harvest.init;

import static joshie.harvest.init.HFConfig.vanilla;
import joshie.harvest.core.HFTab;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class HFVanilla {
    public static void init() {
        if (HFConfig.vanilla.HOES_HIDDEN) {
            Items.wooden_hoe.setCreativeTab(null);
            Items.stone_hoe.setCreativeTab(null);
            Items.iron_hoe.setCreativeTab(null);
            Items.diamond_hoe.setCreativeTab(null);
            Items.golden_hoe.setCreativeTab(null);
        }

        if (vanilla.CARROT_BLOCK_DISABLE_TICKING) Blocks.carrots.setTickRandomly(false);
        if (vanilla.POTATO_BLOCK_DISABLE_TICKING) Blocks.potatoes.setTickRandomly(false);
        if (vanilla.WHEAT_BLOCK_DISABLE_TICKING) Blocks.wheat.setTickRandomly(false);
        if (vanilla.PUMPKIN_BLOCK_DISABLE_TICKING) Blocks.pumpkin_stem.setTickRandomly(false);
        if (vanilla.WATERMELON_BLOCK_DISABLE_TICKING) Blocks.melon_stem.setTickRandomly(false);
        if (vanilla.MOVE_OVERRIDE_TAB) {
            if (vanilla.CARROT_OVERRIDE) Items.carrot.setCreativeTab(HFTab.tabFarming).setHasSubtypes(true);
            if (vanilla.POTATO_OVERRIDE) Items.potato.setCreativeTab(HFTab.tabFarming).setHasSubtypes(true);
            if (vanilla.WHEAT_OVERRIDE) Items.wheat.setCreativeTab(HFTab.tabFarming).setHasSubtypes(true);
            if (vanilla.WATERMELON_OVERRIDE) Items.melon.setCreativeTab(HFTab.tabFarming).setHasSubtypes(true);
            if (vanilla.EGG_OVERRIDE) Items.egg.setCreativeTab(HFTab.tabFarming).setHasSubtypes(true);
            if (vanilla.PUMPKIN_OVERRIDE) {
                Blocks.pumpkin.setCreativeTab(HFTab.tabFarming);
                Item.getItemFromBlock(Blocks.pumpkin).setHasSubtypes(true);
            }
        }
    }
}
