package joshie.harvest.crops.item;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.interfaces.ICreativeSorted;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.crops.HFCrops;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static net.minecraft.init.Blocks.FARMLAND;

public class ItemHFSeeds extends ItemSeeds implements ICreativeSorted {
    public ItemHFSeeds() {
        super(HFCrops.CROPS, FARMLAND);
        setCreativeTab(HFTab.FARMING);
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
    public String getItemStackDisplayName(ItemStack stack) {
        Crop crop = getCropFromStack(stack);
        return (crop == null) ? TextHelper.translate("crop.seeds.useless") : crop.getSeedsName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean debug) {
        Crop crop = getCropFromStack(stack);
        if (crop != null) {
            if (crop.requiresSickle()) list.add("" + TextFormatting.AQUA + TextFormatting.ITALIC + TextHelper.translate("crop.sickle"));
            crop.getGrowthHandler().addInformation(list, crop, debug);
            list.add(crop.getStages() + " " + TextHelper.translate("crop.seeds.days"));
        }
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        } else {
            Crop crop = getCropFromStack(stack);
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
                                if (HFCrops.ALWAYS_GROW) {
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

    private int plantSeedAt(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing facing, Crop crop, int planted) {
        if (player.canPlayerEdit(pos, facing, stack) && player.canPlayerEdit(pos.up(), facing, stack)) {
            if (crop.getGrowthHandler().canSustainCrop(world, pos, world.getBlockState(pos), crop) && world.isAirBlock(pos.up())) {
                if (!world.isRemote) {
                    world.setBlockState(pos.up(), HFCrops.CROPS.getDefaultState(), 2);
                }

                HFApi.crops.plantCrop(player, world, pos.up(), crop, 1);
                planted++;

                if (HFCrops.ALWAYS_GROW) {
                    if (planted >= 2) {
                        return -1;
                    }
                }
            }
        }
        return planted;
    }

    public ItemStack getStackFromCrop(Crop crop) {
        return new ItemStack(this, 1, Crop.REGISTRY.getValues().indexOf(crop));
    }

    public Crop getCropFromStack(ItemStack stack) {
        int id = Math.max(0, Math.min(Crop.REGISTRY.getValues().size() - 1, stack.getItemDamage()));
        return Crop.REGISTRY.getValues().get(id);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (Crop crop : Crop.REGISTRY.getValues()) {
            if (crop != Crop.NULL_CROP) {
                list.add(getStackFromCrop(crop));
            }
        }
    }

    public ItemHFSeeds register(String name) {
        setUnlocalizedName(name.replace("_", "."));
        setRegistryName(new ResourceLocation(MODID, name));
        GameRegistry.register(this);
        return this;
    }
}