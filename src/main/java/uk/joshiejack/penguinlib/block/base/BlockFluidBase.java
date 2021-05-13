package uk.joshiejack.penguinlib.block.base;

import uk.joshiejack.penguinlib.item.base.block.ItemBlockSingular;
import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockFluidBase extends net.minecraftforge.fluids.BlockFluidClassic implements IPenguinBlock {
    public BlockFluidBase(Fluid fluid, String modid, CreativeTabs tabs) {
        super(fluid, Material.WATER);
        setCreativeTab(tabs);
        RegistryHelper.registerBlock(new ResourceLocation(modid, fluidName), this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public ItemBlock createItemBlock() {
        return new ItemBlockSingular(getRegistryName(), this);
    }

    @Override
    public void getSubBlocks(CreativeTabs tabs, NonNullList<ItemStack> items) {}

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(getRegistryName(), getRegistryName().getPath()));
        final ModelResourceLocation fluidLocation = new ModelResourceLocation(getRegistryName(), fluidName);
        ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
            @Nonnull
            @Override
            protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
                return fluidLocation;
            }
        });
    }
}
