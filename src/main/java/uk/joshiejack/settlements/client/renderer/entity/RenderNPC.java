package uk.joshiejack.settlements.client.renderer.entity;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.entity.EntityNPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@SideOnly(Side.CLIENT)
public class RenderNPC extends RenderLiving<EntityNPC> {
    public static final ResourceLocation MISSING = new ResourceLocation(Settlements.MODID, "textures/entity/missing.png");
    private static final Cache<ResourceLocation, Boolean> HAS_SKIN = CacheBuilder.newBuilder().build();
    private static final TextureManager manager = Minecraft.getMinecraft().getTextureManager();
    public static final ModelPlayer STEVE = new ModelPlayer(0.0F, false);
    public static final ModelPlayer ALEX = new ModelPlayer(0.0F, true);

    public RenderNPC(RenderManager renderManager) {
        super(renderManager, STEVE, 0.5F);
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean textureExists(ResourceLocation resource) {
        try {
            return HAS_SKIN.get(resource, () -> {
                ITextureObject itextureobject = Minecraft.getMinecraft().getTextureManager().getTexture(resource);
                if (itextureobject == null) {
                    itextureobject = new SimpleTexture(resource);
                }

                return manager.loadTexture(resource, itextureobject) && itextureobject != TextureUtil.MISSING_TEXTURE;
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ResourceLocation getSkinFromUsernameOrUUID(@Nullable UUID uuid, @Nullable String playerSkin) {
        GameProfile profile = TileEntitySkull.updateGameProfile(new GameProfile(uuid, playerSkin));
        Minecraft minecraft = Minecraft.getMinecraft();
        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(profile);
        if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
            return minecraft.getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
        } else return DefaultPlayerSkin.getDefaultSkin(EntityPlayer.getUUID(profile));
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityNPC npc) {
        return textureExists(npc.getInfo().getSkin()) ? npc.getInfo().getSkin() : MISSING;
    }

    @Override
    protected void renderLivingAt(@Nonnull EntityNPC npc, double x, double y, double z) {
        if (npc.getAnimation() == null || (npc.isEntityAlive() && !npc.getAnimation().renderLiving(npc, x, y, z))) {
            super.renderLivingAt(npc, x, y, z);
        }
    }

    @Override
    protected void applyRotations(EntityNPC npc, float ageInTicks, float rotationYaw, float partialTicks) {
        if (npc.getAnimation() == null || (npc.isEntityAlive() && !npc.getAnimation().applyRotation(npc))) {
            super.applyRotations(npc, ageInTicks, rotationYaw, partialTicks);
        }
    }

    @Override
    @Nonnull
    public ModelPlayer getMainModel() {
        return (ModelPlayer) super.getMainModel();
    }

    @Override
    protected void preRenderCallback(EntityNPC npc, float partialTickTime) {
        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
        float f1 = npc.getInfo().getNPCClass().getHeight();
        float f3 = 1.0F;
        GlStateManager.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    @Override
    protected boolean canRenderName(@Nonnull EntityNPC npc) {
        return false;
    }

    //Renders the Entity
    @Override
    public void doRender(@Nonnull EntityNPC npc, double x, double y, double z, float entityYaw, float partialTicks) {
        updateModel(npc);
        GlStateManager.pushMatrix();
        if (npc.isSneaking()) {
            y -= npc.getYOffset();
        }

        super.doRender(npc, x, y, z, entityYaw, partialTicks);
        GlStateManager.popMatrix();
    }

    private void updateModel(EntityNPC npc) {
        mainModel = npc.getInfo().getNPCClass().hasSmallArms() ? ALEX : STEVE;
    }
}
