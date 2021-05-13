package uk.joshiejack.settlements.entity;

import com.google.common.collect.Sets;
import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.client.animation.Animation;
import uk.joshiejack.settlements.entity.ai.EntityAIActionQueue;
import uk.joshiejack.settlements.entity.ai.EntityAISchedule;
import uk.joshiejack.settlements.entity.ai.EntityAITalkingTo;
import uk.joshiejack.settlements.entity.ai.action.chat.ActionGreet;
import uk.joshiejack.settlements.event.NPCEvent;
import uk.joshiejack.settlements.network.npc.PacketSetAnimation;
import uk.joshiejack.settlements.npcs.Age;
import uk.joshiejack.settlements.npcs.DynamicNPC;
import uk.joshiejack.settlements.npcs.NPC;
import uk.joshiejack.settlements.npcs.NPCInfo;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class EntityNPC extends EntityAgeable implements IEntityAdditionalSpawnData {
    public float renderOffsetX, renderOffsetY, renderOffsetZ;
    protected NBTTagCompound custom;
    protected NPC npc;
    protected NPCInfo info;
    private final Set<EntityPlayer> talkingTo = Sets.newHashSet();
    private int town = 0; //Default unset value
    private Animation animation;
    private EntityAIActionQueue physicalAI;
    private EntityAIActionQueue mentalAI;
    private int lifespan;
    private boolean lootDisabled;

    public EntityNPC(World world) {
        this(world, NPC.NULL_NPC, null);
    }

    public EntityNPC(World world, @Nonnull NPC npc, @Nullable NBTTagCompound customData) {
        super(world);
        this.npc = npc;
        this.custom = customData == null ? new NBTTagCompound() : customData;
        this.info = custom.isEmpty() ? npc : DynamicNPC.fromTag(custom);
        this.enablePersistence();
        this.initNPCData();
        setPathPriority(PathNodeType.WATER, -1.0F);
    }

    public EntityNPC(EntityNPC entity) {
        this(entity.world, entity.npc, entity.custom);
    }

    public void setDropItemsWhenDead(boolean dropWhenDead) {
        this.lootDisabled = !dropWhenDead;
    }

    private void initNPCData() {
        if (info == null || info.getNPCClass() == null) this.setDead();
        else {
            this.lifespan = info.getNPCClass().getLifespan();
            float modifier = info.getNPCClass().getHeight();
            setSize(0.6F * modifier, 1.6F * modifier);
            setEntityInvulnerable(info.getNPCClass().isInvulnerable());
            this.noClip = info.getNPCClass().isImmovable();
            this.setNoGravity(info.getNPCClass().isImmovable());
        }
    }

    public NBTTagCompound getCustomData() {
        return custom;
    }

    @Override
    protected boolean canDropLoot() {
        return super.canDropLoot() && !lootDisabled;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld(); //Call the server trackers for the spawning of this npc
        if (!world.isRemote) {
            info.callScript("onNPCSpawned", this);
        }
    }

    @Override
    protected void initEntityAI() {
        ((PathNavigateGround) getNavigator()).setEnterDoors(true);
        ((PathNavigateGround) getNavigator()).setBreakDoors(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(0, new EntityAITalkingTo(this));
        tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(4, new EntityAIOpenDoor(this, true));
        physicalAI = new EntityAIActionQueue(this, 1);
        mentalAI = new EntityAIActionQueue(this, 1928);
        tasks.addTask(6, mentalAI);
        tasks.addTask(6, physicalAI);
        tasks.addTask(7, new EntityAISchedule(this));
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityNPC.class, 5.0F, 0.02F));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entityIn) {
        float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;

        if (entityIn instanceof EntityLivingBase) {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag) {
            if (i > 0) {
                ((EntityLivingBase) entityIn).knockBack(this, (float) i * 0.5F, MathHelper.sin(this.rotationYaw * 0.017453292F), -MathHelper.cos(this.rotationYaw * 0.017453292F));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0) {
                entityIn.setFire(j * 4);
            }

            if (entityIn instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) entityIn;
                ItemStack itemstack = getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer)) {
                    float f1 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
                    if (rand.nextFloat() < f1) {
                        entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
                        world.setEntityState(entityplayer, (byte) 30);
                    }
                }
            }

            applyEnchantments(this, entityIn);
        }

        return flag;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return npc.getLootTable();
    }

    public int getTown() {
        //If we have the default town id, grab the closest town to us and SET it
        if (town == 0 && !dead) {
            town = TownFinder.find(world, getPosition()).getID();
        }

        return town;
    }

    public EntityAIActionQueue getMentalAI() {
        return mentalAI;
    }

    public EntityAIActionQueue getPhysicalAI() {
        return physicalAI;
    }

    @Override
    @Nonnull
    public String getName() {
        return info.getLocalizedName();
    }

    public boolean IsNotTalkingTo(EntityPlayer player) {
        return !talkingTo.contains(player);
    }

    public void addTalking(EntityPlayer player) {
        talkingTo.add(player);
    }

    public void removeTalking(EntityPlayer player) {
        talkingTo.remove(player);
    }

    public Set<EntityPlayer> getTalkingTo() {
        return talkingTo;
    }

    @Override
    public boolean isChild() {
        return info.getNPCClass().getAge() == Age.CHILD;
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    @Override
    public boolean processInteract(@Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        boolean flag = held.getItem() == Items.SPAWN_EGG;
        if (!flag && isEntityAlive()) {
            if (!world.isRemote) {
                MinecraftForge.EVENT_BUS.post(new NPCEvent.NPCRightClickedEvent(this, player, hand));
                if (mentalAI.getCurrent() == null && mentalAI.all().isEmpty() && talkingTo.isEmpty()) {
                    //If a quest has been started by the event, we'd know this if the npc is talking
                    //If they aren't talking, open the random chat
                    mentalAI.addToEnd(new ActionGreet().withPlayer(player));
                }

                lifespan = npc.getNPCClass().getLifespan(); //Reset the lifespan
            }

            return true;
        } else {
            return super.processInteract(player, hand);
        }
    }

    @Override
    public void onLivingUpdate() {
        updateArmSwingProgress();
        super.onLivingUpdate();
        if (world.isRemote && animation != null) {
            animation.play(this);
        }

        if (!world.isRemote) {
            npc.callScript("onNPCUpdate", this);
        }

        if (!world.isRemote && talkingTo.size() == 0 && physicalAI.all().isEmpty() && mentalAI.all().isEmpty()) {
            if (npc.getNPCClass().getLifespan() > 0) {
                lifespan--;
                if (lifespan <= 0) {
                    dropLoot(false, 0, DamageSource.MAGIC);
                    this.setDead(); //Kill the bitch
                }
            }
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return npc.getNPCClass().canBreatheUnderwater();
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if (npc.getNPCClass().floats()) {
            if (isInWater()) {
                moveRelative(strafe, vertical, forward, 0.02F);
                move(MoverType.SELF, motionX, motionY, motionZ);
                motionX *= 0.800000011920929D;
                motionY *= 0.800000011920929D;
                motionZ *= 0.800000011920929D;
            } else if (isInLava()) {
                moveRelative(strafe, vertical, forward, 0.02F);
                move(MoverType.SELF, motionX, motionY, motionZ);
                motionX *= 0.5D;
                motionY *= 0.5D;
                motionZ *= 0.5D;
            } else {
                float f = 0.91F;

                if (onGround) {
                    BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                    IBlockState underState = this.world.getBlockState(underPos);
                    f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.91F;
                }

                float f1 = 0.16277136F / (f * f * f);
                moveRelative(strafe, vertical, forward, onGround ? 0.1F * f1 : 0.02F);
                f = 0.91F;

                if (onGround) {
                    BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                    IBlockState underState = this.world.getBlockState(underPos);
                    f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.91F;
                }

                move(MoverType.SELF, motionX, motionY, motionZ);
                motionX *= f;
                motionY *= f;
                motionZ *= f;
            }

            prevLimbSwingAmount = limbSwingAmount;
            limbSwingAmount = 0F;
            limbSwing = 0F;
        } else super.travel(strafe, vertical, forward);
    }

    @Override
    public void onDeath(@Nonnull DamageSource cause) {
        super.onDeath(cause);
        if (!world.isRemote) {
            TownServer town = AdventureDataLoader.get(world).getTownByID(dimension, getTown());
            this.town = 0; //Clear out this npcs town
            town.getCensus().onNPCDeath(this); //Remember my actions
            town.getCensus().onNPCsChanged(this.world); //Update invitable list
            AdventureDataLoader.get(this.world).markDirty(); //Save Stuff
        }
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        npc = NPC.getNPCFromRegistry(new ResourceLocation(nbt.getString("NPC")));
        if (npc == NPC.NULL_NPC) this.setDead(); //Kill off null npcs
        physicalAI.deserializeNBT(nbt.getTagList("PhysicalActions", 10));
        mentalAI.deserializeNBT(nbt.getTagList("MentalActions", 10));
        lootDisabled = nbt.getBoolean("LootDisabled");
        town = nbt.getInteger("Town");
        custom = nbt.getCompoundTag("Custom");
        info = custom.isEmpty() ? npc : DynamicNPC.fromTag(custom);
        initNPCData(); //Reload in the data where applicable
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setString("NPC", npc.getRegistryName().toString());
        nbt.setTag("PhysicalActions", physicalAI.serializeNBT());
        nbt.setTag("MentalActions", mentalAI.serializeNBT());
        nbt.setBoolean("LootDisabled", lootDisabled);
        nbt.setInteger("Town", town);
        nbt.setTag("Custom", custom);
    }

    @Override
    public void writeSpawnData(ByteBuf buf) {
        buf.writeBoolean(animation != null);
        if (animation != null) {
            ByteBufUtils.writeUTF8String(buf, animation.getID());
        }

        buf.writeBoolean(npc != NPC.NULL_NPC);
        ByteBufUtils.writeUTF8String(buf, npc.getRegistryName().toString());
        ByteBufUtils.writeTag(buf, custom);
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        if (buf.readBoolean()) setAnimation(ByteBufUtils.readUTF8String(buf)); //Animation bitch
        String name = buf.readBoolean() ? ByteBufUtils.readUTF8String(buf) : "";
        npc = NPC.getNPCFromRegistry(new ResourceLocation(name));
        custom = ByteBufUtils.readTag(buf);
        assert custom != null;
        info = custom.isEmpty() ? npc : DynamicNPC.fromTag(custom);
        initNPCData(); //Update the data on the client side too
    }

    @Override
    @Nonnull
    public EntityAgeable createChild(@Nonnull EntityAgeable ageable) {
        return new EntityNPC(ageable.world, npc, custom);
    }

    public void setAnimation(String animation, Object... object) {
        try {
            this.animation = Animation.create(animation).withData(object);
            PenguinNetwork.sendToNearby(this, new PacketSetAnimation(getEntityId(), this.animation.getID(), this.animation.serializeNBT()));
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void setAnimation(String animation, NBTTagCompound tag) {
        try {
            this.animation = animation.isEmpty() ? null : Animation.create(animation);
            if (this.animation != null) {
                this.animation.deserializeNBT(tag);
            }
        } catch (IllegalAccessException | InstantiationException ignored) {
        }
    }

    public NPCInfo getInfo() {
        return info;
    }

    public NPC getBaseNPC() {
        return npc;
    }

    public String substring(String name) {
        return info.substring(name);
    }

    public Animation getAnimation() {
        return animation;
    }
}