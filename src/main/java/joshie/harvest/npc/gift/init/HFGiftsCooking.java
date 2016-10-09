package joshie.harvest.npc.gift.init;

import joshie.harvest.cooking.block.BlockCookware.Cookware;
import joshie.harvest.cooking.item.ItemUtensil.Utensil;
import joshie.harvest.core.util.annotations.HFLoader;

import static joshie.harvest.api.npc.gift.GiftCategory.*;
import static joshie.harvest.cooking.HFCooking.*;

@HFLoader(priority = 0)
public class HFGiftsCooking extends HFGiftsAbstract {
    public static void init() {
        assignGeneric(MEAL, COOKING);
        assignGeneric(INGREDIENTS, COOKING);
        assignGeneric(COOKBOOK, COOKING);
        assignGeneric(RECIPE, COOKING);
        assignGeneric(COOKWARE.getStackFromEnum(Cookware.POT), COOKING);
        assignGeneric(COOKWARE.getStackFromEnum(Cookware.MIXER), COOKING);
        assignGeneric(COOKWARE.getStackFromEnum(Cookware.FRIDGE), COOKING);
        assignGeneric(COOKWARE.getStackFromEnum(Cookware.FRYING_PAN), COOKING);
        assignGeneric(COOKWARE.getStackFromEnum(Cookware.COUNTER), COOKING);
        assignGeneric(COOKWARE.getStackFromEnum(Cookware.OVEN_OFF), COOKING);
        assignGeneric(UTENSILS.getStackFromEnum(Utensil.KNIFE), COOKING, DANGER, TOOLS);
    }
}