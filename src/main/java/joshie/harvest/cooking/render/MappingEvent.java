package joshie.harvest.cooking.render;

import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.cooking.FoodRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class MappingEvent {
    public static final ResourceLocation OIL = new ResourceLocation(MODID, "fluids/oil_cooking");
    public static final ResourceLocation MILK = new ResourceLocation(MODID, "fluids/milk");

    @SubscribeEvent
    public void onMapping(TextureStitchEvent.Pre event) {
        for (ICookingIngredient component: FoodRegistry.getIngredients()) {
            if (component.getFluid() != null) {
                event.getMap().registerSprite(component.getFluid());
            }
        }
    }
}
