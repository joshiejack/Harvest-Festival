package joshie.harvest.core.base.block;

import joshie.harvest.core.HFTab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockHFEnumCube<B extends BlockHFEnumCube, E extends Enum<E> & IStringSerializable> extends BlockHFEnum<B, E> {
    //Main Constructor
    public BlockHFEnumCube(Material material, Class<E> clazz, CreativeTabs tab) {
        super(material, clazz, tab);
    }

    //Constructor default to farming tab
    public BlockHFEnumCube(Material material, Class<E> clazz) {
        this(material, clazz, HFTab.FARMING);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.SOLID;
    }

    @Override
    public boolean isFullCube(IBlockState blockState) {
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return true;
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return true;
    }
}