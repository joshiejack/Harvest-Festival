package joshie.harvest.animals.type;

import joshie.harvest.api.animals.AnimalFoodType;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalType;
import joshie.harvest.core.config.Calendar;
import net.minecraft.entity.passive.EntityAnimal;

public class AbstractAnimal implements IAnimalType {
    private final AnimalFoodType[] types;
    private final String name;
    private final int min;
    private final int max;
    
    public AbstractAnimal(String name, int min, int max, AnimalFoodType... types) {
        this.name = name;
        this.types = types;
        this.min = min * (Calendar.DAYS_PER_SEASON * 4);
        this.max = max * (Calendar.DAYS_PER_SEASON * 4);
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
    public void newDay(IAnimalData data, EntityAnimal entity) {}
}
