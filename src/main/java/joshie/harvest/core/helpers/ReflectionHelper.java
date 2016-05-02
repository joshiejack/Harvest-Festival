package joshie.harvest.core.helpers;

import net.minecraft.block.Block;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionHelper {
    public static boolean setFinalField(Object object, Object value, String... name) {
        for (String n : name) {
            try {
                Field field = Block.class.getDeclaredField(n);
                field.setAccessible(true);

                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

                field.set(object, value);
                return true;
            } catch (Exception e) { e.printStackTrace(); }
        }

        return false;
    }
}
