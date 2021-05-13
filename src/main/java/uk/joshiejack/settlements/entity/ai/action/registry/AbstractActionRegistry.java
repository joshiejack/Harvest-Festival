package uk.joshiejack.settlements.entity.ai.action.registry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.settlements.entity.ai.action.ActionMental;
import uk.joshiejack.penguinlib.util.PenguinRegistry;

import java.util.Map;

public abstract class AbstractActionRegistry<P extends PenguinRegistry> extends ActionMental {
    protected ResourceLocation resource;
    private final Map<ResourceLocation, P> map;

    public AbstractActionRegistry(Map<ResourceLocation, P> map) {
        this.map = map;
    }

    @Override
    public AbstractActionRegistry<?> withData(Object... params) {
        this.resource = new ResourceLocation((String)params[0]);
        return this;
    }

    @Override
    public EnumActionResult execute(EntityNPC npc) {
        if (player != null) {
            P p = map.get(resource);
            if (p != null) {
                performAction(npc.world, p);
            }
        }

        return EnumActionResult.SUCCESS;
    }

    public abstract void performAction(World world, P object);

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Resource", resource.toString());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        resource = new ResourceLocation(tag.getString("Resource"));
    }
}
