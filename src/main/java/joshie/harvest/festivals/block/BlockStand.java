package joshie.harvest.festivals.block;

import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.festivals.block.BlockStand.Stand;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFEnumRotatableTile;
import joshie.harvest.festivals.tile.TileStand;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockStand extends BlockHFEnumRotatableTile<BlockStand, Stand> {
    public enum Stand implements IStringSerializable {
        COOKING, CHICKEN, LIVESTOCK, POT;

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }

    public BlockStand() {
        super(Material.PISTON, Stand.class, HFTab.TOWN);
        setHardness(2.5F);
        setSoundType(SoundType.WOOD);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileStand) {
            TileStand stand = ((TileStand)tile);
            if (stand.isEmpty() && held != null && held.getItem() instanceof ItemFood && stand.setContents(player, held)) {
                return true;
            } else if (!stand.isEmpty()) {
                ItemStack contents = stand.removeContents();
                if (contents != null) {
                    SpawnItemHelper.addToPlayerInventory(player, contents);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileStand();
    }
}
