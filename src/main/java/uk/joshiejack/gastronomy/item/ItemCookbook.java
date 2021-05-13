package uk.joshiejack.gastronomy.item;

import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.penguinlib.item.base.ItemSingular;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static uk.joshiejack.gastronomy.Gastronomy.MODID;

public class ItemCookbook extends ItemSingular {
    public ItemCookbook() {
        super(new ResourceLocation(MODID, "cookbook"));
        setCreativeTab(Gastronomy.TAB);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        if (!player.isSneaking()) {
            player.openGui(Gastronomy.instance, 0, world, 0, 0, 0);
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        } else return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}
