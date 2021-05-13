package uk.joshiejack.settlements.world.storage.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.quest.Quest;
import uk.joshiejack.settlements.quest.data.QuestTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;
import java.util.Random;

import static uk.joshiejack.settlements.Settlements.MODID;

public class ConditionQuest implements LootCondition {
    private final Quest quest;
    private final int completed;

    public ConditionQuest(Quest quest, int completed) {
        this.quest = quest;
        this.completed = completed;
    }

    @Override
    public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
        if (context.getKillerPlayer() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) context.getKillerPlayer();
            QuestTracker tracker = AdventureDataLoader.get(context.getWorld()).getTrackerForQuest(player, quest);
            return completed == 0 ? !tracker.hasCompleted(quest.getRegistryName()) : tracker.hasCompleted(quest.getRegistryName(), completed);
        }

        return false;
    }

    public static class Serializer extends LootCondition.Serializer<ConditionQuest> {
        public Serializer() {
            super(new ResourceLocation(MODID, "quest"), ConditionQuest.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull ConditionQuest value, @Nonnull JsonSerializationContext context) {
            json.addProperty("id", value.quest.getRegistryName().toString());
            json.addProperty("completed", value.completed);
        }

        @Nonnull
        public ConditionQuest deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new ConditionQuest(Quest.REGISTRY.get(new ResourceLocation(json.get("id").getAsString())), json.get("completed").getAsInt());
        }
    }
}
