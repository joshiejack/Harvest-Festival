package joshie.harvest.core.helpers;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class ModelHelper {
    public static ModelResourceLocation getModelForItem(String string) {
        return new ModelResourceLocation(new ResourceLocation(MODID, string), "inventory");
    }
}
