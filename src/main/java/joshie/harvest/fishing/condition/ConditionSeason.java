package joshie.harvest.fishing.condition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.HFTrackers;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import java.util.Locale;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ConditionSeason extends AbstractCondition {
    private final Season season;

    public ConditionSeason(Season season) {
        this.season = season;
    }

    @Override
    public boolean testCondition(World world, BlockPos pos) {
        return HFTrackers.getCalendar(world).getDate().getSeason() == season;
    }

    public static class Serializer extends LootCondition.Serializer<ConditionSeason> {
        public Serializer() {
            super(new ResourceLocation(MODID, "season"), ConditionSeason.class);
        }

        public void serialize(JsonObject json, ConditionSeason value, JsonSerializationContext context) {
            json.addProperty("season", value.season.name().toLowerCase(Locale.ENGLISH));
        }

        public ConditionSeason deserialize(JsonObject json, JsonDeserializationContext context) {
            return new ConditionSeason(Season.valueOf(JsonUtils.getString(json, "season").toUpperCase(Locale.ENGLISH)));
        }
    }
}
