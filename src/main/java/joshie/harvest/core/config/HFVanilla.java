package joshie.harvest.core.config;

import joshie.harvest.core.HFTab;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import static joshie.harvest.core.config.HFConfig.asm;

public class HFVanilla {
    public static void preInit() {
        if (asm.CARROT_BLOCK_DISABLE_TICKING) Blocks.carrots.setTickRandomly(false);
        if (asm.POTATO_BLOCK_DISABLE_TICKING) Blocks.potatoes.setTickRandomly(false);
        if (asm.WHEAT_BLOCK_DISABLE_TICKING) Blocks.wheat.setTickRandomly(false);
        if (asm.PUMPKIN_BLOCK_DISABLE_TICKING) Blocks.pumpkin_stem.setTickRandomly(false);
        if (asm.WATERMELON_BLOCK_DISABLE_TICKING) Blocks.melon_stem.setTickRandomly(false);
        if (asm.MOVE_OVERRIDE_TAB) {
            if (asm.CARROT_OVERRIDE) Items.CARROT.setCreativeTab(HFTab.tabFarming).setHasSubtypes(true);
            if (asm.POTATO_OVERRIDE) Items.potato.setCreativeTab(HFTab.tabFarming).setHasSubtypes(true);
            if (asm.WHEAT_OVERRIDE) Items.WHEAT.setCreativeTab(HFTab.tabFarming).setHasSubtypes(true);
            if (asm.WATERMELON_OVERRIDE) Items.MELON.setCreativeTab(HFTab.tabFarming).setHasSubtypes(true);
            if (asm.EGG_OVERRIDE) Items.egg.setCreativeTab(HFTab.tabFarming).setHasSubtypes(true);
            if (asm.PUMPKIN_OVERRIDE) {
                Blocks.pumpkin.setCreativeTab(HFTab.tabFarming);
                Item.getItemFromBlock(Blocks.pumpkin).setHasSubtypes(true);
            }
        }
    }
}
