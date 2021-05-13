package uk.joshiejack.harvestcore.item.custom;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.joshiejack.harvestcore.data.custom.item.CustomItemStarter;
import uk.joshiejack.harvestcore.world.SpreadableNotifier;
import uk.joshiejack.penguinlib.item.custom.ItemMultiSpecialCustom;

public class ItemStarter extends ItemMultiSpecialCustom<CustomItemStarter> {
    public ItemStarter(ResourceLocation registry, CustomItemStarter defaults, CustomItemStarter... data) {
        super(registry, defaults, data);
    }

    @Override
    public IBlockState getStateForPlacement(CustomItemStarter e) {
        return e.getBlock();
    }

    @Override
    protected void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState iblockstate1, EntityPlayer player, ItemStack itemstack, CustomItemStarter value) {
        SpreadableNotifier.markAsSpreadable(worldIn, pos);
    }
}
