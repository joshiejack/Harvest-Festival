package uk.joshiejack.gastronomy.client.renderer.tile;

import uk.joshiejack.gastronomy.client.model.ModelOvenDoor;
import uk.joshiejack.gastronomy.tile.TileOven;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static uk.joshiejack.gastronomy.Gastronomy.MODID;
import static net.minecraft.util.EnumFacing.*;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
@PenguinLoader(value = "oven", side = Side.CLIENT)
public class SpecialRendererOven extends SpecialRendererCookware<TileOven> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/blocks/kitchen/oven_door.png");
    private final ModelOvenDoor ovenModel = new ModelOvenDoor();

    @Override
    public void renderFluid(int i, ResourceLocation fluid) {
        renderFluidCube(fluid, 0.5F, 0.3F + (i * 0.001F), 0.5F, 0.725F);
    }

    @Override
    public void translateItem(boolean isBlock, float position, float rotation, float offset1, float offset2) {
        GlStateManager.translate(0.5F, 0.11F, 0.5F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
        if (!isBlock) {
            GlStateManager.rotate(-90, 1F, 0F, 0F);
            GlStateManager.rotate(rotation, 0F, 0F, 1F);
            GlStateManager.translate(offset1, offset2, position);
        } else {
            GlStateManager.rotate(90, 1F, 0F, 0F);
            GlStateManager.translate(offset1 * 1.4F, 0.8F - offset2 * 2.5F, position - 1F);
        }
    }

    @Override
    public void translateResult(TileOven oven, boolean isBlock) {
        GlStateManager.translate(0.5F, 0.32F, 0.5F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
        if (!isBlock) {
            GlStateManager.rotate(-90, 1F, 0F, 0F);
        } else {
            GlStateManager.rotate(90, 0F, 1F, 0F);
        }
    }

    @Override
    public void render(TileOven oven, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (destroyStage >= 0) {
            bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            bindTexture(TEXTURE);
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        if (destroyStage < 0) GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        EnumFacing facing = oven.getFacing();
        int j = facing == NORTH ? 180 : facing == SOUTH ? 0 : facing == WEST ? 90 : -90;
        if (facing == SOUTH) GlStateManager.translate(1F, 0F, 1F);
        else if (facing == WEST) GlStateManager.translate(1F, 0F, 0F);
        else if (facing == EAST) GlStateManager.translate(0F, 0F, 1F);

        GlStateManager.rotate((float) j, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        float f = oven.prevLidAngle + (oven.lidAngle - oven.prevLidAngle) * partialTicks;
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        ovenModel.door.rotateAngleX = -(f * ((float) Math.PI / 2F));
        ovenModel.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }

        //Only render the internals if the oven is open
        if (oven.lidAngle != 0F) {
            super.render(oven, x, y, z, partialTicks, destroyStage, alpha);
        }
    }
}
