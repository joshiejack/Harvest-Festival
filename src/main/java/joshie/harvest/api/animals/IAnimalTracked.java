package joshie.harvest.api.animals;

public interface IAnimalTracked {
    /** Return animal type details **/
    public IAnimalType getType();
    
    /** Return animal data **/
    public IAnimalData getData();
}
