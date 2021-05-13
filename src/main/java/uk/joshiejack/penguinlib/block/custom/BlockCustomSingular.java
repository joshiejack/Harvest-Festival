package uk.joshiejack.penguinlib.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.block.interfaces.IPenguinBlock;
import uk.joshiejack.penguinlib.data.custom.block.AbstractCustomBlockData;
import uk.joshiejack.penguinlib.item.custom.ItemBlockSingularCustom;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.FakePlayerHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.LootHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockCustomSingular extends Block implements IPenguinBlock {
    private final AbstractCustomBlockData data;

    //Main Constructor
    public BlockCustomSingular(ResourceLocation registry, AbstractCustomBlockData data) {
        super(data.material);
        //byName = byNameTemporary;
        this.data = data;
        setDefaultState(blockState.getBaseState());
        String toolType = data.toolType == null ? "pickaxe" : data.toolType.split("&")[0];
        setHarvestLevel(toolType, data.toolLevel);
        setHardness(data.hardness);
        setSoundType(data.soundType);
        RegistryHelper.registerBlock(registry, this);
    }


    @Override
    public boolean canEntityDestroy(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, Entity entity) {
        return getBlockHardness(state, entity.world, pos) != -1F;
    }

    @Override
    public float getExplosionResistance(@Nonnull World world, @Nonnull BlockPos pos, @Nullable Entity exploder, @Nonnull Explosion explosion) {
        return data.explosionResistance == -10F?  super.getExplosionResistance(world, pos, exploder, explosion) : data.explosionResistance;
    }
    @Override
    public boolean canSilkHarvest(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player) {
        return data.silkHarvest != null && data.silkHarvest;
    }

    @Override
    public boolean isToolEffective(@Nonnull String type, @Nonnull IBlockState state) {
        return data.toolType == null ? super.isToolEffective(type, state) : data.getToolTypes(data).contains(type);
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return data.renderLayer == null ? layer == getRenderLayer() : layer == data.renderLayer;
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return data.boundingBox == null ? super.getBoundingBox(state, source, pos) : data.boundingBox;
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return data.collisionBox == null ? super.getCollisionBoundingBox(state, worldIn, pos) : data.collisionBox;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune) {
        ResourceLocation lootTable = data.lootTable;
        if (lootTable != null && world instanceof WorldServer) {
            drops.addAll(LootHelper.getLoot(lootTable, (World) world, FakePlayerHelper.getFakePlayerWithPosition((WorldServer) world, pos), fortune));
        }
    }

    @Override
    public void onEntityCollision(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Entity entity) {
        ResourceLocation script = data.getScript();
        if (script != null) {
            Scripting.callFunction(script, "onEntityCollision", world, pos, state, entity);
        }
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState blockState) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public ItemBlock createItemBlock() {
        return new ItemBlockSingularCustom(getRegistryName(), this, data);
    }

    @Override
    public void registerModels(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
