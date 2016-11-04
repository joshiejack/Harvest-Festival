package joshie.harvest.mining.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.HFTrackers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import java.util.Locale;
import java.util.Random;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class Seasonal implements LootCondition {
    private final Season season;

    public Seasonal(Season season) {
        this.season = season;
    }

    @Override
    public boolean testCondition(Random rand, LootContext context) {
        if (context.getKillerPlayer() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) context.getKillerPlayer();
            return HFTrackers.getCalendar(player.worldObj).getDate().getSeason() == season;
        }

        return false;
    }

    public static class Serializer extends LootCondition.Serializer<Seasonal> {
        public Serializer() {
            super(new ResourceLocation(MODID, "season"), Seasonal.class);
        }

        public void serialize(JsonObject json, Seasonal value, JsonSerializationContext context) {
            json.addProperty("season", value.season.name().toLowerCase(Locale.ENGLISH));
        }

        public Seasonal deserialize(JsonObject json, JsonDeserializationContext context) {
            return new Seasonal(Season.valueOf(JsonUtils.getString(json, "season").toUpperCase(Locale.ENGLISH)));
        }
    }
}