package uk.joshiejack.settlements.client.renderer;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.harvestcore.HarvestCore;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID, value = Side.CLIENT)
public class TextureStitcher {
    public static TextureAtlasSprite EXCLAMATION_MARK;

    @SubscribeEvent
    public static void TextureStitchEvent(TextureStitchEvent event) {
        EXCLAMATION_MARK = event.getMap().registerSprite(new ResourceLocation(Settlements.MODID, "blocks/sign_exclamation"));
    }
}
