package joshie.harvest.buildings.items;

import joshie.harvest.buildings.loader.CodeGeneratorBuildings;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.base.ItemHFEnum;
import joshie.harvest.buildings.items.ItemCheat.Cheat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.buildings.items.ItemCheat.Cheat.CODE_GENERATOR;
import static joshie.harvest.buildings.items.ItemCheat.Cheat.COORD_SETTER;

public class ItemCheat extends ItemHFEnum<ItemCheat, Cheat> {
    public enum Cheat implements IStringSerializable {
        COORD_SETTER, CODE_GENERATOR;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public ItemCheat() {
        super(Cheat.class);
    }

    private static BlockPos pos1;
    private static BlockPos pos2;

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        int damage = stack.getItemDamage();
        if (damage == COORD_SETTER.ordinal()) {
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
        } else if (damage == CODE_GENERATOR.ordinal() && pos1 != null && pos2 != null) {
            new CodeGeneratorBuildings(world, pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ()).getCode(player.isSneaking());
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.LAST;
    }
}