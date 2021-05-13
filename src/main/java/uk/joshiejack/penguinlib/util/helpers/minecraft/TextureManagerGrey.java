package uk.joshiejack.penguinlib.util.helpers.minecraft;

import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.client.GuiElements;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class TextureManagerGrey extends TextureManager {
    private static final ResourceLocation TEXTURE = GuiElements.getTexture(PenguinLib.MOD_ID, "shadow_large");

    public TextureManagerGrey(IResourceManager resourceManager) {
        super(resourceManager);
    }

    @Nonnull
    @Override
    public ITextureObject getTexture(@Nonnull ResourceLocation textureLocation) {
        return super.getTexture(TEXTURE);
    }

    @Override
    public void bindTexture(@Nonnull ResourceLocation resource) {
        super.bindTexture(TEXTURE);
    }

    @Override
    public boolean loadTexture(@Nonnull ResourceLocation textureLocation, @Nonnull ITextureObject textureObj) {
        return super.loadTexture(TEXTURE, new SimpleTexture(TEXTURE)); //Force it to be a simple texture
    }
}
