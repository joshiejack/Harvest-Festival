package joshie.harvest.blocks;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.IAnimalFeeder;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.crops.IBreakCrops;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.api.crops.ICropRenderHandler.PlantSection;
import joshie.harvest.blocks.render.EntityCropDigFX;
import joshie.harvest.core.config.Crops;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.AnimalHelper;
import joshie.harvest.core.helpers.SeedHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.util.base.BlockHFBase;
import joshie.harvest.crops.HFCrops;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import static joshie.harvest.core.helpers.CropHelper.harvestCrop;

public class BlockCrop extends BlockHFBase implements IPlantable, IGrowable, IAnimalFeeder {
    public static final AxisAlignedBB CROP_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    public static final int FRESH = 0;
    public static final int WITHERED = 1;
    public static final int FRESH_DOUBLE = 2;
    public static final int WITHERED_DOUBLE = 3;
    public static final int GROWN = 7;

    public BlockCrop() {
        super(Material.PLANTS);
        setBlockUnbreakable();
        setSoundType(SoundType.GROUND);
        setCreativeTab(null);
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

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
        PlantSection section = getSection(world, pos);
        ICropData data = HFApi.CROPS.getCropAtLocation(MCClientHelper.getWorld(), pos);
        data.getCrop().getCropRenderHandler().setBlockBoundsBasedOnStage(this, section, data.getCrop(), data.getStage());
    }

