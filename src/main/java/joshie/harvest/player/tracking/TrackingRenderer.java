package joshie.harvest.player.tracking;

import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.helpers.StackRenderHelper;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Set;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

@SideOnly(Side.CLIENT)
public class TrackingRenderer {
    private boolean loading;
    private int ticker = 0;
    private int yOffset = 0;
    private final Set<StackSold> sold;

    public TrackingRenderer(Set<StackSold> sold) {
        this.sold = sold;
        this.loading = true;
    }

    private void renderAt(Minecraft mc, StackSold stack, int x, int y) {
        StackRenderHelper.drawStack(stack.getStack(), x + 4, y - 24, 1.25F);
        mc.getTextureManager().bindTexture(HFModInfo.ELEMENTS);
        mc.ingameGUI.drawTexturedModalRect(x + 30, y - 16, 244, 0, 12, 12);
        String text = NumberFormat.getNumberInstance(Locale.ENGLISH).format(stack.getSellValue());
        mc.fontRendererObj.drawStringWithShadow(text, x + 44, y - 13, 0xFFFFFFFF);
    }

    private boolean hasFinishedOrUpdateTickerUp() {
        ticker++;
        if (ticker >= 2) {
            if (yOffset + 1 >= sold.size() * 20) {
                if (ticker >= 500) {
                    ticker = 0; //Reset
                    return true;
                }
            } else {
                ticker = 0;
                yOffset++;
            }
        }

        return false;
    }

    private void moveItemsDown() {
        ticker++;
        if (ticker >= 10) {
            if (yOffset <= 0) {
                MinecraftForge.EVENT_BUS.unregister(this);
            } else {
                ticker = 0;
                yOffset -= 2;
            }
        }
    }

    @SubscribeEvent
    public void onGuiRender(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.HOTBAR) {
            int maxHeight = event.getResolution().getScaledHeight();
            Minecraft mc = MCClientHelper.getMinecraft();
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1F, 1F, 1F, 0.1F);
            int y = 0;
            int currentY = maxHeight + (20 * sold.size()) - yOffset;
            for (StackSold stack: sold) {
                renderAt(mc, stack, 0, currentY - y);
                y += 20; //Increase the y
            }

            GlStateManager.popMatrix();
            if (loading && hasFinishedOrUpdateTickerUp()) loading = false;
            else if (!loading) moveItemsDown();
        }
    }
}
