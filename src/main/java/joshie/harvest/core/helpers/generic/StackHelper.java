package joshie.harvest.core.helpers.generic;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

public class StackHelper {

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
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, (int) (left / size), (int) (top / size), "");
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        GlStateManager.enableAlpha();
    }

    public static ItemStack getStackFromString(String str) {
        return getStackFromArray(str.trim().split(" "));
    }

    public static String getStringFromStack(ItemStack stack) {
        String str = Item.REGISTRY.getNameForObject(stack.getItem()).getResourcePath();
        if (stack.getHasSubtypes() || stack.hasTagCompound()) {
            str = str + " " + stack.getItemDamage();
        }

        if (stack.hasTagCompound()) {
            str = str + " " + stack.getTagCompound().toString();
        }

        return str;
    }

    public static String getStringFromObject(Object object) {
        if (object instanceof Item) {
            return getStringFromStack(new ItemStack((Item) object));
        } else if (object instanceof Block) {
            return getStringFromStack(new ItemStack((Block) object));
        } else if (object instanceof ItemStack) {
            return getStringFromStack((ItemStack) object);
        } else if (object instanceof String) {
            return (String) object;
        } else if (object instanceof List) {
            return getStringFromStack((ItemStack) ((List) object).get(0));
        } else return "";
    }

    public static boolean matches(String str, ItemStack stack) {
        return getStringFromStack(stack).equals(str);
    }

    private static ItemStack getStackFromArray(String[] str) {
        Item item = getItemByText(str[0]);
        int meta = 0;
        if (str.length > 1) {
            meta = parseInt(str[1]);
        }

        ItemStack stack = new ItemStack(item, 1, meta);
        if (str.length > 2) {
            String s = formatNBT(str, 2).getUnformattedText();
            try {
                NBTBase nbtbase = JsonToNBT.getTagFromJson(s);

                if (!(nbtbase instanceof NBTTagCompound)) return null;

                stack.setTagCompound((NBTTagCompound) nbtbase);
            } catch (Exception nbtexception) {
                return null;
            }
        }

        return stack;
    }

    private static Item getItemByText(String location) {
        Item item = Item.REGISTRY.getObject(new ResourceLocation(location));
        if (item == null) {
            try {
                item = Item.getItemById(Integer.parseInt(location));
            } catch (NumberFormatException ignored) {
            }
        }
        return item;
    }

    private static ITextComponent formatNBT(String[] str, int start) {
        TextComponentString textComponents = new TextComponentString("");

        for (int j = start; j < str.length; ++j) {
            if (j > start) {
                textComponents.appendText(" ");
            }

            TextComponentString object = new TextComponentString(str[j]);
            textComponents.appendSibling(object);
        }
        return textComponents;
    }

    private static int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException numberformatexception) {
            return 0;
        }
    }

    public static NBTTagCompound writeItemStackToNBT(NBTTagCompound tag, ItemStack stack) {
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        try {
            tag.setString("Name", Item.REGISTRY.getNameForObject(stack.getItem()).getResourcePath());
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
}