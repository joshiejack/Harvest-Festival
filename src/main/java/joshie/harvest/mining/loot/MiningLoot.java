package joshie.harvest.mining.loot;

import joshie.harvest.blocks.BlockDirt.FloorType;
import joshie.harvest.core.helpers.generic.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

public class MiningLoot {
    private static EnumMap<FloorType, Loot> loots = new EnumMap<FloorType, Loot>(FloorType.class);
    private static Random rand = new Random();


    public static void getLoot(World world, BlockPos pos, EntityPlayer player, int meta) {
        Loot loot = loots.get(FloorType.values()[meta]);
        if (loot == null) return;
        ArrayList<LootChance> lootList = loot.loot;
        for (LootChance l : lootList) {
            if (l.canPlayerObtain(player)) {
                if (rand.nextDouble() <= l.chance) {
                    ItemHelper.spawnItem(world, pos.getX(), pos.getY() + 0.25D, pos.getZ(), l.stack.copy());
                    break;
                }
            }
        }
    }


    public static void registerLoot(FloorType meta, ItemStack stack, double chance) {
        Loot loot = loots.get(meta) != null ? loots.get(meta) : new Loot();
        loot.loot.add(new LootChance(stack, chance));
        loots.put(meta, loot);
    }

    public static void registerLoot(FloorType meta, LootChance chance) {
        Loot loot = loots.get(meta) != null ? loots.get(meta) : new Loot();
        loot.loot.add(chance);
        loots.put(meta, loot);
    }

    private static class Loot {
        ArrayList<LootChance> loot = new ArrayList<LootChance>();
    }
}