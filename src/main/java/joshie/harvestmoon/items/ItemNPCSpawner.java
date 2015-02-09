package joshie.harvestmoon.items;

import java.util.HashMap;

import joshie.harvestmoon.entities.EntityNPC;
import joshie.harvestmoon.entities.EntityNPCBuilder;
import joshie.harvestmoon.entities.NPC;
import joshie.harvestmoon.init.HMNPCs;
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
            EntityNPC entity = null;
            NPC npc = getNPC(stack.getItemDamage());
            if (npc.isBuilder()) {
                entity = new EntityNPCBuilder(world, npc);
            } else entity = new EntityNPC(world, npc);
            entity.setPosition(xCoord, yCoord, zCoord);
            world.spawnEntityInWorld(entity);
        }

        return false;
    }
}
