package joshie.harvest.items;

import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.util.CodeGeneratorBuildings;
import joshie.harvest.core.util.CodeGeneratorRendering;
import joshie.harvest.core.util.base.ItemHFBaseMeta;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCheat extends ItemHFBaseMeta {
    private static final int COORD_SETTER = 0;
    private static final int CODE_GENERATOR = 1;
    private static final int RENDER_GENERATOR = 2;
    private static final int META_CHECKER = 3;
    private static final int CHUNK_TESTER = 4;
    private static BlockPos pos1;
    private static BlockPos pos2;

    @Override
    public int getMetaCount() {
        return 5;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        int damage = stack.getItemDamage();
        if (damage == COORD_SETTER) {
            if (player.isSneaking()) {
                pos2 = pos;
                if (world.isRemote) {
                    MCClientHelper.addToChat("Setting Second Coordinates to " + pos2);
                }
            } else {
                pos1 = pos;
                if (world.isRemote) {
                    MCClientHelper.addToChat("Setting First Coordinates to " + pos1);
                }
            }

            return EnumActionResult.SUCCESS;
        } else if (damage == CODE_GENERATOR && pos1 != null && pos2 != null) {
            new CodeGeneratorBuildings(world, pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ()).getCode(player.isSneaking());
            return EnumActionResult.SUCCESS;
        } else if (damage == RENDER_GENERATOR) {
            new CodeGeneratorRendering().getCode();
        } else if (damage == META_CHECKER) {
            if (world.isRemote) {
                IBlockState state = world.getBlockState(pos);
                MCClientHelper.addToChat("Block: " + state.getBlock());
                MCClientHelper.addToChat("Metadata: " + state.getBlock().getMetaFromState(state));
                MCClientHelper.addToChat("" + state.getBlock().getClass());
            }
        } else if (damage == CHUNK_TESTER) {
            //ChunkTester.activate(world, xCoord, yCoord, zCoord);
            if (!world.isRemote) {
                /*WorldLocation location = TownHelper.getLocationFor(player, HFBuildings.supermarket, Town.SUPERMARKET_BEDROOM);
                if (location != null) {
                    player.setPositionAndUpdate(location.x + 0.5D, location.y + 0D, location.z + 0.5D);
                } */
            }
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case COORD_SETTER:
                return "coord_setter";
            case CODE_GENERATOR:
                return "code_generator";
            case RENDER_GENERATOR:
                return "render_generator";
            case META_CHECKER:
                return "meta_checker";
            case CHUNK_TESTER:
                return "chunk_tester";
            default:
                return "invalid";
        }
    }
}