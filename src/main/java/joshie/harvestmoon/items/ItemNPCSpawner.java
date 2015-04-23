package joshie.harvestmoon.items;

import java.util.HashMap;

import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.api.npc.INPC;
import joshie.harvestmoon.core.HMTab;
import joshie.harvestmoon.core.helpers.NPCHelper;
import joshie.harvestmoon.core.helpers.UUIDHelper;
import joshie.harvestmoon.init.HMNPCs;
import joshie.harvestmoon.npc.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemNPCSpawner extends ItemHMMeta {
    private static HashMap<Integer, INPC> metas = new HashMap();

    public ItemNPCSpawner() {
        super(HMTab.tabTown);
        
        int meta = 0;
        for (INPC npc : HMApi.NPC.getNPCs()) {
            metas.put(meta, npc);
            meta++;
        }
    }

    @Override
    public int getMetaCount() {
        return HMApi.NPC.getNPCs().size();
    }

    public INPC getNPC(int damage) {
        INPC npc = metas.get(damage);
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
            INPC npc = getNPC(stack.getItemDamage());
            EntityNPC entity = NPCHelper.getEntityForNPC(UUIDHelper.getPlayerUUID(player), world, npc);
            entity.setPosition(xCoord + 0.5, yCoord + 1, zCoord + 0.5);
            world.spawnEntityInWorld(entity);
        }

        return false;
    }
}
