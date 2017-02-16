package joshie.harvest.npcs.gui;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.player.RelationshipType;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.core.base.gui.GuiBase;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.entity.EntityNPC;
import joshie.harvest.npcs.packet.PacketGift;
import joshie.harvest.npcs.packet.PacketInfo;
import joshie.harvest.quests.QuestHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.config.GuiUtils;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GuiNPCBase extends GuiBase {
    private static final ResourceLocation chatbox = new ResourceLocation(HFModInfo.MODID, "textures/gui/chatbox.png");
    protected final EntityNPC npc;
    protected final EntityPlayer player;
    protected final Quest quest;
    protected final int nextGui;
    protected int inside;
    protected int outside;
    protected int npcMouseX;
    protected int npcMouseY;
    protected BlockPos pos;

    public GuiNPCBase(EntityPlayer ePlayer, EntityNPC eNpc, int next) {
        super(new ContainerNPCChat(ePlayer, eNpc, next), "chat", 0);
        quest = QuestHelper.getCurrentQuest(ePlayer, eNpc);
        nextGui = next;
        hasInventory = false;
        npc = eNpc;
        player = ePlayer;
        xSize = 256;
        ySize = 256;
        inside = npc.getNPC().getInsideColor();
        outside = npc.getNPC().getOutsideColor();
        pos = new BlockPos(eNpc);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
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
        drawName(x, y);
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();
        drawTabs(x, y);
    }

    protected void drawName(int x, int y) {
        ChatFontRenderer.render(this, x, y, npc.getName(), inside, outside);
    }

    protected void drawTabs(int x, int y) {
        if (npc.getNPC() == HFNPCs.GODDESS || isHoldingItem()) {
            mc.renderEngine.bindTexture(chatbox);
            //Drawing the icons
            //Render the outside of the gift tab
            if (!isPointInRegion(242, 156, 17, 19, npcMouseX, npcMouseY)) {
                ChatFontRenderer.colorise(inside);
            }

            drawTexturedModalRect(x + 241, y + 155, 218, 0, 19, 20); //Inside
            ChatFontRenderer.colorise(outside);
            drawTexturedModalRect(x + 241, y + 155, 237, 0, 19, 20); //Outside
            GlStateManager.color(1F, 1F, 1F);
            mc.renderEngine.bindTexture(HFModInfo.ICONS);
            int textureX = npc.getNPC() == HFNPCs.GODDESS ? 64 : 0;
            drawTexturedModalRect(x + 242, y + 157, textureX, 0, 16, 16);
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
            npc.getNPC().drawInfo(this, x + 242, y + 178);
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
            drawHeart(HFApi.player.getRelationsForPlayer(player).getRelationship(npc.getNPC()));
        }

        GlStateManager.color(1F, 1F, 1F);
        RenderHelper.enableGUIStandardItemLighting();
        drawOverlay(x, y);
        fontRendererObj.setUnicodeFlag(originalFlag);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        npcMouseX = mouseX;
        npcMouseY = mouseY;

        if (isChat()) {
            if ((npc.getNPC() == HFNPCs.GODDESS || isHoldingItem()) && hoveringGift()) {
                String translate = npc.getNPC() == HFNPCs.GODDESS ? "npc.tooltip.book" : "npc.tooltip.gift";
                drawTooltip(Collections.singletonList(TextHelper.translate(translate)), mouseX, mouseY);
            } if (displayInfo() && hoveringInfo()) {
                String translate = npc.getNPC().getInfoTooltip();
                drawTooltip(Collections.singletonList(TextHelper.localize(translate)), mouseX, mouseY);
            }
        }
    }

    protected boolean isChat() {
        return true;
    }

    //Perform the mouse clicks
    @Override
    protected void mouseClicked(int x, int y, int mouseButton) throws IOException {
        super.mouseClicked(x, y, mouseButton);
        if (isChat()) {
            if ((npc.getNPC() == HFNPCs.GODDESS || isHoldingItem()) && hoveringGift()) {
                PacketGift.handleGifting(player, npc);
                PacketHandler.sendToServer(new PacketGift(npc));
            } else if (displayInfo() && hoveringInfo())
                PacketHandler.sendToServer(new PacketInfo(npc));
        }
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
        return npc.getNPC().getInfoButton() != null && npc.getNPC().getInfoButton().canDisplay(npc.getNPC(), player)
                && (npc.getNPC().getShop(player.worldObj, pos, player) == null || !NPCHelper.isShopOpen(player.worldObj, npc, player, npc.getNPC().getShop(player.worldObj, pos, player)));
    }

    public abstract void drawOverlay(int x, int y);

    @Override
    public void drawDefaultBackground() {}

    public String getScript() {
        return "missing chat";
    }

    public void endChat() {
        mc.thePlayer.closeScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
            endChat();
        }
    }

    //Tooltip
    @Override
    protected void renderToolTip(@Nonnull ItemStack stack, int x, int y) {
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
                List<String> wrappedTextLines = new ArrayList<>();
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
            final int borderColor = ( 200 << 24 ) | ( outside & 0x00ffffff );
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColor, borderColor);
            GuiUtils.drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColor, borderColor);
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColor, borderColor);
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColor, borderColor);

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