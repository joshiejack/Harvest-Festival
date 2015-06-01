package joshie.harvest.api.cooking;

public interface IUtensil {
    /** @return     the ordinal value of the utensil **/
    public int ordinal();
    
    /** @return     how much the utensil costs **/
    public int getCost();
}
