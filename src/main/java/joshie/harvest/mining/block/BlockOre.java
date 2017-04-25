package joshie.harvest.mining.block;

import joshie.harvest.api.gathering.ISmashable;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.block.BlockHFSmashable;
import joshie.harvest.core.base.item.ItemToolSmashing;
import joshie.harvest.core.entity.EntityBasket;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.MiningHelper;
import joshie.harvest.mining.block.BlockOre.Ore;
import joshie.harvest.mining.item.ItemMaterial.Material;
import joshie.harvest.tools.HFTools;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

import static joshie.harvest.api.gathering.ISmashable.ToolType.HAMMER;
import static joshie.harvest.core.lib.LootStrings.MINING;
import static joshie.harvest.core.lib.LootStrings.MINING_GEMS;
import static net.minecraft.block.material.Material.ROCK;
import static net.minecraft.init.Items.DIAMOND;

public class BlockOre extends BlockHFSmashable<BlockOre, Ore> implements ISmashable {
    private static final AxisAlignedBB COPPER_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.8D, 0.9D);

    public enum Ore implements IStringSerializable {
        ROCK, COPPER, SILVER, GOLD, MYSTRIL, GEM,
        EMERALD, DIAMOND, RUBY, AMETHYST, TOPAZ, JADE;

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

    //Return 0.75F if the plant isn't withered, otherwise, unbreakable!!!
    @Override
    @SuppressWarnings("deprecation")
    public float getPlayerRelativeBlockHardness(IBlockState state, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos) {
        return (player.getHeldItemMainhand().getItem() == HFTools.HAMMER)
                ? ((HFTools.HAMMER.getTier(player.getHeldItemMainhand()).ordinal() + 2)
                - getToolLevel(getEnumFromState(state))) * 0.025F : -1F;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
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
            case AMETHYST:
            case GEM:
                return 1;
            case SILVER:
            case TOPAZ:
                return 2;
            case GOLD:
            case JADE:
            case RUBY:
                return 3;
            case MYSTRIL:
            case EMERALD:
            case DIAMOND:
                return 4;
            default:
                return 0;
        }
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) {
            EntityPlayer player = harvesters.get();
            if (player != null && player.getHeldItemMainhand().getItem() == getTool()) {
                smashBlock(harvesters.get(), worldIn, pos, state, getTool().getTier(player.getHeldItemMainhand()));
            }
        }
    }

    private static NonNullList<ItemStack> getRandomStack(World world, Material material, int bonus) {
        while (bonus > 0) {
            if (world.rand.nextInt(bonus) == 0)
                return NonNullList.withSize(1, HFMining.MATERIALS.getStackFromEnum(material, 1 + world.rand.nextInt(bonus)));
            bonus--;
        }

        return NonNullList.withSize(1, HFMining.MATERIALS.getStackFromEnum(material, 1));
    }

    private static NonNullList<ItemStack> getRandomStack(World world, Item item, int bonus) {
        return NonNullList.withSize(1, new ItemStack(item, 1 + world.rand.nextInt(bonus)));
    }

    @Override
    public NonNullList<ItemStack> getDrops(EntityPlayer player, World world, BlockPos pos, IBlockState state, float luck) {
        Ore ore = getEnumFromState(state);

        if (world instanceof WorldServer) {
            WorldServer server = ((WorldServer) world);
            server.spawnParticle(EnumParticleTypes.BLOCK_CRACK, pos.getX(), pos.getY(), pos.getZ(), 10, 0.5D, 0.5D, 0.5D, 0.0D, Block.getStateId(Blocks.DIRT.getDefaultState()));
        }

        NonNullList<ItemStack> drops;
        switch (ore) {
            case ROCK:
                drops = world.isRemote ? NonNullList.withSize(1, new ItemStack(this)) : MiningHelper.getLoot(MINING, world, player, luck);
                break;
            case COPPER:
                drops = getRandomStack(world, Material.COPPER, 5);
                break;
            case SILVER:
                drops = getRandomStack(world, Material.SILVER, 4);
                break;
            case GOLD:
                drops = getRandomStack(world, Material.GOLD, 3);
                break;
            case MYSTRIL:
                drops = getRandomStack(world, Material.MYSTRIL, 3);
                break;
            case EMERALD:
                drops = getRandomStack(world, Items.EMERALD, 5);
                break;
            case DIAMOND:
                drops = world.rand.nextInt(512) == 0 ? getRandomStack(world, Material.PINK_DIAMOND, 1) : getRandomStack(world, DIAMOND, 3);
                break;
            case RUBY:
                drops = getRandomStack(world, Material.RUBY, 4);
                break;
            case JADE:
                drops = getRandomStack(world, Material.JADE, 5);
                break;
            case AMETHYST:
                drops = getRandomStack(world, Material.AMETHYST, 3);
                break;
            case TOPAZ:
                drops = getRandomStack(world, Material.TOPAZ, 4);
                break;
            case GEM:
                drops = world.isRemote ? NonNullList.withSize(1, new ItemStack(this)) : MiningHelper.getLoot(MINING_GEMS, world, player, luck);
                break;
            default:
                drops = NonNullList.create();
        }

        EntityBasket.findBasketAndShip(player, drops);
        if (!world.isRemote) {
            for (ItemStack stack : drops) {
                HFTrackers.getPlayerTrackerFromPlayer(player).getTracking().addAsObtained(stack);
            }
        }

        return drops;
    }

    @Override
    public int getSortValue(@Nonnull ItemStack stack) {
        return CreativeSort.TOOLS - 20 + stack.getItemDamage();
    }
}