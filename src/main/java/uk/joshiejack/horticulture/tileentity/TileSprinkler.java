package uk.joshiejack.horticulture.tileentity;

import uk.joshiejack.horticulture.item.HorticultureItems;
import uk.joshiejack.penguinlib.util.helpers.minecraft.FakePlayerHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public abstract class TileSprinkler extends TileEntity implements ITickable {
    private final double height;
    private final int range;

    @SuppressWarnings("WeakerAccess")
    public TileSprinkler(double height, int range) {
        this.height = height;
        this.range = range;
    }

    @Override
    public void update() {
        if (world.getWorldTime() % 15 == 1 && TimeHelper.isBetween(world, 5999, 6250)) {
            if (world.isRemote) {
                int setting = (2 - Minecraft.getMinecraft().gameSettings.particleSetting);
                for (int i = 0; i < setting * 32; i++) {
                    double one = getRandomDouble();
                    double two = getRandomDouble();
                    world.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + height, getPos().getZ() + 0.5D, one, 0D, two);
                    world.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + height, getPos().getZ() + 0.5D, one - 0.05D, 0D, two - 0.05D);
                    world.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + height, getPos().getZ() + 0.5D, one - 0.05D, 0D, two + 0.05D);
                    world.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + height, getPos().getZ() + 0.5D, one + 0.05D, 0D, two - 0.05D);
                    world.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + height, getPos().getZ() + 0.5D, one + 0.05D, 0D, two + 0.05D);
                }
            } else {
                for (int x = -range; x <= range; x++) {
                    for (int z = -range; z <= range; z++) {
                        for (int y = 0; y >= -1; y--) {
                            BlockPos position = new BlockPos(getPos().getX() + x, getPos().getY() + y, getPos().getZ() + z);
                            if (!position.equals(getPos())) {
                                HorticultureItems.WATERING_CAN.water(FakePlayerHelper.getFakePlayerWithPosition((WorldServer)world, position), world, position, ItemStack.EMPTY, EnumHand.MAIN_HAND);
                            }
                        }
                    }
                }
            }
        }
    }

    protected abstract double getRandomDouble();
}
