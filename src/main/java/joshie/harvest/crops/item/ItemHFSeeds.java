package joshie.harvest.crops.item;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.trees.Tree;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.interfaces.ICreativeSorted;
import joshie.harvest.crops.HFCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static net.minecraft.init.Blocks.FARMLAND;

public class ItemHFSeeds extends ItemSeeds implements ICreativeSorted {
    public ItemHFSeeds() {
        super(HFCrops.CROPS, FARMLAND);
        setCreativeTab(HFTab.FARMING);
    }

    @Override
    public int getSortValue(@Nonnull ItemStack stack) {
        return CreativeSort.SEEDS;
    }

    @Override
    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        Crop crop = getCropFromStack(stack);
        return (crop == null) ? TextHelper.translate("crop.seeds.useless") : crop.getSeedsName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(@Nonnull ItemStack stack, EntityPlayer player, List<String> list, boolean debug) {
        Crop crop = getCropFromStack(stack);
        if (crop != null) {
            if (crop.requiresSickle()) list.add("" + TextFormatting.AQUA + TextFormatting.ITALIC + TextHelper.translate("crop.sickle"));
            if (!crop.requiresWater()) list.add("" + TextFormatting.BLUE + TextFormatting.ITALIC + TextHelper.translate("crop.water"));
            crop.getGrowthHandler().addInformation(list, crop, debug);
            int amount = crop instanceof Tree ? ((Tree)crop).getStagesToMaturity() : crop.getStages();
            list.add(amount + " " + TextHelper.translate("crop.seeds.days"));
        }
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        } else {
            ItemStack stack = player.getHeldItem(hand);
            Crop crop = getCropFromStack(stack);
            if (crop != null) {
                int planted = 0;
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        planted = plantSeedAt(player, stack, world, pos.add(x, 1, z), facing, crop, planted, pos.up());
                    }
                }

                if (planted > 0) {
                    stack.shrink(1);
                    return EnumActionResult.SUCCESS;
                } else {
                    return EnumActionResult.PASS;
                }
            }

            return EnumActionResult.FAIL;
        }
    }

    @SuppressWarnings("unchecked")
    private int plantSeedAt(EntityPlayer player, @Nonnull ItemStack stack, World world, BlockPos pos, EnumFacing facing, Crop crop, int planted, BlockPos original) {
        if (player.canPlayerEdit(pos, facing, stack) && player.canPlayerEdit(pos.up(), facing, stack) && world.isAirBlock(pos)) {
            IBlockState down = world.getBlockState(pos.down());
            if (crop.getGrowthHandler().canPlantSeedAt(world, pos, down, crop, original)) {
                HFApi.crops.plantCrop(player, world, pos, crop, 1);
                planted++;
            }
        }

        return planted;
    }

    @Nonnull
    public ItemStack getStackFromCrop(Crop crop) {
        return getStackFromCrop(crop, 1);
    }

    @Nonnull
    public ItemStack getStackFromCrop(Crop crop, int amount) {
        ItemStack stack = new ItemStack(this, amount);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Crop", crop.getResource().toString());
        stack.setTagCompound(tag);
        return stack;
    }

    public Crop getCropFromStack(@Nonnull ItemStack stack) {
        if (stack.getTagCompound() == null || !stack.getTagCompound().hasKey("Crop")) {
            return Crop.NULL_CROP;
        } else return Crop.REGISTRY.get(new ResourceLocation(stack.getTagCompound().getString("Crop")));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        list.addAll(Crop.REGISTRY.values().stream().filter(crop -> crop != Crop.NULL_CROP && crop.getCropStack(1).getItem() != Items.BRICK).map(this::getStackFromCrop).collect(Collectors.toList()));
    }

    public ItemHFSeeds register(String name) {
        setTranslationKey(name.replace("_", "."));
        setRegistryName(new ResourceLocation(MODID, name));
        GameData.register_impl(this);
        return this;
    }
}