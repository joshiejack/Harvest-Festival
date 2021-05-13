package uk.joshiejack.penguinlib.block.base;

import uk.joshiejack.penguinlib.item.base.block.ItemBlockSingular;
import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import net.minecraft.block.BlockBed;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

public class BlockBedBase extends BlockBed implements IPenguinBlock {
    public BlockBedBase(ResourceLocation registry) {
        RegistryHelper.registerBlock(registry, this);
    }

    @Override
    public ItemBlock createItemBlock() {
        return new ItemBlockSingular(getRegistryName(), this);
    }

    @Override
    public void registerModels(Item item) {}
}
