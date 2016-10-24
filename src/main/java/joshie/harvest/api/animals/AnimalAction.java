package joshie.harvest.api.animals;

public enum AnimalAction {
    CLEAN,
    FEED,
    HEAL,
    TREAT_SPECIAL,
    TREAT_GENERIC,
    IMPREGNATE,
    TEST_MOUNT,
    DISMOUNT,
    TEST_PRODUCT, //Called to check if this animal is ready to give its product
    CLAIM_PRODUCT,
    OUTSIDE
}
