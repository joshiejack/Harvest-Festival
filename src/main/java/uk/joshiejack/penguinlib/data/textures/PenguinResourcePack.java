package uk.joshiejack.penguinlib.data.textures;

import com.google.common.collect.ImmutableSet;
import uk.joshiejack.penguinlib.PenguinLib;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class PenguinResourcePack implements IResourcePack {
    private static final Set<String> DOMAINS = ImmutableSet.of(PenguinLib.MOD_ID);

    private File getFileFromResourceLocation(ResourceLocation resource) {
        File textures = new File(PenguinLib.getDirectory(), "assets");
        if (!textures.exists()) textures.mkdir();
        return new File(textures, resource.getPath());
    }

    @Nonnull
    @Override
    public InputStream getInputStream(@Nonnull ResourceLocation location) throws IOException {
        return FileUtils.openInputStream(getFileFromResourceLocation(location));
    }

    @Override
    public boolean resourceExists(@Nonnull ResourceLocation location) {
        return getFileFromResourceLocation(location).exists();
    }

    @Nonnull
    @Override
    public Set<String> getResourceDomains() {
        return DOMAINS;
    }

    @Nullable
    @Override
    public <T extends IMetadataSection> T getPackMetadata(@Nonnull MetadataSerializer metadataSerializer, @Nonnull String metadataSectionName) {
        return null;
    }

    @Override
    public BufferedImage getPackImage() {
        return null;
    }

    @Override
    public String getPackName() {
        return PenguinLib.MOD_ID;
    }
}
