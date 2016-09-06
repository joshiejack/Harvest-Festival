package joshie.harvest.crops;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.api.crops.ICropRegistry;
import joshie.harvest.api.crops.IStateHandler.PlantSection;
import joshie.harvest.core.util.HFApiImplementation;
import joshie.harvest.core.util.holder.ItemStackHolder;
import joshie.harvest.crops.block.BlockHFCrops;
import joshie.harvest.crops.tile.TileCrop;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import static joshie.harvest.crops.CropHelper.*;
import static joshie.harvest.crops.block.BlockHFCrops.Stage.FRESH;
import static joshie.harvest.crops.block.BlockHFCrops.Stage.FRESH_DOUBLE;

@HFApiImplementation
public class CropRegistry implements ICropRegistry {
    public static final CropRegistry INSTANCE = new CropRegistry();
    private final HashMap<ItemStackHolder, Crop> providers = new HashMap<>();

    @Override
    public BlockStateContainer getStateContainer(PropertyInteger stages) {
        return new BlockStateContainer(HFCrops.CROPS, stages);
    }

    @Override
    public ItemStack getSeedStack(Crop crop, int amount) {
        ItemStack stack = HFCrops.SEEDS.getStackFromCrop(crop);
        stack.stackSize = amount;
        return stack;
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
        if (stack.getItem() instanceof ICropProvider) {
            return ((ICropProvider)stack.getItem()).getCrop(stack);
        }

        Crop crop = providers.get(ItemStackHolder.of(stack.getItem(), OreDictionary.WILDCARD_VALUE));
        return crop != null ? crop : providers.get(ItemStackHolder.of(stack));
    }

    @Override
    public Crop getCropAtLocation(World world, BlockPos pos) {
        PlantSection section = BlockHFCrops.getSection(world.getBlockState(pos));
        if (section == null) return null;
        if (section == PlantSection.BOTTOM) return ((TileCrop)world.getTileEntity(pos)).getData().getCrop();
        else if (section == PlantSection.TOP) return ((TileCrop)world.getTileEntity(pos.down())).getData().getCrop();
        else return null;
    }

    @Override
    public void plantCrop(@Nullable EntityPlayer player, World world, BlockPos pos, Crop theCrop, int stage) {
        world.setBlockState(pos, HFCrops.CROPS.getStateFromEnum(FRESH));
        if (theCrop.isDouble(stage)) {
            world.setBlockState(pos.up(), HFCrops.CROPS.getStateFromEnum(FRESH_DOUBLE));
        }

        TileCrop tile = (TileCrop) world.getTileEntity(pos);
        tile.getData().setCrop(theCrop, stage);
        tile.saveAndRefresh();
    }

    @Override
    public ItemStack harvestCrop(@Nullable EntityPlayer player, World world, BlockPos pos) {
        TileCrop tile = world.getTileEntity(pos) instanceof TileCrop ? (TileCrop) world.getTileEntity(pos) : null;
        if (tile != null) {
            CropData data = tile.getData();
            ItemStack harvest = data.harvest(player, true);
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
        boolean ret = false;
        IBlockState state = world.getBlockState(pos);
        if (isSoil(state) && !isWetSoil(state)) {
            world.setBlockState(pos, WET_SOIL);
            ret = true;
        }

        return ret;
    }
}