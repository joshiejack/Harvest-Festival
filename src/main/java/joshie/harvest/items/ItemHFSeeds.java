package joshie.harvest.items;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.Crops;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.SeedHelper;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.crops.Crop;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@Optional.Interface(modid = "AgriCraft", iface = "com.InfinityRaider.AgriCraft.farming.ICropOverridingSeed")
public class ItemHFSeeds extends ItemSeeds implements ICreativeSorted {

    public ItemHFSeeds() {
        super(HFBlocks.CROPS, Blocks.FARMLAND);
        setCreativeTab(HFTab.FARMING);
        setHasSubtypes(true);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.SEEDS;
    }

    @Override
    public Item setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        RegistryHelper.registerItem(this, name);
        return this;
    }

    public int getMetaCount() {
        return Crop.CROPS.size();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            return "Corrupted Seeds, Yo!";
        }

        ICrop crop = SeedHelper.getCropFromSeed(stack);
        return (crop == null) ? "Bloody Useless Seeds" : crop.getSeedsName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean debug) {
        if (!stack.hasTagCompound()) return;
        ICrop crop = SeedHelper.getCropFromSeed(stack);
        if (crop != null) {
            for (Season season : crop.getSeasons()) {
                ISeasonData data = HFApi.CALENDAR.getDataForSeason(season);
                list.add(data.getTextColor() + data.getLocalized());
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP || !stack.hasTagCompound()) {
            return EnumActionResult.FAIL;
        } else {
            ICrop crop = SeedHelper.getCropFromSeed(stack);
            if (crop != null) {
                int planted = 0;
                if (player.isSneaking()) {
                    planted = plantSeedAt(player, stack, world, pos, facing, crop, planted);
                } else {
                    labelTop:
                    for (int x = pos.getX() - 1; x <= pos.getX() + 1; x++) {
                        for (int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++) {
                            if (crop.growsToSide() == null || !((x == pos.getX() && z == pos.getZ()))) {
                                planted = plantSeedAt(player, stack, world, new BlockPos(x, pos.getY(), z), facing, crop, planted);
                            }

                            if (planted < 0) {
                                if (Crops.ALWAYS_GROW) {
                                    planted = 2;
                                    break labelTop;
                                }
                            }
                        }
                    }
                }

                if (planted > 0) {
                    --stack.stackSize;
                    return EnumActionResult.SUCCESS;
                } else {
                    return EnumActionResult.PASS;
                }
            }

            return EnumActionResult.FAIL;
        }
    }

    private int plantSeedAt(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing facing, ICrop crop, int planted) {
        if (player.canPlayerEdit(pos, facing, stack) && player.canPlayerEdit(pos.up(), facing, stack)) {
            IBlockState soil = world.getBlockState(pos.down());
            if (crop.getSoilHandler().canSustainPlant(soil, world, pos.up(), (IPlantable) HFBlocks.CROPS) && world.isAirBlock(pos.up())) {
                HFTrackers.getCropTracker().plantCrop(player, world, pos.up(), crop, 1);

                if (!world.isRemote) {
                    world.setBlockState(pos.up(), HFBlocks.CROPS.getDefaultState());
                }

                planted++;

                if (Crops.ALWAYS_GROW) {
                    if (planted >= 2) {
                        return -1;
                    }
                }
            }
        }
        return planted;
    }

    /*@Override //Moved in vanilla
    public int getColorFromItemStack(ItemStack stack, int pass) {
        if (!stack.hasTagCompound()) return super.getColorFromItemStack(stack, pass);
        ICrop crop = SeedHelper.getCropFromSeed(stack);
        if (pass == 0 && crop != null) return crop.getColor();
        else return super.getColorFromItemStack(stack, pass);
    }*/

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (ICrop crop : Crop.CROPS) {
            list.add(SeedHelper.getSeedsFromCrop(crop));
        }
    }

    /*//Agricraft
    @Optional.Method(modid = "AgriCraft")
    @Override
    public CropOverride getOverride(TileEntityCrop crop) {
        return HFAgricraftOverride.getCropOverride(crop);
    }

    @Optional.Method(modid = "AgriCraft")
    @Override
    public boolean hasGrowthRequirement() {
        return false;
    }

    @Optional.Method(modid = "AgriCraft")
    @Override
    public GrowthRequirement getGrowthRequirement() {
        return null;
    }*/
}