package joshie.harvest.crops.block;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.IAnimalFeeder;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.IStateHandler.PlantSection;
import joshie.harvest.api.trees.Tree;
import joshie.harvest.core.base.block.BlockHFEnum;
import joshie.harvest.core.base.item.ItemBlockHF;
import joshie.harvest.core.entity.EntityBasket;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.crops.CropData;
import joshie.harvest.crops.CropHelper;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockHFCrops.CropType;
import joshie.harvest.crops.tile.TileCrop;
import joshie.harvest.crops.tile.TileWithered;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;

import static joshie.harvest.api.crops.IStateHandler.PlantSection.BOTTOM;
import static joshie.harvest.api.crops.IStateHandler.PlantSection.TOP;
import static joshie.harvest.core.helpers.MCServerHelper.markTileForUpdate;
import static joshie.harvest.crops.CropHelper.harvestCrop;
import static joshie.harvest.crops.block.BlockHFCrops.CropType.*;

public class BlockHFCrops extends BlockHFEnum<BlockHFCrops, CropType> implements IPlantable, IGrowable, IAnimalFeeder {
       public enum CropType implements IStringSerializable {
        FRESH(BOTTOM), WITHERED(BOTTOM), FRESH_DOUBLE(TOP), WITHERED_DOUBLE(TOP);

        private final PlantSection section;

        CropType(PlantSection section) {
            this.section = section;
        }

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }

        public boolean isWithered() {
            return this == WITHERED || this == WITHERED_DOUBLE;
        }

