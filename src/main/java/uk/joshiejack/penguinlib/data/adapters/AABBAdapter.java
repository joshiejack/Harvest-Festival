package uk.joshiejack.penguinlib.data.adapters;

import com.google.gson.*;
import net.minecraft.util.math.AxisAlignedBB;

import java.lang.reflect.Type;


public class AABBAdapter implements JsonSerializer<AxisAlignedBB>, JsonDeserializer<AxisAlignedBB> {
    @Override
    public JsonElement serialize(AxisAlignedBB src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.minX + " " + src.minY + " " + src.minZ + " " + src.maxX + " " + src.maxY + " " + src.maxZ);
    }

    @Override
    public AxisAlignedBB deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String[] pos = json.getAsString().split(" ");
        return new AxisAlignedBB(Double.parseDouble(pos[0]), Double.parseDouble(pos[1]), Double.parseDouble(pos[2]),
                Double.parseDouble(pos[3]), Double.parseDouble(pos[4]), Double.parseDouble(pos[5]));
    }
}

