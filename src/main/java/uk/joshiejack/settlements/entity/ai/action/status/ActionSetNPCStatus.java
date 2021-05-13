package uk.joshiejack.settlements.entity.ai.action.status;

import joptsimple.internal.Strings;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionMental;
import uk.joshiejack.settlements.scripting.wrappers.NPCStatusJS;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;

@PenguinLoader("set_npc_status")
public class ActionSetNPCStatus extends ActionMental {
    private String npcRegistryName = Strings.EMPTY;
    private String status;
    private int value;

    @Override
    public ActionSetNPCStatus withData(Object... params) {
        String var1 = (String) params[0];
        int i = 0;
        // If the first var is an npc we will use that npc
        // Otherwise we just take the current NPC instead
        if (var1.contains(":")) {
            this.npcRegistryName = var1;
            i++;
        }
        
        this.status = (String) params[i];
        this.value = Integer.parseInt(String.valueOf(params[i + 1]));
        return this;
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        if (player != null) {
            NPCStatusJS wrapper = WrapperRegistry.wrap(npcRegistryName == null ? npc.getBaseNPC() : new ResourceLocation(npcRegistryName));
            wrapper.set(WrapperRegistry.wrap(player), status, value);
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("RegistryName", npcRegistryName);
        tag.setString("Status", status);
        tag.setInteger("Value", value);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        npcRegistryName = tag.getString("RegistryName");
        status = tag.getString("Status");
        value = tag.getInteger("Value");
    }
}
