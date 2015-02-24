package joshie.harvestmoon.core.helpers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AnimalHelper {
    /** Call Server Side Only **/
    public static EntityPlayer getOwner(EntityAnimal animal) {
        if (animal.worldObj.isRemote) return null;
        return ServerHelper.getAnimalTracker().getOwner(animal);
    }

    public static boolean canProduceProduct(EntityAnimal animal) {
        if (animal.worldObj.isRemote) {
            return ClientHelper.getAnimalTracker().canProduceProduct(animal);
        } else return ServerHelper.getAnimalTracker().canProduceProduct(animal);
    }

    //Increase product produced, then update the client with the new information
    public static void setProducedProduct(EntityAnimal animal) {
        if (!animal.worldObj.isRemote) {
            ServerHelper.getAnimalTracker().setProducedProduct(animal);
        }
    }

    /** Causes the player to feed the animal, affecting it's relationship 
     * @param b **/
    public static void feed(EntityPlayer player, EntityAnimal animal) {
        if (!animal.worldObj.isRemote) {
            if (ServerHelper.getAnimalTracker().setFed(animal)) {
                if (player != null) {
                    ServerHelper.getPlayerData(player).affectRelationship(animal, 5);
                }
            }
        }
    }

    public static void onDeath(EntityAnimal animal) {
        if (!animal.worldObj.isRemote) {
            ServerHelper.getAnimalTracker().onDeath(animal);
        } else {
            ClientHelper.getAnimalTracker().onDeath(animal);
        }
    }

    public static void throwChicken(EntityPlayer player, EntityChicken chicken) {
        if (ServerHelper.getAnimalTracker().setThrown(chicken)) {
            ServerHelper.getPlayerData(player).affectRelationship(chicken, 25);
        }
    }

    public static void clean(EntityPlayer player, EntityAnimal animal) {
        if (ServerHelper.getAnimalTracker().setCleaned(animal)) {
            ServerHelper.getPlayerData(player).affectRelationship(animal, 25);
        }
    }

    public static boolean heal(EntityAnimal animal) {
        return ServerHelper.getAnimalTracker().heal(animal);
    }

    public static void treat(ItemStack stack, EntityPlayer player, EntityLivingBase living) {
        ServerHelper.getAnimalTracker().treat(stack, player, (EntityAnimal) living);
    }

    public static void newDay() {
        ServerHelper.getAnimalTracker().newDay();
    }

    public static boolean impregnate(EntityPlayer player, EntityAnimal animal) {
        if (ServerHelper.getAnimalTracker().impregnate(animal)) {
            ServerHelper.getPlayerData(player).affectRelationship(animal, 200);
            return true;
        } else return false;
    }

    public static boolean addEgg(World world, int x, int y, int z) {
        return ServerHelper.getAnimalTracker().addEgg(world, x, y, z);
    }

    public static boolean addFodder(World world, int x, int y, int z) {
        return ServerHelper.getAnimalTracker().addFodder(world, x, y, z);
    }

    public static void addNest(World world, int x, int y, int z) {
        ServerHelper.getAnimalTracker().addNest(world, x, y, z);
    }

    public static void addTrough(World world, int x, int y, int z) {
        ServerHelper.getAnimalTracker().addTrough(world, x, y, z);
    }

    public static void removeNest(World world, int x, int y, int z) {
        ServerHelper.getAnimalTracker().removeNest(world, x, y, z);
    }

    public static void removeTrough(World world, int x, int y, int z) {
        ServerHelper.getAnimalTracker().removeTrough(world, x, y, z);
    }
}
