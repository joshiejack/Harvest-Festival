package joshie.harvest.crops;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.ICropRegistry;
import joshie.harvest.api.crops.WateringHandler;
import joshie.harvest.core.handlers.DisableHandler;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.core.util.holders.ItemStackHolder;
import joshie.harvest.crops.tile.TileWithered;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static joshie.harvest.crops.block.BlockHFCrops.CropType.FRESH;
import static joshie.harvest.crops.block.BlockHFCrops.CropType.FRESH_DOUBLE;

@HFApiImplementation
public class CropRegistry implements ICropRegistry {
    public static final CropRegistry INSTANCE = new CropRegistry();
    public final Set<WateringHandler> wateringHandlers = new HashSet<>();
    public final HashMap<IBlockState, IBlockState> farmlandToDirtMap = new HashMap<>();
    private final HashMap<ItemStackHolder, Crop> providers = new HashMap<>();
    private final Set<ItemStackHolder> sickles = new HashSet<>();
    private static final NonNullList<ItemStack> EMPTY = NonNullList.create();

    @Override
    public BlockStateContainer getStateContainer(PropertyInteger stages) {
        return new BlockStateContainer(HFCrops.CROPS, HFCrops.CROPS.property, stages);
    }

    @Override
    @Nonnull
    public ItemStack getSeedStack(Crop crop, int amount) {
        return HFCrops.SEEDS.getStackFromCrop(crop, amount);
    }

    @Override
    public void registerSeedForBlacklisting(@Nonnull ItemStack item) {
        DisableHandler.SEEDS_BLACKLIST.register(item);
    }

    @Override
    public void registerBlockForSeedRemoval(Block block) {
        DisableHandler.GRASS.add(block);
    }

    @Override
    public void registerCropProvider(@Nonnull ItemStack stack, Crop crop) {
        providers.put(ItemStackHolder.of(stack), crop);
    }

    @Override
    public void registerSickle(@Nonnull ItemStack stack) {
        sickles.add(ItemStackHolder.of(stack));
    }

    @Override
    public boolean isSickle(@Nonnull ItemStack stack) {
        return sickles.contains(ItemStackHolder.of(stack)) || sickles.contains(ItemStackHolder.of(stack.getItem(), OreDictionary.WILDCARD_VALUE));
    }

    public List<ItemStack> getStacksForCrop(Crop crop) {
        List<ItemStack> list = new ArrayList<>();
        list.add(crop.getCropStack(1));
        ItemStackHolder holder = ItemStackHolder.of(crop.getCropStack(1));
        providers.entrySet().stream().filter(entry -> entry.getValue().equals(crop))
                .forEach(entry -> list.addAll(entry.getKey().getMatchingStacks().stream().filter(stack -> !holder.matches(stack)).collect(Collectors.toList())));
        return list;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Crop getCropFromStack(@Nonnull ItemStack stack) {
        Crop crop = providers.get(ItemStackHolder.of(stack.getItem(), OreDictionary.WILDCARD_VALUE));
        return crop != null ? crop : providers.get(ItemStackHolder.of(stack));
    }

    @Override
    public Crop getCropAtLocation(World world, BlockPos pos) {
        CropData data = CropHelper.getCropDataAt(world, pos);
        return data != null ? data.getCrop() : null;
    }

    @Override
    public void plantCrop(@Nullable EntityPlayer player, World world, BlockPos pos, Crop theCrop, int stage) {
        world.setBlockState(pos, HFCrops.CROPS.getStateFromEnum(FRESH), 2);
        if (theCrop.isCurrentlyDouble(stage)) {
            world.setBlockState(pos.up(), HFCrops.CROPS.getStateFromEnum(FRESH_DOUBLE));
        }

        TileWithered tile = (TileWithered) world.getTileEntity(pos);
        if (tile != null) {
            tile.getData().setCrop(theCrop, stage);
            tile.saveAndRefresh();
        }
    }

    @Override
    public NonNullList<ItemStack> harvestCrop(@Nullable EntityPlayer player, World world, BlockPos pos) {
        TileWithered tile = world.getTileEntity(pos) instanceof TileWithered ? (TileWithered) world.getTileEntity(pos) : null;
        if (tile != null) {
            CropData data = tile.getData();
            NonNullList<ItemStack> harvest = data.harvest(player, true);
            if (harvest != null) {
                if (data.hasCompletedMaxHarvests()) {
                    if (!world.isRemote) {
                        world.setBlockToAir(pos);
                    }
                } else tile.saveAndRefresh();

                return harvest;
            } else return EMPTY;
        } else return EMPTY;
    }

    @Override
    public boolean hydrateSoil(@Nullable EntityPlayer player, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        WateringHandler checker = CropHelper.getWateringHandler(world, pos, state);
        return checker != null && !checker.isWet(world, pos, state) && world.setBlockState(pos, checker.hydrate(world, pos, state));
    }

    @Override
    public void registerWateringHandler(WateringHandler handler) {
        wateringHandlers.add(handler);
    }

    @Override
    public void registerFarmlandToDirtMapping(IBlockState farmland, IBlockState dirt) {
        farmlandToDirtMap.put(farmland, dirt);
    }
}