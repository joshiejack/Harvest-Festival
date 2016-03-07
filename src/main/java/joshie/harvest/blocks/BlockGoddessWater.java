package joshie.harvest.blocks;

import java.util.Random;

import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.generic.IHasMetaBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGoddessWater extends BlockFluidClassic {
    public static IIcon still;
    public static IIcon flowing;

    public BlockGoddessWater(Fluid fluid) {
        super(fluid, Material.water);
        quantaPerBlock = 8;
        quantaPerBlockFloat = 8;
    }

    @Override
    public FluidStack drain(World world, int x, int y, int z, boolean doDrain) {
        return null;
    }

    private int tick;

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        super.updateTick(world, x, y, z, rand);
        tick++;

        if (tick % 15 == 0 && !world.isRemote && world instanceof WorldServer) {
            
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta == 0 ? still : flowing;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        flowing = iconRegister.registerIcon(HFModInfo.MODPATH + ":goddess_flow");
        still = iconRegister.registerIcon(HFModInfo.MODPATH + ":goddess_still");
    }
    
    @Override
    public BlockGoddessWater setBlockName(String name) {
        super.setBlockName(name);
        String register = name.replace(".", "_");
        if(this instanceof IHasMetaBlock) {
            Class clazz = ((IHasMetaBlock) this).getItemClass();
            if(clazz == null) {
                String pack = this.getClass().getPackage().getName() + ".items.";
                String thiz = "Item" + this.getClass().getSimpleName();
                try {
                    clazz = (Class<? extends ItemBlock>) Class.forName(pack + thiz);
                } catch (Exception e) {}
            }
            
            GameRegistry.registerBlock(this, clazz, register);
        } else GameRegistry.registerBlock(this, register);
        
        return this;
    }
}