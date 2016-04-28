package joshie.harvest.items.render;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.items.ItemNPCSpawner;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.HashMap;

/*public class RenderItemNPC implements IItemRenderer {
    private static HashMap<INPC, EntityLivingBase> render = new HashMap();
    private static boolean isInit = false;

    public boolean init() {
        for (INPC npc : HFApi.NPC.getNPCs()) {
            EntityNPC entity = new EntityNPC(null, MCClientHelper.getWorld(), npc);
            entity.hideName = true;
            render.put(npc, entity);
        }

        return true;
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
        INPC npc = ItemNPCSpawner.getNPC(stack.getItemDamage());

        if (!isInit) {
            isInit = init();
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        GuiInventory.func_147046_a(8, 16, 7, 0F, 0F, render.get(npc));
        GL11.glPopMatrix();

        RenderHelper.enableGUIStandardItemLighting();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        short short1 = 240;
        short short2 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)short1 / 1.0F, (float)short2 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}*/