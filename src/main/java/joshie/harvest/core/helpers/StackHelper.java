package joshie.harvest.core.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class StackHelper {
    @SideOnly(Side.CLIENT)
    public static void drawStack(ItemStack stack, int left, int top, float size) {
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.scale(size, size, size);
        GlStateManager.disableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F); //Forge: Reset color in case Items change it.
        GlStateManager.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft mc = MCClientHelper.getMinecraft();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, (int) (left / size), (int) (top / size));
        String display = stack.stackSize > 1 ? stack.stackSize + "" : "";
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, (int) (left / size), (int) (top / size), display);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        GlStateManager.enableAlpha();
    }

    public static ItemStack getStackFromString(String str) {
        if (str == null || str.equals("")) return null;
        return getStackFromArray(str.trim().split(" "));
    }

    public static String getStringFromStack(ItemStack stack) {
        String str = Item.REGISTRY.getNameForObject(stack.getItem()).toString().replace(" ", "%20");
        if (stack.getHasSubtypes() || stack.isItemStackDamageable()) {
            str = str + " " + stack.getItemDamage();
        }

        if (stack.stackSize > 1) {
            str = str + " *" + stack.stackSize;
        }

        if (stack.hasTagCompound()) {
            str = str + " " + stack.getTagCompound().toString();
        }

        return str;
    }

    public static NBTTagCompound getTag(String[] str, int pos) {
        String s = formatNBT(str, pos).getUnformattedText();
        try {
            NBTBase nbtbase = JsonToNBT.getTagFromJson(s);
            if (!(nbtbase instanceof NBTTagCompound)) return null;
            return (NBTTagCompound) nbtbase;
        } catch (Exception nbtexception) {
            return null;
        }
    }

    public static boolean isMeta(String str) {
        return !isNBT(str) && !isAmount(str);
    }

    public static boolean isNBT(String str) {
        return str.startsWith("{");
    }

    public static boolean isAmount(String str) {
        return str.startsWith("*");
    }

    private static ItemStack getStackFromArray(String[] str) {
        Item item = getItemByText(str[0]);
        if (item == null) return null;

        int meta = 0;
        int amount = 1;
        ItemStack stack = new ItemStack(item, 1, meta);
        NBTTagCompound tag = null;

        for (int i = 1; i <= 3; i++) {
            if (str.length > i) {
                if (isMeta(str[i])) meta = parseMeta(str[i]);
                if (isAmount(str[i])) amount = parseAmount(str[i]);
                if (isNBT(str[i])) tag = getTag(str, i);
            }
        }

        stack.setItemDamage(meta);
        stack.setTagCompound(tag);
        stack.stackSize = amount;
        return stack;
    }

    public static Item getItemByText(String str) {
        str = str.replace("%20", " ");
        Item item = Item.REGISTRY.getObject(new ResourceLocation(str));
        if (item == null) {
            try {
                Item item1 = Item.getItemById(Integer.parseInt(str));
                item = item1;
            } catch (NumberFormatException numberformatexception) {
            }
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

    public static NBTTagCompound writeItemStackToNBT(NBTTagCompound tag, ItemStack stack) {
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        try {
            tag.setString("Name", Item.REGISTRY.getNameForObject(stack.getItem()).toString());
            tag.setInteger("Count", (byte) stack.stackSize);
            tag.setInteger("Damage", (short) stack.getItemDamage());

            if (stack.getTagCompound() != null) {
                tag.setTag("tag", stack.getTagCompound());
            }
        } catch (Exception ignored) {
        }
        return tag;
    }

    public static ItemStack getItemStackFromNBT(NBTTagCompound tag) {
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        Item item = Item.REGISTRY.getObject(new ResourceLocation(tag.getString("Name")));
        int count = tag.getInteger("Count");
        int damage = tag.getInteger("Damage");
        if (damage < 0) {
            damage = 0;
        }

        ItemStack stack = new ItemStack(item, count, damage);
        if (tag.hasKey("tag", 10)) {
            stack.setTagCompound(tag.getCompoundTag("tag"));
        }
        return stack;
    }

    private static final List<ItemStack> allStacks = new ArrayList<>();

    @SideOnly(Side.CLIENT)
    public static List<ItemStack> getAllStacks() {
        if (allStacks.size() == 0) {
            for (CreativeTabs tab: CreativeTabs.CREATIVE_TAB_ARRAY) {
                tab.displayAllRelevantItems(allStacks);
            }
        }

        return allStacks;
    }
}