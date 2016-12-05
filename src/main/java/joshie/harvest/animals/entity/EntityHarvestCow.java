package joshie.harvest.animals.entity;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.entity.ai.EntityAIEatLivestock;
import joshie.harvest.animals.entity.ai.EntityAIFindShelterOrSun;
import joshie.harvest.animals.item.ItemAnimalTool.Tool;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.IAnimalHandler.AnimalType;
import joshie.harvest.api.player.RelationshipType;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.player.relationships.RelationshipData;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import java.util.UUID;

import static joshie.harvest.api.animals.IAnimalHandler.ANIMAL_STATS_CAPABILITY;
import static joshie.harvest.core.helpers.InventoryHelper.ITEM;
import static joshie.harvest.core.helpers.InventoryHelper.ITEM_STACK;

public class EntityHarvestCow extends EntityCow {
    private final AnimalStats<NBTTagCompound> stats = HFApi.animals.newStats(AnimalType.MILKABLE);
    private static ItemStack[] stacks;

    public EntityHarvestCow(World world) {
        super(world);
        setSize(1.4F, 1.4F);
        setPathPriority(PathNodeType.WATER, 0.0F);
    }

    @Override
    protected void initEntityAI() {
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
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
    }

    private static ItemStack[] getStacks() {
        if (stacks != null) return stacks;
        stacks = new ItemStack[] {
                HFAnimals.TOOLS.getStackFromEnum(Tool.BRUSH),
                HFAnimals.TOOLS.getStackFromEnum(Tool.MILKER),
                HFAnimals.TOOLS.getStackFromEnum(Tool.MEDICINE),
                HFAnimals.TOOLS.getStackFromEnum(Tool.MIRACLE_POTION)
        };

        return stacks;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if (player == null) return false;
        boolean special = ITEM_STACK.matchesAny(stack, getStacks()) || ITEM.matchesAny(stack, HFAnimals.TREATS);
        if (stack == null || !special) {
            RelationshipData data = HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships();
            UUID uuid = EntityHelper.getEntityUUID(this);
            if (data.hasTalked(uuid)) return false;
            else {
                HFTrackers.getPlayerTrackerFromPlayer(player).getRelationships().talkTo(RelationshipType.ANIMAL, player, EntityHelper.getEntityUUID(this));
                SoundEvent s = getAmbientSound();
                if (s != null) {
                    playSound(s, 2F, getSoundPitch());
                }

                return true;
            }
        } else return true;
    }

    @Override
    public EntityCow createChild(EntityAgeable ageable) {
        return new EntityHarvestCow(this.worldObj);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == ANIMAL_STATS_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
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
}