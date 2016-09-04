package joshie.harvest.mining.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import java.util.Random;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class Obtained implements LootCondition {
    private final ItemStack stack;

    public Obtained(Item item, int meta) {
        this.stack = new ItemStack(item, meta);
    }

    @Override
    public boolean testCondition(Random rand, LootContext context) {
        EntityPlayer player = (EntityPlayer) context.getKillerPlayer();
        if (player != null) {
            return HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().hasObtainedItem(stack);
        }

        return false;
    }

    public static class Serializer extends LootCondition.Serializer<Obtained> {
        public Serializer() {
            super(new ResourceLocation(MODID, "obtained"), Obtained.class);
        }

        public void serialize(JsonObject json, Obtained value, JsonSerializationContext context) {
            json.addProperty("item", value.stack.getItem().getRegistryName().toString());
            json.addProperty("meta", value.stack.getItemDamage());
        }

        public Obtained deserialize(JsonObject json, JsonDeserializationContext context) {
            return new Obtained(JsonUtils.getItem(json, "item"), JsonUtils.getInt(json, "meta", 0));
        }
    }
}
