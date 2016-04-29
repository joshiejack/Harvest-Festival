package joshie.harvest.player.relationships;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.helpers.generic.EntityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;

import java.util.UUID;

public class RelationshipHandlerEntity implements IRelatableDataHandler {
    @Override
    public String name() {
        return "entity";
    }

    @Override
    public IRelatableDataHandler copy() {
        return new RelationshipHandlerEntity();
    }

    private int id;

    @Override
    public void toBytes(IRelatable relatable, ByteBuf buf) {
        id = ((Entity) relatable).getEntityId();
        buf.writeInt(id);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
    }

    @Override
    public IRelatable onMessage(boolean particles) {
        Entity entity = joshie.harvest.core.helpers.generic.MCClientHelper.getWorld().getEntityByID(id);
        if (entity != null) {
            if (particles) {
                for (int j = 0; j < 3D; j++) {
                    double d7 = (entity.posY - 0.5D) + entity.worldObj.rand.nextFloat();
                    double d8 = (entity.posX - 0.5D) + entity.worldObj.rand.nextFloat();
                    double d9 = (entity.posZ - 0.5D) + entity.worldObj.rand.nextFloat();
                    entity.worldObj.spawnParticle(EnumParticleTypes.HEART, d8, 1.0D + d7 - 0.125D, d9, 0, 0, 0);
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
        tag.setInteger("Dimension", animal.worldObj.provider.getDimension());
    }
}
