package uk.joshiejack.furniture.item;

import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.furniture.network.FurnitureGuiHandler;
import uk.joshiejack.penguinlib.item.base.ItemSingular;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemBasket extends ItemSingular {
    public ItemBasket() {
        super(new ResourceLocation(Furniture.MODID, "basket"));
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking()) {
            player.openGui(Furniture.instance, FurnitureGuiHandler.BASKET, world, 0, 0, hand.ordinal());
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        } else return super.onItemRightClick(world, player, hand);
    }
}
