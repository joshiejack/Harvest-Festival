package joshie.harvest.mining.block;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.gathering.ISmashable;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFSmashable;
import joshie.harvest.core.base.item.ItemToolSmashing;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.lib.LootStrings;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.MiningHelper;
import joshie.harvest.mining.block.BlockOre.Ore;
import joshie.harvest.mining.item.ItemMaterial;
import joshie.harvest.tools.HFTools;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import static joshie.harvest.api.gathering.ISmashable.ToolType.HAMMER;
import static net.minecraft.block.material.Material.ROCK;

public class BlockOre extends BlockHFSmashable<BlockOre, Ore> implements ISmashable {
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

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return COPPER_AABB;
    }

    @Override
    public ToolType getToolType() {
        return HAMMER;
    }

    @Override
    public ItemToolSmashing getTool() {
        return HFTools.HAMMER;
    }

    @Override
    public ItemStack getDrop(EntityPlayer player, World world, BlockPos pos, IBlockState state, float luck) {
        Ore ore = getEnumFromState(state);

        if (world instanceof WorldServer) {
            WorldServer server = ((WorldServer)world);
            server.spawnParticle(EnumParticleTypes.BLOCK_CRACK, pos.getX(), pos.getY(), pos.getZ(), 10, 0.5D, 0.5D, 0.5D, 0.0D, Block.getStateId(Blocks.DIRT.getDefaultState()));
        }

        switch (ore) {
            case ROCK: {
                ResourceLocation loot = HFApi.calendar.getDate(world).getSeason() == Season.WINTER ? LootStrings.MINE_WINTER : LootStrings.MINE_SPRING;
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