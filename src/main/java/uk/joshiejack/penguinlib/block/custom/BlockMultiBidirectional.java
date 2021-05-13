package uk.joshiejack.penguinlib.block.custom;

import uk.joshiejack.penguinlib.data.custom.block.AbstractCustomBlockData;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class BlockMultiBidirectional extends BlockMultiCustom {
    private static final PropertyBool EAST_WEST = PropertyBool.create("ew");

    public BlockMultiBidirectional(ResourceLocation registry, AbstractCustomBlockData defaults, AbstractCustomBlockData... data) {
        super(registry, defaults, data);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        boolean ew = state.getValue(EAST_WEST);
        return ids.get(state.getValue(property)) + (ew ? 8 : 0);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean ew = meta > 8;
        int ordinal = meta % 8;
        return getDefaultState().withProperty(property, getDataFromMeta(ordinal)).withProperty(EAST_WEST, ew);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary, EAST_WEST);
        return new BlockStateContainer(this, property, EAST_WEST);
    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(Item item, AbstractCustomBlockData cd) {
        if (Character.toLowerCase(property.getName().charAt(0)) < Character.toLowerCase('e')) ModelLoader.setCustomModelResourceLocation(item, ids.get(cd), cd.getModel(getRegistryName(), property.getName() + "=" + cd.name + ",ew=false"));
        else ModelLoader.setCustomModelResourceLocation(item, ids.get(cd), cd.getModel(getRegistryName(), "ew=false," + property.getName() + "=" + cd.name));
    }
}
