package joshie.harvest.crops;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.api.crops.ICropRegistry;
import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.api.crops.IStateHandler.PlantSection;
import joshie.harvest.crops.blocks.BlockHFCrops;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.HashMap;

public class CropRegistry implements ICropRegistry {
    private final HashMap<Pair<Item, Integer>, ICrop> providers = new HashMap<Pair<Item, Integer>, ICrop>();

    @Override
    public ICrop getCrop(ResourceLocation resource) {
        return Crop.CROPS.get(resource);
    }

    @Override
    public ICropData getCropAtLocation(World world, BlockPos pos) {
        PlantSection section = BlockHFCrops.getSection(world.getBlockState(pos));
        return section == PlantSection.BOTTOM ? HFTrackers.getCropTracker(world).getCropDataForLocation(world, pos) : HFTrackers.getCropTracker(world).getCropDataForLocation(world, pos.down());
    }

    @Override
    public ICrop registerCrop(ResourceLocation resource, int cost, int sell, int stages, int regrow, int year, int color, Season... seasons) {
        return new Crop(resource, seasons, cost, sell, stages, regrow, year, color);
    }

    @Override
    public ICrop registerCrop(ICrop crop) {
        Crop.CROPS.put(crop.getResource(), crop);
        return crop;
    }

    @Override
    public Collection<ICrop> getCrops() {
        return Crop.CROPS.values();
    }

    @Override
    public ICrop registerCropProvider(ItemStack stack, ICrop crop) {
        providers.put(Pair.of(stack.getItem(), stack.getItemDamage()), crop);
        return crop;
    }

    @Override
    public ICrop getCropFromStack(ItemStack stack) {
        if (stack.getItem() instanceof ICropProvider) {
            return ((ICropProvider)stack.getItem()).getCrop(stack);
        }

        ICrop crop = providers.get(Pair.of(stack.getItem(), OreDictionary.WILDCARD_VALUE));
        return crop != null ? crop : providers.get(Pair.of(stack.getItem(), stack.getItemDamage()));
    }
}