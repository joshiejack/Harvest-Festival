package joshie.harvest.crops;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.api.crops.ICropRegistry;
import joshie.harvest.api.crops.WateringHandler;
import joshie.harvest.core.handlers.DisableHandler;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.core.util.holders.ItemStackHolder;
import joshie.harvest.crops.item.ItemCrop;
import joshie.harvest.crops.tile.TileWithered;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

import static joshie.harvest.crops.block.BlockHFCrops.CropType.FRESH;
import static joshie.harvest.crops.block.BlockHFCrops.CropType.FRESH_DOUBLE;

@HFApiImplementation
public class CropRegistry implements ICropRegistry {
    public static final CropRegistry INSTANCE = new CropRegistry();
    public final Set<WateringHandler> wateringHandlers = new HashSet<>();
    private final HashMap<ItemStackHolder, Crop> providers = new HashMap<>();

    @Override
    public BlockStateContainer getStateContainer(PropertyInteger stages) {
        return new BlockStateContainer(HFCrops.CROPS, HFCrops.CROPS.property, stages);
    }

    @Override
    public ItemStack getSeedStack(Crop crop, int amount) {
        ItemStack stack = HFCrops.SEEDS.getStackFromCrop(crop);
        stack.stackSize = amount;
        return stack;
    }

    @Override
    @Deprecated //TODO: Remove in 0.7+
    public ItemStack getCropStack(Crop crop, int amount) {
        for (ItemCrop.Crop crop1: ItemCrop.Crop.values()) {
            if (crop1.getCrop() == crop) return HFCrops.CROP.getStackFromEnum(crop1, amount);
        }

        ItemStack stack = HFCrops.CROP.getStackFromEnum(ItemCrop.Crop.TURNIP);
        stack.stackSize = amount;
        return stack;
    }

    @Override
    public void registerSeedForBlacklisting(ItemStack item) {
        DisableHandler.BLACKLIST.register(item);
    }

    @Override
    public void registerCropProvider(ItemStack stack, Crop crop) {
        providers.put(ItemStackHolder.of(stack), crop);
    }

    public List<ItemStack> getStacksForCrop(Crop crop) {
        List<ItemStack> list = new ArrayList<>();
        list.add(crop.getCropStack(1));
        ItemStackHolder holder = ItemStackHolder.of(crop.getCropStack(1));
        for (Entry<ItemStackHolder, Crop> entry: providers.entrySet()) {
            if (entry.getValue().equals(crop)) {
                for (ItemStack stack: entry.getKey().getMatchingStacks()) {
                    if (!holder.matches(stack)) list.add(stack);
                }
            }
        }

        return list;
    }

    @Override
    public Crop getCropFromStack(ItemStack stack) {
        //TODO: Remove in 0.7+
        if (stack.getItem() instanceof ICropProvider) {
            return ((ICropProvider)stack.getItem()).getCrop(stack);
        }

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
    public List<ItemStack> harvestCrop(@Nullable EntityPlayer player, World world, BlockPos pos) {
        TileWithered tile = world.getTileEntity(pos) instanceof TileWithered ? (TileWithered) world.getTileEntity(pos) : null;
        if (tile != null) {
            CropData data = tile.getData();
            List<ItemStack> harvest = data.harvest(player, true);
            if (harvest != null) {
                if (data.getCrop().getRegrowStage() <= 0) {
                    if (!world.isRemote) {
                        world.setBlockToAir(pos);
                    }
                } else tile.saveAndRefresh();

                return harvest;
            } else return null;
        } else return null;
    }

    @Override
    public boolean hydrateSoil(@Nullable EntityPlayer player, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        WateringHandler checker = CropHelper.getWateringHandler(state);
        if (checker != null) {
            checker.water(world, pos);
        }

        return checker != null;
    }

    @Override
    public void registerWateringHandler(WateringHandler handler) {
        wateringHandlers.add(handler);
    }
}