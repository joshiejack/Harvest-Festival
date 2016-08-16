package joshie.harvest.core.base;

import joshie.harvest.api.gathering.ISmashable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockHFSmashable<B extends BlockHFSmashable, E extends Enum<E> & IStringSerializable> extends BlockHFEnum<B, E> implements ISmashable {
    //Main Constructor
    public BlockHFSmashable(Material material, Class<E> clazz, CreativeTabs tab) {
        super(material, clazz, tab);
    }

    public abstract ItemToolSmashing getTool();

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
        ItemToolSmashing tool = getTool();
        if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == tool) {
            if (player.motionY <= -0.1F) {
                tool.smashBlock(world, player, pos, player.getHeldItemMainhand(), true);
            }
        }
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)  {
        if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) {
            ItemStack stack = getDrop(harvesters.get(), worldIn, pos, state, fortune);
            if (stack != null) {
                List<ItemStack> items = new ArrayList<>();
                items.add(stack);
                chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, chance, false, harvesters.get());

                for (ItemStack item : items) {
                    if (worldIn.rand.nextFloat() <= chance) {
                        spawnAsEntity(worldIn, pos, item);
                    }
                }
            }
        }
    }

    @Override
    protected int getToolLevel(E e) {
        switch (e.ordinal()) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 3:
                return 3;
            case 2:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            default:
                return 0;
        }
    }
}