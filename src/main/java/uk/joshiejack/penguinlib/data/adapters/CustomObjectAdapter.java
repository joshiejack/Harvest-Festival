package uk.joshiejack.penguinlib.data.adapters;

import com.google.gson.*;
import uk.joshiejack.penguinlib.data.custom.*;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Array;
import java.lang.reflect.Type;


public class CustomObjectAdapter implements JsonSerializer<AbstractCustomObject>, JsonDeserializer<AbstractCustomObject> {
    @Override
    public JsonElement serialize(AbstractCustomObject src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.addProperty("registryName", src.registryName.toString());
        result.addProperty("type", src.type);
        if (src instanceof AbstractCustomObject.Singular) {
            result.add("data", context.serialize(((AbstractCustomObject.Singular) src).data, AbstractCustomObject.TYPE_REGISTRY.get(src.type).getClass()).getAsJsonObject());
        } else {
            result.add("defaults", context.serialize(((AbstractCustomObject.Multi) src).defaults, AbstractCustomObject.TYPE_REGISTRY.get(src.type).getClass()).getAsJsonObject());
            result.add("data", context.serialize(((AbstractCustomObject.Multi) src).data, AbstractCustomObject.TYPE_REGISTRY.get(src.type).getClass()).getAsJsonArray());
        }

        return result;
    }

    @Override
    public AbstractCustomObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        AbstractCustomObject object = jsonObject.has("defaults") ? new AbstractCustomObject.Multi() : new AbstractCustomObject.Singular();
        object.registryName = new ResourceLocation(jsonObject.get("registryName").getAsString());
        object.type = jsonObject.get("type").getAsString();
        if (jsonObject.has("data")) {
            Class<?> clazz = AbstractCustomObject.TYPE_REGISTRY.get(object.type).getClass();
            if (object instanceof AbstractCustomObject.Singular) {
                ((AbstractCustomObject.Singular) object).data = context.deserialize(jsonObject.getAsJsonObject("data"), clazz);
            } else {
                ((AbstractCustomObject.Multi) object).defaults = context.deserialize(jsonObject.getAsJsonObject("defaults"), clazz);
                ((AbstractCustomObject.Multi) object).data = context.deserialize(jsonObject.getAsJsonArray("data"), Array.newInstance(clazz, 0).getClass());
            }

            String subType = object.type.split(":")[1];
            if (object instanceof AbstractCustomObject.Singular) ((AbstractCustomObject.Singular) object).data.type = subType;
            else {
                ((AbstractCustomObject.Multi) object).defaults.type = subType;
                for (AbstractCustomData<?, ?> data : ((AbstractCustomObject.Multi) object).data) {
                    data.type = subType; //Set the type individually
                }
            }
        }

        return object;
    }
}

