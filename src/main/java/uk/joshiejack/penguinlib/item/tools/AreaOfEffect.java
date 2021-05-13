package uk.joshiejack.penguinlib.item.tools;

import com.google.common.collect.ImmutableList;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TerrainHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;

public class AreaOfEffect {
    public static final AreaOfEffect SINGULAR = new AreaOfEffect(0, 0, 0).setSingular(); //NEIN
    public final int widthAndHeight;
    public final int depth;
    public boolean singular;
    private final int heightStart;

    public AreaOfEffect(int widthAndHeight, int heightStart, int depth) {
        this.widthAndHeight = widthAndHeight;
        this.heightStart = heightStart;
        this.depth = depth;
    }

    private AreaOfEffect setSingular() {
        this.singular = true;
        return this;
    }

    @SuppressWarnings("unchecked")
    public boolean onBlockDestroyed(@Nonnull ItemStack stack, World world, BlockPos position, EntityPlayer player) {
        if (stack.getItemDamage() < stack.getMaxDamage()) {
            NonNullList<ItemStack> drops = NonNullList.create();
            for (BlockPos pos : getPositions(player, world, position)) {
                if (stack.getMaxDamage() < stack.getMaxDamage()) {
                    stack.damageItem(1, player);
                }

                int count = drops.size();
                NonNullList<ItemStack> drops_sub = NonNullList.create();
                TerrainHelper.collectDrops(world, pos, world.getBlockState(pos), player, drops_sub, EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0);
                if (drops_sub.size() > 0 && world.getBlockState(pos).getBlockHardness(world, pos) != -1F && player instanceof EntityPlayerMP) {
                    int exp = ForgeHooks.onBlockBreakEvent(world, (((EntityPlayerMP)player).interactionManager.getGameType()), (((EntityPlayerMP)player)), pos);
                    if (exp != -1) {
                        world.setBlockToAir(pos);
                    }
                }

                drops.addAll(drops_sub);
            }

            drops.forEach(item -> Block.spawnAsEntity(world, position, item));
        }

        return true;
    }

    private boolean isEffectiveAgainst(ItemStack stack, IBlockState state) {
        return stack.getItem().getDestroySpeed(stack, state) != 1F;
    }

    @SuppressWarnings("ConstantConditions")
    public ImmutableList<BlockPos> getPositions(EntityPlayer player, World world, BlockPos pos) {
        if (singular || player.isSneaking() || !isEffectiveAgainst(player.getHeldItemMainhand(), world.getBlockState(pos))) {
            return ImmutableList.of();
        } else {
            ImmutableList.Builder<BlockPos> builder = ImmutableList.builder();
            RayTraceResult rt = rayTrace(world, player, true);
            if (rt == null || !pos.equals(rt.getBlockPos())) {
                rt = rayTrace(world, player, false);
                if (rt == null || !pos.equals(rt.getBlockPos())) {
                    return ImmutableList.of();
                }
            }

            ItemStack tool = player.getHeldItemMainhand();
            //Grab the side hit from the ray trace
            EnumFacing front = rt.sideHit;
            EnumFacing playerFacing = EntityHelper.getFacingFromEntity(player);
            for (int w = -widthAndHeight; w <= widthAndHeight; w++) {
                for (int d = 0; d <= depth; d++) {
                    for (int h = heightStart; h <= widthAndHeight; h++) {
                        BlockPos target = front == EnumFacing.EAST || front == EnumFacing.WEST ?
                                pos.add(((front == EnumFacing.WEST) ? d : -d), h, w) :
                                pos.add(w, h, ((front == EnumFacing.NORTH) ? d : -d));
                        if (front == EnumFacing.DOWN || front == EnumFacing.UP) {
                            switch (playerFacing) {
                                case EAST:
                                    target = pos.add(-h, (front == EnumFacing.UP ? -d : d), w);
                                    break;
                                case WEST:
                                    target = pos.add(h, (front == EnumFacing.UP ? -d : d), w);
                                    break;
                                case SOUTH:
                                    target = pos.add(w, (front == EnumFacing.UP ? -d : d), -h);
                                    break;
                                case NORTH:
                                    target = pos.add(w, (front == EnumFacing.UP ? -d : d), +h);
                                    break;
                            }
                        }

                        IBlockState blockState = world.getBlockState(target);
                        if (isEffectiveAgainst(tool, blockState)) {
                            builder.add(target);
                        }
                    }
                }
            }

            return builder.build();
        }
    }

    private RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids) {
        float f = playerIn.rotationPitch;
        float f1 = playerIn.rotationYaw;
        double d0 = playerIn.posX;
        double d1 = playerIn.posY + (double) playerIn.getEyeHeight();
        double d2 = playerIn.posZ;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float) Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float) Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = playerIn.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        Vec3d vec3d1 = vec3d.add((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);
        return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
    }
}
