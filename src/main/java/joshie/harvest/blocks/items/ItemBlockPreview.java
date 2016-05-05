package joshie.harvest.blocks.items;

import joshie.harvest.api.buildings.IBuilding;
import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.core.util.base.ItemBlockHFBase;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockPreview extends ItemBlockHFBase implements ICreativeSorted {
    public ItemBlockPreview(Block block) {
        super(block);
    }

    @Override
    public int getMetadata(int meta) {
        return 0;
    }

    @Override
    public String getName(ItemStack stack) {
        if (stack.getItemDamage() >= BuildingRegistry.buildings.size()) return "invalid";
        IBuilding group = HFBlocks.PREVIEW.getBuildingFromStack(stack);
        if (group != null) {
            return group.getName();
        } else return "invalid";
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String unlocalized = block.getUnlocalizedName().replace("preview", "structures").replace("tile.", "").replace("_", ".");
        String name = getName(stack).replaceAll("(.)([A-Z])", "$1$2").toLowerCase();
        return Text.localize(unlocalized + "." + name.replace("_", "."));
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 105;
    }
}