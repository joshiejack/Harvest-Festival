package joshie.harvest.core.base.render;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class MeshIdentical implements ItemMeshDefinition {
    private final ModelResourceLocation model;

    public MeshIdentical(Item item) {
        model = new ModelResourceLocation(item.getRegistryName(), "inventory");
        ModelBakery.registerItemVariants(item, model);
    }

    @Override
    @Nonnull
    public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
        return model;
    }
}
