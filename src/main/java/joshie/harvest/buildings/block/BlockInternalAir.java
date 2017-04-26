package joshie.harvest.buildings.block;

import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.buildings.item.ItemCheat.Cheat;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFBase;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.CreativeSort;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;

public class BlockInternalAir extends BlockHFBase<BlockInternalAir> {
    public BlockInternalAir() {
        super(Material.GLASS, HFTab.TOWN);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return TextHelper.localizeFully(getUnlocalizedName());
    }

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return false;
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(@Nonnull Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        if (HFCore.DEBUG_MODE) {
            super.getSubBlocks(itemIn, tab, list);
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.LAST - 1;
    }

    public static void onPlaced(World world, BlockPos pos, EntityPlayer player) {
        MinecraftForge.EVENT_BUS.register(new RemoveIfHolding(world, pos, player));
    }

    private static class RemoveIfHolding {
        private final World world;
        private final BlockPos pos;
        private final EntityPlayer player;

        RemoveIfHolding (World world, BlockPos pos, EntityPlayer player) {
            this.world = world;
            this.pos = pos;
            this.player = player;
        }

        @SubscribeEvent
        public void onPlayerTick(PlayerTickEvent event) {
            if (event.player == player && event.phase == Phase.END) {
                if (InventoryHelper.getHandItemIsIn(event.player, ITEM_STACK, HFBuildings.CHEAT.getStackFromEnum(Cheat.AIR_REMOVER)) != null) {
                    world.setBlockToAir(pos);
                    try {
                        MinecraftForge.EVENT_BUS.unregister(this);
                    } catch (Exception e) {/**/}
                }
            }
        }
    }
}
