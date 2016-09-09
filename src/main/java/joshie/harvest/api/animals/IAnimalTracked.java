package joshie.harvest.api.animals;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import java.util.UUID;


/** This interface should be implemented on entities that want to have
 *  additional data tracked, the entity must be of the type EntityAnimal
 *  or their will be serious issues... */
public interface IAnimalTracked<A extends EntityAnimal> extends IEntityAdditionalSpawnData {
    /** Return animal data **/
    IAnimalData getData();

    @SuppressWarnings("unchecked")
    default A getAsEntity() {
        return (A) this;
    }

    /** Returns the relatable object **/
    default UUID getUUID() {
        return getAsEntity().getPersistentID();
    }
}