package joshie.harvest.animals.type;

import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalType;
import joshie.harvest.api.calendar.CalendarDate;
import net.minecraft.entity.passive.EntityAnimal;

import java.util.Random;

public abstract class AnimalAbstract implements IAnimalType {
    protected static final Random rand = new Random();
    private final AnimalFoodType[] types;
    private final String name;
    private final int min;
    private final int max;

    public AnimalAbstract(String name, int min, int max, AnimalFoodType... types) {
        this.name = name;
        this.types = types;
        this.min = min * (CalendarDate.DAYS_PER_SEASON * 4);
        this.max = max * (CalendarDate.DAYS_PER_SEASON * 4);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public AnimalFoodType[] getFoodTypes() {
        return types;
    }

    @Override
    public int getMinLifespan() {
        return min;
    }

    @Override
    public int getMaxLifespan() {
        return max;
    }

    @Override
    public int getDaysBetweenProduction() {
        return 0;
    }

    @Override
    public void refreshProduct(IAnimalData data, EntityAnimal entity) {}

    @Override
    public boolean getsDirty() {
        return true;
    }
}