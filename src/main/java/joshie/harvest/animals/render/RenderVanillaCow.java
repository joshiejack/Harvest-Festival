package joshie.harvest.animals.render;

import net.minecraft.client.model.ModelCow;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderVanillaCow extends RenderCow {
    public RenderVanillaCow(RenderManager manager) {
        super(manager, new ModelCow(), 0.7F);
    }
}
