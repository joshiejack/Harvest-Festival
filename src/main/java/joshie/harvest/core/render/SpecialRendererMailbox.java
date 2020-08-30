package joshie.harvest.core.render;

import joshie.harvest.core.base.render.TileSpecialRendererItem;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.tile.TileMailbox;
import joshie.harvest.knowledge.letter.LetterHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.item.ItemNPCTool.NPCTool;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class SpecialRendererMailbox extends TileSpecialRendererItem<TileMailbox> {
    private static final ItemStack SPEECH = HFNPCs.TOOLS.getStackFromEnum(NPCTool.SPEECH);
    private static final ItemStack MAIL = HFNPCs.TOOLS.getStackFromEnum(NPCTool.MAIL);

    @Override
    public void render(@Nonnull TileMailbox tile, double x, double y, double z, float tick, int destroyStage, float alpha) {
        if (LetterHelper.hasUnreadLetters(MCClientHelper.getPlayer())) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            EnumFacing facing = tile.getFacing();
            if (facing == EnumFacing.NORTH) {
                renderItem(SPEECH, 0F, 0.5F, 1F, 0F);
                renderItem(MAIL, -0.1F, 0.5F, 1F, 0.025F);
            } else if (facing == EnumFacing.SOUTH) {
                renderItem(SPEECH, 0F, 0.5F, 0F, 0F);
                renderItem(MAIL, -0.1F, 0.5F, 0F, 0.025F);
            } else if (facing == EnumFacing.WEST) {
                renderItem(SPEECH, 0F, 1F, 0.5F, 0F);
                renderItem(MAIL, -0.1F, 1F, 0.5F, 0.025F);
            } else if (facing == EnumFacing.EAST) {
                renderItem(SPEECH, 0F, 0F, 0.5F, 0F);
                renderItem(MAIL, -0.1F, 0F, 0.5F, 0.025F);
            }

            GlStateManager.popMatrix();
        }
    }

    @Override
    protected void translateItem(boolean isBlock, float position, float positionX, float positionZ, float offset2) {
        GlStateManager.translate(positionX, 1.4F, positionZ);
        if (position != -0.1F) {
            position = 0F;
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
        } else {
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
        }

        if (!isBlock) {
            GlStateManager.rotate(-MCClientHelper.getPlayer().rotationYaw, 0F, 1F, 0F);
            GlStateManager.translate(0F, offset2 * 3.5F, position * 0.75F);
        } else {
            GlStateManager.rotate(-MCClientHelper.getPlayer().rotationYaw, 0F, 1F, 0F);
            GlStateManager.translate(0F, 2F, position - 1F);
        }
    }
}
