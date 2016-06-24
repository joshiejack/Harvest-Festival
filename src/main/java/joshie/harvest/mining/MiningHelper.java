package joshie.harvest.mining;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.lib.LootStrings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;

public class MiningHelper {
    public static int getFloorFromPlayer(EntityPlayer player) {
        return 59;
    }

    public static ItemStack getLoot(World world, BlockPos pos, EntityPlayer player, float luck) {
        LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) world);
        lootcontext$builder.withLuck(player.getLuck() + luck);
        lootcontext$builder.withPlayer(player);
        ResourceLocation loot = HFTrackers.getCalendar(world).getSeasonAt(pos) == Season.WINTER ? LootStrings.MINE_WINTER : LootStrings.MINE_SPRING;
        for (ItemStack itemstack : world.getLootTableManager().getLootTableFromLocation(loot).generateLootForPools(world.rand, lootcontext$builder.build())) {
            return itemstack;
        }

        return null;
    }
}
