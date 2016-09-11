package joshie.harvest.crops.block;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.animals.IAnimalFeeder;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.IBreakCrops;
import joshie.harvest.api.crops.IStateHandler.PlantSection;
import joshie.harvest.core.base.block.BlockHFEnum;
import joshie.harvest.core.base.item.ItemBlockHF;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.HFEvents;
import joshie.harvest.crops.CropData;
import joshie.harvest.crops.CropHelper;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockHFCrops.Stage;
import joshie.harvest.crops.tile.TileCrop;
import joshie.harvest.crops.tile.TileCrop.TileWithered;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;
import java.util.Random;

import static joshie.harvest.api.crops.IStateHandler.PlantSection.BOTTOM;
import static joshie.harvest.api.crops.IStateHandler.PlantSection.TOP;
import static joshie.harvest.core.network.PacketHandler.sendRefreshPacket;
import static joshie.harvest.crops.CropHelper.WET_SOIL;
import static joshie.harvest.crops.CropHelper.harvestCrop;
import static joshie.harvest.crops.block.BlockHFCrops.Stage.*;

public class BlockHFCrops extends BlockHFEnum<BlockHFCrops, Stage> implements IPlantable, IGrowable, IAnimalFeeder {
       public enum Stage implements IStringSerializable {
        FRESH(false, BOTTOM), WITHERED(false, BOTTOM), FRESH_DOUBLE(false, TOP), WITHERED_DOUBLE(true, TOP);

        private final boolean isWithered;
        private final PlantSection section;

        Stage(boolean isWithered, PlantSection section) {
            this.isWithered = isWithered;
            this.section = section;
        }

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }

        public boolean isWithered() {
            return isWithered;
        }

