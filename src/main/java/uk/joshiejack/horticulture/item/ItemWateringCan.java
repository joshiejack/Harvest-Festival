package uk.joshiejack.horticulture.item;

import uk.joshiejack.horticulture.Horticulture;
import uk.joshiejack.horticulture.HorticultureConfig;
import uk.joshiejack.penguinlib.item.base.ItemBaseWateringCan;
import uk.joshiejack.penguinlib.util.helpers.minecraft.WorldHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWateringCan extends ItemBaseWateringCan {
    public ItemWateringCan() {
        super(new ResourceLocation(Horticulture.MODID, "watering_can"), 64);
        setCreativeTab(Horticulture.TAB);
    }

    @Override
    protected void applyBonemealEffect(World world, BlockPos pos, EntityPlayer player, ItemStack itemstack, EnumHand hand) {
        if (HorticultureConfig.bonemealWateringCan) {
            if (world.rand.nextInt(16) == 0 && WorldHelper.applyBonemealAffectNoStackShrinkage(itemstack, world, pos, player, hand)) {
                if (!world.isRemote) {
                    world.playEvent(2005, pos, 0);
                }
            }
        }
    }
}
