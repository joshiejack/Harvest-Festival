package joshie.harvest.npc.entity;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.lib.HFSounds;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.npc.entity.ai.EntityAILookAtPlayer;
import joshie.harvest.npc.entity.ai.EntityAITalkingTo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

public class EntityNPCGoddess extends AbstractEntityNPC<EntityNPCGoddess> {
    public EntityNPCGoddess(World world) {
        super(world, (NPC) HFNPCs.GODDESS);
        setSize(0.6F, (2F * npc.getHeight()));
    }

    public EntityNPCGoddess(World world, NPC npc) {
        super(world, npc);
        setSize(0.6F, (2F * npc.getHeight()));
    }

    public EntityNPCGoddess(EntityNPCGoddess entity) {
        super(entity);
        setSize(0.6F, (2F * npc.getHeight()));
    }

    @Override
    protected EntityNPCGoddess getNewEntity(EntityNPCGoddess entity) {
        return new EntityNPCGoddess(entity);
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAITalkingTo(this));
        tasks.addTask(1, new EntityAILookAtPlayer(this));
        tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        tasks.addTask(4, new EntityAIOpenDoor(this, true));
        tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0F, 1.0F));
        tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        super.moveEntityWithHeading(strafe, forward);
        prevLimbSwingAmount = 0F;
        limbSwing = 0F; //Keep those limbs still
        limbSwingAmount = 0F;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        //Spawn Particles around the goddess
        for (int i = 0; i < 16; i++) {
            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + 0.1 * rand.nextFloat(), posY + 0.2 * rand.nextFloat(), posZ + 0.1 * rand.nextFloat(), 0, -0.05, 0);
        }
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        String name = buf.readBoolean() ? ByteBufUtils.readUTF8String(buf) : "";
        npc = name.equals("") ? (NPC) HFNPCs.GODDESS : NPCRegistry.REGISTRY.getObject(new ResourceLocation(name));
        townID = UUID.fromString(ByteBufUtils.readUTF8String(buf));

        for (int i = 0; i < 16; i++) {
            worldObj.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, posX * 0.2 * rand.nextFloat(), posY + 0.5 + 0.2 * rand.nextFloat(), posZ + 0.2 * rand.nextFloat(), 0, 0, 0);
        }
       // worldObj.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, posX + rand.nextFloat(), posY + rand.nextFloat(), posZ + rand.nextFloat(), 0, 0.05, 0);
        worldObj.playSound(posX, posY, posZ, HFSounds.GODDESS_SPAWN, SoundCategory.NEUTRAL, 1F, 1F, true);
    }
}