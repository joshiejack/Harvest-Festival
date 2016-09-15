package joshie.harvest.cooking.render;

import joshie.harvest.cooking.tile.TileMixer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SpecialRendererMixer extends SpecialRendererCookware<TileMixer> {
    @Override
    protected void renderCookware(TileMixer mixer) {
        //Render the blade as an item
        renderIngredient(TileMixer.BLADE_STACK, -0.38F, mixer.blade, 0F, 0F);
        super.renderCookware(mixer);
    }

    @Override
    public void renderFluid(int i, World world, ResourceLocation fluid) {
        renderFluidCube(fluid, 0.5F, 0.375F + (i * 0.001F), 0.5F, 0.3249F);
    }

    @Override
    public void translateIngredient(boolean isBlock, float position, float rotation, float offset1, float offset2) {
        if (!isBlock) {
            if (offset2 != 0F) { //Rendering the blade
                GlStateManager.enableLighting();
                GlStateManager.translate(0.5F, 0.45F, 0.5F);
                GlStateManager.scale(0.175F, 0.175F, 0.175F);
                GlStateManager.rotate(rotation - 90F, 0F, 1F, 0F);
            } else {
                GlStateManager.translate(0.5F, 0.4F, 0.5F);
                GlStateManager.scale(0.25F, 0.2F, 0.25F);
                GlStateManager.rotate(-90, 1F, 0F, 0F);
            }

            GlStateManager.rotate(rotation, 0F, 0F, 1F);
            GlStateManager.translate(offset1, offset2, position);
        } else {
            GlStateManager.rotate(90, 1F, 0F, 0F);
            GlStateManager.translate(offset1 * 1.4F, 0.8F - offset2 * 2.5F, position - 1F);
        }
    }

    @Override
    public void translateResult(TileMixer mixer, boolean isBlock) {
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
        GlStateManager.translate(1.25F, 1.1F, 1.25F);
        if (mixer.getFacing() == EnumFacing.EAST || mixer.getFacing() == EnumFacing.WEST) {
            GlStateManager.rotate(-90, 0F, 1F, 0F);
        }

        if (!isBlock) {
            //GlStateManager.rotate(90, 1F, 0F, 0F);
        } else {
            GlStateManager.rotate(90, 0F, 1F, 0F);
        }
    }
}