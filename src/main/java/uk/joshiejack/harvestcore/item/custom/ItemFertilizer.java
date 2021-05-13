package uk.joshiejack.harvestcore.item.custom;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.data.custom.item.CustomItemFertilizer;
import uk.joshiejack.harvestcore.registry.Fertilizer;
import uk.joshiejack.harvestcore.ticker.crop.SoilTicker;
import uk.joshiejack.penguinlib.item.custom.ItemMultiCustom;
import uk.joshiejack.penguinlib.ticker.DailyTicker;
import uk.joshiejack.penguinlib.ticker.TickerHelper;

import javax.annotation.Nonnull;

public class ItemFertilizer extends ItemMultiCustom<CustomItemFertilizer> {
    public ItemFertilizer(ResourceLocation registry, CustomItemFertilizer defaults, CustomItemFertilizer... data) {
        super(registry, defaults, data);
    }

    public static EnumActionResult applyFertilizer(EntityPlayer player, EnumHand hand, BlockPos pos) {
        if (player != null && !player.world.isRemote) {
            DailyTicker entry = TickerHelper.getTicker(player.world, pos);
            if (entry != null && entry.getClass() == SoilTicker.class && player.world.isAirBlock(pos.up())) {
                ItemStack held = player.getHeldItem(hand);
                SoilTicker soil = ((SoilTicker) entry);
                if (soil.getFertilizer().equals(Fertilizer.NONE)) {
                    Fertilizer fertilizer = getFertilizerFromStack(held);
                    soil.setFertilizer((WorldServer) player.world, pos, fertilizer);
                    held.shrink(1);

                    return EnumActionResult.SUCCESS;
                }
            }
        }

        return EnumActionResult.PASS;
    }

    @SuppressWarnings("ConstantConditions")
    @Nonnull
    private static Fertilizer getFertilizerFromStack(ItemStack stack) {
        if (stack.getItem() instanceof ItemFertilizer) {
           return  ((ItemFertilizer)stack.getItem()).getDataFromStack(stack).getFertilizer();
        } else return getBonemeal();
    }

    @Nonnull
    public static Fertilizer getBonemeal() {
        return Fertilizer.REGISTRY.getOrDefault(new ResourceLocation(HarvestCore.MODID, "bonemeal"), Fertilizer.NONE);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return applyFertilizer(player, hand, pos);
    }

    @Nonnull
    protected ItemStack getCreativeStack(CustomItemFertilizer fertilizer) {
        return fertilizer.getFertilizer() == getBonemeal() ? ItemStack.EMPTY : super.getCreativeStack(fertilizer);
    }
}
