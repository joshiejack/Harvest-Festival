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

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Random;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class Seasonal implements LootCondition {
    private final Season season;

    public Seasonal(Season season) {
        this.season = season;
    }

    @Override
    public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
        if (context.getKillerPlayer() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) context.getKillerPlayer();
            return HFTrackers.getCalendar(player.world).getDate().getSeason() == season;
        }

        return false;
    }

    public static class Serializer extends LootCondition.Serializer<Seasonal> {
        public Serializer() {
            super(new ResourceLocation(MODID, "season"), Seasonal.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull Seasonal value, @Nonnull JsonSerializationContext context) {
            json.addProperty("season", value.season.name().toLowerCase(Locale.ENGLISH));
        }

        @Nonnull
        public Seasonal deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new Seasonal(Season.valueOf(JsonUtils.getString(json, "season").toUpperCase(Locale.ENGLISH)));
        }
    }
}