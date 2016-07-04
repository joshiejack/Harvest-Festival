package joshie.harvest.mining.blocks;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.gathering.ISmashable;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.lib.LootStrings;
import joshie.harvest.core.util.base.BlockHFEnum;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.MiningHelper;
import joshie.harvest.mining.blocks.BlockOre.Ore;
import joshie.harvest.mining.items.ItemMaterial;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static joshie.harvest.api.gathering.ISmashable.ToolType.HAMMER;
import static net.minecraft.block.material.Material.ROCK;

public class BlockOre extends BlockHFEnum<BlockOre, Ore> implements ISmashable {
    private static final AxisAlignedBB COPPER_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.8D, 0.9D);

    public enum Ore implements IStringSerializable {
        ROCK, COPPER, SILVER, GOLD, MYSTRIL, /*GEM*/;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public BlockOre() {
        super(ROCK, Ore.class, HFTab.MINING);
        setHardness(1.5F);
        setSoundType(SoundType.STONE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return COPPER_AABB;
    }

    @Override
    public ToolType getToolType() {
        return HAMMER;
    }

    @Override
    public ItemStack getDrop(EntityPlayer player, World world, BlockPos pos, IBlockState state, float luck) {
        Ore ore = getEnumFromState(state);
        switch (ore) {
            case ROCK: {
                ResourceLocation loot = HFTrackers.getCalendar(world).getSeasonAt(pos) == Season.WINTER ? LootStrings.MINE_WINTER : LootStrings.MINE_SPRING;
                return MiningHelper.getLoot(loot, world, player, luck);
            }

            case COPPER:
                return HFMining.MATERIALS.getStackFromEnum(ItemMaterial.Material.COPPER, 1 + world.rand.nextInt(5));
            case SILVER:
                return HFMining.MATERIALS.getStackFromEnum(ItemMaterial.Material.SILVER, 1 + world.rand.nextInt(3));
            case GOLD:
                return HFMining.MATERIALS.getStackFromEnum(ItemMaterial.Material.GOLD, 1 + world.rand.nextInt(2));
            case MYSTRIL:
                return HFMining.MATERIALS.getStackFromEnum(ItemMaterial.Material.MYSTRIL); /*
            case GEM: {
                ResourceLocation loot = HFTrackers.getCalendar(world).getSeasonAt(pos) == Season.WINTER ? LootStrings.MINE_WINTER_GEM : LootStrings.MINE_SPRING_GEM;
                return MiningHelper.getLoot(loot, world, player, luck);
            } */
            default:
                return null;
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TOOLS - 20 + stack.getItemDamage();
    }
}