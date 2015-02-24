package joshie.harvestmoon.core.helpers.generic;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class StackHelper {
    private final static RenderItem itemRenderer = new RenderItem();

    public static void drawStack(ItemStack stack, int left, int top, float size) {
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glPushMatrix();
        GL11.glScalef(size, size, size);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor3f(1F, 1F, 1F); //Forge: Reset color in case Items change it.
        GL11.glEnable(GL11.GL_BLEND); //Forge: Make sure blend is enabled else tabs show a white border.
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft mc = MCClientHelper.getMinecraft();
        itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, (int) (left / size), (int) (top / size));
        itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, (int) (left / size), (int) (top / size));
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    public static void renderStack(Minecraft mc, RenderBlocks blockRenderer, RenderItem itemRenderer, ItemStack stack, int x, int y) {
        if (stack != null && stack.getItem() != null) {
            try {
                GL11.glPushMatrix();
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                RenderHelper.enableGUIStandardItemLighting();
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                if (!ForgeHooksClient.renderInventoryItem(blockRenderer, mc.getTextureManager(), stack, itemRenderer.renderWithColor, itemRenderer.zLevel, x, y)) {
                    itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, x, y, false);
                }

                RenderHelper.disableStandardItemLighting();
                GL11.glPopMatrix();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            } catch (Exception e) {}
        }
    }

    public static ItemStack getStackFromString(String str) {
        return getStackFromArray(str.trim().split(" "));
    }

    public static String getStringFromStack(ItemStack stack) {
        String str = Item.itemRegistry.getNameForObject(stack.getItem());
        if (stack.getHasSubtypes() || stack.hasTagCompound()) {
            str = str + " " + stack.getItemDamage();
        }

        if (stack.hasTagCompound()) {
            str = str + " " + stack.stackTagCompound.toString();
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
                NBTBase nbtbase = JsonToNBT.func_150315_a(s);

                if (!(nbtbase instanceof NBTTagCompound)) return null;

                stack.setTagCompound((NBTTagCompound) nbtbase);
            } catch (Exception nbtexception) {
                return null;
            }
        }

        return stack;
    }

    private static Item getItemByText(String str) {
        Item item = (Item) Item.itemRegistry.getObject(str);
        if (item == null) {
            try {
                Item item1 = Item.getItemById(Integer.parseInt(str));
                item = item1;
            } catch (NumberFormatException numberformatexception) {
                ;
            }
        }

        return item;
    }

    private static IChatComponent formatNBT(String[] str, int start) {
        ChatComponentText chatcomponenttext = new ChatComponentText("");

        for (int j = start; j < str.length; ++j) {
            if (j > start) {
                chatcomponenttext.appendText(" ");
            }

            Object object = new ChatComponentText(str[j]);
            chatcomponenttext.appendSibling((IChatComponent) object);
        }

        return chatcomponenttext;
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
            tag.setString("Name", Item.itemRegistry.getNameForObject(stack.getItem()));
            tag.setInteger("Count", (byte) stack.stackSize);
            tag.setInteger("Damage", (short) stack.getItemDamage());

            if (stack.stackTagCompound != null) {
                tag.setTag("tag", stack.stackTagCompound);
            }
        } catch (Exception e) {}

        return tag;
    }

    public static ItemStack getItemStackFromNBT(NBTTagCompound tag) {
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        Item item = (Item) Item.itemRegistry.getObject(tag.getString("Name"));
        int count = tag.getInteger("Count");
        int damage = tag.getInteger("Damage");
        if (damage < 0) {
            damage = 0;
        }

        ItemStack stack = new ItemStack(item, count, damage);
        if (tag.hasKey("tag", 10)) {
            stack.stackTagCompound = tag.getCompoundTag("tag");
        }

        return stack;
    }
}
