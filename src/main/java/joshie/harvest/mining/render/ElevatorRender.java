package joshie.harvest.mining.render;

import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.mining.HFMining;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@HFEvents(Side.CLIENT)
public class ElevatorRender {
    public static boolean ELEVATOR;
    private static int fogStart = 0;
    private static int fogTarget = 0;

    @SubscribeEvent
    public void onFogRender(RenderFogEvent event) {
        if (ELEVATOR && event.getEntity().worldObj.provider.getDimension() == HFMining.MINING_ID) {
            if (fogTarget != fogStart) {
                if (fogTarget > fogStart) {
                    fogStart += 10;
                } else if (fogTarget < fogStart) {
                    fogStart -= 10;
                }
            }

            fogTarget = -1000000;
            GlStateManager.setFogEnd(Math.min(event.getFarPlaneDistance(), 150F) * 0.5F);
            GlStateManager.setFogStart((float) fogStart / 10F);
        } else {
            fogStart = 100;
            fogTarget = 100;
        }
    }

    @SubscribeEvent
    public void onFogColor(FogColors event) {
        if (ELEVATOR && event.getEntity().worldObj.provider.getDimension() == HFMining.MINING_ID) {
            event.setRed(0F);
            event.setBlue(0F);
            event.setGreen(0F);
        }
    }
}
