package uk.joshiejack.settlements.client.renderer.item;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import uk.joshiejack.settlements.client.renderer.entity.RenderNPC;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.item.AdventureItems;
import uk.joshiejack.settlements.data.database.NPCLoader;
import uk.joshiejack.settlements.npcs.Age;
import uk.joshiejack.settlements.npcs.NPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import java.util.concurrent.ExecutionException;

import static uk.joshiejack.settlements.Settlements.MODID;

@SideOnly(Side.CLIENT)
public class NPCSpawnerRenderer extends TileEntityItemStackRenderer {
    private static final ResourceLocation SHADOW = new ResourceLocation(MODID, "textures/entity/shadow.png");
    private final Cache<ItemStack, ResourceLocation> skins = CacheBuilder.newBuilder().build();
    public static boolean renderShadow;
    private static EntityNPC fakeEntityNPC;

    private EntityNPC getFakeEntityNPC() {
        if (fakeEntityNPC == null) {
            fakeEntityNPC = new EntityNPC(Minecraft.getMinecraft().world);
        }

        return fakeEntityNPC;
    }

    @Override
    public void renderByItem(@Nonnull ItemStack stack) {
        NPC npc = AdventureItems.NPC_SPAWNER.getObjectFromStack(stack);
        NPCLoader.NPCClass clazz = npc.getNPCClass();
        if (clazz != null) {
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

            ResourceLocation skin = null;
            try {
                skin = skins.get(stack, () -> AdventureItems.NPC_SPAWNER.getSkinFromStack(npc, stack));
            } catch (ExecutionException ignored) {}

            if (skin == null || renderShadow) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(SHADOW);
                GlStateManager.disableLighting();
            } else Minecraft.getMinecraft().getTextureManager().bindTexture(skin);

            ModelPlayer model = clazz.hasSmallArms() ? RenderNPC.ALEX : RenderNPC.STEVE;
            model.isChild = clazz.getAge() == Age.CHILD;
            model.render(getFakeEntityNPC(), 0F, 0F, 0F, 0F, 0F, 0.0625F);
            if (renderShadow) GlStateManager.enableLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
    }
}
