package uk.joshiejack.penguinlib.util;

import uk.joshiejack.penguinlib.util.helpers.forge.OreDictionaryHelper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Matcher<T> {
    public static final AbstractOreMatcher ORE = new AbstractOreMatcher() {
        @Override
        boolean matches(String name, String ore) {
            return name.equals(ore);
        }
    };

    public static final AbstractOreMatcher ORE_PREFIX = new AbstractOreMatcher() {
        private final String[] PREFIXES = new String[] { "dye", "ingot", "block", "gem", "item", "food", "dust" }; //TODO: Load from csv?

        @Nullable
        @Override
        public String qualifies(String ore) {
            for (String s: PREFIXES) {
                if (ore.startsWith(s)) return s;
            }

            return null;
        }

        @Override
        boolean matches(String name, String ore) {
            return name.startsWith(ore);
        }
    };

    public static final AbstractOreMatcher ORE_SUFFIX = new AbstractOreMatcher() {
        private final String[] SUFFIXES = new String[] { "Wood" }; //TODO: Load from csv?

        @Nullable
        @Override
        public String qualifies(String ore) {
            for (String s: SUFFIXES) {
                if (ore.endsWith(s)) return s;
            }

            return null;
        }

        @Override
        boolean matches(String name, String ore) {
            return name.endsWith(ore);
        }
    };

    public static final AbstractOreMatcher ORE_CONTAINS = new AbstractOreMatcher() {
        @Override
        boolean matches(String name, String ore) {
            return name.contains(ore);
        }
    };

    public abstract boolean matches(@Nonnull ItemStack stack, T t);


    @SafeVarargs
    public final boolean matchesAny(@Nonnull ItemStack stack, T... t) {
        for (T value: t) {
            if (matches(stack, value)) return true;
        }

        return false;
    }

    public abstract static class AbstractOreMatcher extends Matcher<String> {
        @Nullable
        public String qualifies(String ore) {
            return ore;
        }

        @Override
        public boolean matches(@Nonnull ItemStack stack, String ore) {
            for (String name: OreDictionaryHelper.getOreNames(stack)) {
                if (matches(name, ore)) {
                    return true;
                }
            }

            return false;
        }

        abstract boolean matches(String name, String ore);
    }
}
