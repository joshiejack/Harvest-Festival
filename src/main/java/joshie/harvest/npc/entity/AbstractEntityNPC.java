package joshie.harvest.npc.entity;

import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.api.npc.INPCRegistry;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableProvider;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.core.helpers.TownHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.npc.entity.ai.*;
import joshie.harvest.town.TownData;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityZombie;
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

public abstract class AbstractEntityNPC<E extends AbstractEntityNPC> extends EntityAgeable implements IEntityAdditionalSpawnData, IRelatableProvider {
    protected AbstractTask task; //Currently executing task
    protected NPC npc;
    protected AbstractEntityNPC lover;
    protected EntityPlayer talkingTo;
    protected boolean isPlaying;
    protected Mode mode = Mode.DEFAULT;
    protected int lastTeleport;
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

    public TownData getHomeTown() {
        if (homeTown == null) {
            if (townID != null) homeTown = TownHelper.getTownByID(worldObj, townID);
            else {
                homeTown = TownHelper.getClosestTownToEntityOrCreate(this);
                townID = homeTown.getID(); //Set the town id when we create one
            }
        }

        return homeTown;
    }

    public AbstractEntityNPC(World world) {
        this(world, (NPC) HFNPCs.GODDESS);
    }

    public AbstractEntityNPC(World world, NPC npc) {
        super(world);
        this.npc = npc;
        this.enablePersistence();
        setSize(0.6F, (1.8F * npc.getHeight()));
    }

    protected abstract E getNewEntity(E entity);

    public void resetSpawnHome() {
        this.homeTown = TownHelper.getClosestTownToEntityOrCreate(this);
        this.townID = homeTown.getID();
        this.spawned = homeTown.getCoordinatesFor(getNPC().getHome());
        if (this.spawned == null) {
            this.spawned = new BlockPos(this);
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
    public void onUpdate() {
        if (!joshie.harvest.core.config.NPC.FREZZE_NPCS) {
            super.onUpdate();
        }
    }

    @Override
    protected void initEntityAI() {
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIAvoidEntity<EntityZombie>(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        tasks.addTask(1, new EntityAITalkingTo(this));
        tasks.addTask(1, new EntityAILookAtPlayer(this));
        tasks.addTask(2, new AIEntityNPC(this));
        tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        tasks.addTask(4, new EntityAIOpenDoor(this, true));
        tasks.addTask(8, new joshie.harvest.npc.entity.ai.EntityAIPlay(this, 0.32D));
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

    public ResourceLocation getSkin() {
        return new ResourceLocation(npc.getRegistryName().getResourceDomain() + ":" + "textures/entity/" + npc.getRegistryName().getResourcePath() + ".png");
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
        //Respawn a new bugger
        if (npc.respawns()) {
            E clone = getNewEntity((E) this);
            BlockPos home = getHomeCoordinates();
            if (home != null) {
                clone.setPositionAndUpdate(home.getX() + 0.5D, home.getY() + 0.5D, home.getZ() + 0.5D);
                clone.resetSpawnHome();
                worldObj.spawnEntityInWorld(clone);
            }
        }

        if (!this.dead) {
            this.dead = true;
            this.getCombatTracker().reset();
            this.worldObj.setEntityState(this, (byte)3);
        }
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
                player.openGui(HarvestFestival.instance, NPCHelper.getGuiIDForNPC(npc, worldObj, player, player.isSneaking() && held != null), worldObj, getEntityId(), -1, player.getActiveHand().ordinal());
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
        if (npc != null) {
            char[] name = npc.getRegistryName().toString().toCharArray();
            buf.writeByte(name.length);
            for (char c : name) {
                buf.writeChar(c);
            }
        }

        ByteBufUtils.writeUTF8String(buf, townID.toString());
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        char[] name = new char[buf.readByte()];
        for (int i = 0; i < name.length; i++) {
            name[i] = buf.readChar();
        }

        npc = NPCRegistry.REGISTRY.getObject(new ResourceLocation(new String(name)));
        if (npc == null) {
            npc = (NPC) HFNPCs.GODDESS;
        }

        townID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return getNewEntity((E)ageable);
    }
}