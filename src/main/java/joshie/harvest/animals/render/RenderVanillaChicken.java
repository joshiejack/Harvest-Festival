package joshie.harvest.animals.render;

import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderVanillaChicken extends RenderChicken {
    public RenderVanillaChicken(RenderManager manager) {
        super(manager, new ModelChicken(), 0.3F);
    }
}
