package joshie.harvest.blocks;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalFeeder;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.crops.IBreakCrops;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.api.crops.IStateHandler.PlantSection;
import joshie.harvest.blocks.BlockCrop.Stage;
import joshie.harvest.core.config.Crops;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.AnimalHelper;
import joshie.harvest.core.helpers.CropHelper;
import joshie.harvest.core.helpers.SeedHelper;
import joshie.harvest.core.helpers.WorldHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.base.BlockHFBaseEnum;
import joshie.harvest.crops.HFCrops;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
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

import java.util.Random;

import static joshie.harvest.api.crops.IStateHandler.PlantSection.BOTTOM;
import static joshie.harvest.api.crops.IStateHandler.PlantSection.TOP;
import static joshie.harvest.core.helpers.CropHelper.harvestCrop;

public class BlockCrop extends BlockHFBaseEnum<Stage> implements IPlantable, IGrowable, IAnimalFeeder {
    public enum Stage implements IStringSerializable {
        FRESH(false, BOTTOM), WITHERED(false, BOTTOM), FRESH_DOUBLE(false, TOP), WITHERED_DOUBLE(true, TOP);

        private final boolean isWithered;
        private final PlantSection section;

        private Stage(boolean isWithered, PlantSection section) {
            this.isWithered = true;
            this.section = section;
        }

        @Override
        public String getName() {
            return toString().toLowerCase();
        }

        public boolean isWithered() {
            return isWithered;
        }

        public PlantSection getSection() {
            return section;
        }
    }

    public static final AxisAlignedBB CROP_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

    public BlockCrop() {
        super(Material.PLANTS, Stage.class, null);
        setBlockUnbreakable();
        setSoundType(SoundType.GROUND);
        setTickRandomly(Crops.ALWAYS_GROW);
        disableStats();
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
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    //Only called if crops are set to tick randomly
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (Crops.ALWAYS_GROW) {
            if (!world.isRemote) {
                Stage stage = getEnumFromState(state);
                if (stage == Stage.WITHERED) return; //If withered do nothing
                if (world.getLightFromNeighbors(pos.up()) >= 9) {
                    float chance = getGrowthChance(this, world, pos);
                    if (rand.nextInt((int) (25.0F / chance) + 1) == 0) {
                        //We are Growing!
                        HFTrackers.getCropTracker().grow(world, pos);
                    }
                }
            }
        }
    }

