package joshie.harvest.crops;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.api.crops.ICropRegistry;
import joshie.harvest.api.crops.IStateHandler.PlantSection;
import joshie.harvest.core.util.HFApiImplementation;
import joshie.harvest.core.util.holder.ItemStackHolder;
import joshie.harvest.crops.block.BlockHFCrops;
import joshie.harvest.crops.tile.TileCrop;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import static joshie.harvest.crops.CropHelper.*;
import static joshie.harvest.crops.block.BlockHFCrops.Stage.FRESH;
import static joshie.harvest.crops.block.BlockHFCrops.Stage.FRESH_DOUBLE;

@HFApiImplementation
public class CropRegistry implements ICropRegistry {
    public static final IForgeRegistry<Crop> REGISTRY = new RegistryBuilder<Crop>().setName(new ResourceLocation("harvestfestival", "crops")).setType(Crop.class).setIDRange(0, 32000).create();
    public static final CropRegistry INSTANCE = new CropRegistry();
    private final HashMap<ItemStackHolder, Crop> providers = new HashMap<>();
    private final Cache<Crop, List<ItemStack>> cropToStacks = CacheBuilder.newBuilder().maximumSize(128).build();

    @Override
    public Crop getCrop(ResourceLocation resource) {
        return REGISTRY.getValue(resource);
    }

    @Override
    public ICrop registerCrop(ResourceLocation resource, int cost, int sell, int stages, int regrow, int year, int color, Season... seasons) {
        return new Crop(resource, seasons, cost, sell, stages, regrow, year, color);
    }

    @Override
    public void registerCropProvider(ItemStack stack, ResourceLocation crop) {
        providers.put(ItemStackHolder.of(stack), getCrop(crop));
    }

    public List<ItemStack> getStacksForCrop(Crop crop) {
        try {
            return cropToStacks.get(crop, new Callable<List<ItemStack>>() {
                @Override
                public List<ItemStack> call() throws Exception {
                    List<ItemStack> list = new ArrayList<>();
                    for (Entry<ItemStackHolder, Crop> entry: providers.entrySet()) {
                        if (entry.getValue().equals(crop)) {
                            list.addAll(entry.getKey().getMatchingStacks());
                        }
                    }

                    return list;
                }
            });
        } catch (Exception e) { return new ArrayList<>(); }
    }

    @Override
    public ICrop getCropFromStack(ItemStack stack) {
        if (stack.getItem() instanceof ICropProvider) {
            return ((ICropProvider)stack.getItem()).getCrop(stack);
        }

        ICrop crop = providers.get(ItemStackHolder.of(stack.getItem(), OreDictionary.WILDCARD_VALUE));
        return crop != null ? crop : providers.get(ItemStackHolder.of(stack));
    }

    @Override
    public ICropData getCropAtLocation(World world, BlockPos pos) {
        PlantSection section = BlockHFCrops.getSection(world.getBlockState(pos));
        if (section == null) return null;
        if (section == PlantSection.BOTTOM) return ((TileCrop)world.getTileEntity(pos)).getData();
        else if (section == PlantSection.TOP) return ((TileCrop)world.getTileEntity(pos.down())).getData();
        else return null;
    }

    @Override
    public void plantCrop(@Nullable EntityPlayer player, World world, BlockPos pos, ICrop theCrop, int stage) {
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