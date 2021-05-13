package uk.joshiejack.harvestcore.item;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.registry.Blueprint;
import uk.joshiejack.penguinlib.item.base.ItemSingular;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ItemBlueprint extends ItemSingular {
    public ItemBlueprint() {
        super(new ResourceLocation(HarvestCore.MODID, "blueprint"));
        setHasSubtypes(true);
        setCreativeTab(CreativeTabs.MISC);
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        Blueprint blueprint = stack.hasTagCompound() ? Blueprint.REGISTRY.get(new ResourceLocation(Objects.requireNonNull(stack.getTagCompound()).getString("Blueprint"))) : null;
        return blueprint == null ? super.getItemStackDisplayName(stack) : "Blueprint: " + blueprint.getResult().getDisplayName();
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        Blueprint blueprint = stack.hasTagCompound() ? Blueprint.REGISTRY.get(new ResourceLocation(Objects.requireNonNull(stack.getTagCompound()).getString("Blueprint"))) : null;
        if (blueprint != null) {
            if (!world.isRemote) {
                blueprint.unlock(player);
            }

            stack.shrink(1);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    private ItemStack getBlueprintWithStack(Blueprint blueprint) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Blueprint", blueprint.getRegistryName().toString());
        ItemStack stack = new ItemStack(this);
        stack.setTagCompound(tag);
        return stack;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            Blueprint.REGISTRY.values().forEach(recipe -> items.add(getBlueprintWithStack(recipe)));
        }
    }
}
