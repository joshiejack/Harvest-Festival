package uk.joshiejack.penguinlib.client.util;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.WeightedRandom;

public class WeightedFloorOverlay extends WeightedRandom.Item {
    public final TextureAtlasSprite sprite;

    public WeightedFloorOverlay(TextureAtlasSprite sprite, int weight) {
        super(weight);
        this.sprite = sprite;
    }
}
