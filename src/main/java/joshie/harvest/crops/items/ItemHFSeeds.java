package joshie.harvest.crops.items;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.Crops;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.SeedHelper;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.generic.Text;
import joshie.harvest.crops.Crop;
import joshie.harvest.crops.CropRegistry;
import joshie.harvest.crops.HFCrops;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static net.minecraft.init.Blocks.FARMLAND;

public class ItemHFSeeds extends ItemSeeds implements ICreativeSorted {

    public ItemHFSeeds() {
        super(HFCrops.CROPS, FARMLAND);
        setCreativeTab(HFTab.FARMING);
        setMaxDamage(Short.MAX_VALUE); //You know, max damage yo
        setHasSubtypes(true);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
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

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        ICrop crop = SeedHelper.getCropFromSeed(stack);
        return (crop == null) ? Text.translate("crop.seeds.useless") : crop.getSeedsName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean debug) {
        ICrop crop = SeedHelper.getCropFromSeed(stack);
        if (crop != null) {
            for (Season season : crop.getSeasons()) {
                ISeasonData data = HFApi.calendar.getDataForSeason(season);
                list.add(data.getTextColor() + data.getLocalized());
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP) {
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
                                if (Crops.alwaysGrow) {
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
            if (crop.getSoilHandler().canSustainCrop(world, pos, world.getBlockState(pos), crop) && world.isAirBlock(pos.up())) {
                HFTrackers.getCropTracker(world).plantCrop(player, pos.up(), crop, 1);

                if (!world.isRemote) {
                    world.setBlockState(pos.up(), HFCrops.CROPS.getDefaultState());
                }

                planted++;

                if (Crops.alwaysGrow) {
                    if (planted >= 2) {
                        return -1;
                    }
                }
            }
        }
        return planted;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (Crop crop : CropRegistry.REGISTRY.getValues()) {
            list.add(SeedHelper.getSeedsFromCrop(crop));
        }
    }
}