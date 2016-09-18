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

import java.util.Locale;

import static joshie.harvest.api.gathering.ISmashable.ToolType.HAMMER;
import static net.minecraft.block.material.Material.ROCK;

public class BlockOre extends BlockHFSmashable<BlockOre, Ore> implements ISmashable {
    private static final AxisAlignedBB COPPER_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.8D, 0.9D);

    public enum Ore implements IStringSerializable {
        ROCK, COPPER, SILVER, GOLD, MYSTRIL, /*GEM*/;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }

    public BlockOre() {
        super(ROCK, Ore.class, HFTab.MINING);
        setBlockUnbreakable();
        setSoundType(SoundType.STONE);
    }

    @Override
    //Return 0.75F if the plant isn't withered, otherwise, unbreakable!!!
    public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World world, BlockPos pos) {
        return (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == HFTools.HAMMER)
                ? ((HFTools.HAMMER.getTier(player.getHeldItemMainhand()).ordinal() + 1) * 0.05F)
                   - (getToolLevel(getEnumFromState(state)) * 0.025F): -1F;
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
    protected int getToolLevel(Ore ore) {
        switch (ore) {
            case ROCK:
            case COPPER:
                return 1;
            case SILVER:
                return 2;
            case GOLD:
                return 3;
            case MYSTRIL:
                return 4;
            default:
                return 0;
        }
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)  {
        if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots)  {
            EntityPlayer player = harvesters.get();
            if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == getTool()) {
                if (smashBlock(harvesters.get(), worldIn, pos, state, getTool().getTier(player.getHeldItemMainhand()))) return;
            }
        }
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