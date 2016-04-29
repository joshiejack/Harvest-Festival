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
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;

public class ItemNPCSpawner extends ItemHFMeta {
    private static HashMap<Integer, INPC> metas = new HashMap<Integer, INPC>();

    public ItemNPCSpawner() {
        super(HFTab.TOWN);

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
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && stack.getItemDamage() < getMetaCount()) {
            INPC npc = getNPC(stack.getItemDamage());
            EntityNPC entity = NPCHelper.getEntityForNPC(UUIDHelper.getPlayerUUID(player), world, npc);
            entity.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            world.spawnEntityInWorld(entity);
        }
        return EnumActionResult.FAIL;
    }
}