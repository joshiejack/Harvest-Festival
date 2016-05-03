package joshie.harvest.items;

import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.entity.EntityHarvestSheep;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.base.ItemHFBaseMeta;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemAnimal extends ItemHFBaseMeta {
    public static final int COW = 0;
    public static final int SHEEP = 1;
    public static final int CHICKEN = 2;

    public ItemAnimal() {
        super(HFTab.FARMING);
    }

    public EntityAgeable getEntityFromMeta(World world, int meta) {
        switch (meta) {
            case COW:
                return new EntityHarvestCow(world);
            case SHEEP:
                return new EntityHarvestSheep(world);
            default:
                return null;
        }
    }

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case CHICKEN:
                return "chicken";
            case SHEEP:
                return "sheep";
            case COW:
                return "cow";
            default:
                return "null";
        }
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && stack.getItemDamage() < getMetaCount()) {
            EntityAgeable entity = getEntityFromMeta(world, stack.getItemDamage());
            entity.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            world.spawnEntityInWorld(entity);
        }

        return EnumActionResult.FAIL;
    }

    @Override
    public int getMetaCount() {
        return 3;
    }
}