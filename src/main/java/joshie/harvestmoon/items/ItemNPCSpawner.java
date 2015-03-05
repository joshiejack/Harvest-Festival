package joshie.harvestmoon.items;

import java.util.HashMap;

import joshie.harvestmoon.core.helpers.UUIDHelper;
import joshie.harvestmoon.init.HMNPCs;
import joshie.harvestmoon.npc.EntityNPC;
import joshie.harvestmoon.npc.NPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemNPCSpawner extends ItemHMMeta {
    private static HashMap<Integer, NPC> metas = new HashMap();

    public ItemNPCSpawner() {
        int meta = 0;
        for (NPC npc : HMNPCs.getNPCs()) {
            metas.put(meta, npc);
            meta++;
        }
    }

    @Override
    public int getMetaCount() {
        return HMNPCs.getNPCs().size();
    }

    public NPC getNPC(int damage) {
        NPC npc = metas.get(damage);
        return npc != null ? npc : HMNPCs.goddess;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.getItemDamage() < getMetaCount()) {
            return getNPC(stack.getItemDamage()).getLocalizedName();
        } else return super.getItemStackDisplayName(stack);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int xCoord, int yCoord, int zCoord, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && stack.getItemDamage() < getMetaCount()) {            
            NPC npc = getNPC(stack.getItemDamage());
            EntityNPC entity = npc.getEntity(UUIDHelper.getPlayerUUID(player), world);
            entity.setPosition(xCoord + 0.5, yCoord + 1, zCoord + 0.5);
            world.spawnEntityInWorld(entity);
        }

        return false;
    }
}
