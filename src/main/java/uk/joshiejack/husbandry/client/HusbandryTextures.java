package uk.joshiejack.husbandry.client;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static uk.joshiejack.husbandry.Husbandry.MODID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID, value = Side.CLIENT)
public class HusbandryTextures {
    public static final ResourceLocation FODDER = new ResourceLocation(MODID, "blocks/fodder");
    public static final ResourceLocation SLOP = new ResourceLocation(MODID, "blocks/slop");

    @SubscribeEvent
    public static void onMapping(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(FODDER);
        event.getMap().registerSprite(SLOP);
    }
}
