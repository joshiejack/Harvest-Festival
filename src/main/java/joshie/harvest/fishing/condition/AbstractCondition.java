package joshie.harvest.fishing.condition;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import java.util.Random;

public abstract class AbstractCondition implements LootCondition {
    @Override
    public boolean testCondition(Random rand, LootContext context) {
        if (context.getLootedEntity() != null) {
            return testCondition(context.getLootedEntity().worldObj, new BlockPos(context.getLootedEntity()));
        }

        return false;
    }

    public abstract boolean testCondition(World world, BlockPos pos);
}
