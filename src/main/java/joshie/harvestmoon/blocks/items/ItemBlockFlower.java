package joshie.harvestmoon.blocks.items;

import joshie.harvestmoon.blocks.BlockFlower;
import joshie.harvestmoon.core.util.base.ItemBlockBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockFlower extends ItemBlockBase {
    public ItemBlockFlower(Block block) {
        super(block);
    }

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case BlockFlower.GODDESS:
                return "goddess";
            default:
                return "invalid";
        }
    }

    @Override
    public int getEntityLifespan(ItemStack stack, World world) {
        return stack.getItemDamage() == BlockFlower.GODDESS ? 60 : super.getEntityLifespan(stack, world);
    }
}
