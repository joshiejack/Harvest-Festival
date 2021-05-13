package uk.joshiejack.penguinlib.world.storage.loot;

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

public class ConditionUnobtained implements LootCondition {
    private final Holder holder;
    private final String listName;

    public ConditionUnobtained(String listName, Holder holder) {
        this.listName = listName;
        this.holder = holder;
    }

    @Override
    public boolean testCondition(@Nonnull Random rand, @Nonnull LootContext context) {
        if (context.getKillerPlayer() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) context.getKillerPlayer();
            return !DataScripting.obtained((PlayerJS) WrapperRegistry.wrap(player), listName, holder.toString());
        }

        return false;
    }

    public static class Serializer extends LootCondition.Serializer<ConditionUnobtained> {
        public Serializer() {
            super(new ResourceLocation(PenguinLib.MOD_ID, "unobtained"), ConditionUnobtained.class);
        }

        public void serialize(@Nonnull JsonObject json, @Nonnull ConditionUnobtained value, @Nonnull JsonSerializationContext context) {
            json.addProperty("listName", value.listName);
            json.addProperty("stack", StackHelper.getStringFromStack(value.holder.getStacks().get(0)));
        }

        @Nonnull
        public ConditionUnobtained deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new ConditionUnobtained(json.get("listName").getAsString(), Holder.getFromString(json.get("stack").getAsString()));
        }
    }
}
