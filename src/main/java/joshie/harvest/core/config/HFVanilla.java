package joshie.harvest.core.config;

import joshie.harvest.core.HFTab;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import static joshie.harvest.core.config.HFConfig.asm;

public class HFVanilla {
    public static void preInit() {
        if (asm.CARROT_BLOCK_DISABLE_TICKING) Blocks.CARROTS.setTickRandomly(false);
        if (asm.POTATO_BLOCK_DISABLE_TICKING) Blocks.POTATOES.setTickRandomly(false);
        if (asm.WHEAT_BLOCK_DISABLE_TICKING) Blocks.WHEAT.setTickRandomly(false);
        if (asm.PUMPKIN_BLOCK_DISABLE_TICKING) Blocks.PUMPKIN_STEM.setTickRandomly(false);
        if (asm.WATERMELON_BLOCK_DISABLE_TICKING) Blocks.MELON_STEM.setTickRandomly(false);
        if (asm.MOVE_OVERRIDE_TAB) {
            if (asm.CARROT_OVERRIDE) Items.CARROT.setCreativeTab(HFTab.FARMING).setHasSubtypes(true);
            if (asm.POTATO_OVERRIDE) Items.POTATO.setCreativeTab(HFTab.FARMING).setHasSubtypes(true);
            if (asm.WHEAT_OVERRIDE) Items.WHEAT.setCreativeTab(HFTab.FARMING).setHasSubtypes(true);
            if (asm.WATERMELON_OVERRIDE) Items.MELON.setCreativeTab(HFTab.FARMING).setHasSubtypes(true);
            if (asm.EGG_OVERRIDE) Items.EGG.setCreativeTab(HFTab.FARMING).setHasSubtypes(true);
            if (asm.PUMPKIN_OVERRIDE) {
                Blocks.PUMPKIN.setCreativeTab(HFTab.FARMING);
                Item.getItemFromBlock(Blocks.PUMPKIN).setHasSubtypes(true);
            }
        }
    }
}