package joshie.harvest.mining.render;

import joshie.harvest.animals.render.ModelHarvestCow;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderDarkCow extends RenderDarkMob {
    public RenderDarkCow(RenderManager manager) {
        super(manager, new ModelHarvestCow.Adult(), "dark_cow");
    }
}
