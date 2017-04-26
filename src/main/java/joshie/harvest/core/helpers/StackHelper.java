package joshie.harvest.core.helpers;

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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class StackHelper {

    @Nonnull
    public static ItemStack getStackFromString(String str) {
        if (str == null || str.equals("")) return ItemStack.EMPTY;
        return getStackFromArray(str.trim().split(" "));
    }

    @SuppressWarnings("ConstantConditions")
    public static String getStringFromStack(@Nonnull ItemStack stack) {
        String str = Item.REGISTRY.getNameForObject(stack.getItem()).toString().replace(" ", "%20");
        if (stack.getHasSubtypes() || stack.isItemStackDamageable()) {
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

    public static NBTTagCompound getTag(String str) {
        String s = formatNBT(str).getUnformattedText().replace("%20", " ");
        try {
            NBTBase nbtbase = JsonToNBT.getTagFromJson(s);
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
            } catch (NumberFormatException ignored) {}
        }

        return item;
    }

    private static ITextComponent formatNBT(String str) {
        TextComponentString chatcomponenttext = new TextComponentString("");
        Object object = new TextComponentString(str);
        chatcomponenttext.appendSibling((ITextComponent) object);
        return chatcomponenttext;
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

    private static final NonNullList<ItemStack> allStacks = NonNullList.create();

    @SideOnly(Side.CLIENT)
    public static NonNullList<ItemStack> getAllStacks() {
        if (allStacks.size() == 0) {
            for (CreativeTabs tab: CreativeTabs.CREATIVE_TAB_ARRAY) {
                tab.displayAllRelevantItems(allStacks);
            }
        }

        return allStacks;
    }

    @Nonnull
    public static ItemStack toStack(@Nonnull ItemStack stack, int size) {
        ItemStack copy = stack.copy();
        copy.setCount(size);
        return copy;
    }
}