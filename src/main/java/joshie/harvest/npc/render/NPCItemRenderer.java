package joshie.harvest.npc.render;

import joshie.harvest.api.npc.INPCRegistry;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.render.FakeEntityRenderer.EntityItemRenderer;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.npc.entity.EntityNPCVillager;
import joshie.harvest.npc.render.NPCItemRenderer.NPCTile;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class NPCItemRenderer extends TileEntitySpecialRenderer<NPCTile> {
    private ModelNPC modelAlex;
    private ModelNPC modelSteve;
    private EntityNPC fake;
    private ModelNPC model;

    public NPCItemRenderer() {
        this.modelAlex = new ModelNPC(true);
        this.modelSteve = new ModelNPC(false);
    }

    @Override
    public void renderTileEntityAt(NPCTile fake, double x, double y, double z, float partialTicks, int destroyStage) {
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
        bindTexture(fake.npc.getSkin());
        model = fake.npc.isAlexSkin() ? modelAlex : modelSteve;
        model.isChild = fake.npc.getAge() == INPCRegistry.Age.CHILD;
        model.render(getNPC(), 0F, 0F, 0F, 0F, 0F, 0.0625F);
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

    private EntityNPC getNPC() {
        if (fake == null) fake = new EntityNPCVillager(MCClientHelper.getWorld());
        return fake;
    }

    public static class NPCTile extends EntityItemRenderer {
        public static final NPCTile INSTANCE = new NPCTile();
        public NPC npc;

        @Override
        public void setID(int id) {
            this.npc = NPCRegistry.REGISTRY.getObjectById(id);
        }
    }
}
