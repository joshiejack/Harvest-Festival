package joshie.harvest.quests.town.festivals.cooking;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Random;

class CookingContestEntry {
    private final ItemStack meal;
    private final Utensil category;
    private final String owner;
    private EntityPlayer player;
    private NPC npc;

    CookingContestEntry(NPC npc, Random rand) {
        this.npc = npc;
        owner = npc.getResource().toString();
        meal = HFCooking.MEAL.getRandomMeal(rand);
        category = getUtensilFromStack(meal);
    }

    CookingContestEntry(EntityPlayer player, ItemStack stack) {
        this.player = player;
        owner = EntityHelper.getPlayerUUID(player).toString();
        meal = stack.copy();
        category = getUtensilFromStack(stack);
    }

    private Utensil getUtensilFromStack(ItemStack stack) {
        return Utensil.FRYING_PAN;
    }

    public String getName() {
        return owner;
    }

    public String getDisplayName() {
        return player != null ? player.getName() : npc.getLocalizedName();
    }

    @Nullable
    public EntityPlayer getPlayer() {
        return player;
    }

    @Nullable
    public ItemStack getMeal() {
        return meal;
    }

    int getScore(Utensil utensil) {
        int hunger = ((ItemFood)meal.getItem()).getHealAmount(meal);
        float saturation = ((ItemFood)meal.getItem()).getSaturationModifier(meal);
        long gold = HFApi.shipping.getSellValue(meal);
        return (int)(hunger * saturation + gold) - ((category == utensil) ? 0: -1000);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CookingContestEntry that = (CookingContestEntry) o;
        return owner != null ? owner.equals(that.owner) : that.owner == null;
    }

    @Override
    public int hashCode() {
        return owner != null ? owner.hashCode() : 0;
    }
}
