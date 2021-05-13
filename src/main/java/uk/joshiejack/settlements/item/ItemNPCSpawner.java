package uk.joshiejack.settlements.item;

import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.client.renderer.entity.RenderNPC;
import uk.joshiejack.settlements.client.renderer.item.NPCSpawnerRenderer;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.npcs.NPC;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.TownServer;
import uk.joshiejack.penguinlib.item.base.ItemMultiMap;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

public class ItemNPCSpawner extends ItemMultiMap<NPC> {
    public ItemNPCSpawner() {
        super(new ResourceLocation(Settlements.MODID, "npc_spawner"), NPC.REGISTRY);
        setCreativeTab(Settlements.TAB);
    }

    @Override
    protected NPC getNullEntry() {
        return NPC.NULL_NPC;
    }

    public ItemStack withPlayerSkin(UUID player) {
        ItemStack stack = new ItemStack(this);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setString("UUID", player.toString());
        return stack;
    }

    public ResourceLocation getSkinFromStack(NPC npc, ItemStack stack) {
        ResourceLocation skin = null;
        if (stack.hasTagCompound()) {
            if (stack.getTagCompound().hasKey("UUID")) {
                UUID uuid = UUID.fromString(stack.getTagCompound().getString("UUID"));
                skin = RenderNPC.getSkinFromUsernameOrUUID(uuid, UsernameCache.getLastKnownUsername(uuid));
            } else if (stack.getTagCompound().hasKey("PlayerSkin")) {
                skin =  RenderNPC.getSkinFromUsernameOrUUID(null, stack.getTagCompound().getString("PlayerSkin"));
            } else if (stack.getTagCompound().hasKey("ResourceSkin")) {
                skin = new ResourceLocation(stack.getTagCompound().getString("ResourceSkin"));
            } else skin = npc.getSkin();
        }

        return skin != null && RenderNPC.textureExists(skin) ? skin : RenderNPC.MISSING;
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        NPC npc = getObjectFromStack(stack);
        if (npc != null) {
            if (!world.isRemote) {
                TownServer town = TownFinder.getFinder(world).findOrCreate(player, pos);
                EntityNPC entity = town.getCensus().getSpawner().getNPC(world, npc, npc.getRegistryName(), null, pos.up());
                if (entity != null) {
                    world.spawnEntity(entity);
                }
            }

            stack.splitStack(1);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @Override
    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return I18n.translateToLocalFormatted("settlements.item.npc.name", getObjectFromStack(stack).getLocalizedName());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), "inventory"));
        AdventureItems.NPC_SPAWNER.setTileEntityItemStackRenderer(new NPCSpawnerRenderer());
    }
}
