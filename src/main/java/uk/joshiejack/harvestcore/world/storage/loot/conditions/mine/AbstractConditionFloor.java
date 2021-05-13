package uk.joshiejack.harvestcore.world.storage.loot.conditions.mine;

import uk.joshiejack.harvestcore.world.mine.MineHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;
import java.util.Random;

public abstract class AbstractConditionFloor implements LootCondition {
    @Override
    public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
        if (context.getKillerPlayer() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) context.getKillerPlayer();
            return testFloor(MineHelper.getFloorFromEntity(player));
        }

        return false;
    }

    public abstract boolean testFloor(int floor);
}
