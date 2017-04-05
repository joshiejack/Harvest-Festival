package joshie.harvest.core.helpers;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.api.animals.AnimalTest;
import joshie.harvest.api.core.Size;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.NavigableMap;
import java.util.TreeMap;

public class SizeableHelper {
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
        if (stats.performTest(AnimalTest.WON_CONTEST) && stats.getHappiness() >= 20000 && stats.getAnimal().worldObj.rand.nextInt(100) == 0) {
            return sizeable.getStackOfSize(HFAnimals.ANIMAL_PRODUCT, Size.LARGE, size);
        }

        return sizeable.getStackOfSize(HFAnimals.ANIMAL_PRODUCT, getSizeFromAnimal(stats.getHappiness(), stats.getAnimal()), size);
    }

    private static Size getSizeFromAnimal(int happiness, EntityAnimal animal) {
        WeightedSize weighted = new WeightedSize();
        for (Size size: Size.values()) {
            int value = 27000 -(27000 -(happiness - size.getRelationshipRequirement()));
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
