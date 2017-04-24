package joshie.harvest.buildings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LootHelper {
    public static ItemStack getStack(World world, EntityPlayer player, ResourceLocation lootTable) {
        long lootTableSeed = world.rand.nextLong();
        LootTable loottable = world.getLootTableManager().getLootTableFromLocation(lootTable);
        Random random = lootTableSeed == 0 ? new Random() : new Random(lootTableSeed);
        LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)world);
        if (player != null) lootcontext$builder.withLuck(player.getLuck());
        List<ItemStack> list = loottable.generateLootForPools(random, lootcontext$builder.build());
        Collections.shuffle(list); //Shuffle up the list
        return list.size() > 0 ? list.get(0) : ItemStack.EMPTY;
    }
}