package uk.joshiejack.penguinlib.util.helpers.generic;

import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static net.minecraftforge.fml.relauncher.ReflectionHelper.findField;

public class ReflectionHelper {
    public static <T, E> void setPrivateValue(Class<? super T> classToAccess, T instance, E value, String... fieldNames) {
        net.minecraftforge.fml.relauncher.ReflectionHelper.setPrivateValue(classToAccess, instance, value, fieldNames);
    }

    public static <T, E> void setPrivateFinalValue(Class<? super T> classToAccess, T instance, E value, String... fieldNames) {
        try {
            Field field = findField(classToAccess, fieldNames);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(instance, value);
        } catch (Exception e) {
            throw new UnableToAccessFieldException(fieldNames, e);
        }
    }

    public static <E, I> I getPrivateValue(Class <? super E > classToAccess, E instance, String... fieldNames) {
        return net.minecraftforge.fml.relauncher.ReflectionHelper.getPrivateValue(classToAccess, instance, fieldNames);
    }
}
