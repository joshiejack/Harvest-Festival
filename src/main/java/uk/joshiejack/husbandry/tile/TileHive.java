package uk.joshiejack.husbandry.tile;

import uk.joshiejack.husbandry.item.HusbandryItems;
import uk.joshiejack.husbandry.item.ItemFood;
import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.tile.machine.TileMachineRegistry;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.FakePlayerHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.util.FakePlayer;

@SuppressWarnings("unused")
@PenguinLoader("hive")
public class TileHive extends TileMachineRegistry {
    public static final HolderRegistry<ItemStack> registry = new HolderRegistry<>(ItemStack.EMPTY);

    public TileHive() {
        super(registry, "day");
    }

    @Override
    public long getOperationalTime() {
        return super.getOperationalTime() * 4; //Four days for the operation
    }

    @Override
    public void checkCanStart() {
        //If it is day, we can start the timer, and see the sky, and it can't snow here and it isn't a desert/nether/end
        if (handler.getStackInSlot(0).isEmpty() && world.isDaytime() && world.canBlockSeeSky(pos) && !world.canSnowAt(pos, false)
                && !BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.DRY) && !world.isRaining()) {
            startMachine(FakePlayerHelper.getFakePlayerWithPosition((WorldServer) world, pos));
        }
    }

    @Override
    public void finishMachine() {
        int radius = 5;
        int radiusSquared = radius * radius;


        BlockPos currentPos = null;
        double currentDistance = Double.MAX_VALUE;
        for (int i = -radius; i <= radius; i++) {
            for (int l = -radius; l <= radius; l++) {
                if (i * i + l * l >= (radius + 0.50f) * (radius + 0.50f)) {
                    continue;
                }

                BlockPos target = pos.add(i, 0, l);
                if (!target.equals(pos)) {
                    IBlockState state = world.getBlockState(target);
                    FakePlayer fake = FakePlayerHelper.getFakePlayerWithPosition((WorldServer) world, target);
                    ItemStack stack = state.getBlock().getPickBlock(state, new RayTraceResult(fake), world, target, fake);
                    if (!registry.getValue(stack).isEmpty()) {
                        double distance = pos.getDistance(target.getX(), target.getY(), target.getZ());
                        if (distance < currentDistance) {
                            currentPos = target;
                            currentDistance = distance;
                        }
                    }
                }
            }
        }

        //We found a flower so add one!
        if (currentPos != null) {
            IBlockState state = world.getBlockState(currentPos);
            FakePlayer fake = FakePlayerHelper.getFakePlayerWithPosition((WorldServer) world, currentPos);
            ItemStack stack = state.getBlock().getPickBlock(state, new RayTraceResult(fake), world, currentPos, fake);
            ItemStack result = registry.getValue(stack).copy();
            if (!result.isEmpty()) {
                result.setStackDisplayName(stack.getDisplayName() + " " + result.getDisplayName());
                assert result.getTagCompound() != null;
                result.getTagCompound().setBoolean("Generated", true);
                handler.setStackInSlot(0, result);
            }
        } else handler.setStackInSlot(0, HusbandryItems.FOOD.getStackFromEnum(ItemFood.Food.HONEY));
    }
}
