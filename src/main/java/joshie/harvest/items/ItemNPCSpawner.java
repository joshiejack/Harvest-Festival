package joshie.harvest.items;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.NPCHelper;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.HashMap;

public class ItemNPCSpawner extends ItemHFMeta {
    private static HashMap<Integer, INPC> metas = new HashMap();

    public ItemNPCSpawner() {
        super(HFTab.tabTown);
        
        int meta = 0;
        for (INPC npc : HFApi.NPC.getNPCs()) {
            metas.put(meta, npc);
            meta++;
        }
    }

    @Override
    public int getMetaCount() {
        return HFApi.NPC.getNPCs().size();
    }

    public static INPC getNPC(int damage) {
        INPC npc = metas.get(damage);
        return npc != null ? npc : HFNPCs.goddess;
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
