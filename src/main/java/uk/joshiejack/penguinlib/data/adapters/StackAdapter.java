package uk.joshiejack.penguinlib.data.adapters;

import com.google.gson.*;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Type;


public class StackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(StackHelper.getStringFromStack(src));
    }

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return StackHelper.getStackFromString(json.getAsString());
    }
}

