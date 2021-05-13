package uk.joshiejack.penguinlib.block.custom;

import uk.joshiejack.penguinlib.data.custom.block.AbstractCustomBlockData;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockMultiDoorway extends BlockMultiCustom {
    private static final PropertyBool EAST_WEST = PropertyBool.create("ew");
    private static final PropertyEnum<Section> SECTION = PropertyEnum.create("section", Section.class);

    public BlockMultiDoorway(ResourceLocation registry, AbstractCustomBlockData defaults, AbstractCustomBlockData... data) {
        super(registry, defaults, data);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        if (property == null) return new BlockStateContainer(this, temporary, EAST_WEST, SECTION);
        return new BlockStateContainer(this, property, EAST_WEST, SECTION);
    }

    private boolean isSameBlock(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == this;
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean connectedUp = isSameBlock(world, pos.up());
        boolean connectedDown = isSameBlock(world, pos.down());
        boolean connectedEast = isSameBlock(world, pos.east());
        boolean connectedWest = isSameBlock(world, pos.west());
        boolean connectedSouth = isSameBlock(world, pos.south());
        boolean connectedNorth = isSameBlock(world, pos.north());
        if (connectedDown && ((!connectedEast && connectedWest) || (connectedNorth && !connectedSouth))) {
            if (connectedWest) return state.withProperty(SECTION, Section.TL).withProperty(EAST_WEST, true);
            else return state.withProperty(SECTION, Section.TL).withProperty(EAST_WEST, false);
        } else if (connectedDown && ((connectedEast && !connectedWest) || (!connectedNorth && connectedSouth))) {
            if (connectedEast) return state.withProperty(SECTION, Section.TR).withProperty(EAST_WEST, true);
            return state.withProperty(SECTION, Section.TR).withProperty(EAST_WEST, false);
        } else if (connectedDown && (connectedEast || connectedNorth)) {
            if (connectedWest) return state.withProperty(SECTION, Section.TM).withProperty(EAST_WEST, true);
            return state.withProperty(SECTION, Section.TM).withProperty(EAST_WEST, false);
        } else if (connectedUp && ((!connectedEast && connectedWest) || (connectedNorth && !connectedSouth))) {
            if (connectedWest) return state.withProperty(SECTION, Section.BL).withProperty(EAST_WEST, true);
            return state.withProperty(SECTION, Section.BL).withProperty(EAST_WEST, false);
        } else if (connectedUp && ((connectedEast && !connectedWest) || (!connectedNorth && connectedSouth))) {
            if (connectedEast) return state.withProperty(SECTION, Section.BR).withProperty(EAST_WEST, true);
            return state.withProperty(SECTION, Section.BR).withProperty(EAST_WEST, false);
        } else if (connectedUp && (connectedEast || connectedNorth)) {
            if (connectedEast) return state.withProperty(SECTION, Section.BM).withProperty(EAST_WEST, true);
            return state.withProperty(SECTION, Section.BM).withProperty(EAST_WEST, false);
        } else return state;
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(Item item, AbstractCustomBlockData cd) {
        if (StringHelper.isEarlierThan(property.getName(), 'e')) ModelLoader.setCustomModelResourceLocation(item, ids.get(cd), cd.getModel(getRegistryName(), property.getName() + "=" + cd.name + ",ew=false,section=tm"));
        else if (StringHelper.isEarlierThan(property.getName(), 's')) ModelLoader.setCustomModelResourceLocation(item, ids.get(cd), cd.getModel(getRegistryName(), "ew=false," + property.getName() + "=" + cd.name + ",section=tm"));
        else ModelLoader.setCustomModelResourceLocation(item, ids.get(cd), cd.getModel(getRegistryName(), "ew=false,section=tm," + property.getName() + "=" + cd.name));
    }

    public enum Section implements IStringSerializable {
        TL, TM, TR, BL, BM, BR;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
