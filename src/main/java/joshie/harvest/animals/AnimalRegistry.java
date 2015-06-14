package joshie.harvest.animals;

import java.util.HashMap;

import joshie.harvest.animals.type.AnimalChicken;
import joshie.harvest.animals.type.AnimalCow;
import joshie.harvest.animals.type.AnimalSheep;
import joshie.harvest.api.animals.IAnimalData;
import joshie.harvest.api.animals.IAnimalHandler;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.api.animals.IAnimalType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;

public class AnimalRegistry implements IAnimalHandler {
    private static final HashMap<String, IAnimalType> types = new HashMap();
    private static boolean isInit = false;

    @Override
    public IAnimalData newData(IAnimalTracked animal) {
        return new AnimalData(animal);
    }

    @Override
    public IAnimalType getTypeFromString(String string) {
        return types.get(string);
    }
    
    private void init() {
        types.put("cow", new AnimalCow());
        types.put("sheep", new AnimalSheep());
        types.put("chicken", new AnimalChicken());
    }

    @Override
    public IAnimalType getType(EntityAnimal animal) {
        if (!isInit) {
            init();
            isInit = true;
        }
        
        //Return aminals
        if (animal instanceof EntityCow) {
            return types.get("cow");
        } else if (animal instanceof EntitySheep) {
            return types.get("sheep");
        } else if (animal instanceof EntityChicken) {
            return types.get("chicken");
        } else return null;
    }
}
