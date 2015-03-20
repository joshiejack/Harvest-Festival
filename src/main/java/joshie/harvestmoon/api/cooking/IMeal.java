package joshie.harvestmoon.api.cooking;


public interface IMeal {
    /** @return     the unlocalized name, must be unique*/
    String getUnlocalizedName();

    /** @return     how much hunger the meal restores **/
    int getHunger();

    /** @return     how much saturation the meal provides **/
    float getSaturation();

    /** @return     how much stamina the meal adds **/
    int getStamina();

    /** @return     how much fatigue the meal adds **/
    int getFatigue();

    /** @return     whether the meal is drunk (rather than eaten) **/
    boolean isDrink();

    /** @return     how long the meal takes to eat **/
    int getEatTime();
    
    /** @return     the maximum hunger this meal can provide (including optional ingredients) **/
    int getHungerCap();
    
    /** @return     the maximum saturation this meal can provide (including optional ingredients) **/
    float getSaturationCap();

    /** Adds extra data for the ingredients to this meal
     *  @param      the cooking component we're adding as an ingredient
     *  @return     the adjusted meal **/
    IMeal addIngredient(ICookingComponent optional);
}
