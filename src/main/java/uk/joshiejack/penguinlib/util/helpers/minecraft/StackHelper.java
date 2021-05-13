package uk.joshiejack.penguinlib.util.helpers.minecraft;

import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.interfaces.IPenguinMulti;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class StackHelper {
    private static final Map<String, ItemStack> SYNONYMNS = Maps.newHashMap();

    public static void registerSynonym(String name, ItemStack stack) {
        SYNONYMNS.put(name, stack);
    }

    public static void registerSynonym(String name, Item item, int damage) {
        SYNONYMNS.put(name, new ItemStack(item, 1, damage));
    }

    public static boolean isSynonymn(String string) {
        return SYNONYMNS.containsKey(string);
    }

    @SafeVarargs
    public static <I extends IPenguinMulti<E>, E extends Enum> void registerSynonym(I... items) {
        for (I item: items) {
            for (E e : item.getValues()) {
                String name = e.name().toLowerCase(Locale.ENGLISH);
                if (item instanceof Item)
                    registerSynonym(name, new ItemStack((Item) item, 1, e.ordinal()));
                else if (item instanceof Block)
                    registerSynonym(name, new ItemStack((Block) item, 1, e.ordinal()));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("synonymns").rows().forEach(row -> {
            String name = row.get("name");
            ItemStack stack = StackHelper.getStackFromString(row.get("item"));
            registerSynonym(name, stack);
        });
    }

    @SafeVarargs
    public static <I extends IPenguinMulti<E>, E extends Enum> void registerSynonymWithSuffix(String suffix, I... items) {
        for (I item: items) {
            for (E e : item.getValues()) {
                String name = e.name().toLowerCase(Locale.ENGLISH) + "_" + suffix;
                if (item instanceof Item)
                    registerSynonym(name, new ItemStack((Item) item, 1, e.ordinal()));
                else if (item instanceof Block)
                    registerSynonym(name, new ItemStack((Block) item, 1, e.ordinal()));
            }
        }
    }

    @Nonnull
    public static ItemStack getStackFromString(String str) {
        if (str == null || str.equals("") || str.equals("none")) return ItemStack.EMPTY;
         if (SYNONYMNS.containsKey(str)) {
            return SYNONYMNS.get(str).copy();
        }

         String[] split = str.split(" ");
         if (split.length == 2 && split[1].startsWith("*")) {
             if (SYNONYMNS.containsKey(split[0])) {
                 ItemStack ret = SYNONYMNS.get(split[0]).copy();
                 ret.setCount(parseAmount(split[1]));
                 return ret;
             }
         }

        if (!str.contains(":")) str = "minecraft:" + str;
        return getStackFromArray(str.trim().split(" "));
    }

    @SuppressWarnings("ConstantConditions")
    public static String getStringFromStack(ItemStack stack) {
        for (Map.Entry<String, ItemStack> entry: SYNONYMNS.entrySet()) {
            if (ItemStack.areItemsEqual(stack, entry.getValue()) && ItemStack.areItemStackTagsEqual(stack, entry.getValue())) {
                return entry.getKey();
            }
        }

        String str = Item.REGISTRY.getNameForObject(stack.getItem()).toString().replace(" ", "%20").replace("minecraft:", "");
        if (stack.getItemDamage() != OreDictionary.WILDCARD_VALUE && (stack.getHasSubtypes() || (stack.isItemStackDamageable() && stack.getItemDamage() != 0))) {
            str = str + " " + stack.getItemDamage();
        }

        if (stack.getCount() > 1) {
            str = str + " *" + stack.getCount();
        }

        if (stack.hasTagCompound()) {
            str = str + " " + stack.getTagCompound().toString().replace(" ", "%20");
        }

        return str;
    }

    @SuppressWarnings("ConstantConditions")
    private static NBTTagCompound getTag(String[] str, int pos) {
        String s = formatNBT(str, pos).getUnformattedText().replace("%20", " ");
        try {
            NBTBase nbtbase = JsonToNBT.getTagFromJson(s);
            if (!(nbtbase instanceof NBTTagCompound)) return null;
            return (NBTTagCompound) nbtbase;
        } catch (Exception nbtexception) {
            return null;
        }
    }

    private static boolean isMeta(String str) {
        return !isNBT(str) && !isAmount(str);
    }

    private static boolean isNBT(String str) {
        return str.startsWith("{");
    }

    private static boolean isAmount(String str) {
        return str.startsWith("*");
    }

    @SuppressWarnings("ConstantConditions")
    @Nonnull
    private static ItemStack getStackFromArray(String[] str) {
        Item item = getItemByText(str[0]);
        if (item == null) return ItemStack.EMPTY;

        int meta = 0;
        int amount = 1;

        NBTTagCompound tag = null;
        for (int i = 1; i <= 3; i++) {
            if (str.length > i) {
                if (isMeta(str[i])) meta = parseMeta(str[i]);
                if (isAmount(str[i])) amount = parseAmount(str[i]);
                if (isNBT(str[i])) tag = getTag(str, i);
            }
        }

        ItemStack stack = new ItemStack(item, 1, meta);
        stack.setTagCompound(tag);
        stack.setCount(amount);
        return stack;
    }


    private static Item getItemByText(String str) {
        str = str.replace("%20", " ");
        Item item = Item.REGISTRY.getObject(new ResourceLocation(str));
        if (item == null) {
            try {
                item = Item.getItemById(Integer.parseInt(str));
            } catch (NumberFormatException numberformatexception) { /*ignore*/}
        }

        return item;
    }

    private static ITextComponent formatNBT(String[] str, int start) {
        TextComponentString chatcomponenttext = new TextComponentString("");
        for (int j = start; j < str.length; ++j) {
            if (j > start) {
                chatcomponenttext.appendText(" ");
            }

            Object object = new TextComponentString(str[j]);
            chatcomponenttext.appendSibling((ITextComponent) object);
        }

        return chatcomponenttext;
    }

    private static int parseMeta(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException numberformatexception) {
            numberformatexception.printStackTrace();
            return 0;
        }
    }

    private static int parseAmount(String str) {
        try {
            return Integer.parseInt(str.substring(1, str.length()));
        } catch (NumberFormatException numberformatexception) {
            return 0;
        }
    }

    private static final NonNullList<ItemStack> stacks = NonNullList.create();

    public static NonNullList<ItemStack> getAllItemsCopy() {
        NonNullList<ItemStack> stacks = NonNullList.create();
        StackHelper.getAllItems().forEach(stack -> stacks.add(stack.copy()));
        return stacks;
    }

    public static NonNullList<ItemStack> getAllItems() {
        if (stacks.isEmpty()) {
            for (Item item : Item.REGISTRY) {
                for (CreativeTabs creativeTab : item.getCreativeTabs()) {
                    if (creativeTab == null) continue;
                    item.getSubItems(creativeTab, stacks);
                }
            }

            stacks.removeIf(Objects::isNull);
        }

        return stacks;
    }

    public static void writeToNBT(ItemStack stack, NBTTagCompound nbt) {
        stack.writeToNBT(nbt);
        if (stack.getCount() > 127) {
            nbt.removeTag("Count");
            nbt.setInteger("Count", stack.getCount());
        }
    }

    public static ItemStack readFromNBT(NBTTagCompound nbt) {
        ItemStack stack = new ItemStack(nbt);
        if (nbt.hasKey("Count", Constants.NBT.TAG_INT)) {
            stack.setCount(nbt.getInteger("Count"));
        }

        return stack;
    }

    public static ItemStack toStack(ItemStack stack, int size) {
        ItemStack copy = stack.copy();
        copy.setCount(size);
        return copy;
    }

    public static NonNullList<ItemStack> getStacksFromString(String text) {
        NonNullList<ItemStack> list = NonNullList.create();
        for (String s: text.split(",")) {
            ItemStack created = StackHelper.getStackFromString(s.trim());
            if (!created.isEmpty()) {
                list.add(created);
            }
        }

        return list;
    }

    public static ItemStack withNBT(Item item, NBTTagCompound tag) {
        ItemStack stack = new ItemStack(item);
        stack.setTagCompound(tag);
        return stack;
    }

    public static ItemStack getStackFromState(IBlockState state) {
        try {
            return (ItemStack) ReflectionHelper.findMethod(Block.class, "getSilkTouchDrop", "func_180643_i", IBlockState.class).invoke(state.getBlock(), state);
        } catch (IllegalAccessException | InvocationTargetException e) { return ItemStack.EMPTY; }
    }
}
