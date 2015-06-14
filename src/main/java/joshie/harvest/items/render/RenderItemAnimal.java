package joshie.harvest.items.render;

import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.entity.EntityHarvestSheep;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.items.ItemAnimal;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderItemAnimal implements IItemRenderer {
    private static EntityChicken chicken;
    private static EntityCow cow;
    private static EntitySheep sheep;
    
    public void init() {
        chicken = new EntityChicken(MCClientHelper.getWorld());
        cow = new EntityHarvestCow(MCClientHelper.getWorld());
        sheep = new EntityHarvestSheep(MCClientHelper.getWorld());
    }

    @Override
    public boolean handleRenderType(ItemStack stack, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        if (chicken == null) {
            init();
        }
        
        GL11.glPushMatrix();
        if (stack.getItemDamage() == ItemAnimal.CHICKEN) {
            GuiInventory.func_147046_a(7, 15, 14, 50F, 0F, chicken);
        } else if (stack.getItemDamage() == ItemAnimal.COW) {
            GuiInventory.func_147046_a(7, 15, 9, 35F, 0F, cow);
        } else if (stack.getItemDamage() == ItemAnimal.SHEEP) {
            GuiInventory.func_147046_a(7, 15, 10, 50F, 0F, sheep);
        }

        GL11.glPopMatrix();
        
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        short short1 = 240;
        short short2 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)short1 / 1.0F, (float)short2 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