        public PlantSection getSection() {
            return section;
        }
    }

    public static final AxisAlignedBB CROP_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

    public BlockHFCrops() {
        super(Material.PLANTS, Stage.class, null);
        setBlockUnbreakable();
        setSoundType(SoundType.GROUND);
        setTickRandomly(HFCrops.ALWAYS_GROW);
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
        if (!world.isRemote) {
            if (HFCrops.ALWAYS_GROW) {
                Stage stage = getEnumFromState(state);
                if (stage == Stage.WITHERED) return; //If withered do nothing
                if (world.getLightFromNeighbors(pos.up()) >= 9) {
                    if (rand.nextInt(20) == 0) {
                        //We are Growing!
                        grow(world, rand, pos, state);
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    //Return 0.75F if the plant isn't withered, otherwise, unbreakable!!!
    public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world, BlockPos pos) {
        Stage stage = getEnumFromState(state);
        ItemStack held = player.getHeldItemMainhand();
        CropData data = CropHelper.getCropDataAt(world, pos);
        if (data.getCrop().growsToSide() != null && data.getStage() == data.getCrop().getStages()) {
            return 0F; //If the crop is fully grown, and grows to the side. Make it immune to destruction.
        }

        //If this crop is withered return 0
        if (held == null || (!(held.getItem() instanceof IBreakCrops))) return stage.isWithered() ? 0 : 0.75F;
        return ((IBreakCrops) held.getItem()).getStrengthVSCrops(player, world, pos, state, held);
    }

    public static PlantSection getSection(IBlockState state) {
        if (state.getBlock() != HFCrops.CROPS) return null;
        int stage = state.getBlock().getMetaFromState(state); //Can't get the Enum from state, because this method is static.
        PlantSection section = BOTTOM;
        if (stage == Stage.WITHERED_DOUBLE.ordinal() || stage == Stage.FRESH_DOUBLE.ordinal()) {
            section = PlantSection.TOP;
        }

        return section;
    }

    public static boolean isWithered(IBlockState state) {
        int stage = state.getBlock().getMetaFromState(state); //Can't get the Enum from state, because this method is static.
        return stage == Stage.WITHERED.ordinal() || stage == Stage.WITHERED_DOUBLE.ordinal();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    public boolean canStay(World world, BlockPos pos) {
        Crop crop = HFApi.crops.getCropAtLocation(world, pos);
        if (crop != null && crop.getGrowthHandler() != null) {
            IBlockState down = world.getBlockState(pos.down());
            return down.getBlock() == this || crop.getGrowthHandler().canSustainCrop(world, pos.down(), world.getBlockState(pos.down()), crop);
        } else return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        Stage stage = getEnumFromState(state);
        if (stage == Stage.WITHERED || stage == Stage.WITHERED_DOUBLE) return false; //If Withered with suck!
        if (player.isSneaking()) return false;
        else {
            PlantSection section = getSection(state);
            CropData data = CropHelper.getCropDataAt(world, pos);
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

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock) {
        if (!world.isRemote) {
            if (!canStay(world, pos)) {
                world.setBlockToAir(pos);
            }
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        Stage stage = getEnumFromState(state);
        if (stage == Stage.WITHERED || stage == Stage.WITHERED_DOUBLE)
            return world.setBlockToAir(pos); //JUST KILL IT IF WITHERED

        PlantSection section = getSection(state);
        CropData data = CropHelper.getCropDataAt(world, pos);
        if (data == null) {
            return super.removedByPlayer(state, world, pos, player, willHarvest);
        }

        Crop crop = data.getCrop();
        boolean isSickle = player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof IBreakCrops;
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
        if (stage == FRESH || stage == Stage.WITHERED) {
            if (world.getBlockState(pos.up()).getBlock() == this) {
                Stage above = getEnumFromState(world.getBlockState(pos.up()));
                if (above == FRESH_DOUBLE || stage == WITHERED_DOUBLE) {
                    world.setBlockToAir(pos.up());
                }
            }
        } else if (stage == Stage.FRESH_DOUBLE || stage == Stage.WITHERED_DOUBLE) {
            world.setBlockToAir(pos.down());
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        Stage stage = getEnumFromState(state);
        AxisAlignedBB aabb = CropHelper.getCropBoundingBox(world, pos, stage.getSection(), stage.isWithered());
        return aabb != null ? aabb : CROP_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        if (getEnumFromState(state) == Stage.WITHERED) return new ItemStack(Blocks.DEADBUSH); //It's Dead soo???

        CropData data = CropHelper.getCropDataAt(world, pos);
        return HFCrops.SEEDS.getStackFromCrop(data.getCrop());
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        Stage stage = getEnumFromState(state);
        if (stage.getSection() == TOP) {
            return CropHelper.getBlockState(world, pos.down(), stage.getSection(), stage.isWithered());
        } else  {
            return CropHelper.getBlockState(world, pos, stage.getSection(), stage.isWithered());
        }
    }

    public static class RainingSoil {
        private int existence;
        private World world;
        private BlockPos pos;

        public RainingSoil(World world, BlockPos pos) {
            this.world = world;
            this.pos = pos;
        }

        @SubscribeEvent
        public void onTick(TickEvent.WorldTickEvent event) {
            if (event.world != world) return;
            boolean remove = existence >= 30;
            if (remove) {
                world.setBlockState(pos, WET_SOIL);
                MinecraftForge.EVENT_BUS.unregister(this);
            }

            existence++;
        }
    }

    @HFEvents
    public static class EventHandler {
        @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
        public void onDirtTilled(UseHoeEvent event) {
            if (!event.getWorld().isRemote && !event.isCanceled()) {
                if (HFTrackers.getCalendar(event.getWorld()).getTodaysWeather().isRain()) {
                    MinecraftForge.EVENT_BUS.register(new RainingSoil(event.getWorld(), event.getPos()));
                }

                HFApi.tickable.addTickable(event.getWorld(), event.getPos(), HFApi.tickable.getTickableFromBlock(Blocks.FARMLAND));
            }
        }

        @SubscribeEvent
        public void onBlockBreak(BreakEvent event) {
            IBlockState above = event.getWorld().getBlockState(event.getPos().up());
            if (above .getBlock() == HFCrops.CROPS) {
                event.setCanceled(true);
            }
        }
    }

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
        if (HFCrops.ENABLE_BONEMEAL) {
            if (HFCrops.SEASONAL_BONEMEAL) {
                CropData data = CropHelper.getCropDataAt(world, pos);
                if (data.getCrop().getGrowthHandler().canGrow(world, pos, data.getCrop())) {
                    return canGrow(world, pos, state);
                }

                return false;
            } else return canGrow(world, pos, state);
        } else return false;
    }

    public boolean canGrow(World world, BlockPos pos, IBlockState state) {
        TileCrop crop = getEnumFromState(state).section == TOP ? (TileCrop) world.getTileEntity(pos.down()): (TileCrop) world.getTileEntity(pos);
        return crop.getData().getStage() < crop.getData().getCrop().getStages();
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
        TileCrop crop = getEnumFromState(state).section == TOP ? (TileCrop) world.getTileEntity(pos.down()): (TileCrop) world.getTileEntity(pos);
        crop.getData().grow(world, pos);
        crop.saveAndRefresh();
        sendRefreshPacket(crop);
    }

    @Override
    public boolean feedAnimal(IAnimalTracked tracked, World world, BlockPos pos, IBlockState state) {
        if (HFApi.animals.canAnimalEatFoodType(tracked, AnimalFoodType.GRASS)) {
            CropData data = CropHelper.getCropDataAt(world, pos);
            Crop theCrop = data.getCrop();
            if (theCrop == HFCrops.GRASS) {
                int stage = data.getStage();
                if (stage > 5) {
                    HFApi.crops.plantCrop(tracked.getData().getOwner(), world, pos, theCrop, stage - 5);
                    return true;
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
    public TileEntity createTileEntity(World world, IBlockState state) {
        return getEnumFromState(state).isWithered() ? new TileWithered() : new TileCrop();
    }

    @Override
    public ItemBlockHF getItemBlock() {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(Item item, String name) {
        for (int i = 0; i < values.length; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(new ResourceLocation(HFModInfo.MODID, property.getName() + "_" + getEnumFromMeta(i).getName()), "inventory"));
        }
    }
}