package uk.joshiejack.horticulture.item;

import uk.joshiejack.horticulture.Horticulture;
import uk.joshiejack.horticulture.block.BlockCrop;
import uk.joshiejack.horticulture.block.BlockTrellis;
import uk.joshiejack.penguinlib.item.base.ItemMultiPlantable;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nonnull;

import static uk.joshiejack.horticulture.item.ItemCrop.Crops.SWEET_POTATO;

public class ItemSeeds extends ItemMultiPlantable<ItemCrop.Crops> {
    public ItemSeeds() {
        super(new ResourceLocation(Horticulture.MODID, "seeds"), ItemCrop.Crops.class);
        setCreativeTab(Horticulture.TAB);
    }

    @Override
    protected String getPrefixFromRegistryName(ResourceLocation registry) {
        return registry.getNamespace() + ".item.crop.";
    }

    @Override
    protected IBlockState getStateForPlacement(EntityPlayer player, ItemStack stack) {
        ItemCrop.Crops crop = getEnumFromStack(stack);
        if (crop.getType() == ItemCrop.SeedType.TRELLIS) {
            BlockTrellis trellis = (BlockTrellis) BlockCrop.BLOCKS.get(crop);
            EnumFacing facing = EntityHelper.getFacingFromEntity(player);
            return facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH ? trellis.getNS().getDefaultState() : trellis.getEW().getDefaultState();
        } else return getStateFromEnum(crop);
    }

    @Override
    protected IBlockState getStateFromEnum(ItemCrop.Crops crop) {
        return BlockCrop.BLOCKS.get(crop).getDefaultState();
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        ItemCrop.Crops crop = getEnumFromStack(stack);
        if (crop.getType()== ItemCrop.SeedType.TRELLIS) return I18n.translateToLocalFormatted("horticulture.item.starter.name", super.getItemStackDisplayName(stack));
        else if (crop == SWEET_POTATO) return I18n.translateToLocalFormatted("horticulture.item.slips.name", super.getItemStackDisplayName(stack));
        else return I18n.translateToLocalFormatted("horticulture.item.seeds.name", super.getItemStackDisplayName(stack));
    }

    @Override
    @Nonnull
    public String getTranslationKey(ItemStack stack) {
        return prefix + getEnumFromStack(stack).getName();
    }

    @Nonnull
    protected ItemStack getCreativeStack(ItemCrop.Crops crops) {
        return crops.getType() == ItemCrop.SeedType.SAPLING ? ItemStack.EMPTY : super.getCreativeStack(crops);
    }
}
