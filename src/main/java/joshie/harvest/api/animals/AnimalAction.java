package joshie.harvest.api.animals;

public enum AnimalAction {
    CLEAN, //Cleans the animal
    FEED, //Feeds the animal
    HEAL, //Heals the animal
    TREAT_SPECIAL, //Treats the animal with their specific treat
    TREAT_GENERIC, //Treats the animal with a generic treat
    TREAT_INCORRECT, //Treats the animal with an incorrect treat
    IMPREGNATE, //Impregnates the animal
    DISMOUNT, //Called when an animal that was carried was dismounted
    CLAIM_PRODUCT, //Gets the product
    OUTSIDE, //Used for adding a bonus to relationship for being outside
    MAKE_GOLDEN //Marks the animal as golden
}
