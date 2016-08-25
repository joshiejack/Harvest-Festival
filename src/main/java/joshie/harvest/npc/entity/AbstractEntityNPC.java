package joshie.harvest.npc.entity;

import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.api.npc.INPCRegistry;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableProvider;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.core.helpers.TownHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.npc.entity.ai.*;
import joshie.harvest.town.TownData;
import joshie.harvest.town.TownDataServer;
import joshie.harvest.town.TownTracker;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import java.util.UUID;

import static joshie.harvest.api.npc.INPC.Location.HOME;
import static joshie.harvest.core.handlers.GuiHandler.GIFT;

public abstract class AbstractEntityNPC<E extends AbstractEntityNPC> extends EntityAgeable implements IEntityAdditionalSpawnData, IRelatableProvider {
    protected AbstractTask task; //Currently executing task
    protected NPC npc;
    protected AbstractEntityNPC lover;
    protected EntityPlayer talkingTo;
    protected boolean isPlaying;
    protected Mode mode = Mode.DEFAULT;
    protected BlockPos spawned;
    protected TownData homeTown;
    protected UUID townID;

    public enum Mode {
        DEFAULT, GIFT
    }

    public AbstractEntityNPC(E entity) {
        this(entity.worldObj, entity.npc);
        npc = entity.getNPC();
        lover = entity.lover;
    }

    @SuppressWarnings("unchecked")
    public <T extends TownData> T getHomeTown() {
        if (homeTown == null) {
            if (townID != null) homeTown = TownHelper.getTownByID(worldObj, townID);
            else {
                homeTown = TownHelper.getClosestTownToEntity(this);
                townID = homeTown.getID(); //Set the town id when we create one
            }
        }

        return (T) homeTown;
    }

    public AbstractEntityNPC(World world) {
        this(world, (NPC) HFNPCs.MAYOR);
    }

    public AbstractEntityNPC(World world, NPC npc) {
        super(world);
        this.npc = npc;
        this.enablePersistence();
        setSize(0.6F, (1.8F * npc.getHeight()));
    }

    protected abstract E getNewEntity(E entity);

    public void resetSpawnHome() {
        TownHelper.ensureTownExists(worldObj, new BlockPos(this)); //Force a town to exist near this entity
        this.homeTown = TownHelper.getClosestTownToEntity(this);
        //A town cannot exist without a builder
        if (this.homeTown == TownTracker.NULL_TOWN) this.setDead();
        else {
            this.townID = homeTown.getID();
            this.spawned = homeTown.getCoordinatesFor(getNPC().getLocation(HOME));
            if (this.spawned == null) {
                this.spawned = new BlockPos(this);
            }
        }
    }

    public void setSpawnHome(TownData data) {
        this.homeTown = data;
        if (this.homeTown == TownTracker.NULL_TOWN) this.setDead();
        else {
            this.townID = homeTown.getID();
            this.spawned = homeTown.getCoordinatesFor(getNPC().getLocation(HOME));
            if (this.spawned == null) {
                this.spawned = new BlockPos(this);
            }
        }
    }

    @Override
    public IRelatable getRelatable() {
        return npc;
    }

    public AbstractTask getTask() {
        return task;
    }

    public void setTask(AbstractTask task) {
        this.task = task;
    }

    @Override
    protected void initEntityAI() {
        ((PathNavigateGround) this.getNavigator()).setEnterDoors(true);
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAITalkingTo(this));
        tasks.addTask(1, new EntityAILookAtPlayer(this));
        tasks.addTask(4, new EntityAIOpenDoor(this, true));
        tasks.addTask(5, new EntityAISchedule(this));
        tasks.addTask(6, new EntityAITask(this));
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        tasks.addTask(9, new EntityAIWatchClosest(this, AbstractEntityNPC.class, 5.0F, 0.02F));
        tasks.addTask(9, new EntityAIWander(this, 0.6D));
        tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    public NPC getNPC() {
        return npc;
    }

    public AbstractEntityNPC getLover() {
        return lover;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    @Override
    public String getName() {
        return npc.getLocalizedName();
    }

    public boolean isTalking() {
        return talkingTo == null;
    }

    public void setTalking(EntityPlayer player) {
        talkingTo = player;
    }

    public EntityPlayer getTalkingTo() {
        return talkingTo;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public boolean isChild() {
        return npc.getAge() == INPCRegistry.Age.CHILD;
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    @Override
    protected void onDeathUpdate() {
        super.onDeathUpdate();
    }

    @Override
    public void onDeath(DamageSource cause) {
        if (!worldObj.isRemote && this.posY >= -32D) {
            //Respawn a new bugger
            if (npc.respawns()) {
                this.<TownDataServer>getHomeTown().markNPCDead(getNPC().getRegistryName());
                HFTrackers.markDirty(worldObj); //Mark this npc as dead, ready for tomorrow to be reborn
            }
        }

        super.onDeath(cause);
    }

    public BlockPos getHomeCoordinates() {
        BlockPos home = NPCHelper.getHomeForEntity(this);
        if (home == null) home = spawned;
        return home;
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        ItemStack held = player.getHeldItem(hand);
        boolean flag = held != null && held.getItem() == Items.SPAWN_EGG;
        if (!flag && isEntityAlive()) {
            if (!worldObj.isRemote) {
                int guiID = NPCHelper.getGuiIDForNPC(npc, worldObj, player, player.isSneaking() && held != null);
                int third = guiID == GIFT ? player.getActiveHand().ordinal() : -1;
                player.openGui(HarvestFestival.instance, guiID, worldObj, getEntityId(), -1, third);
                setTalking(player);
            }

            return true;
        } else {
            return super.processInteract(player, hand, stack);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        spawned = NBTHelper.readBlockPos("Original", nbt);
        townID = NBTHelper.readUUID("Town", nbt);
        npc = NPCRegistry.REGISTRY.getObject(new ResourceLocation(nbt.getString("NPC")));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        if (npc != null) {
            nbt.setString("NPC", npc.getRegistryName().toString());
        }

        NBTHelper.writeUUID("Town", nbt, townID);
        NBTHelper.writeBlockPos("Original", nbt, spawned);
    }

    @Override
    public void writeSpawnData(ByteBuf buf) {
        buf.writeBoolean(npc != null);
        if (npc != null) {
            ByteBufUtils.writeUTF8String(buf, npc.getRegistryName().toString());
        }

        ByteBufUtils.writeUTF8String(buf, townID.toString());
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        String name = buf.readBoolean() ? ByteBufUtils.readUTF8String(buf) : "";
        npc = name.equals("") ? (NPC) HFNPCs.MAYOR : NPCRegistry.REGISTRY.getObject(new ResourceLocation(name));
        townID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return getNewEntity((E)ageable);
    }
}