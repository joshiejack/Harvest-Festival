package joshie.harvest.core.helpers;

import joshie.harvest.npc.render.NPCItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@SideOnly(Side.CLIENT)
public class StackRenderHelper {
    public static final TextureManager textureManager = MCClientHelper.getMinecraft().getTextureManager();
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation(MODID, "textures/gui/shadow.png");

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
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
        Minecraft mc = MCClientHelper.getMinecraft();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, (int) (left / size), (int) (top / size));
        String display = stack.stackSize > 1 ? stack.stackSize + "" : "";
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, (int) (left / size), (int) (top / size), display);
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        GlStateManager.enableAlpha();
    }

    @SideOnly(Side.CLIENT)
    public static void drawGreyStack(ItemStack stack, int left, int top, float size) {
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.scale(size, size, size);
        GlStateManager.disableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F); //Forge: Reset color in case Items change it.
        GlStateManager.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
        Minecraft mc = MCClientHelper.getMinecraft();
        renderItemAndEffectIntoGUI(mc.getRenderItem(), MCClientHelper.getPlayer(), stack, (int) (left / size), (int) (top / size));
        String display = stack.stackSize > 1 ? stack.stackSize + "" : "";
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, (int) (left / size), (int) (top / size), display);
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        GlStateManager.enableAlpha();
    }

    @SideOnly(Side.CLIENT)
    private static void renderItemAndEffectIntoGUI(RenderItem render, @Nullable EntityLivingBase p_184391_1_, final ItemStack p_184391_2_, int p_184391_3_, int p_184391_4_) {
        if (p_184391_2_ != null && p_184391_2_.getItem() != null)
        {
            render.zLevel += 50.0F;

            try
            {
                renderItemModelIntoGUI(render, p_184391_2_, p_184391_3_, p_184391_4_, render.getItemModelWithOverrides(p_184391_2_, null, p_184391_1_));
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
                crashreportcategory.setDetail("Item Type", () -> { return String.valueOf(p_184391_2_.getItem()); });
                crashreportcategory.setDetail("Item Aux", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return String.valueOf(p_184391_2_.getMetadata());
                    }
                });
                crashreportcategory.setDetail("Item NBT", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return String.valueOf((Object)p_184391_2_.getTagCompound());
                    }
                });
                crashreportcategory.setDetail("Item Foil", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return String.valueOf(p_184391_2_.hasEffect());
                    }
                });
                throw new ReportedException(crashreport);
            }

            render.zLevel -= 50.0F;
        }
    }

    @SideOnly(Side.CLIENT)
    private static void renderItemModelIntoGUI(RenderItem render, ItemStack stack, int x, int y, IBakedModel bakedmodel) {
        GlStateManager.pushMatrix();
        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        render.setupGuiTransform(x, y, bakedmodel.isGui3d());
        bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
        renderItem(render, stack, bakedmodel);
        GlStateManager.disableAlpha();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
    }

    @SideOnly(Side.CLIENT)
    private static void renderItem(RenderItem render, ItemStack stack, IBakedModel model)
    {
        if (stack != null)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);

            if (model.isBuiltInRenderer()) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableRescaleNormal();
                NPCItemRenderer.renderShadow = true;
                TileEntityItemStackRenderer.instance.renderByItem(stack);
                NPCItemRenderer.renderShadow = false;
            } else {
                render.renderModel(model, -1, stack);
                renderEffect(render, model);
            }

            GlStateManager.popMatrix();
        }
    }

    @SideOnly(Side.CLIENT)
    private static void renderEffect(RenderItem render, IBakedModel model) {
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        textureManager.bindTexture(RES_ITEM_GLINT);
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        render.renderModel(model, 0xFFFFFFFF);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        render.renderModel(model, 0xFFFFFFFF);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
    }
}
