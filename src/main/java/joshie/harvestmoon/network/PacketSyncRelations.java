package joshie.harvestmoon.network;

import static cpw.mods.fml.common.network.ByteBufUtils.readUTF8String;
import static cpw.mods.fml.common.network.ByteBufUtils.writeUTF8String;
import static joshie.harvestmoon.HarvestMoon.handler;
import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.entities.NPC;
import joshie.harvestmoon.init.HMNPCs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncRelations implements IMessage, IMessageHandler<PacketSyncRelations, IMessage> {
    private NPC npc;
    private boolean isEntityPacket;
    private boolean doParticles;
    private boolean isSenderClient;
    private int id;
    private int value;

    public PacketSyncRelations() {}

    public PacketSyncRelations(int id) {
        this.isSenderClient = true;
        this.isEntityPacket = true;
        this.id = id;
    }

    public PacketSyncRelations(int id, int value, boolean doParticles) {
        this.isSenderClient = false;
        this.isEntityPacket = true;
        this.id = id;
        this.value = value;
        this.doParticles = doParticles;
    }

    public PacketSyncRelations(NPC npc, int value, boolean doParticles) {
        this.isSenderClient = false;
        this.isEntityPacket = false;
        this.npc = npc;
        this.value = value;
        this.doParticles = doParticles;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isSenderClient);
        buf.writeBoolean(isEntityPacket);
        if (isEntityPacket) {
            buf.writeInt(id);
        } else {
            writeUTF8String(buf, npc.getUnlocalizedName());
        }

        if (!isSenderClient) {
            buf.writeInt(value);
            buf.writeBoolean(doParticles);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isSenderClient = buf.readBoolean();
        isEntityPacket = buf.readBoolean();
        if (isEntityPacket) {
            id = buf.readInt();
        } else {
            npc = HMNPCs.get(readUTF8String(buf));
            if(npc == null) {
                npc = HMNPCs.goddess;
            }
        }

        if (!isSenderClient) {
            value = buf.readInt();
            doParticles = buf.readBoolean();
        }
    }

    @Override
    public IMessage onMessage(PacketSyncRelations message, MessageContext ctx) {
        if (message.value == -1) return null;
        if (message.isSenderClient) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            Entity entity = player.worldObj.getEntityByID(message.id);
            if (entity instanceof EntityLivingBase) {
                int relationship = handler.getServer().getPlayerData(player).getRelationship((EntityLivingBase) entity);
                PacketHandler.sendToClient(new PacketSyncRelations(entity.getEntityId(), relationship, false), player);
            }
        } else {
            if (message.isEntityPacket) {
                Entity entity = joshie.harvestmoon.helpers.generic.ClientHelper.getWorld().getEntityByID(message.id);
                if (entity != null) {
                    handler.getClient().getPlayerData().setRelationship(entity.getPersistentID(), message.value);

                    if (message.doParticles) {
                        for (int j = 0; j < 3D; j++) {
                            double d7 = (entity.posY - 0.5D) + entity.worldObj.rand.nextFloat();
                            double d8 = (entity.posX - 0.5D) + entity.worldObj.rand.nextFloat();
                            double d9 = (entity.posZ - 0.5D) + entity.worldObj.rand.nextFloat();
                            entity.worldObj.spawnParticle("heart", d8, 1.0D + d7 - 0.125D, d9, 0, 0, 0);
                        }
                    }
                }
            } else {
                handler.getClient().getPlayerData().setRelationship(message.npc, message.value);
            }
        }

        return null;
    }
}