    //Only called if crops are set to tick randomly
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (Crops.ALWAYS_GROW) {
            if (!world.isRemote) {
                int meta = world.getBlockMetadata(pos);
                if (meta == WITHERED) return; //If withered do nothing
                if (world.getBlockLightValue(pos.up()) >= 9) {
                    float chance = getGrowthChance(world, pos);
                    if (rand.nextInt((int) (25.0F / chance) + 1) == 0) {
                        //We are Growing!
                        HFTrackers.getCropTracker().grow(world, pos);
                    }
                }
            }
        }
    }

    private float getGrowthChance(World world, int x, int y, int z) {
        float f = 1.0F;
        Block block = world.getBlock(x, y, z - 1);
        Block block1 = world.getBlock(x, y, z + 1);
        Block block2 = world.getBlock(x - 1, y, z);
        Block block3 = world.getBlock(x + 1, y, z);
        Block block4 = world.getBlock(x - 1, y, z - 1);
        Block block5 = world.getBlock(x + 1, y, z - 1);
        Block block6 = world.getBlock(x + 1, y, z + 1);
        Block block7 = world.getBlock(x - 1, y, z + 1);
        boolean xTrue = block2 == this || block3 == this;
        boolean zTrue = block == this || block1 == this;
        boolean cornerTrue = block4 == this || block5 == this || block6 == this || block7 == this;

        for (int l = x - 1; l <= x + 1; ++l) {
            for (int i1 = z - 1; i1 <= z + 1; ++i1) {
                float f1 = 0.0F;

                if (world.getBlock(l, y - 1, i1).canSustainPlant(world, l, y - 1, i1, EnumFacing.UP, this)) {
                    f1 = 1.0F;

                    if (world.getBlock(l, y - 1, i1).isFertile(world, l, y - 1, i1)) {
                        f1 = 3.0F;
                    }
                }

                if (l != x || i1 != z) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        if (cornerTrue || xTrue && zTrue) {
            f /= 2.0F;
        }

        return f;
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        IBlockState stateUp = world.getBlockState(pos.up());
        int aboveMeta = state.getBlock().getMetaFromState(stateUp);
        int thisMeta = state.getBlock().getMetaFromState(state);
        if (stateUp.getBlock() == this) {
            if (thisMeta == FRESH) return aboveMeta == FRESH_DOUBLE;
            else if (thisMeta == WITHERED) return aboveMeta == WITHERED_DOUBLE;
        }
        return false;
    }

    @Override
    //Return 0.75F if the plant isn't withered, otherwise, unbreakable!!!
    public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world, BlockPos pos) {
        int meta = world.getBlockMetadata(pos);
        ItemStack held = player.getActiveItemStack();
        ICropData crop = HFApi.CROPS.getCropAtLocation(world, pos);
        if (crop.getCrop().growsToSide() != null && crop.getStage() == crop.getCrop().getStages()) {
            return 0F; //If the crop is fully grown, and grows to the side. Make it immune to destruction.
        }

        //If this crop is withered return 0
        if (held == null || (!(held.getItem() instanceof IBreakCrops))) return meta == WITHERED ? 0 : 0.75F;
        return ((IBreakCrops) held.getItem()).getStrengthVSCrops(player, world, pos, state, held);
    }

    public static PlantSection getSection(IBlockAccess world, BlockPos pos) {
        int meta = world.getBlockMetadata(pos);
        PlantSection section = PlantSection.BOTTOM;
        if (meta == WITHERED_DOUBLE || meta == FRESH_DOUBLE) {
            section = PlantSection.TOP;
        }

        return section;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(IBlockState state, World world, RayTraceResult target, EffectRenderer effectRenderer) {
        int x = target.blockX;
        int y = target.blockY;
        int z = target.blockZ;
        int side = target.sideHit;

        //^ setup vars
        Block block = world.getBlock(x, y, z);
        if (block.getMaterial() != Material.AIR) {
            float f = 0.1F;
            double d0 = (double) x + world.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (double) (f * 2.0F)) + (double) f + block.getBlockBoundsMinX();
            double d1 = (double) y + world.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (double) (f * 2.0F)) + (double) f + block.getBlockBoundsMinY();
            double d2 = (double) z + world.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (double) (f * 2.0F)) + (double) f + block.getBlockBoundsMinZ();

            if (side == 0) {
                d1 = (double) y + block.getBlockBoundsMinY() - (double) f;
            }

            if (side == 1) {
                d1 = (double) y + block.getBlockBoundsMaxY() + (double) f;
            }

            if (side == 2) {
                d2 = (double) z + block.getBlockBoundsMinZ() - (double) f;
            }

            if (side == 3) {
                d2 = (double) z + block.getBlockBoundsMaxZ() + (double) f;
            }

            if (side == 4) {
                d0 = (double) x + block.getBlockBoundsMinX() - (double) f;
            }

            if (side == 5) {
                d0 = (double) x + block.getBlockBoundsMaxX() + (double) f;
            }

            effectRenderer.addEffect((new EntityCropDigFX(icon, world, d0, d1, d2, x, y, z, block, world.getBlockMetadata(x, y, z))).applyColourMultiplier(x, y, z).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
        }

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer) {
        byte b0 = 4;
        for (int i1 = 0; i1 < b0; ++i1) {
            for (int j1 = 0; j1 < b0; ++j1) {
                for (int k1 = 0; k1 < b0; ++k1) {
                    double d0 = (double) x + ((double) i1 + 0.5D) / (double) b0;
                    double d1 = (double) y + ((double) j1 + 0.5D) / (double) b0;
                    double d2 = (double) z + ((double) k1 + 0.5D) / (double) b0;
                    effectRenderer.addEffect((new EntityCropDigFX(icon, world, d0, d1, d2, d0 - (double) x - 0.5D, d1 - (double) y - 0.5D, d2 - (double) z - 0.5D, world.getBlock(x, y, z), meta)).applyColourMultiplier(x, y, z));
                }
            }
        }

        return true;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        int meta = world.getBlockMetadata(pos);
        if (meta == WITHERED || meta == WITHERED_DOUBLE) return false; //If Withered with suck!

        if (player.isSneaking()) return false;
        else {
            PlantSection section = getSection(world, pos);
            ICropData data = HFApi.CROPS.getCropAtLocation(world, pos);
            if (data == null || data.getCrop().requiresSickle() || data.getCrop().growsToSide() != null) {
                return false;
            } else {
                if (section == PlantSection.BOTTOM) {
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
            ICrop crop = HFApi.CROPS.getCropAtLocation(world, pos).getCrop();
            if (!crop.getSoilHandler().canSustainPlant(soil, world, pos, this)) {
                world.setBlockToAir(pos);
            }
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        int meta = world.getBlockMetadata(pos);
        if (meta == WITHERED || meta == WITHERED_DOUBLE) return world.setBlockToAir(pos); //JUST KILL IT IF WITHERED

        PlantSection section = getSection(world, pos);
        ICropData data = HFApi.CROPS.getCropAtLocation(world, pos);
        if (data == null) {
            return super.removedByPlayer(state, world, pos, player, willHarvest);
        }

        ICrop crop = data.getCrop();
        boolean isSickle = player.getActiveItemStack() != null && player.getActiveItemStack().getItem() instanceof IBreakCrops;
        if (isSickle || !crop.requiresSickle()) {
            if (section == PlantSection.BOTTOM) {
                harvestCrop(player, world, pos);
            } else harvestCrop(player, world, pos.down());
        }

        return world.setBlockToAir(pos);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (meta == FRESH || meta == WITHERED) {
            HFTrackers.getCropTracker().removeCrop(world, pos);
        } else if (meta == FRESH_DOUBLE || meta == WITHERED_DOUBLE) {
            world.setBlockToAir(pos.down());
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CROP_AABB;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        int meta = world.getBlockMetadata(pos);
        if (meta == WITHERED) return new ItemStack(Blocks.DEADBUSH); //It's Dead soo???

        ICropData data = HFApi.CROPS.getCropAtLocation(world, pos);
        return SeedHelper.getSeedsFromCrop(data.getCrop());
    }

    /*@Override
    @SideOnly(Side.CLIENT) //TODO
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return meta == WITHERED ? 0x333333 : super.colorMultiplier(world, x, y, z); //Darken the dead shit
    }*/

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    @Override
    public Block getPlant(IBlockAccess world, BlockPos pos) {
        return world.getBlock(pos);
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, BlockPos pos) {
        return 0;
    }

    //Can Apply Bonemeal (Not Fully Grown)
    @Override
    public boolean func_149851_a(World world, BlockPos pos, boolean isRemote) {
        int meta = world.getBlockMetadata(pos);
        if (meta == WITHERED) return false; //It's dead it can't grow...

        if (Crops.ENABLE_BONEMEAL) {
            return HFTrackers.getCropTracker().canBonemeal(world, pos);
        } else return false;
    }

    /* Only called server side **/
    //Whether the bonemeal has been used and we should call the function below to make the plant grow
    @Override
    public boolean func_149852_a(World world, Random rand, BlockPos pos) {
        return true;
    }

    /* Only called server side **/
    //Apply the bonemeal and grow!
    @Override
    public void func_149853_b(World world, Random rand, BlockPos pos) {
        HFTrackers.getCropTracker().grow(world, pos);
    }

    @Override
    public boolean canFeedAnimal(IAnimalTracked tracked, World world, BlockPos pos) {
        if (AnimalHelper.eatsGrass(tracked)) {
            ICropData crop = HFApi.CROPS.getCropAtLocation(world, pos);
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
}