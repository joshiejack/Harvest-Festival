package joshie.harvest.blocks;

import joshie.harvest.blocks.tiles.TileMarker;
import joshie.harvest.buildings.Building;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.General;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.lib.RenderIds;
import joshie.harvest.core.util.base.BlockHFBaseMeta;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockPreview extends BlockHFBaseMeta {
    public static final int N1_T__N2_T__SWAP_F = 0;
    public static final int N1_T__N2_T__SWAP_T = 1;
    public static final int N1_T__N2_F__SWAP_F = 2;
    public static final int N1_T__N2_F__SWAP_T = 3;
    public static final int N1_F__N2_F__SWAP_F = 4;
    public static final int N1_F__N2_F__SWAP_T = 5;
    public static final int N1_F__N2_T__SWAP_F = 6;
    public static final int N1_F__N2_T__SWAP_T = 7;

    public static boolean getN1FromMeta(int meta) {
        return meta <= 3;
    }

    public static boolean getN2FromMeta(int meta) {
        return meta <= 1 || meta >= 6;
    }

    public static boolean getSwapFromMeta(int meta) {
        return meta % 2 == 1;
    }

    public BlockPreview() {
        super(Material.WOOD, HFTab.TOWN);
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public int getRenderType() {
        return RenderIds.ALL;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public int getMetaCount() {
        return Building.buildings.size();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            int meta = world.getBlockMetadata(x, y, z);
            if (meta < 7) {
                meta++;
            } else meta = 0;
            return world.setBlockMetadataWithNotify(x, y, z, meta, 2);
        } else {
            int meta = world.getBlockMetadata(x, y, z);
            TileMarker marker = (TileMarker) world.getTileEntity(x, y, z);
            EntityNPCBuilder builder = HFTrackers.getPlayerTracker(player).getBuilder(world);
            if (builder != null && !builder.isBuilding()) {
                builder.setPosition(x, y, z); //Teleport the builder to the position
                builder.startBuilding(marker.getBuilding(), x, y, z, getN1FromMeta(meta), getN2FromMeta(meta), getSwapFromMeta(meta), UUIDHelper.getPlayerUUID(player));
                world.setBlockToAir(x, y, z);
                return true;
            } else return false;
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileMarker();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
        if (!(player instanceof EntityPlayer)) return;
        if (stack.getItemDamage() >= Building.buildings.size()) return;
        Building group = Building.buildings.get(stack.getItemDamage());
        if (group != null) {
            TileMarker marker = (TileMarker) world.getTileEntity(x, y, z);
            marker.setBuilding(group);
            //Create a builder if none exists
            if (!world.isRemote) {
                HFTrackers.getPlayerTracker((EntityPlayer) player).getBuilder(world);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        if (tab != HFTab.TOWN) return;
        if (General.DEBUG_MODE) {
            for (int i = 0; i < getMetaCount(); i++) {
                list.add(new ItemStack(item, 1, i));
            }
        }
    }
}
