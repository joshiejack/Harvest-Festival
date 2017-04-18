package joshie.harvest.core.helpers;

import com.google.common.collect.Lists;
import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.core.Size;
import joshie.harvest.api.player.RelationshipType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

public class SizeableHelper {
    public static List<ItemStack> getSizeablesForDisplay(AnimalStats stats, Sizeable type) {
        List<ItemStack> list = Lists.newArrayList();
        for (Size size: Size.values()) {
            int value = RelationshipType.ANIMAL.getMaximumRP() -(RelationshipType.ANIMAL.getMaximumRP() -(stats.getHappiness() - size.getRelationshipRequirement()));
            if (value > 0 || (size == Size.LARGE && stats.performTest(AnimalTest.WON_CONTEST) && stats.getHappiness() >= (Size.LARGE.getRelationshipRequirement() + 3000))) {
                list.add(type.getStackOfSize(HFAnimals.ANIMAL_PRODUCT, size, stats.getProductsPerDay()));
            }
        }

        //Add the small size to the list, if we have none added
        if (list.size() == 0) {
            list.add(type.getStackOfSize(HFAnimals.ANIMAL_PRODUCT, Size.SMALL, stats.getProductsPerDay()));
        }

        return list;
    }

    public static ItemStack getMilk(AnimalStats stats) {
        return SizeableHelper.getSizeable(stats, Sizeable.MILK, stats.getProductsPerDay());
    }

    public static ItemStack getWool(AnimalStats stats) {
        return SizeableHelper.getSizeable(stats, Sizeable.WOOL, stats.getProductsPerDay());
    }

    public static ItemStack getEgg(AnimalStats stats) {
        return SizeableHelper.getSizeable(stats, Sizeable.EGG, 1);
    }

    private static ItemStack getSizeable(AnimalStats stats, Sizeable sizeable, int size) {
        if (stats.performTest(AnimalTest.WON_CONTEST) && stats.getHappiness() >= (Size.LARGE.getRelationshipRequirement() + 3000) && stats.getAnimal().worldObj.rand.nextInt(100) == 0) {
            return sizeable.getStackOfSize(HFAnimals.ANIMAL_PRODUCT, Size.LARGE, size);
        }

        return sizeable.getStackOfSize(HFAnimals.ANIMAL_PRODUCT, getSizeFromAnimal(stats.getHappiness(), stats.getAnimal()), size);
    }

    private static Size getSizeFromAnimal(int happiness, EntityAnimal animal) {
        WeightedSize weighted = new WeightedSize();
        for (Size size: Size.values()) {
            int value = RelationshipType.ANIMAL.getMaximumRP() -(RelationshipType.ANIMAL.getMaximumRP() -(happiness - size.getRelationshipRequirement()));
            if (value > 0) {
                weighted.add(size, value);
            }
        }

        return weighted.get(animal);
    }

    private static class WeightedSize {
        private final NavigableMap<Double, Size> map = new TreeMap<>();
        private double total = 0;
        public void add(Size size, double weight) {
            if (weight > 0) {
                total += weight;
                map.put(total, size);
            }
        }

        @Nullable
        public Size get(EntityAnimal animal) {
            if (map.size() == 0) return Size.SMALL;
            return map.ceilingEntry((animal.worldObj.rand.nextDouble() * total)).getValue();
        }
    }
}
