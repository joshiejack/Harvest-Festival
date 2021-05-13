package uk.joshiejack.gastronomy.tile;

import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.gastronomy.client.renderer.tile.SpecialRenderDataCooking;
import uk.joshiejack.gastronomy.GastronomySounds;
import uk.joshiejack.gastronomy.tile.base.TileCookingTicking;
import uk.joshiejack.penguinlib.client.renderer.tile.SpecialRenderData;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

@PenguinLoader("mixer")
public class TileMixer extends TileCookingTicking {
    private static final float MAX_OFFSET1 = 0.5F;
    private static final float MIN_OFFSET1 = -0.5F;
    private static final float MAX_OFFSET2 = 0F / 1.75F;
    private static final float MIN_OFFSET2 = 1F / 1.75F;

    public float blade = 0F;
    public TileMixer() {
        super(Appliance.MIXER);
    }

    @Override
    public SpecialRenderData createRenderData() {
        return new SpecialRenderDataCooking() {
            @Override
            public void rotate(World world) {
                for (int k = 0; k < rotations.length; k++) {
                    rotations[k] = rotations[k] + 10F;
                }

                for (int k = 0; k < offset1.length; k++) {
                    if (world.rand.nextFloat() < 0.1F) {
                        offset1[k] = clampOffset1(offset1[k] + (world.rand.nextBoolean() ? 0.05F : -0.05F));
                        offset2[k] = clampOffset2(offset2[k] + (world.rand.nextBoolean() ? 0.05F : -0.05F));
                    }
                }
            }
        };
    }

    @Override
    public void animate() {
        super.animate();
        blade += 100F;
        //Play the sound effect
        if (cookTimer == 1) {
            world.playSound(pos.getX(), pos.getY() + 0.5D, pos.getZ(), GastronomySounds.MIXER, SoundCategory.BLOCKS, 1.5F, world.rand.nextFloat() * 0.1F + 0.9F, false);
        }
    }

    private float clampOffset1(float f) {
        return Math.max(MIN_OFFSET1, Math.min(MAX_OFFSET1, f));
    }

    private float clampOffset2(float f) {
        return Math.max(MIN_OFFSET2, Math.min(MAX_OFFSET2, f));
    }
}
