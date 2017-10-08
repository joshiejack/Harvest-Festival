package joshie.harvest.animals.entity;

import io.netty.buffer.ByteBuf;
import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.entity.ai.EntityAIEatLivestock;
import joshie.harvest.animals.entity.ai.EntityAIFindShelterOrSun;
import joshie.harvest.animals.item.ItemAnimalTool.Tool;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalAction;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.animals.IAnimalHandler.AnimalType;
import joshie.harvest.core.entity.EntityBasket;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.api.animals.IAnimalHandler.ANIMAL_STATS_CAPABILITY;
import static joshie.harvest.core.helpers.InventoryHelper.ITEM;
import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;

public class EntityHarvestSheep extends EntitySheep implements IEntityAdditionalSpawnData {
    private final AnimalStats<NBTTagCompound> stats = HFApi.animals.newStats(AnimalType.LIVESTOCK);
    private static ItemStack[] stacks;

    public EntityHarvestSheep(World world) {
        super(world);
        setSize(1.4F, 1.4F);
        setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    protected void initEntityAI() {
        entityAIEatGrass = new EntityAIEatGrass(this);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 2.0D));
        tasks.addTask(2, new EntityAITempt(this, 1.25D, Items.WHEAT, false));
        tasks.addTask(3, new EntityAIFollowParent(this, 1.25D));
        tasks.addTask(4, new EntityAIEatLivestock(this));
        tasks.addTask(5, new EntityAIFindShelterOrSun(this));
        tasks.addTask(6, new EntityAIWander(this, 1.0D));
        tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
    }

    private static ItemStack[] getStacks() {
        if (stacks != null) return stacks;
        stacks = new ItemStack[] {
                HFAnimals.TOOLS.getStackFromEnum(Tool.BRUSH),
                HFAnimals.TOOLS.getStackFromEnum(Tool.MEDICINE),
                HFAnimals.TOOLS.getStackFromEnum(Tool.MIRACLE_POTION)
        };

        return stacks;
    }

    @Override
    public boolean processInteract(@Nullable EntityPlayer player, @Nullable EnumHand hand, ItemStack stack) {
        if (player == null) return false;
        boolean special = ITEM_STACK.matchesAny(stack, getStacks()) || ITEM.matchesAny(stack, HFAnimals.TREATS, Items.SHEARS);
        if (stack == null || !special) {
            if (!stats.performTest(AnimalTest.BEEN_LOVED)) {
                stats.performAction(world, null, AnimalAction.PETTED); //Love <3
                SoundEvent s = getAmbientSound();
                if (s != null) {
                    playSound(s, 2F, getSoundPitch());
                }

                return true;
            } else return false;
        } else return false;
    }

    @Override
    @Nonnull
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess blockAccess, BlockPos pos, int fortune) {
        List<ItemStack> ret = new ArrayList<>();
        EntityPlayer player = world.getClosestPlayerToEntity(this, 178D);
        if (!isChild()) {
            if (player != null) {
                ret.add(stats.getType().getProduct(stats));
                if (!world.isRemote) {
                    setSheared(true);
                    stats.setProduced(stats.getProductsPerDay());
                }

                playSound(SoundEvents.ENTITY_SHEEP_SHEAR, 1.0F, 1.0F);
            }
        }

        if (player != null) {
            EntityBasket.findBasketAndShip(player, ret);
        }

        return ret;
    }

    @Override
    @Nonnull
    public EntitySheep createChild(EntityAgeable ageable) {
        return new EntityHarvestSheep(world);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing) {
        return capability == ANIMAL_STATS_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked, ConstantConditions")
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing) {
        return capability == ANIMAL_STATS_CAPABILITY ? (T) stats : super.getCapability(capability, facing);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("Stats", stats.serializeNBT());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("Stats")) stats.deserializeNBT(compound.getCompoundTag("Stats"));
        //TODO: Remove in 0.7+
        else if (compound.hasKey("CurrentLifespan")) stats.deserializeNBT(compound);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        ByteBufUtils.writeTag(buffer, stats.serializeNBT());
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        stats.setEntity(this);
        stats.deserializeNBT(ByteBufUtils.readTag(buffer));
    }
}
