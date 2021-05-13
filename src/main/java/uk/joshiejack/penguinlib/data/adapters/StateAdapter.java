package uk.joshiejack.penguinlib.data.adapters;

import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.Level;
import uk.joshiejack.penguinlib.PenguinLib;

import javax.annotation.Nullable;
import java.lang.reflect.Type;


public class StateAdapter implements JsonSerializer<IBlockState>, JsonDeserializer<IBlockState> {
    public static <T extends Comparable<T>> IBlockState withProperty(IProperty<T> property, IBlockState state, String s) {
        if (property == null) {
            PenguinLib.logger.log(Level.ERROR, "Massive issue with loading: " + s + "on block: " + state);
            return state;
        }

        com.google.common.base.Optional<?> theProperty = property.parseValue(s);
        if (theProperty.isPresent()) {
            return state.withProperty(property, property.parseValue(s).get());
        } else {
            PenguinLib.logger.log(Level.ERROR, "Could not create a block state with the properties " + s);
            return state;
        }
    }

    @Nullable
    public static IBlockState fromString(String string) {
        if (string.contains("[")) string = string.replace("[", ",").replace("]", ""); //remove the brackets
        if (string.contains("=")) {
            String[] sections = string.split(",");
            ResourceLocation resource = new ResourceLocation(sections[0]);
            if (!Loader.isModLoaded(resource.getNamespace())) return null;
            Block block = Block.REGISTRY.getObject(resource);
            IBlockState state = block.getDefaultState();
            for (int i = 1; i < sections.length; i++) {
                String[] sProperty = sections[i].split("=");
                String propertyName = sProperty[0];
                String propertyValue = sProperty[1];

                IProperty<?> property = block.getBlockState().getProperty(propertyName);
                state = withProperty(property, state, propertyValue);
            }

            return state;
        } else{
            String[] state = string.split(" ");
            ResourceLocation block = new ResourceLocation(state[0]);
            if (!Loader.isModLoaded(block.getNamespace())) return null;
            int meta = state.length == 2 ? Integer.parseInt(state[1]) : 0;
            return Block.REGISTRY.getObject(block).getStateFromMeta(meta);
        }
    }

    public static String toString(IBlockState src) {
        String s = src.toString().replace("[", ",").replace("]", "");
        if (s.startsWith("minecraft:")) s = s.replace("minecraft:", "");
        return s;
    }

    @Override
    public JsonElement serialize(IBlockState src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(toString(src));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return fromString(json.getAsString());
    }
}