        public PlantSection getSection() {
            return section;
        }
    }

    public static final AxisAlignedBB CROP_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

    public BlockHFCrops() {
        super(Material.PLANTS, CropType.class, null);
        setSoundType(SoundType.GROUND);
        if (!HFCrops.GROWS_DAILY) setTickRandomly(true);
        disableStats();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getPropertyKeys().contains(property)) return (state.getValue(property)).ordinal();
        else return 0;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    //Only called if crops are set to tick randomly
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!HFCrops.GROWS_DAILY && !world.isRemote) {
            CropType stage = getEnumFromState(state);
            if (stage == CropType.WITHERED) return; //If withered do nothing
            if (world.getLightFromNeighbors(pos.up()) >= 9) {
                if (rand.nextInt(20) == 0) {
                    //We are Growing!
                    grow(world, rand, pos, state);
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        CropType type = getEnumFromState(state);
        if (type.isWithered()) return 0.5F;
        else return isWood(world, pos) ? 2F : 0.1F;
    }

    @Override
    public boolean isWood(IBlockAccess world, BlockPos pos) {
        CropData data = CropHelper.getCropDataAt(world, pos);
        if (data != null && data.getCrop() instanceof Tree) {
            if (data.getStage() >= ((Tree)data.getCrop()).getStagesToMaturity()) return true;
        }

        return false;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        CropData data = CropHelper.getCropDataAt(world, pos);
        return data != null && data.getCrop() instanceof Tree ? SoundType.WOOD : getSoundType();
    }

    public static PlantSection getSection(IBlockState state) {
        if (state.getBlock() != HFCrops.CROPS) return null;
        int stage = state.getBlock().getMetaFromState(state); //Can't get the Enum from state, because this method is static.
        PlantSection section = BOTTOM;
        if (stage == CropType.WITHERED_DOUBLE.ordinal() || stage == CropType.FRESH_DOUBLE.ordinal()) {
            section = PlantSection.TOP;
        }

        return section;
    }

    public static boolean isWithered(IBlockState state) {
        int stage = state.getBlock().getMetaFromState(state); //Can't get the Enum from state, because this method is static.
        return stage == CropType.WITHERED.ordinal() || stage == CropType.WITHERED_DOUBLE.ordinal();
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.AIR;
    }

    @SuppressWarnings("unchecked")
    private boolean canStay(World world, BlockPos pos) {
        CropData data = CropHelper.getCropDataAt(world, pos);
        if (data != null) {
            Crop crop = data.getCrop();
            if (crop.getGrowthHandler() != null) {
                IBlockState down = world.getBlockState(pos.down());
                return down.getBlock() == this || down.getBlock().canSustainPlant(down, world, pos, EnumFacing.UP, crop);
            }
        }

        return false;
    }

    private int getXMinus(EnumFacing facing, int x) {
        return facing.getAxis() == Axis.Z ? x - 1 : x;
    }

    private int getXPlus(EnumFacing facing, int x) {
        return facing.getAxis() == Axis.Z ? x + 1 : x;
    }

    private int getZMinus(EnumFacing facing, int z) {
        return facing.getAxis() == Axis.X ? z - 1 : z;
    }

    private int getZPlus(EnumFacing facing, int z) {
        return facing.getAxis() == Axis.X ? z + 1 : z;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        CropType stage = getEnumFromState(state);
        if (stage == CropType.WITHERED || stage == CropType.WITHERED_DOUBLE) return false; //If Withered with suck!
        if (player.isSneaking()) return false;
        else {
            PlantSection section = getSection(state);
            CropData data = CropHelper.getCropDataAt(world, pos);
            if (data == null || data.getCrop().requiresSickle() || data.getCrop() instanceof Tree) {
                return false;
            } else {
                EnumFacing front = EntityHelper.getFacingFromEntity(player);
                if (EntityBasket.isWearingBasket(player)) {
                    for (int x2 = getXMinus(front, pos.getX()); x2 <= getXPlus(front, pos.getX()); x2++) {
                        for (int z2 = getZMinus(front, pos.getZ()); z2 <= getZPlus(front, pos.getZ()); z2++) {
                            BlockPos position = new BlockPos(x2, pos.getY(), z2);
                            IBlockState theState = world.getBlockState(position);
                            PlantSection theSection = BlockHFCrops.getSection(theState);
                            CropData theData = CropHelper.getCropDataAt(world, position);
                            if (!(theData == null || theData.getCrop().requiresSickle() || theData.getCrop() instanceof Tree)) {
                                if (theSection == BOTTOM) harvestCrop(player, world, position);
                                else harvestCrop(player, world, position.down());
                            }
                        }
                    }
                }

                if (section == BOTTOM) {
                    return harvestCrop(player, world, pos);
                } else return harvestCrop(player, world, pos.down());
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!world.isRemote) {
            if (!canStay(world, pos)) {
                world.setBlockToAir(pos);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getPlayerRelativeBlockHardness(IBlockState state, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos) {
        if (isWood(world, pos)) return super.getPlayerRelativeBlockHardness(state, player, world, pos);
        float hardness = state.getBlockHardness(world, pos);
        if (hardness < 0.0F) {
            return 0.0F;
        }

        ItemStack held = player.getHeldItemMainhand();
        if (HFApi.crops.isSickle(held)) {
            return player.getDigSpeed(state, pos) / hardness / 10F;
        } else {
            return player.getDigSpeed(state, pos) / hardness / 100F;
        }
    }

    @Override
    public boolean removedByPlayer(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {
        CropType stage = getEnumFromState(state);
        if (stage == CropType.WITHERED || stage == CropType.WITHERED_DOUBLE)
            return world.setBlockToAir(pos); //JUST KILL IT IF WITHERED

        PlantSection section = getSection(state);
        CropData data = CropHelper.getCropDataAt(world, pos);
        if (data == null) {
            return super.removedByPlayer(state, world, pos, player, willHarvest);
        }

        Crop crop = data.getCrop();
        int originalStage = data.getStage();
        boolean isSickle = player.getHeldItemMainhand() != null && HFApi.crops.isSickle(player.getHeldItemMainhand());
        if (isSickle || !crop.requiresSickle()) {
            if (section == BOTTOM) {
                harvestCrop(player, world, pos);
            } else harvestCrop(player, world, pos.down());
        }

        if (crop.isCurrentlyDouble(data.getStage())) {
            if (section == PlantSection.BOTTOM) {
                IBlockState stateUp = world.getBlockState(pos.up());
                if (stateUp.getBlock() == this) {
                    CropHelper.onBottomBroken(pos.up(), getActualState(stateUp, world, pos.up()));
                }
            }
        }

        if (isSickle && crop.getMinimumCut() != 0 && crop.requiresSickle() && originalStage >= crop.getMinimumCut()) {
            return data.markSafe(world, pos, section);
        }

        return world.setBlockToAir(pos);
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        CropType stage = getEnumFromState(state);
        if (stage == FRESH || stage == CropType.WITHERED) {
            CropData data = CropHelper.getCropDataAt(world, pos);
            if (data == null || data.isClearable()) {
                if (world.getBlockState(pos.up()).getBlock() == this) {
                    CropType above = getEnumFromState(world.getBlockState(pos.up()));
                    if (above == FRESH_DOUBLE || above == WITHERED_DOUBLE) {
                        world.setBlockToAir(pos.up());
                    }
                }
            } else {
                if (world.getBlockState(pos.up()).getBlock() == this && !data.getCrop().isCurrentlyDouble(data.getStage())) data.markSafe(world, pos.up(), PlantSection.TOP);
                HFApi.crops.plantCrop(null, world, pos, data.getCrop(), data.getCrop().getRegrowStage());
            }
        } else if (stage == CropType.FRESH_DOUBLE || stage == CropType.WITHERED_DOUBLE) {
            CropData data = CropHelper.getCropDataAt(world, pos.down());
            if (data == null || data.isClearable()) {
                world.setBlockToAir(pos.down());
            }
        }

        super.breakBlock(world, pos, state); //Break them crops
    }

    @Override
    @SuppressWarnings("deprecation, unchecked")
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        CropType stage = getEnumFromState(state);
        CropData data = CropHelper.getCropDataAt(world, pos);
        return data != null ? data.getCrop().getStateHandler().getBoundingBox(world, pos, stage.getSection(), data.getCrop(), data.getStage(), stage.isWithered()) : CROP_AABB;
    }

    @Override
    @SuppressWarnings("deprecation, unchecked")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        CropType stage = getEnumFromState(state);
        CropData data = CropHelper.getCropDataAt(world, pos);
        return data != null ? data.getCrop().getStateHandler().getCollisionBoundingBox(world, pos, stage.getSection(), data.getCrop(), data.getStage(), stage.isWithered()) : NULL_AABB;
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        if (getEnumFromState(state) == CropType.WITHERED) return new ItemStack(Blocks.DEADBUSH); //It's Dead soo???
        CropData data = CropHelper.getCropDataAt(world, pos);
        return data == null ? new ItemStack(Blocks.DEADBUSH) : HFCrops.SEEDS.getStackFromCrop(data.getCrop());
    }

    @Override
    @SuppressWarnings("deprecation, unchecked")
    @Nonnull
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        CropType stage = getEnumFromState(state);
        TileWithered crop = CropHelper.getTile(world, pos, stage.getSection());
        if (stage.getSection() == PlantSection.TOP && crop == null) {
            IBlockState theState = CropHelper.getTempState(pos);
            return theState == null ? Blocks.GRASS.getDefaultState(): theState;
        }

        if (crop != null) return crop.getData().getCrop().getStateHandler().getState(world, pos, stage.getSection(), crop.getData().getCrop(), crop.getData().getStage(), stage.isWithered());
        else return Blocks.DEADBUSH.getDefaultState();
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        CropData data = CropHelper.getCropDataAt(world, pos);
        return data == null ? EnumPlantType.Crop : data.getCrop().getPlantType();
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return getActualState(world.getBlockState(pos.down()), world, pos.down());
    }

    //Can Apply Bonemeal (Not Fully Grown)
    @Override
    @SuppressWarnings("unchecked")
    public boolean canGrow(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isClient) {
        if (getEnumFromState(state) == CropType.WITHERED) return false; //It's dead it can't grow...
        if (HFCrops.ENABLE_BONEMEAL) {
            if (HFCrops.SEASONAL_BONEMEAL) {
                CropData data = CropHelper.getCropDataAt(world, pos);
                return data != null && data.getCrop().getGrowthHandler().canGrow(world, pos, data.getCrop()) && canGrow(world, pos, state);
            } else return canGrow(world, pos, state);
        } else return false;
    }

    private boolean canGrow(World world, BlockPos pos, IBlockState state) {
        TileWithered crop = CropHelper.getTile(world, pos, getSection(state));
        return crop != null && crop.getData().getStage() < crop.getData().getCrop().getStages();
    }

    /* Only called server side **/
    //Whether the bonemeal has been used and we should call the function below to make the plant grow
    @Override
    public boolean canUseBonemeal(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        return true;
    }

    /* Only called server side **/
    //Apply the bonemeal and grow!
    @Override
    public void grow(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        PlantSection section = getSection(state);
        TileWithered crop = CropHelper.getTile(world, pos, section);
        if (crop != null) {
            crop.getData().grow(world, section == BOTTOM ? pos: pos.down());
            crop.saveAndRefresh();
            markTileForUpdate(crop);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean feedAnimal(AnimalStats stats, World world, BlockPos pos, IBlockState state, boolean simulate) {
        if (HFApi.animals.canAnimalEatFoodType(stats, AnimalFoodType.GRASS)) {
            CropData data = CropHelper.getCropDataAt(world, pos);
            if (data != null) {
                if (data.getCrop() == HFCrops.GRASS) {
                    int stage = data.getStage();
                    if (stage > 5) {
                        if (simulate) return true;
                        HFApi.crops.plantCrop(null, world, pos, data.getCrop(), stage - 5);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return getEnumFromState(state).section == BOTTOM;
    }

    @Override
    @Nonnull
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return !getEnumFromState(state).isWithered() ? new TileCrop() : new TileWithered();
    }

    @Override
    public ItemBlockHF getItemBlock() {
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addHitEffects(IBlockState state, World world, RayTraceResult target, ParticleManager manager) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(Item item, String name) {
        for (int i = 0; i < values.length; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(HFModInfo.MODID, property.getName() + "_" + getEnumFromMeta(i).getName()), "inventory"));
        }
    }
}