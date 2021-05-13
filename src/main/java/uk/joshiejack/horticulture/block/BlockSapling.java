package uk.joshiejack.horticulture.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.horticulture.Horticulture;
import uk.joshiejack.penguinlib.block.base.BlockMultiSapling;
import uk.joshiejack.penguinlib.template.Template;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ResourceLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.GsonHelper;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Random;

import static uk.joshiejack.horticulture.Horticulture.MODID;
import static uk.joshiejack.penguinlib.client.StateMaps.NO_STAGES;

public class BlockSapling extends BlockMultiSapling<BlockSapling.Sapling> {
    private static final Template.Replaceable ONLY_AIR = new Template.Replaceable() {
        @Override
        public boolean canReplace(World world, BlockPos transformed) {
            if (!super.canReplace(world, transformed)) return false;
            else {
                IBlockState state = world.getBlockState(transformed);
                return state.getBlock().canBeReplacedByLeaves(state, world, transformed);
            }
        }
    };

    private final EnumMap<Sapling, Template> templates = new EnumMap<>(Sapling.class);

    public BlockSapling() {
        super(new ResourceLocation(MODID, "sapling"), Sapling.class);
        setCreativeTab(Horticulture.TAB);
    }

    private Template getTemplate(Sapling tree) {
        return templates.computeIfAbsent(tree, t -> GsonHelper.get().fromJson(ResourceLoader.getJSONResource(new ResourceLocation(MODID, t.getName()), "trees"), Template.class));
    }

    @Override
    public void generateTree(World world, BlockPos pos, IBlockState state, Random rand) {
        Sapling tree = getEnumFromState(state);
        //Generate tree code
        Rotation rotation = Rotation.values()[world.rand.nextInt(Rotation.values().length)];
        getTemplate(tree).placeBlocks(world, getAdjustedPositionBasedOnRotation(tree, pos, rotation), rotation, ONLY_AIR);
    }

    private BlockPos getAdjustedPositionBasedOnRotation(Sapling tree, BlockPos pos, Rotation rotation) {
        if (tree == Sapling.ORANGE) {
            switch (rotation) {
                case NONE:
                    return pos.west(4).north(4);
                case CLOCKWISE_90:
                    return pos.north(4).east(4);
                case CLOCKWISE_180:
                    return pos.east(4).south(4);
                case COUNTERCLOCKWISE_90:
                    return pos.south(4).west(4);
            }
        } else if (tree == Sapling.APPLE) {
            switch (rotation) {
                case NONE:
                    return pos.west(3).north(4);
                case CLOCKWISE_90:
                    return pos.north(3).east(4);
                case CLOCKWISE_180:
                    return pos.east(3).south(4);
                case COUNTERCLOCKWISE_90:
                    return pos.south(3).west(4);
            }
        } else if (tree == Sapling.BANANA) {
            switch (rotation) {
                case NONE:
                    return pos.west(4).north(4);
                case CLOCKWISE_90:
                    return pos.north(4).east(4);
                case CLOCKWISE_180:
                    return pos.east(4).south(4);
                case COUNTERCLOCKWISE_90:
                    return pos.south(4).west(4);
            }
        } else if (tree == Sapling.PEACH) {
            switch (rotation) {
                case NONE:
                    return pos.west(3).north(3);
                case CLOCKWISE_90:
                    return pos.north(3).east(3);
                case CLOCKWISE_180:
                    return pos.east(3).south(3);
                case COUNTERCLOCKWISE_90:
                    return pos.south(3).west(3);
            }
        }

        return pos;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item) {
        ModelLoader.setCustomStateMapper(this, NO_STAGES);
        for (Sapling tree: values) {
            ModelLoader.setCustomModelResourceLocation(item, tree.ordinal(), new ModelResourceLocation(new ResourceLocation(MODID, property.getName() + "_" + tree.getName()), "inventory"));
        }
    }

    public enum Sapling implements IStringSerializable {
        ORANGE, APPLE, BANANA, PEACH;

        @Nonnull
        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
