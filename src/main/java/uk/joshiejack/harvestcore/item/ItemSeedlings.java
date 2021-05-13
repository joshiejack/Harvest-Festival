package uk.joshiejack.harvestcore.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.data.holder.HolderRegistryList;
import uk.joshiejack.penguinlib.item.base.ItemSingular;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.Seasons;
import uk.joshiejack.seasons.data.SeasonData;
import uk.joshiejack.seasons.handlers.SeasonHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.util.Strings;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ItemSeedlings extends ItemSingular {
    public static final Map<String, TreeData> data = Maps.newHashMap();
    public static final HolderRegistryList SEEDLINGS = new HolderRegistryList();

    public static void register(String name, int color, ItemStack seeds, IBlockState seedling, int days, @Nullable SeasonHandler seasonHandler) {
        register(name, new TreeData(seeds, seedling, color, days, seasonHandler));
    }

    private static void register(String name, TreeData tree) {
        data.put(name, tree);
        if (!tree.sapling.isEmpty()) SEEDLINGS.add(Holder.getFromStack(tree.sapling)); //Woo!////////////////
        StackHelper.registerSynonym(name + "_seedling_bag", HCItems.SEEDLING_BAG.withTree(name));
    }

    private ItemStack withTree(String name) {
        return withTree(new ItemStack(this), name);
    }

    public ItemStack withTree(ItemStack stack, String name) {
        stack.setTagCompound(new NBTTagCompound());
        assert stack.getTagCompound() != null;
        stack.getTagCompound().setString("Tree", name);
        return stack;
    }

    public ItemSeedlings() {
        super(new ResourceLocation(HarvestCore.MODID, "seedling_bag"));
        setMaxStackSize(64);
        setCreativeTab(CreativeTabs.MISC);
        setHasSubtypes(true);
    }

    @Nullable
    public TreeData getSapling(ItemStack stack) {
        if (stack.getTagCompound() == null) return null;
        return data.get(stack.getTagCompound().getString("Tree"));
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        TreeData data = getSapling(stack);
        return data != null ? data.getName() : super.getItemStackDisplayName(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, World world, @Nonnull List<String> list, @Nonnull ITooltipFlag debug) {
        TreeData data = getSapling(stack);
        if (data != null) {
            for (Season season : data.seasons) {
                list.add(SeasonData.DATA.get(season).hud + StringHelper.localize(Seasons.MODID + "." + season.name().toLowerCase(Locale.ENGLISH)));
            }

            list.add(data.days + " Days");
        }
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos,
                                      @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        TreeData seeds = getSapling(player.getHeldItem(hand));
        if (seeds != null) {
            IBlockState iblockstate = world.getBlockState(pos);
            Block block = iblockstate.getBlock();
            if (!block.isReplaceable(world, pos)) {
                pos = pos.offset(facing);
            }

            ItemStack itemstack = player.getHeldItem(hand);
            if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && world.mayPlace(seeds.seedling.getBlock(), pos, false, facing, player)) {
                IBlockState iblockstate1 = seeds.seedling;
                if (placeBlockAt(itemstack, player, world, pos, iblockstate1)) {
                    iblockstate1 = world.getBlockState(pos);
                    SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, world, pos, player);
                    world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    itemstack.shrink(1);
                }

                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.FAIL;
            }
        }

        return EnumActionResult.PASS;
    }

    private boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, IBlockState newState) {
        if (!world.setBlockState(pos, newState, 11)) return false;
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == newState.getBlock()) {
            newState.getBlock().onBlockPlacedBy(world, pos, state, player, stack);

            if (player instanceof EntityPlayerMP)
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, stack);
        }

        return true;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            data.keySet().forEach(crop -> items.add(withTree(crop)));
        }
    }

    public static class TreeData {
        private final ItemStack sapling;
        private final IBlockState seedling;
        private final int color;
        private final List<Season> seasons;
        private String name = Strings.EMPTY;
        private final int days;


        TreeData(ItemStack sapling, IBlockState seedling, int color, int days, @Nullable SeasonHandler seasonHandler) {
            this.sapling = sapling;
            this.color = color;
            this.seedling = seedling;
            this.seasons = seasonHandler != null ? Lists.newArrayList(seasonHandler.getSeasons()) : Lists.newArrayList();
            seasons.sort(Enum::compareTo);
            this.days = days + 1;
        }

        public int getColor() {
            return color;
        }

        @SuppressWarnings("ConstantConditions")
        public String getName() {
            return name.isEmpty() ? sapling.isEmpty() ? seedling.getBlock().getRegistryName().toString() : sapling.getDisplayName() : StringHelper.localize(name);
        }

        public void setName(String displayname) {
            this.name = displayname;
        }
    }
}
