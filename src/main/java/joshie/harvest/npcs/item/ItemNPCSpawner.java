package joshie.harvest.npcs.item;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFRegistry;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.NPCHelper;
import joshie.harvest.npcs.entity.EntityNPC;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import java.util.UUID;

public class ItemNPCSpawner extends ItemHFRegistry<ItemNPCSpawner, NPC> {
    public ItemNPCSpawner() {
        super("NPC", NPCHelper.OLD_REGISTRY, NPC.REGISTRY, HFTab.TOWN);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return getObjectFromStack(stack).getLocalizedName();
    }

    private void spawnNPC(World world, BlockPos pos, NPC npc) {
        EntityNPC entity = NPCHelper.getEntityForNPC(world, npc);
        entity.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
        entity.resetSpawnHome();
        if (npc == HFNPCs.CARPENTER) {
            entity.setUniqueId(TownHelper.getClosestTownToEntity(entity, true).getID());
        }

        world.spawnEntityInWorld(entity);
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        NPC npc = getObjectFromStack(stack);
        if (npc != null) {
            if (!world.isRemote) {
                if (npc == HFNPCs.CARPENTER) {
                    WorldServer server = (WorldServer) world;
                    UUID uuid = TownHelper.getClosestTownToEntity(player, true).getID();
                    Entity entity = server.getEntityFromUuid(uuid);
                    if (entity instanceof EntityNPC) {
                        entity.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                        ((EntityNPC)entity).resetSpawnHome();
                    } else spawnNPC(world, pos, npc);
                } else spawnNPC(world, pos, npc);
            }

            stack.splitStack(1);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @Override
    protected NPC getDefaultValue() {
        return HFNPCs.CARPENTER;
    }
}