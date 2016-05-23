package joshie.harvest.api.cooking;

public interface IUtensil {
    /** @return     the ordinal value of the utensil **/
    int ordinal();
    
    /** @return     how much the utensil costs **/
    int getCost();
}