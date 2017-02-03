package joshie.harvest.cooking.render;

import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.core.util.annotations.HFEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFEvents(Side.CLIENT)
public class IngredientMappingEvent {
    public static final ResourceLocation OIL = new ResourceLocation(MODID, "fluids/oil_cooking");
    public static final ResourceLocation MILK = new ResourceLocation(MODID, "fluids/milk");
    public static final ResourceLocation WINE = new ResourceLocation(MODID, "fluids/wine");

    @SubscribeEvent
    public void onMapping(TextureStitchEvent.Pre event) {
        for (Ingredient component: Ingredient.INGREDIENTS.values()) {
            if (component.getFluid() != null) {
                event.getMap().registerSprite(component.getFluid());
            }
        }
    }
}
