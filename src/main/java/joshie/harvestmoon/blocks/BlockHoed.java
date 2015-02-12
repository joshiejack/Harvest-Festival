package joshie.harvestmoon.blocks;

import static joshie.harvestmoon.lib.HMModInfo.MODPATH;
import net.minecraft.block.BlockFarmland;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHoed extends BlockFarmland {
    private IIcon mine_icon;

    public BlockHoed() {
        super();
        setTickRandomly(false);
    }

    @Override
    public BlockHoed setBlockName(String name) {
        super.setBlockName(name);
        GameRegistry.registerBlock(this, "hoed");
        return this;
    }

    public static int getDirtType(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return mine_icon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        super.registerBlockIcons(register);
        mine_icon = register.registerIcon(MODPATH + ":mine_hoe");
    }
}
