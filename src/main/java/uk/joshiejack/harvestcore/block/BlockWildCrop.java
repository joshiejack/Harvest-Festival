package uk.joshiejack.harvestcore.block;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.database.WildernessRegistry;
import uk.joshiejack.penguinlib.block.base.BlockBaseCropChangeState;
import uk.joshiejack.seasons.Season;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.IPlantable;

import java.util.Locale;
import java.util.Random;

public class BlockWildCrop extends BlockBaseCropChangeState {
    private static final Random random = new Random();
    private final Season season;

    public BlockWildCrop(Season season) {
        super(new ResourceLocation(HarvestCore.MODID, "wild_crop_" + season.name().toLowerCase(Locale.ENGLISH)), Blocks.AIR.getDefaultState(), ItemStack.EMPTY, 4);
        this.season = season;
    }

    @Override
    protected ItemStack getSeedStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public IBlockState getFinalState() {
        IBlockState state = super.getFinalState();
        while (state != null && !(state.getBlock() instanceof IPlantable)) {
            state = WildernessRegistry.registries.get(season).get(random);
        }

        return state;
    }
}
