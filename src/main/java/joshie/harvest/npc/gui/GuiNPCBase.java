package joshie.harvest.npc.gui;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.player.RelationshipType;
import joshie.harvest.core.base.gui.GuiBase;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.item.ItemNPCTool.NPCTool;
import joshie.harvest.npc.packet.PacketGift;
import joshie.harvest.npc.packet.PacketInfo;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiNPCBase extends GuiBase {
    private static final ItemStack GIFT = HFNPCs.TOOLS.getStackFromEnum(NPCTool.GIFT);
    private static final ResourceLocation chatbox = new ResourceLocation(HFModInfo.MODID, "textures/gui/chatbox.png");
    protected final EntityNPC npc;
    protected final EntityPlayer player;
    protected final int nextGui;
    private final int inside;
    private final int outside;
    protected int npcMouseX;
    protected int npcMouseY;

    public GuiNPCBase(EntityPlayer ePlayer, EntityNPC eNpc, EnumHand hand, int next) {
        super(new ContainerNPCChat(ePlayer, eNpc, hand, next), "chat", 0);

        hasInventory = false;
        npc = eNpc;
        player = ePlayer;
        xSize = 256;
        ySize = 256;
        nextGui = next;
        inside = npc.getNPC().getInsideColor();
        outside = npc.getNPC().getOutsideColor();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public FontRenderer getFont() {
        return fontRendererObj;
    }

    @Override
    public void drawBackground(int x, int y) {
        GlStateManager.pushMatrix();
        mc.renderEngine.bindTexture(chatbox);
        drawTexturedModalRect(x, y + 150, 0, 150, 256, 51);
        GlStateManager.enableBlend();
        ChatFontRenderer.colorise(inside);
        drawTexturedModalRect(x, y + 150, 0, 100, 256, 51);
        ChatFontRenderer.colorise(outside);
        drawTexturedModalRect(x, y + 150, 0, 50, 256, 51);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.disableBlend();
        ChatFontRenderer.render(this, x, y, npc.getName(), inside, outside);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();

        mc.renderEngine.bindTexture(chatbox);
        if (isHoldingItem() && npc.getNPC() != HFNPCs.GODDESS) {
            //Drawing the icons
            //Render the outside of the gift tab
            if (!isPointInRegion(242, 156, 17, 19, npcMouseX, npcMouseY)) {
                ChatFontRenderer.colorise(inside);
            }

            drawTexturedModalRect(x + 241, y + 155, 218, 0, 19, 20); //Inside
            ChatFontRenderer.colorise(outside);
            drawTexturedModalRect(x + 241, y + 155, 237, 0, 19, 20); //Outside
            GlStateManager.color(1F, 1F, 1F);
            joshie.harvest.core.helpers.RenderHelper.drawStack(GIFT, x + 242, y + 157, 1F);
        }

        //Info section
        if (displayInfo()) {
            mc.renderEngine.bindTexture(chatbox);
            if (!isPointInRegion(242, 177, 17, 19, npcMouseX, npcMouseY)) {
                ChatFontRenderer.colorise(inside);
            }

            drawTexturedModalRect(x + 241, y + 176, 218, 0, 19, 20); //Inside
            ChatFontRenderer.colorise(outside);
            drawTexturedModalRect(x + 241, y + 176, 237, 0, 19, 20); //Outside
            GlStateManager.color(1F, 1F, 1F);
            joshie.harvest.core.helpers.RenderHelper.drawStack(npc.getNPC().hasInfo(), x + 242, y + 178, 1F);
        }
    }

    private void drawHeart(int value) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.disableLighting();
        int xPos = (int) (((double) value / RelationshipType.NPC.getMaximumRP()) * 7);
        drawTexturedModalRect(240, 130, 0, 0, 25, 25);
        drawTexturedModalRect(240, 130, 25 + (25 * xPos), 0, 25, 25);
    }

    @Override
    public void drawForeground(int x, int y) {
        boolean originalFlag = fontRendererObj.getUnicodeFlag();
        fontRendererObj.setUnicodeFlag(true);
        mc.renderEngine.bindTexture(HFModInfo.ELEMENTS);
        if (npc.getNPC().isMarriageCandidate()) {
            drawHeart(HFApi.player.getRelationsForPlayer(player).getRelationship(npc.getNPC().getUUID()));
        }

        drawOverlay(x, y);
        fontRendererObj.setUnicodeFlag(originalFlag);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        npcMouseX = mouseX;
        npcMouseY = mouseY;

        if (npc.getNPC() != HFNPCs.GODDESS && isHoldingItem() && hoveringGift())
            renderToolTip(GIFT, mouseX, mouseY);
        if (displayInfo() && hoveringInfo())
            renderToolTip(npc.getNPC().hasInfo(), mouseX, mouseY);
    }

    //Perform the mouse clicks
    protected void onMouseClick(int mouseX, int mouseY) {
        if (isHoldingItem() && hoveringGift())
            PacketHandler.sendToServer(new PacketGift(npc));
        else if (displayInfo() && hoveringInfo())
            PacketHandler.sendToServer(new PacketInfo(npc));
    }

    boolean hoveringGift() {
        return isPointInRegion(242, 156, 17, 19, npcMouseX, npcMouseY);
    }

    boolean isHoldingItem() {
        return player.getHeldItemMainhand() != null || player.getHeldItemOffhand() != null;
    }

    boolean hoveringInfo() {
        return isPointInRegion(242, 177, 17, 19, npcMouseX, npcMouseY);
    }

    boolean displayInfo() {
        return npc.getNPC().hasInfo() != null && npc.getNPC().canDisplayInfo(player);
    }

    public abstract void drawOverlay(int x, int y);

    @Override
    public void drawDefaultBackground() {
    }

    public String getScript() {
        return "missing chat";
    }

    public void endChat() {
        mc.thePlayer.closeScreen();
    }

    //Tooltip
    @Override
    protected void renderToolTip(ItemStack stack, int x, int y) {
        List<String> textLines = stack.getTooltip(this.mc.thePlayer, false);
        for (int i = 0; i < textLines.size(); ++i) {
            if (i == 0) {
                textLines.set(i, stack.getRarity().rarityColor + textLines.get(i));
            } else {
                textLines.set(i, TextFormatting.GRAY + textLines.get(i));
            }
        }

        net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
        if (!textLines.isEmpty()) {
            RenderTooltipEvent.Pre event = new RenderTooltipEvent.Pre(stack, textLines, x, y, width, height, -1, fontRendererObj);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return;
            }
            mouseX = event.getX();
            mouseY = event.getY();
            int screenWidth = event.getScreenWidth();
            int screenHeight = event.getScreenHeight();
            int maxTextWidth = event.getMaxWidth();
            fontRendererObj = event.getFontRenderer();

            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int tooltipTextWidth = 0;

            for (String textLine : textLines) {
                int textLineWidth = fontRendererObj.getStringWidth(textLine);

                if (textLineWidth > tooltipTextWidth) {
                    tooltipTextWidth = textLineWidth;
                }
            }

            boolean needsWrap = false;

            int titleLinesCount = 1;
            int tooltipX = mouseX + 12;
            if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
                tooltipX = mouseX - 16 - tooltipTextWidth;
                if (tooltipX < 4) // if the tooltip doesn't fit on the screen
                {
                    if (mouseX > screenWidth / 2) {
                        tooltipTextWidth = mouseX - 12 - 8;
                    } else {
                        tooltipTextWidth = screenWidth - 16 - mouseX;
                    }
                    needsWrap = true;
                }
            }

            if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
                tooltipTextWidth = maxTextWidth;
                needsWrap = true;
            }

            if (needsWrap) {
                int wrappedTooltipWidth = 0;
                List<String> wrappedTextLines = new ArrayList<String>();
                for (int i = 0; i < textLines.size(); i++) {
                    String textLine = textLines.get(i);
                    List<String> wrappedLine = fontRendererObj.listFormattedStringToWidth(textLine, tooltipTextWidth);
                    if (i == 0) {
                        titleLinesCount = wrappedLine.size();
                    }

                    for (String line : wrappedLine) {
                        int lineWidth = fontRendererObj.getStringWidth(line);
                        if (lineWidth > wrappedTooltipWidth) {
                            wrappedTooltipWidth = lineWidth;
                        }
                        wrappedTextLines.add(line);
                    }
                }
                tooltipTextWidth = wrappedTooltipWidth;
                textLines = wrappedTextLines;

                if (mouseX > screenWidth / 2) {
                    tooltipX = mouseX - 16 - tooltipTextWidth;
                } else {
                    tooltipX = mouseX + 12;
                }
            }

            int tooltipY = mouseY - 12;
            int tooltipHeight = 8;

            if (textLines.size() > 1) {
                tooltipHeight += (textLines.size() - 1) * 10;
                if (textLines.size() > titleLinesCount) {
                    tooltipHeight += 2; // gap between title lines and next lines
                }
            }

            if (tooltipY + tooltipHeight + 6 > screenHeight) {
                tooltipY = screenHeight - tooltipHeight - 6;
            }

            final int zLevel = 300;
            final int backgroundColor = ( 200 << 24 ) | ( inside & 0x00ffffff );
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(zLevel, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            final int borderColorStart = ( 200 << 24 ) | ( outside & 0x00ffffff );
            final int borderColorEnd = borderColorStart;
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            GuiUtils.drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

            MinecraftForge.EVENT_BUS.post(new RenderTooltipEvent.PostBackground(stack, textLines, tooltipX, tooltipY, fontRendererObj, tooltipTextWidth, tooltipHeight));
            int tooltipTop = tooltipY;

            for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber) {
                String line = textLines.get(lineNumber);
                fontRendererObj.drawStringWithShadow(line, (float) tooltipX, (float) tooltipY, -1);

                if (lineNumber + 1 == titleLinesCount) {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }

            MinecraftForge.EVENT_BUS.post(new RenderTooltipEvent.PostText(stack, textLines, tooltipX, tooltipTop, fontRendererObj, tooltipTextWidth, tooltipHeight));

            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }

        net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
    }
}