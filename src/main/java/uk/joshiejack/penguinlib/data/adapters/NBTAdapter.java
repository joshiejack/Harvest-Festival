package uk.joshiejack.penguinlib.data.adapters;

import com.google.gson.*;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.Type;


public class NBTAdapter implements JsonSerializer<NBTTagCompound>, JsonDeserializer<NBTTagCompound> {
    @Override
    public JsonElement serialize(NBTTagCompound src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString().replace(" ", "%20"));
    }

    @Override
    public NBTTagCompound deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return JsonToNBT.getTagFromJson(json.getAsString().replace("%20", " "));
        } catch (NBTException e) {
            e.printStackTrace();
           return new NBTTagCompound();
        }
    }
}

