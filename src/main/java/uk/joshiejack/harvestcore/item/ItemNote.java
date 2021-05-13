package uk.joshiejack.harvestcore.item;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.registry.Note;
import uk.joshiejack.penguinlib.item.base.ItemSingular;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ItemNote extends ItemSingular {
    public ItemNote() {
        super(new ResourceLocation(HarvestCore.MODID, "note"));
        setHasSubtypes(true);
        setCreativeTab(CreativeTabs.MISC);
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        Note note = stack.hasTagCompound() ? Note.REGISTRY.get(new ResourceLocation(Objects.requireNonNull(stack.getTagCompound()).getString("Note"))) : null;
        return note == null ? super.getItemStackDisplayName(stack) : "Note: " + note.getLocalizedName();
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        Note note = stack.hasTagCompound() ? Note.REGISTRY.get(new ResourceLocation(Objects.requireNonNull(stack.getTagCompound()).getString("Note"))) : null;
        if (note != null) {
            if (!world.isRemote) {
                note.unlock(player);
            }

            stack.shrink(1);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    private ItemStack getNoteWithStack(Note note) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Note", note.getRegistryName().toString());
        ItemStack stack = new ItemStack(this);
        stack.setTagCompound(tag);
        return stack;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            Note.REGISTRY.values().forEach(recipe -> items.add(getNoteWithStack(recipe)));
        }
    }
}
