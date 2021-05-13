package uk.joshiejack.penguinlib.util.helpers.minecraft;

import uk.joshiejack.penguinlib.client.GuiElements;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class StackRenderHelper {
    private static TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
    private static TextureManager shadowManager = new TextureManagerGrey(Minecraft.getMinecraft().getResourceManager());

    @SideOnly(Side.CLIENT)
    public static void drawStack(@Nonnull ItemStack stack, int left, int top, float size) {
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.scale(size, size, size);
        GlStateManager.disableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F); //Forge: Reset color in case Items change it.
        GlStateManager.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
        Minecraft mc = Minecraft.getMinecraft();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, (int) (left / size), (int) (top / size));
        String display = stack.getCount() >= 1000 ? "" + StringHelper.convertNumberToString(stack.getCount(), true) : stack.getCount() > 1 ? stack.getCount() + "" : "";
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, stack, (int) (left / size), (int) (top / size), display);
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        GlStateManager.enableAlpha();
    }

    @SideOnly(Side.CLIENT)
    public static void drawGreyStack(ItemStack stack, int left, int top, float size) {
        //"Clear out the TextureManager *//

        //TileEntityRendererDispatcher.instance.renderEngine = textureManager;
        //ReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(), textureManager, "renderEngine");
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.scale(size, size, size);
        GlStateManager.disableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F); //Forge: Reset color in case Items change it.
        GlStateManager.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
        Minecraft mc = Minecraft.getMinecraft();
        renderItemAndEffectIntoGUI(mc.getRenderItem(), mc.player, stack, (int) (left / size), (int) (top / size));
        String display = stack.getCount() >= 1000 ? "" + StringHelper.convertNumberToString(stack.getCount(), true) : stack.getCount() > 1 ? stack.getCount() + "" : "";
        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, stack, (int) (left / size), (int) (top / size), display);
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        GlStateManager.enableAlpha();

    }

    @SideOnly(Side.CLIENT)
    private static void renderItemAndEffectIntoGUI(RenderItem render, @Nullable EntityLivingBase p_184391_1_, final ItemStack p_184391_2_, int p_184391_3_, int p_184391_4_) {
        if (p_184391_2_ != null) {
            render.zLevel += 50.0F;

            try {
                renderItemModelIntoGUI(render, p_184391_2_, p_184391_3_, p_184391_4_, render.getItemModelWithOverrides(p_184391_2_, null, p_184391_1_));
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
                crashreportcategory.addDetail("Item Type", () -> String.valueOf(p_184391_2_.getItem()));
                crashreportcategory.addDetail("Item Aux", () -> String.valueOf(p_184391_2_.getMetadata()));
                crashreportcategory.addDetail("Item NBT", () -> String.valueOf(p_184391_2_.getTagCompound()));
                crashreportcategory.addDetail("Item Foil", () -> String.valueOf(p_184391_2_.hasEffect()));
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
    private static void renderItem(RenderItem render, ItemStack stack, IBakedModel model) {
        if (stack != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);

            if (model.isBuiltInRenderer()) {
                Minecraft.getMinecraft().renderEngine = shadowManager;
                TileEntityRendererDispatcher.instance.renderEngine = shadowManager;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1F);
                GlStateManager.disableLighting();
                GlStateManager.enableRescaleNormal();
                TileEntityItemStackRenderer.instance.renderByItem(stack);
                Minecraft.getMinecraft().renderEngine = textureManager;
                TileEntityRendererDispatcher.instance.renderEngine = textureManager;
            } else {
                render.renderModel(model, -1, stack);
                renderShadowEffect(render, model);
            }

            GlStateManager.popMatrix();
        }
    }

    @SideOnly(Side.CLIENT)
    private static void renderShadowEffect(RenderItem render, IBakedModel model) {
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        textureManager.bindTexture(GuiElements.RES_ITEM_GLINT);
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
