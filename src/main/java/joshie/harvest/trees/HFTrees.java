package joshie.harvest.trees;

import joshie.harvest.api.trees.Tree;
import joshie.harvest.core.util.annotations.HFLoader;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.api.calendar.Season.AUTUMN;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static net.minecraft.init.Blocks.LEAVES;
import static net.minecraft.init.Blocks.LOG;

@HFLoader
public class HFTrees {
    public static final Tree APPLE = registerTree("apple").setGoldValues(1500, 100).setGrowthLength(5, 10, 10, 11).setSeedColours(0xE73921).setSeasons(AUTUMN)
                                        .setBlocks(LOG.getDefaultState(), LEAVES.getDefaultState()).setItem(new ItemStack(Items.APPLE));

    private static Tree registerTree(String name) { return new Tree(new ResourceLocation(MODID, name)); }
}
