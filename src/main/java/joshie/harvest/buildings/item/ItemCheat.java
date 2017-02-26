package joshie.harvest.buildings.item;

import joshie.harvest.buildings.BuildingRegistry;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.buildings.block.BlockInternalAir;
import joshie.harvest.buildings.item.ItemCheat.Cheat;
import joshie.harvest.buildings.loader.CodeGeneratorBuildings;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFEnum;
import joshie.harvest.core.helpers.ChatHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.HFTemplate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;

import static joshie.harvest.buildings.item.ItemCheat.Cheat.*;

public class ItemCheat extends ItemHFEnum<ItemCheat, Cheat> {
    public enum Cheat implements IStringSerializable {
        COORD_SETTER, CODE_GENERATOR,
        AIR_PLACER, AIR_REMOVER,
        PARK_PLACER, PARK_ENDSTONE;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public ItemCheat() {
        super(HFTab.TOWN, Cheat.class);
    }

    private static BlockPos pos1;
    private static BlockPos pos2;

    @Override
    @Nonnull
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        int damage = stack.getItemDamage();
        if (damage == COORD_SETTER.ordinal()) {
            if (player.isSneaking()) {
                pos2 = pos;
                if (world.isRemote) ChatHelper.displayChat("Setting Second Coordinates to " + pos2);
            } else {
                pos1 = pos;
                if (world.isRemote) ChatHelper.displayChat("Setting First Coordinates to " + pos1);

            }

            return EnumActionResult.SUCCESS;
        } else if (damage == CODE_GENERATOR.ordinal() && pos1 != null && pos2 != null) {
            new CodeGeneratorBuildings(world, pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ()).getCode();
            return EnumActionResult.SUCCESS;
        } else if (damage == AIR_PLACER.ordinal()) {
            if (!world.isRemote) {
                Set<BlockPos> positions = new HashSet<>();
                if (world.isAirBlock(pos.up())) {
                    positions.add(pos.up());
                    int loop = 24;
                    for (int j = 0; j < loop; j++) {
                        Set<BlockPos> temp = new HashSet<>(positions);
                        for (BlockPos coord : temp) {
                            for (EnumFacing theFacing: EnumFacing.VALUES) {
                                BlockPos offset = coord.offset(theFacing);
                                if (world.isAirBlock(offset)) {
                                    positions.add(offset);
                                }
                            }
                        }
                    }

                    for (BlockPos position: positions) {
                        world.setBlockState(position, HFBuildings.AIR.getDefaultState());
                        BlockInternalAir.onPlaced(world, position, player);
                    }
                }
            }
        } else if (damage == AIR_REMOVER.ordinal()) {
            Set<BlockPos> positions = new HashSet<>();
            if (world.getBlockState(pos.up()).getBlock() == HFBuildings.AIR) {
                positions.add(pos.up());
                int loop = 24;
                for (int j = 0; j < loop; j++) {
                    Set<BlockPos> temp = new HashSet<>(positions);
                    for (BlockPos coord : temp) {
                        for (EnumFacing theFacing: EnumFacing.VALUES) {
                            BlockPos offset = coord.offset(theFacing);
                            if (world.getBlockState(offset).getBlock() == HFBuildings.AIR) {
                                positions.add(offset);
                            }
                        }
                    }
                }

                for (BlockPos position: positions) {
                    world.setBlockToAir(position);
                    BlockInternalAir.onPlaced(world, position, player);
                }
            }
        } else if (damage == PARK_PLACER.ordinal()) {
            List<Pair<BlockPos, IBlockState>> states = new ArrayList<>();
            for (int  x = 0; x < 38; x++) {
                for (int z = 0; z < 31; z++) {
                    BlockPos target = pos.add(x, 0, z);
                    if (world.getBlockState(target).getBlock() == Blocks.END_STONE) {
                        states.add(Pair.of(target, Blocks.GRASS.getDefaultState()));
                    } else if (world.getBlockState(target).getBlock() == Blocks.AIR) states.add(Pair.of(target, Blocks.GRASS_PATH.getDefaultState()));
                }
            }

            HFTemplate template = BuildingRegistry.INSTANCE.getTemplateForBuilding(HFBuildings.FESTIVAL_GROUNDS);
            template.placeBlocks(world, pos, Rotation.NONE, null);
            for (Pair<BlockPos, IBlockState> pair: states) {
                world.setBlockState(pair.getLeft(), pair.getRight());
            }

            world.setBlockState(pos, Blocks.END_STONE.getDefaultState());
            world.setBlockState(pos.south(30).east(37).up(9), Blocks.END_STONE.getDefaultState());
        } else if (damage == PARK_ENDSTONE.ordinal()) {
            List<Pair<BlockPos, IBlockState>> states = new ArrayList<>();
            for (int  x = 0; x < 38; x++) {
                for (int z = 0; z < 31; z++) {
                    BlockPos target = pos.add(x, 0, z);
                    if (world.getBlockState(target).getBlock() == Blocks.GRASS) {
                        states.add(Pair.of(target, Blocks.END_STONE.getDefaultState()));
                    } else states.add(Pair.of(target, Blocks.AIR.getDefaultState()));
                }
            }

            HFTemplate template = BuildingRegistry.INSTANCE.getTemplateForBuilding(HFBuildings.FESTIVAL_GROUNDS);
            template.removeBlocks(world, pos, Rotation.NONE, Blocks.END_STONE.getDefaultState());
            for (Pair<BlockPos, IBlockState> pair: states) {
                world.setBlockState(pair.getLeft(), pair.getRight());
            }

            world.setBlockState(pos, Blocks.END_STONE.getDefaultState());
            world.setBlockState(pos.south(30).east(37).up(9), Blocks.END_STONE.getDefaultState());
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public boolean shouldDisplayInCreative(Cheat cheat) {
        return true;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.LAST;
    }
}