package uk.joshiejack.penguinlib.client.renderer.entity;

import uk.joshiejack.penguinlib.entity.custom.EntityCustomChicken;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCustomChicken extends RenderChicken {
    public RenderCustomChicken(RenderManager rm) {
        super(rm);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityChicken entity) {
        return ((EntityCustomChicken)entity).data.texture;
    }
}
