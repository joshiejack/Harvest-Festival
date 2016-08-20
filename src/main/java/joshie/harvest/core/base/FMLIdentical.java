package joshie.harvest.core.base;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FMLIdentical implements ItemMeshDefinition {
    private ModelResourceLocation model;

    public FMLIdentical(ItemHFFML item) {
        model = new ModelResourceLocation(item.getRegistryName(), "inventory");
        ModelBakery.registerItemVariants(item, model);
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
        return model;
    }
}
