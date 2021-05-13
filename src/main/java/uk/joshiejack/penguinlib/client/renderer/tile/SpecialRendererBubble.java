package uk.joshiejack.penguinlib.client.renderer.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.penguinlib.item.PenguinItems;
import uk.joshiejack.penguinlib.item.base.ItemSpecial;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public abstract class SpecialRendererBubble<T extends TileEntity> extends SpecialRendererItem<T> {
    private static ItemStack SPEECH;

    protected ItemStack getSpeechBubble() {
        if (SPEECH == null) {
            SPEECH = PenguinItems.SPECIAL.getStackFromEnum(ItemSpecial.Special.SPEECH_BUBBLE);
        }

        return SPEECH;
    }

    @Override
    public void render(@Nonnull T tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (shouldRender(tile)) {
            ItemStack stack = getStack(tile);
            if (!stack.isEmpty()) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(x, y, z);
                RenderHelper.disableStandardItemLighting();
                RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
                GlStateManager.enableRescaleNormal();
                GlStateManager.translate(0.5F, getYOffset(), 0.5F);
                GlStateManager.scale(1.75F, 1.75F, 1.75F);
                GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate((float) (renderManager.options.thirdPersonView == 2 ? -1 : 1) * renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
                itemRenderer.renderItem(getSpeechBubble(), ItemCameraTransforms.TransformType.GROUND);
                GlStateManager.scale(0.75F, 0.75F, 0.75F);
                GlStateManager.translate(0F, 0.1F, 0.01F);
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.4F, 0.4F, -0.015F);
                GlStateManager.translate(0F, 0.25F, -2.5F);
                itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.GUI);
                GlStateManager.popMatrix();
                GlStateManager.disableRescaleNormal();
                RenderHelper.enableStandardItemLighting();
                GlStateManager.popMatrix();
            }
        }
    }

    public abstract ItemStack getStack(T tile);

    public abstract boolean shouldRender(T tile);

    public float getYOffset() {
        return 1.25F;
    }
}
