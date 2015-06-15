package joshie.harvest.api.animals;

import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableProvider;



/** This interface should be implemented on entities that want to have
 *  additional data tracked, the entity must be of the type EntityAnimal
 *  or their will be serious issues... */
public interface IAnimalTracked extends IRelatable, IRelatableProvider {
    /** Return animal type details **/
    public IAnimalType getType();
    
    /** Return animal data **/
    public IAnimalData getData();
}