    private static float getGrowthChance(Block block, World world, BlockPos pos) {
        float f = 1.0F;
        BlockPos posDown = pos.down();

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                float f1 = 0.0F;
                IBlockState state = world.getBlockState(posDown.add(i, 0, j));

                if (state.getBlock().canSustainPlant(state, world, posDown.add(i, 0, j), EnumFacing.UP, (IPlantable) block)) {
                    f1 = 1.0F;

                    if (state.getBlock().isFertile(world, posDown.add(i, 0, j))) {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos posNorth = pos.north();
        BlockPos posSouth = pos.south();
        BlockPos posWest = pos.west();
        BlockPos posEast = pos.east();
        boolean xTrue = block == world.getBlockState(posWest).getBlock() || block == world.getBlockState(posEast).getBlock();
        boolean yTrue = block == world.getBlockState(posNorth).getBlock() || block == world.getBlockState(posSouth).getBlock();

        if (xTrue && yTrue) {
            f /= 2.0F;
        } else {
            boolean cornerTrue = block == world.getBlockState(posWest.north()).getBlock() || block == world.getBlockState(posEast.north()).getBlock() || block == world.getBlockState(posEast.south()).getBlock() || block == world.getBlockState(posWest.south()).getBlock();

            if (cornerTrue) {
                f /= 2.0F;
            }
        }
        return f;
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        IBlockState stateUp = world.getBlockState(pos.up());
        if (stateUp.getBlock() != this) return false;
        Stage aboveStage = getEnumFromState(stateUp);
        Stage thisStage = getEnumFromState(state);
        if (stateUp.getBlock() == this) {
            if (thisStage == Stage.FRESH) return aboveStage == Stage.FRESH_DOUBLE;
            else if (thisStage == Stage.WITHERED) return aboveStage == Stage.WITHERED_DOUBLE;
        }
        return false;
    }

    @Override
    //Return 0.75F if the plant isn't withered, otherwise, unbreakable!!!
    public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world, BlockPos pos) {
        Stage stage = getEnumFromState(state);
        ItemStack held = player.getHeldItemMainhand();
        ICropData crop = HFApi.crops.getCropAtLocation(world, pos);
        if (crop.getCrop().growsToSide() != null && crop.getStage() == crop.getCrop().getStages()) {
            return 0F; //If the crop is fully grown, and grows to the side. Make it immune to destruction.
        }

        //If this crop is withered return 0
        if (held == null || (!(held.getItem() instanceof IBreakCrops))) return stage.isWithered() ? 0 : 0.75F;
        return ((IBreakCrops) held.getItem()).getStrengthVSCrops(player, world, pos, state, held);
    }

    public static PlantSection getSection(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        int stage = state.getBlock().getMetaFromState(state); //Can't get the Enum from state, because this method is static.
        PlantSection section = BOTTOM;
        if (stage == Stage.WITHERED_DOUBLE.ordinal() || stage == Stage.FRESH_DOUBLE.ordinal()) {
            section = PlantSection.TOP;
        }

        return section;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    /* //TODO: Fix Hit effects
    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(IBlockState state, World world, RayTraceResult rayTraceResult, EffectRenderer effectRenderer) {
        BlockPos pos = rayTraceResult.getBlockPos();
        EnumFacing facing = rayTraceResult.sideHit;

        //^ setup vars
        Block block = state.getBlock();
        if (state.getMaterial() != Material.AIR) {
            AxisAlignedBB aabb = block.getBoundingBox(state, world, pos);
            float f = 0.1F;
            double d0 = (double) pos.getX() + world.rand.nextDouble() * (aabb.maxX - aabb.minX) - (double) (f * 2.0F) + (double) f + aabb.minX;
            double d1 = (double) pos.getY() + world.rand.nextDouble() * (aabb.maxY - aabb.minY - (double) (f * 2.0F)) + (double) f + aabb.minY;
            double d2 = (double) pos.getZ() + world.rand.nextDouble() * (aabb.maxZ - aabb.minZ - (double) (f * 2.0F)) + (double) f + aabb.minZ;

            if (facing == EnumFacing.DOWN || facing == EnumFacing.UP) {
                d1 = (double) pos.getY() + aabb.maxY - (double) f;
            }

            if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) {
                d2 = (double) pos.getZ() + aabb.minZ - (double) f;
            }

            if (facing == EnumFacing.WEST || facing == EnumFacing.EAST) {
                d0 = (double) pos.getX() + aabb.minX - (double) f;
            }

            effectRenderer.addEffect((new EntityCropDigFX(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(Item.getItemFromBlock(this)), world, d0, d1, d2, pos.getX(), pos.getY(), pos.getZ(), world.getBlockState(pos)))/*.applyColourMultiplier(pos)*.multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
        }
        return true;
    } */

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer) {
        int b0 = 4;
        for (int i1 = 0; i1 < b0; ++i1) {
            for (int j1 = 0; j1 < b0; ++j1) {
                for (int k1 = 0; k1 < b0; ++k1) {
                    double d0 = (double) pos.getX() + ((double) i1 + 0.5D) / (double) b0;
                    double d1 = (double) pos.getY() + ((double) j1 + 0.5D) / (double) b0;

                    //TODO: Fix Block Crop destroy effects
                    //effectRenderer.addEffect((new EntityCropDigFX(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(Item.getItemFromBlock(this)), world, d0, d1, d2, d0 - (double) pos.getX() - 0.5D, d1 - (double) pos.getY() - 0.5D, d2 - (double) pos.getZ() - 0.5D, world.getBlockState(pos)))/*.applyColourMultiplier(pos));
                }
            }
        }
        return true;
    } */

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        Stage stage = getEnumFromState(state);
        if (stage == Stage.WITHERED || stage == Stage.WITHERED_DOUBLE) return false; //If Withered with suck!
        if (player.isSneaking()) return false;
        else {
            PlantSection section = getSection(world, pos);
            ICropData data = HFApi.crops.getCropAtLocation(world, pos);
            if (data == null || data.getCrop().requiresSickle() || data.getCrop().growsToSide() != null) {
                return false;
            } else {
                if (section == BOTTOM) {
                    return harvestCrop(player, world, pos);
                } else {
                    return harvestCrop(player, world, pos.down());
                }
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!world.isRemote) {
            IBlockState soil = world.getBlockState(pos.down());
            ICrop crop = HFApi.crops.getCropAtLocation(world, pos).getCrop();
            if (crop != null && crop.getSoilHandler() != null) {
                if (!crop.getSoilHandler().canSustainPlant(soil, world, pos, this)) {
                    world.setBlockToAir(pos);
                }
            }
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        Stage stage = getEnumFromState(state);
        if (stage == Stage.WITHERED || stage == Stage.WITHERED_DOUBLE)
            return world.setBlockToAir(pos); //JUST KILL IT IF WITHERED

        PlantSection section = getSection(world, pos);
        ICropData data = HFApi.crops.getCropAtLocation(world, pos);
        if (data == null) {
            return super.removedByPlayer(state, world, pos, player, willHarvest);
        }

        ICrop crop = data.getCrop();
        boolean isSickle = player.getActiveItemStack() != null && player.getActiveItemStack().getItem() instanceof IBreakCrops;
        if (isSickle || !crop.requiresSickle()) {
            if (section == BOTTOM) {
                harvestCrop(player, world, pos);
            } else harvestCrop(player, world, pos.down());
        }

        return world.setBlockToAir(pos);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        Stage stage = getEnumFromState(state);
        if (stage == Stage.FRESH || stage == Stage.WITHERED) {
            HFTrackers.getCropTracker().removeCrop(world, pos);
        } else if (stage == Stage.FRESH_DOUBLE || stage == Stage.WITHERED_DOUBLE) {
            world.setBlockToAir(pos.down());
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (world instanceof World) {
            Stage stage = getEnumFromState(state);
            return CropHelper.getCropBoundingBox((World) world, pos, stage.getSection(), stage.isWithered());
        } else return CROP_AABB;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        if (getEnumFromState(state) == Stage.WITHERED) return new ItemStack(Blocks.DEADBUSH); //It's Dead soo???

        ICropData data = HFApi.crops.getCropAtLocation(world, pos);
        return SeedHelper.getSeedsFromCrop(data.getCrop());
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess access, BlockPos pos) {
        World world = WorldHelper.getWorld(access);
        if (world != null) {
            Stage stage = getEnumFromState(state);
            if (stage.getSection() == TOP) {
                return CropHelper.getBlockState(world, pos.down(), stage.getSection(), stage.isWithered());
            } else  {
                return CropHelper.getBlockState(world, pos, stage.getSection(), stage.isWithered());
            }
        } else return state;
    }

    /*@Override
    @SideOnly(Side.CLIENT) //TODO Is no longer in Block. Moved to Minecraft.getMinecraft().getItemColors.registerItemColorHandler()
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return meta == WITHERED ? 0x333333 : super.colorMultiplier(world, x, y, z); //Darken the dead shit
    }*/

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != this) return getDefaultState();
        return state;
    }

    //Can Apply Bonemeal (Not Fully Grown)
    @Override
    public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
        if (getEnumFromState(state) == Stage.WITHERED) return false; //It's dead it can't grow...

        if (Crops.ENABLE_BONEMEAL) {
            return HFTrackers.getCropTracker().canBonemeal(world, pos);
        } else return false;
    }

    /* Only called server side **/
    //Whether the bonemeal has been used and we should call the function below to make the plant grow
    @Override
    public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }

    /* Only called server side **/
    //Apply the bonemeal and grow!
    @Override
    public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
        HFTrackers.getCropTracker().grow(world, pos);
    }

    @Override
    public boolean canFeedAnimal(IAnimalTracked tracked, World world, BlockPos pos) {
        if (AnimalHelper.eatsGrass(tracked)) {
            ICropData crop = HFApi.crops.getCropAtLocation(world, pos);
            ICrop theCrop = crop.getCrop();
            if (theCrop == HFCrops.grass) {
                int stage = crop.getStage();
                if (stage > 5) {
                    HFTrackers.getCropTracker().plantCrop(tracked.getData().getOwner(), world, pos, theCrop, stage - 5);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(Item item, String name) {
        for (int i = 0; i < values.length; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(HFModInfo.MODID, property.getName() + "_" + getEnumFromMeta(i).getName()), "inventory"));
        }
    }
}