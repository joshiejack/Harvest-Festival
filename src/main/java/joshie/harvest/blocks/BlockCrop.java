package joshie.harvest.blocks;

import static joshie.harvest.core.helpers.CropHelper.harvestCrop;

import java.util.Random;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.IBreakCrops;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.api.crops.ICropRenderHandler.PlantSection;
import joshie.harvest.core.config.Crops;
import joshie.harvest.core.helpers.CropHelper;
import joshie.harvest.core.helpers.DigFXHelper;
import joshie.harvest.core.helpers.SeedHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.lib.RenderIds;
import joshie.harvest.crops.Crop;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCrop extends BlockHFBase implements IPlantable, IGrowable {
    public static final int FRESH = 0;
    public static final int WITHERED = 1;
    public static final int FRESH_DOUBLE = 2;
    public static final int WITHERED_DOUBLE = 3;
    public static final int GROWN = 7;

    public BlockCrop() {
        super(Material.plants);
        setBlockUnbreakable();
        setStepSound(Block.soundTypeGrass);
        setCreativeTab(null);
        setBlockBounds(0F, 0F, 0F, 1F, 0.5F, 1F);
        setTickRandomly(Crops.ALWAYS_GROW);
        disableStats();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return RenderIds.CROPS;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {        
        PlantSection section = getSection(world, x, y, z);
        ICropData data = HFApi.CROPS.getCropAtLocation(MCClientHelper.getWorld(), x, y, z);
        data.getCrop().getCropRenderHandler().setBlockBoundsBasedOnStage(this, section, data.getCrop(), data.getStage());
    }

    //Only called if crops are set to tick randomly
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (Crops.ALWAYS_GROW) {
            if (!world.isRemote) {
                int meta = world.getBlockMetadata(x, y, z);
                if (meta == WITHERED) return; //If withered do nothing
                if (world.getBlockLightValue(x, y + 1, z) >= 9) {
                    float chance = getGrowthChance(world, x, y, z);
                    if (rand.nextInt((int) (25.0F / chance) + 1) == 0) {
                        //We are Growing!
                        CropHelper.grow(world, x, y, z);
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

                if (world.getBlock(l, y - 1, i1).canSustainPlant(world, l, y - 1, i1, ForgeDirection.UP, this)) {
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
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
        Block block = world.getBlock(x, y + 1, z);
        int aboveMeta = world.getBlockMetadata(x, y + 1, z);
        int thisMeta = world.getBlockMetadata(x, y, z);
        if (block == this) {
            if (thisMeta == FRESH) return aboveMeta == FRESH_DOUBLE;
            else if (thisMeta == WITHERED) return aboveMeta == WITHERED_DOUBLE;
        }

        return false;
    }

    @Override
    //Return 0.75F if the plant isn't withered, otherwise, unbreakable!!!
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        ItemStack held = player.getCurrentEquippedItem();
        ICropData crop = HFApi.CROPS.getCropAtLocation(world, x, y, z); 
        if(crop.getCrop().growsToSide() != null && crop.getStage() == crop.getCrop().getStages()) {
            return 0F; //If the crop is fully grown, and grows to the side. Make it immune to destruction.
        }
        
        //If this crop is withered return 0
        if (held == null || (!(held.getItem() instanceof IBreakCrops))) return meta == WITHERED? 0 : 0.75F;
        return ((IBreakCrops) held.getItem()).getStrengthVSCrops(player, world, x, y, z, held);
    }

    public static PlantSection getSection(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        PlantSection section = PlantSection.BOTTOM;
        if (meta == WITHERED_DOUBLE || meta == FRESH_DOUBLE) {
            section = PlantSection.TOP;
        }

        return section;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return HFApi.CROPS.getCropAtLocation(MCClientHelper.getWorld(), x, y, z).getCropIcon(BlockCrop.getSection(world, x, y, z));
    }

    @Override
    public Item getItemDropped(int meta, Random rand, int side) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer) {
        DigFXHelper.addBlockHitEffects(world, target.blockX, target.blockY, target.blockZ, target.sideHit, effectRenderer);
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        IIcon icon = HFApi.CROPS.getCropAtLocation(world, x, y, z).getCropIcon(getSection(world, x, y, z));
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == WITHERED || meta == WITHERED_DOUBLE) return false; //If Withered with suck!

        if (player.isSneaking()) return false;
        else {
            PlantSection section = getSection(world, x, y, z);
            ICropData data = HFApi.CROPS.getCropAtLocation(world, x, y, z);
            if (data == null || data.getCrop().requiresSickle() || data.getCrop().growsToSide() != null) {
                return false;
            } else {
                if (section == PlantSection.BOTTOM) {
                    return harvestCrop(player, world, x, y, z);
                } else {
                    return harvestCrop(player, world, x, y - 1, z);
                }
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block blockz) {
        if (!world.isRemote) {
            Block block = world.getBlock(x, y - 1, z);
            ICrop crop = HFApi.CROPS.getCropAtLocation(world, x, y, z).getCrop();
            if (!crop.getSoilHandler().canSustainPlant(world, x, y, z, this)) {
                world.setBlockToAir(x, y, z);
            }
        }
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == WITHERED || meta == WITHERED_DOUBLE) return world.setBlockToAir(x, y, z); //JUST KILL IT IF WITHERED

        PlantSection section = getSection(world, x, y, z);
        ICropData data = HFApi.CROPS.getCropAtLocation(world, x, y, z);
        if (data == null) {
            return super.removedByPlayer(world, player, x, y, z, willHarvest);
        }

        ICrop crop = data.getCrop();
        boolean isSickle = player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof IBreakCrops;
        if (isSickle || !crop.requiresSickle()) {
            if (section == PlantSection.BOTTOM) {
                harvestCrop(player, world, x, y, z);
            } else harvestCrop(player, world, x, y - 1, z);
        }

        return world.setBlockToAir(x, y, z);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (meta == FRESH || meta == WITHERED) {
            CropHelper.removeCrop(world, x, y, z);
        } else if (meta == FRESH_DOUBLE || meta == WITHERED_DOUBLE) {
            world.setBlockToAir(x, y - 1, z);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == WITHERED) return new ItemStack(Blocks.deadbush); //It's Dead soo???

        ICropData data = HFApi.CROPS.getCropAtLocation(world, x, y, z);
        return SeedHelper.getSeedsFromCrop(data.getCrop());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        return meta == WITHERED ? 0x333333 : super.colorMultiplier(world, x, y, z); //Darken the dead shit
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        for (ICrop crop : Crop.crops) {
            crop.getCropRenderHandler().registerIcons(register);
        }
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return EnumPlantType.Crop;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z) {
        return world.getBlock(x, y, z);
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
        return 0;
    }

    //Can Apply Bonemeal (Not Fully Grown)
    @Override
    public boolean func_149851_a(World world, int x, int y, int z, boolean isRemote) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == WITHERED) return false; //It's dead it can't grow...

        if (Crops.ENABLE_BONEMEAL) {
            return CropHelper.canBonemeal(world, x, y, z);
        } else return false;
    }

    /* Only called server side **/
    //Whether the bonemeal has been used and we should call the function below to make the plant grow
    @Override
    public boolean func_149852_a(World world, Random rand, int x, int y, int z) {
        return true;
    }

    /* Only called server side **/
    //Apply the bonemeal and grow!
    @Override
    public void func_149853_b(World world, Random rand, int x, int y, int z) {
        CropHelper.grow(world, x, y, z);
    }
}
