package uk.joshiejack.settlements.item;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.client.renderer.item.NPCSpawnerRenderer;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.npcs.DynamicNPC;
import uk.joshiejack.settlements.npcs.NPC;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.penguinlib.item.base.ItemSingular;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ItemRandomNPC extends ItemSingular {
    public static final List<String> NAMES = Lists.newArrayList();

    public ItemRandomNPC() {
        super(new ResourceLocation(Settlements.MODID, "custom_npc_spawner"));
        setCreativeTab(Settlements.TAB);
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            TownServer town = TownFinder.getFinder(world).findOrCreate(player, pos);
            //Let's create a custom npc to show up in the book?
            ResourceLocation uniqueID = new ResourceLocation("custom", UUID.randomUUID().toString());
            DynamicNPC.Builder builder = new DynamicNPC.Builder(world.rand, uniqueID);
            NBTTagCompound data = builder.build();
            town.getCensus().createCustomNPCFromData(world, uniqueID, NPC.CUSTOM_NPC, data);
            EntityNPC entity = town.getCensus().getSpawner().getNPC(world, NPC.CUSTOM_NPC, uniqueID, data, pos.up());
            if (entity != null) {
                world.spawnEntity(entity);
            }
        }

        stack.splitStack(1);
        return EnumActionResult.SUCCESS;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), "inventory"));
        AdventureItems.CUSTOM_NPC_SPAWNER.setTileEntityItemStackRenderer(new NPCSpawnerRenderer());
    }
}
