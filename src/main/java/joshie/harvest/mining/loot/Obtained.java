package joshie.harvest.mining.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.core.HFTrackers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;
import java.util.Random;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class Obtained implements LootCondition {
    @Nonnull
    private final ItemStack stack;

    public Obtained(Item item, int meta) {
        this.stack = new ItemStack(item, 1, meta);
    }

    @Override
    public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
        EntityPlayer player = (EntityPlayer) context.getKillerPlayer();
        return player != null && HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().hasObtainedItem(stack);
    }

    public static class Serializer extends LootCondition.Serializer<Obtained> {
        public Serializer() {
            super(new ResourceLocation(MODID, "obtained"), Obtained.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull Obtained value, @Nonnull JsonSerializationContext context) {
            json.addProperty("item", value.stack.getItem().getRegistryName().toString());
            json.addProperty("meta", value.stack.getItemDamage());
        }

        @Nonnull
        public Obtained deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new Obtained(JsonUtils.getItem(json, "item"), JsonUtils.getInt(json, "meta", 0));
        }
    }
}
