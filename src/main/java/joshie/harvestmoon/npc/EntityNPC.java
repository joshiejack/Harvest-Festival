package joshie.harvestmoon.npc;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import joshie.harvestmoon.HarvestMoon;
import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.npc.INPC;
import joshie.harvestmoon.core.helpers.NPCHelper;
import joshie.harvestmoon.core.lib.HMModInfo;
import joshie.harvestmoon.init.HMNPCs;
import joshie.harvestmoon.npc.ai.EntityAIGoHome;
import joshie.harvestmoon.npc.ai.EntityAILookAtPlayer;
import joshie.harvestmoon.npc.ai.EntityAIPlay;
import joshie.harvestmoon.npc.ai.EntityAITalkingTo;
import joshie.harvestmoon.npc.ai.EntityAITeleportHome;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityNPC extends EntityAgeable implements IEntityAdditionalSpawnData {
    protected INPC npc;
    protected EntityNPC lover;
    private EntityPlayer talkingTo;
    private boolean isPlaying;
    public UUID owning_player;
    public int lastTeleport;

    public EntityNPC(UUID owning_player, EntityNPC entity) {
        this(owning_player, entity.worldObj, entity.npc);
        lover = entity.lover;
    }

    public EntityNPC(World world) {
        this(null, world, HMNPCs.goddess);
    }

    @Override
    public void onUpdate() {
        if (!joshie.harvestmoon.core.config.NPC.FREEZE_NPC) {
            super.onUpdate();
        }
    }

    public EntityNPC(UUID owning_player, World world, INPC npc) {
        super(world);
        this.npc = npc;

        this.owning_player = owning_player;
        this.setSize(0.6F, (1.8F * npc.getHeight()));
        this.getNavigator().setBreakDoors(true);
        this.getNavigator().setAvoidsWater(true);

        if (owning_player != null) {
            this.tasks.addTask(0, new EntityAISwimming(this));
            this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
            this.tasks.addTask(1, new EntityAITalkingTo(this));
            this.tasks.addTask(1, new EntityAILookAtPlayer(this));
            this.tasks.addTask(2, new EntityAITeleportHome(this));
            this.tasks.addTask(2, new EntityAIGoHome(this));
            this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
            this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
            this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
            this.tasks.addTask(8, new EntityAIPlay(this, 0.32D));
            this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
            this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityNPC.class, 5.0F, 0.02F));
            this.tasks.addTask(9, new EntityAIWander(this, 0.6D));
            this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
    }

    @Override
    public boolean isAIEnabled() {
        return true;
    }

    public ResourceLocation getSkin() {
        return new ResourceLocation(HMModInfo.MODPATH + ":" + "textures/entity/" + npc.getUnlocalizedName() + ".png");
    }

    public INPC getNPC() {
        return npc;
    }

    public EntityNPC getLover() {
        return lover;
    }

    @Override
    public String getCommandSenderName() {
        return npc.getLocalizedName();
    }

    @Override
    protected void updateEntityActionState() {
        if (this.lastTeleport > 0) {
            this.lastTeleport--;
        }

        if (!isTalking()) {
            super.updateEntityActionState();
        } else {
            addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
        }
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
        return npc.isChild();
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    @Override
    public void setDead() {
        if (!worldObj.isRemote && npc.respawns() && !isDead) {
            EntityNPC clone = new EntityNPC(owning_player, this);
            worldObj.spawnEntityInWorld(clone);
        }

        isDead = true;
    }

    @Override
    public boolean interact(EntityPlayer player) {
        ItemStack held = player.inventory.getCurrentItem();
        boolean flag = held != null && held.getItem() == Items.spawn_egg;
        if (!flag && isEntityAlive()) {
            if (!worldObj.isRemote) {
                player.openGui(HarvestMoon.instance, NPCHelper.getGuiIDForNPC(npc, worldObj, player.isSneaking() && held != null), worldObj, getEntityId(), 0, 0);
                setTalking(player);
            }

            return true;
        } else {
            return super.interact(player);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        npc = HMApi.NPC.get(nbt.getString("NPC"));
        owning_player = new UUID(nbt.getLong("Owner-UUIDMost"), nbt.getLong("Owner-UUIDLeast"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        if (npc != null) {
            nbt.setString("NPC", npc.getUnlocalizedName());
        }

        if (owning_player != null) {
            nbt.setLong("Owner-UUIDMost", owning_player.getMostSignificantBits());
            nbt.setLong("Owner-UUIDLeast", owning_player.getLeastSignificantBits());
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buf) {
        if (npc != null) {
            char[] name = npc.getUnlocalizedName().toCharArray();
            buf.writeByte(name.length);
            for (char c : name) {
                buf.writeChar(c);
            }
        }
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        char[] name = new char[buf.readByte()];
        for (int i = 0; i < name.length; i++) {
            name[i] = buf.readChar();
        }

        npc = HMApi.NPC.get(new String(name));
        if (npc == null) {
            npc = HMNPCs.goddess;
        }
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return new EntityNPC(owning_player, worldObj, HMNPCs.goddess);
    }
}