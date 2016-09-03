package joshie.harvest.api.animals;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.api.relations.IRelatableProvider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;


/** This interface should be implemented on entities that want to have
 *  additional data tracked, the entity must be of the type EntityAnimal
 *  or their will be serious issues... */
public interface IAnimalTracked<A extends EntityAnimal> extends IEntityAdditionalSpawnData, IRelatable, IRelatableProvider {
    /** Return animal data **/
    IAnimalData getData();

    @SuppressWarnings("unchecked")
    default A getAsEntity() {
        return (A) this;
    }

    /** Returns the relatable object **/
    default IRelatable getRelatable() {
        return this;
    }

    /** Returns the data handler for this entity **/
    default IRelatableDataHandler getDataHandler() {
        return HFApi.relationships.getDataHandler("entity");
    }
}