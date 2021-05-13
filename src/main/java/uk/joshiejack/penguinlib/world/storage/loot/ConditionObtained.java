package uk.joshiejack.penguinlib.world.storage.loot;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.scripting.DataScripting;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.Set;

public class ConditionObtained implements LootCondition {
    private final Set<Holder> holders;
    private final String listName;

    public ConditionObtained(String listName, Set<Holder> holders) {
        this.listName = listName;
        this.holders = holders;
    }

    @Override
    public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
        if (context.getKillerPlayer() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) context.getKillerPlayer();
            for (Holder holder: holders) {
                if (!DataScripting.obtained((PlayerJS) WrapperRegistry.wrap(player), listName, holder.toString())) return false;
            }

            return true;
        }

        return false;
    }

    public static class Serializer extends LootCondition.Serializer<ConditionObtained> {
        public Serializer() {
            super(new ResourceLocation(PenguinLib.MOD_ID, "obtained"), ConditionObtained.class);
        }

        @SuppressWarnings("ConstantConditions")
        public void serialize(@Nonnull JsonObject json, @Nonnull ConditionObtained value, @Nonnull JsonSerializationContext context) {
            if (value.holders.size() > 1) {
                StringBuilder builder = new StringBuilder();
                boolean first = true;
                for (Holder holder: value.holders) {
                    if (!first) builder.append(";");
                    builder.append(StackHelper.getStringFromStack(holder.getStacks().get(0)));

                    if (first) {
                        first = false;
                    }
                }

                json.addProperty("stack", builder.toString());
            } else {
                Holder holder = value.holders.stream().findFirst().orElse(Holder.getFromString("apple"));
                json.addProperty("stack", StackHelper.getStringFromStack(holder.getStacks().get(0)));
            }
        }

        @Nonnull
        public ConditionObtained deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            if (json.has("stacks")) {
                String[] split = json.get("stacks").getAsString().split(";");
                Set<Holder> holders = Sets.newHashSet();
                for (String s: split) {
                    holders.add(Holder.getFromString(s));
                }

                return new ConditionObtained(json.get("listName").getAsString(), holders);
            } else return new ConditionObtained(json.get("listName").getAsString(), Sets.newHashSet(Holder.getFromString(json.get("stack").getAsString())));
        }
    }
}
