package uk.joshiejack.harvestcore.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.util.Strings;
import uk.joshiejack.harvestcore.HCConfig;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.stage.StageHandler;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.data.holder.HolderRegistryList;
import uk.joshiejack.penguinlib.item.base.ItemSingular;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.Seasons;
import uk.joshiejack.seasons.data.SeasonData;
import uk.joshiejack.seasons.handlers.SeasonHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ItemSeeds extends ItemSingular {
    public static final Map<String, CropData> data = Maps.newHashMap();
    public static final HolderRegistryList SEEDS = new HolderRegistryList();

    public static void register(String name, int color, Block block, StageHandler stageHandler, SeasonHandler seasonHandler, boolean requiresWater) {
        register(name, new CropData(block, color, EnumPlantType.Crop, stageHandler, seasonHandler, requiresWater));
    }

    public static void register(String name, int color, ItemStack seeds, StageHandler stageHandler, SeasonHandler seasonHandler, boolean requiresWater) {
        register(name, new CropData(seeds, color, stageHandler, seasonHandler, requiresWater));
    }

    private static void register(String name, CropData seeds) {
        data.put(name, seeds);
        if (!seeds.seeds.isEmpty()) SEEDS.add(Holder.getFromStack(seeds.seeds)); //Woo!////////////////
        StackHelper.registerSynonym(name + "_seed_bag", HCItems.SEED_BAG.withCrop(name));
    }

    private ItemStack withCrop(String name) {
        return withCrop(new ItemStack(this), name);
    }

    public ItemStack withCrop(ItemStack stack, String name) {
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setString("Crop", name);
        return stack;
    }

    public ItemSeeds() {
        super(new ResourceLocation(HarvestCore.MODID, "seed_bag"));
        setMaxStackSize(64);
        setCreativeTab(CreativeTabs.MISC);
        setHasSubtypes(true);
    }

    @Nullable
    public CropData getSeeds(ItemStack stack) {
        if (!stack.hasTagCompound()) return null;
        return data.get(stack.getTagCompound().getString("Crop"));
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        CropData data = getSeeds(stack);
        return data != null ? data.getName() : super.getItemStackDisplayName(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag debug) {
        CropData data = getSeeds(stack);
        if (data != null) {
            if (data.requiresSickle) list.add("" + TextFormatting.AQUA + TextFormatting.ITALIC + "Requires Sickle");
            if (!data.requiresWater) list.add("" + TextFormatting.BLUE + TextFormatting.ITALIC + "No Water Required");
            for (Season season: data.seasons) {
                list.add(SeasonData.DATA.get(season).hud + StringHelper.localize(Seasons.MODID + "." + season.name().toLowerCase(Locale.ENGLISH)));
            }

            list.add(data.days + " Days");
        }
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        CropData seeds = getSeeds(player.getHeldItem(hand));
        if (seeds != null) {
            ItemStack held = player.getHeldItem(hand).copy();
            boolean placed = false;
            for (BlockPos p : BlockPos.getAllInBox(pos.north(HCConfig.seedRange).east(HCConfig.seedRange), pos.south(HCConfig.seedRange).west(HCConfig.seedRange))) {
                if (!seeds.seeds.isEmpty()) {
                    player.setHeldItem(hand, seeds.seeds.copy());
                    if (seeds.seeds.getItem().onItemUse(player, world, p, hand, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS) {
                        placed = true;
                    }

                } else {
                    ItemStack itemstack = player.getHeldItem(hand);
                    IBlockState state = world.getBlockState(p);
                    if (facing == EnumFacing.UP && player.canPlayerEdit(p.offset(facing), facing, itemstack)
                            && state.getBlock().canSustainPlant(state, world, p, EnumFacing.UP, seeds.plantable) && world.isAirBlock(p.up())) {
                        world.setBlockState(p.up(), seeds.block.getDefaultState());

                        if (player instanceof EntityPlayerMP) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, p.up(), itemstack);
                        }

                        placed = true;
                    }
                }
            }

            if (placed) {
                if (!player.capabilities.isCreativeMode) {
                    held.shrink(1); //Reduce the stack
                }
            }

            //Set back to the initial
            player.setHeldItem(hand, held.copy());
            return placed ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
        }

        return EnumActionResult.PASS;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            data.keySet().forEach(crop -> items.add(withCrop(crop)));
        }
    }

    public static class CropData {
        private final IPlantable plantable;
        private final ItemStack seeds;
        private final Block block;
        private final int color;
        private final List<Season> seasons;
        private final boolean requiresWater;
        private final boolean requiresSickle;
        private String name = Strings.EMPTY;
        private int days;


        CropData(ItemStack seeds, int color, StageHandler stageHandler, SeasonHandler seasonHandler, boolean requiresWater) {
            this.seeds = seeds;
            this.color = color;
            this.block = Blocks.AIR;
            this.plantable = null;
            this.seasons = Lists.newArrayList(seasonHandler.getSeasons());
            seasons.sort(Enum::compareTo);
            this.requiresWater = requiresWater;
            this.requiresSickle = seeds.getItem() == Items.WHEAT_SEEDS;
            this.days = stageHandler.getMaximumStage() + 1;
        }

        CropData(Block block, int color, EnumPlantType type, StageHandler stageHandler, SeasonHandler seasonHandler, boolean requiresWater) {
            this.seeds = ItemStack.EMPTY;
            this.color = color;
            this.block = block;
            this.plantable = new IPlantable() {
                @Override
                public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
                    return type;
                }

                @Override
                public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
                    return block.getDefaultState();
                }
            };

            this.seasons = Lists.newArrayList(seasonHandler.getSeasons());
            seasons.sort(Enum::compareTo);
            this.requiresWater = requiresWater;
            this.requiresSickle = block == Blocks.WHEAT;
            this.days = stageHandler.getMaximumStage() + 1;
        }

        public int getColor() {
            return color;
        }

        @SuppressWarnings("ConstantConditions")
        public String getName() {
            return name.isEmpty() ? seeds.isEmpty() ? block.getRegistryName().toString() : seeds.getDisplayName() : StringHelper.localize(name);
        }

        public void setName(String displayname) {
            this.name = displayname;
        }
    }
}
