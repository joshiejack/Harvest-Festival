package joshie.harvest.animals.item;

import joshie.harvest.animals.entity.EntityHarvestCow;
import joshie.harvest.animals.entity.EntityHarvestSheep;
import joshie.harvest.animals.item.ItemAnimalSpawner.Spawner;
import joshie.harvest.core.util.base.ItemHFEnum;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemAnimalSpawner extends ItemHFEnum<Spawner> {
    public enum Spawner {
        COW, SHEEP, CHICKEN;
    }

    public ItemAnimalSpawner() {
        super(Spawner.class);
    }

    public EntityAgeable getEntityFromEnum(World world, Spawner spawner) {
        switch (spawner) {
            case COW:
                return new EntityHarvestCow(world);
            case SHEEP:
                return new EntityHarvestSheep(world);
            default:
                return null;
        }
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            EntityAgeable entity = getEntityFromEnum(world, getEnumFromStack(stack));
            entity.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            world.spawnEntityInWorld(entity);
        }

        return EnumActionResult.FAIL;
    }
}