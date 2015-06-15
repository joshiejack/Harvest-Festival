package joshie.harvest.relations.data;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.helpers.generic.EntityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;

public class DataHandlerEntity implements IRelatableDataHandler {
    @Override
    public String name() {
        return "entity";
    }

    @Override
    public IRelatableDataHandler copy() {
        return new DataHandlerEntity();
    }

    private int id;
    private boolean displayParticles;

    @Override
    public void toBytes(IRelatable relatable, ByteBuf buf, Object... data) {
        id = ((Entity) relatable).getEntityId();
        buf.writeInt(id);
        buf.writeBoolean((Boolean) data[0]);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
        displayParticles = buf.readBoolean();
    }

    @Override
    public IRelatable onMessage() {
        Entity entity = joshie.harvest.core.helpers.generic.MCClientHelper.getWorld().getEntityByID(id);
        if (entity != null) {
            if (displayParticles) {
                for (int j = 0; j < 3D; j++) {
                    double d7 = (entity.posY - 0.5D) + entity.worldObj.rand.nextFloat();
                    double d8 = (entity.posX - 0.5D) + entity.worldObj.rand.nextFloat();
                    double d9 = (entity.posZ - 0.5D) + entity.worldObj.rand.nextFloat();
                    entity.worldObj.spawnParticle("heart", d8, 1.0D + d7 - 0.125D, d9, 0, 0, 0);
                }
            }

            return (IRelatable) entity;
        } else return null;
    }

    @Override
    public IRelatable readFromNBT(NBTTagCompound tag) {
        UUID uuid = UUID.fromString(tag.getString("UUID"));
        int dimension = tag.getInteger("Dimension");
        return (IRelatable) EntityHelper.getAnimalFromUUID(dimension, uuid);
    }

    @Override
    public void writeToNBT(IRelatable relatable, NBTTagCompound tag) {
        EntityAnimal animal = (EntityAnimal) relatable;
        tag.setString("UUID", UUIDHelper.getEntityUUID(animal).toString());
        tag.setInteger("Dimension", animal.worldObj.provider.dimensionId);
    }
}
