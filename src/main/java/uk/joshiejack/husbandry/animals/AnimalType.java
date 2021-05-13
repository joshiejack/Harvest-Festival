package uk.joshiejack.husbandry.animals;

import com.google.common.collect.Maps;
import uk.joshiejack.husbandry.animals.traits.AnimalTrait;
import uk.joshiejack.husbandry.animals.traits.action.AnimalTraitAction;
import uk.joshiejack.husbandry.animals.traits.ai.AnimalTraitAI;
import uk.joshiejack.husbandry.animals.traits.data.AnimalTraitData;
import uk.joshiejack.husbandry.HusbandryConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnimalType {
    public static final Map<ResourceLocation, AnimalType> TYPES = Maps.newHashMap();
    //Min and max length of time this animal will live
    private final int minimumLifespan; //Years IRL
    private final int maximumLifespan; //Years IRL
    private final int genericTreats;
    private final int typeTreats;
    private final int daysToBirth; //Months (weeks if < month) IRL
    private final int daysToMaturity; //Years * 7 IRL
    private final AnimalProducts products;
    private final List<AnimalTraitAI> traitsAI;
    private final List<AnimalTraitAction> traitsAction;
    private final List<AnimalTraitData> traitsData;
    private final ItemStack treat;

    public AnimalType(int minimumLifespan, int maximumLifespan, ItemStack treat, int genericTreats, int typeTreats, int daysToBirth, int daysToMaturity, @Nonnull AnimalProducts products, List<AnimalTrait> traits) {
        this.minimumLifespan = minimumLifespan * HusbandryConfig.daysPerYear;
        this.maximumLifespan = maximumLifespan * HusbandryConfig.daysPerYear;
        this.treat = treat;
        this.genericTreats = genericTreats;
        this.typeTreats = typeTreats;
        this.daysToBirth = daysToBirth;
        this.daysToMaturity = daysToMaturity;
        this.products = products;
        this.traitsAI = traits.stream().filter(t-> t instanceof AnimalTraitAI).map(a-> (AnimalTraitAI)a).collect(Collectors.toList());
        this.traitsAction = traits.stream().filter(t -> t instanceof AnimalTraitAction).map(a-> (AnimalTraitAction)a).collect(Collectors.toList());
        this.traitsData = traits.stream().filter(t -> t instanceof AnimalTraitData).map(a-> ((AnimalTraitData)a)).collect(Collectors.toList());
    }

    public List<AnimalTraitAction> getActionTraits() {
        return traitsAction;
    }

    public List<AnimalTraitAI> getAITraits() {
        return traitsAI;
    }

    public List<AnimalTraitData> getDataTraits() {
        return traitsData;
    }

    public boolean hasTrait(String trait) {
        return traitsAction.stream().anyMatch(t -> t.getName().equals(trait)) || traitsAI.stream().anyMatch(t -> t.getName().equals(trait));
    }

    public AnimalProducts getProducts() { return products; }

    public int getMinAge() {
        return minimumLifespan;
    }

    public int getMaxAge() {
        return maximumLifespan;
    }

    public int getGenericTreats() {
        return genericTreats;
    }

    public int getTypeTreats() {
        return typeTreats;
    }

    public int getDaysToBirth() {
        return daysToBirth;
    }

    public int getDaysToMaturity() {
        return daysToMaturity;
    }

    public ItemStack getTreat() {
        return treat;
    }
}
