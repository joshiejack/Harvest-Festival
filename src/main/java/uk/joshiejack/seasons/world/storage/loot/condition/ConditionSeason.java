package uk.joshiejack.seasons.world.storage.loot.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.Seasons;
import uk.joshiejack.seasons.world.storage.AbstractWorldData;
import uk.joshiejack.seasons.world.storage.SeasonsSavedData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Random;

@SuppressWarnings("WeakerAccess")
public class ConditionSeason implements LootCondition {
    private final Season season;

    public ConditionSeason(Season season) {
        this.season = season;
    }

    @Override
    public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
        if (context.getKillerPlayer() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) context.getKillerPlayer();
            return SeasonsSavedData.getWorldData(player.world).getSeasonAt(player.world, new BlockPos(player), AbstractWorldData.CheckMoreThanBiome.YES).contains(season);
        }

        return false;
    }

    public static class Serializer extends LootCondition.Serializer<ConditionSeason> {
        public Serializer() {
            super(new ResourceLocation(Seasons.MODID, "season"), ConditionSeason.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull ConditionSeason value, @Nonnull JsonSerializationContext context) {
            json.addProperty("season", value.season.name().toLowerCase(Locale.ENGLISH));
        }

        @Nonnull
        public ConditionSeason deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new ConditionSeason(Season.valueOf(JsonUtils.getString(json, "season").toUpperCase(Locale.ENGLISH)));
        }
    }
}
