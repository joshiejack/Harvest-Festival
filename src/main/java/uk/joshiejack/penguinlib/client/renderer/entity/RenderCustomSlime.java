package uk.joshiejack.penguinlib.client.renderer.entity;

import uk.joshiejack.penguinlib.entity.custom.EntityCustomSlime;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSlime;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCustomSlime extends RenderSlime {
    public RenderCustomSlime(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySlime entity) {
        return ((EntityCustomSlime)entity).data.texture;
    }
}
