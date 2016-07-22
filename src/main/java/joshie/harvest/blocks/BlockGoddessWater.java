package joshie.harvest.blocks;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.handlers.GoddessHandler;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class BlockGoddessWater extends BlockFluidClassic {
    public BlockGoddessWater(Fluid fluid) {
        super(fluid, Material.WATER);
    }

    @Override
    public FluidStack drain(World world, BlockPos pos, boolean doDrain) {
        return null;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!world.isRemote && entity instanceof EntityItem) {
            ItemStack stack = ((EntityItem)entity).getEntityItem();
            if (stack != null) {
                boolean spawn = false;
                if (stack.getItem() instanceof ItemBlock) {
                    Block block = Block.getBlockFromItem(stack.getItem());
                    if (block instanceof BlockCrops || block instanceof BlockLeaves || block instanceof BlockBush) {
                        spawn = true;
                    }
                } else if (HFApi.crops.getCropFromStack(stack) != null) {
                    spawn = true;
                }

                if (spawn) {
                    GoddessHandler.spawnGoddess(world, entity);
                    entity.setDead();
                }
            }
        }
    }

    public BlockGoddessWater register(String name) {
        setUnlocalizedName(name.replace("_", "."));
        setRegistryName(new ResourceLocation(MODID, name));
        GameRegistry.register(this);
        ItemBlock item = new ItemBlock(this);
        item.setUnlocalizedName(name.replace("_", "."));
        item.setRegistryName(new ResourceLocation(MODID, name));
        GameRegistry.register(item);
        RegistryHelper.registerFluidBlockRendering(this, name);
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(item));
    }
}