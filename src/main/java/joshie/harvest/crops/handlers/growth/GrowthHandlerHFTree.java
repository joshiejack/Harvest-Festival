package joshie.harvest.crops.handlers.growth;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.trees.GrowthHandlerTree;
import joshie.harvest.api.trees.Tree;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.util.HFTemplate;
import joshie.harvest.core.util.HFTemplate.Replaceable;
import joshie.harvest.core.util.ResourceLoader;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockFruit.Fruit;
import joshie.harvest.crops.block.BlockHFCrops.CropType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static joshie.harvest.buildings.HFBuildings.getGson;
import static joshie.harvest.core.lib.HFModInfo.MODID;

@SuppressWarnings("WeakerAccess")
public abstract class GrowthHandlerHFTree extends GrowthHandlerTree {
    private static final Replaceable ONLY_AIR = new Replaceable() {
        @Override
        public boolean cantReplace(World world, BlockPos transformed) {
            return super.cantReplace(world, transformed) || !world.isAirBlock(transformed);
        }
    };

    private final HFTemplate template;
    private final Fruit fruit;
    private final int radius;
    private final int number;

    public GrowthHandlerHFTree(Fruit fruit, int radius) {
        this.fruit = fruit;
        this.radius = radius;
        this.number = (radius * 2) + 1;
        this.template = getGson().fromJson(ResourceLoader.getJSONResource(new ResourceLocation(MODID, fruit.getName()), "trees"), HFTemplate.class);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(List<String> list, Tree tree, boolean debug) {
        list.add(TextFormatting.DARK_GREEN + "" + TextFormatting.ITALIC + TextHelper.formatHF("tree.area", number, number));
        for (Season season : tree.getSeasons()) {
            list.add(season.getDisplayName());
        }
    }

    @Override
    protected boolean canGrow(World world, BlockPos pos) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (x == 0 && z == 0) continue;
                BlockPos target = pos.add(x, 1, z);
                if (!world.isAirBlock(target)) return false;
            }
        }

        return true;
    }

    @Override
    protected void growJuvenile(World world, BlockPos pos) {
        world.setBlockState(pos.up(), HFCrops.CROPS.getStateFromEnum(CropType.FRESH_DOUBLE));
    }

    @Override
    protected void growTree(World world, BlockPos pos) {
        Rotation rotation = Rotation.values()[world.rand.nextInt(Rotation.values().length)];
        template.placeBlocks(world, getAdjustedPositionBasedOnRotation(pos, rotation), rotation, null, ONLY_AIR);
    }

    protected abstract BlockPos getAdjustedPositionBasedOnRotation(BlockPos pos, Rotation rotation);

    protected abstract boolean isLeaves(IBlockState state);

    @Override
    protected void growFruit(World world, BlockPos thePos) {
        int attempts = 0;
        while (true) {
            BlockPos pos = thePos.add(world.rand.nextInt(16) - 8, world.rand.nextInt(8), world.rand.nextInt(16) - 8);
            if (world.isAirBlock(pos)) {
                IBlockState above = world.getBlockState(pos.up());
                if (isLeaves(above)) {
                    world.setBlockState(pos, HFCrops.FRUITS.getStateFromEnum(fruit));
                    break;
                }
            }

            if (attempts == 1000) break;
            attempts++;
        }
    }
}
