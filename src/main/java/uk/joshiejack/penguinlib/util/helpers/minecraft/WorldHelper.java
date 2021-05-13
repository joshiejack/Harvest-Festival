package uk.joshiejack.penguinlib.util.helpers.minecraft;

import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class WorldHelper {

    public static boolean applyBonemealAffectNoStackShrinkage(ItemStack stack, World worldIn, BlockPos target, EntityPlayer player, @javax.annotation.Nullable EnumHand hand) {
        IBlockState iblockstate = worldIn.getBlockState(target);

        int hook = net.minecraftforge.event.ForgeEventFactory.onApplyBonemeal(player, worldIn, target, iblockstate, stack, hand);
        if (hook != 0) return hook > 0;

        if (iblockstate.getBlock() instanceof IGrowable) {
            IGrowable igrowable = (IGrowable) iblockstate.getBlock();

            if (igrowable.canGrow(worldIn, target, iblockstate, worldIn.isRemote)) {
                if (!worldIn.isRemote) {
                    if (igrowable.canUseBonemeal(worldIn, worldIn.rand, target, iblockstate)) {
                        igrowable.grow(worldIn, worldIn.rand, target, iblockstate);
                    }
                }

                return true;
            }
        }

        return false;
    }

    public static boolean isBihourly(TickEvent.WorldTickEvent event) {
        return !event.world.isRemote && event.phase == TickEvent.Phase.END && event.world.getWorldTime() % TimeHelper.scaleTime(2000) == 1;
    }

    public static boolean snows(World world, BlockPos pos) {
        float biomeTemperature = world.getBiome(pos).getTemperature(pos);
        int rainHeight = world.getPrecipitationHeight(pos).getY();
        return world.getBiomeProvider().getTemperatureAtHeight(biomeTemperature, rainHeight) < 0.15F;
    }

}
