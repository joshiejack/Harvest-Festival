package uk.joshiejack.gastronomy.block;

import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.gastronomy.tile.TileFridge;
import uk.joshiejack.penguinlib.block.base.BlockMultiTileRotatableDouble;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Objects;

public class BlockFridge extends BlockMultiTileRotatableDouble<BlockFridge.Storage> {
    public BlockFridge() {
        super(new ResourceLocation(Gastronomy.MODID, "storage"), Material.IRON, Storage.class);
        setHardness(2.5F);
        setSoundType(SoundType.METAL);
        setCreativeTab(Gastronomy.TAB);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return Objects.requireNonNull(((TileFridge) (isTop(state) ? world.getTileEntity(pos.down()) : world.getTileEntity(pos))))
                .getFacing() == face ? BlockFaceShape.CENTER : BlockFaceShape.SOLID;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (isTop(state)) {
            ((TileFridge) Objects.requireNonNull(world.getTileEntity(pos.down()))).animatingTop = true;
            return super.onBlockActivated(world, pos.down(), world.getBlockState(pos.down()), player, hand, side, hitX, hitY, hitZ);
        } else {
            ((TileFridge) Objects.requireNonNull(world.getTileEntity(pos))).animatingBottom = true;
            return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileFridge();
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    @Override
    protected void registerModel(Item item, Storage storage) {
        ModelLoader.setCustomModelResourceLocation(item, storage.ordinal(), new ModelResourceLocation(getRegistryName(), "inventory_fridge"));
    }

    public enum Storage implements IStringSerializable {
        FRIDGE;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
