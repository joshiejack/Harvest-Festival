package uk.joshiejack.settlements.entity.ai.action.status;

import joptsimple.internal.Strings;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionMental;
import uk.joshiejack.settlements.scripting.wrappers.NPCStatusJS;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;

@PenguinLoader("adjust_ncp_status")
public class ActionAdjustNPCStatus extends ActionMental {
    private String npcRegistryName = Strings.EMPTY;
    private String status;
    private int[] value;

    @Override
    public ActionAdjustNPCStatus withData(Object... params) {
        String var1 = (String) params[0];
        int i = 0;
        // If the first var is an npc we will use that npc
        // Otherwise we just take the current NPC instead
        if (var1.contains(":")) {
            this.npcRegistryName = var1;
            i++;
        }
        
        this.status = (String) params[i];
        if (params.length >= 4) {
            value[1] = (int) params[params.length - 3];
            value[1] = (int) params[params.length - 2];
            value[2] = (int) params[params.length - 1];
        } else {
            value[0] = (int) params[params.length - 1];
        }

        return this;
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        if (player != null) {
            NPCStatusJS wrapper = WrapperRegistry.wrap(npcRegistryName.isEmpty() ? npc.getBaseNPC() : new ResourceLocation(npcRegistryName));
            PlayerJS wrappedPlayer = WrapperRegistry.wrap(player);
            if (value.length == 3) {
                wrapper.adjustWithRange(wrappedPlayer, status, value[0], value[1], value[2]);
            } else wrapper.adjust(wrappedPlayer, status, value[0]);
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("RegistryName", npcRegistryName);
        tag.setString("Status", status);
        tag.setIntArray("Value", value);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        npcRegistryName = tag.getString("RegistryName");
        status = tag.getString("Status");
        value = tag.getIntArray("Value");
    }
}
