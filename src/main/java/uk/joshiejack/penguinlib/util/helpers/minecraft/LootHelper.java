package uk.joshiejack.penguinlib.util.helpers.minecraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class LootHelper {
    @Nonnull
    public static ItemStack getStack(World world, EntityPlayer player, ResourceLocation lootTable, Random random) {
        LootTable loottable = world.getLootTableManager().getLootTableFromLocation(lootTable);
        LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)world);

        if (player != null) {
            lootcontext$builder.withLuck(player.getLuck());
        }

        return loottable.generateLootForPools(random, lootcontext$builder.build()).get(0);
    }

    public static List<ItemStack> getLoot(ResourceLocation loot, World world, EntityPlayer player, int fortune) {
        LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) world);
        lootcontext$builder.withLuck(player.getLuck() + fortune);
        lootcontext$builder.withPlayer(player);
        return world.getLootTableManager().getLootTableFromLocation(loot).generateLootForPools(world.rand, lootcontext$builder.build());
    }
}
