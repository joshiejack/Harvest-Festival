package joshie.harvest.npc.items;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.base.ItemHFEnum;
import joshie.harvest.npc.items.ItemNPCTool.NPCTool;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import static joshie.harvest.npc.items.ItemNPCTool.NPCTool.NPC_KILLER;
import static net.minecraft.util.text.TextFormatting.AQUA;

public class ItemNPCTool extends ItemHFEnum<ItemNPCTool, NPCTool> {
    public enum NPCTool implements IStringSerializable {
        BLUE_FEATHER, NPC_KILLER;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public ItemNPCTool() {
        super(HFTab.TOWN, NPCTool.class);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if (getEnumFromStack(stack) == NPC_KILLER) {
            entity.setDead();
            return true;
        }

        return false;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 1;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return AQUA + super.getItemStackDisplayName(stack);
    }
}