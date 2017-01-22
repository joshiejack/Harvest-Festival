package joshie.harvest.npcs.render;

import joshie.harvest.api.npc.INPCHelper;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.base.render.FakeEntityRenderer.EntityItemRenderer;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.npcs.entity.EntityNPC;
import joshie.harvest.npcs.entity.EntityNPCVillager;
import joshie.harvest.npcs.render.NPCItemRenderer.NPCTile;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class NPCItemRenderer extends TileEntitySpecialRenderer<NPCTile> {
    private static final ResourceLocation SHADOW = new ResourceLocation(MODID, "textures/entity/shadow.png");
    public static boolean renderShadow;
    private final ModelNPC modelAlex;
    private final ModelNPC modelSteve;
    private EntityNPC fake;

    public NPCItemRenderer() {
        this.modelAlex = new ModelNPC(true);
        this.modelSteve = new ModelNPC(false);
    }

    @Override
    public void renderTileEntityAt(@Nullable NPCTile fake, double x, double y, double z, float partialTicks, int destroyStage) {
        if (fake != null) {
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
            if (renderShadow) bindTexture(SHADOW);
            else bindTexture(fake.npc.getSkin());
            ModelNPC model = fake.npc.isAlexSkin() ? modelAlex : modelSteve;
            model.isChild = fake.npc.getAge() == INPCHelper.Age.CHILD;
            model.render(getNPC(), 0F, 0F, 0F, 0F, 0F, 0.0625F);
            GlStateManager.disableRescaleNormal();
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
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
            id = Math.max(0, Math.min(NPC.REGISTRY.getValues().size() - 1, id));
            this.npc = NPC.REGISTRY.getValues().get(id);
        }
    }
}
