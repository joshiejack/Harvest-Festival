package joshie.harvest.npc.render;

import joshie.harvest.api.npc.INPCRegistry;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.entity.AbstractEntityNPC;
import joshie.harvest.npc.render.FakeNPCRenderer.FakeNPCTile;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class FakeNPCRenderer extends TileEntitySpecialRenderer<FakeNPCTile> {
    private ModelNPC model;
    private AbstractEntityNPC entity;
    private NPC npc;

    public FakeNPCRenderer(NPC npc) {
        this.model = new ModelNPC(npc.isAlexSkin());
        this.model.isChild = npc.getAge() == INPCRegistry.Age.CHILD;
        this.npc = npc;
    }

    @Override
    public void renderTileEntityAt(FakeNPCTile fake, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5F, -0.05F, 0.5F);
        GlStateManager.scale(-0.75F, 0.75F, 0.75F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180F, 1.0F, 0.0F, 0.0F);
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.translate(0.0F, -1.501F, 0.0F);
        bindTexture(getNPC().getSkin());
        model.isChild = npc.getAge() == INPCRegistry.Age.CHILD;
        model.render(getNPC(), 0F, 0F, 0F, 0F, 0F, 0.0625F);
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

    private AbstractEntityNPC getNPC() {
        if (entity == null) {
            entity = NPCHelper.getEntityForNPC(MCClientHelper.getWorld(), npc);
        }

        return entity;
    }

    public abstract static class FakeNPCTile extends TileEntity {}
}
