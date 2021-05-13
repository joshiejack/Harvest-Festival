package uk.joshiejack.penguinlib.scripting.wrappers;

import uk.joshiejack.penguinlib.data.adapters.MaterialAdapter;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class EntityJS<E extends Entity> extends AbstractJS<E> {
    public EntityJS(E entity) {
        super(entity);
    }

    @SuppressWarnings("ConstantConditions")
    public boolean is(String name) {
        Entity object = penguinScriptingObject;
        if (name.equals("player")) return object instanceof EntityPlayer;
        EntityEntry entry = EntityRegistry.getEntry(object.getClass());
        return entry != null && entry.getRegistryName().equals(new ResourceLocation(name));
    }

    public String registry() {
        Entity object = penguinScriptingObject;
        EntityEntry entry = EntityRegistry.getEntry(object.getClass());
        return entry != null ? entry.getRegistryName().toString() : "none";
    }

    public DataJS data() {
        return WrapperRegistry.wrap(penguinScriptingObject.getEntityData());
    }

    public boolean isInsideOf(String material) {
        return penguinScriptingObject.isInsideOfMaterial(MaterialAdapter.byName(material));
    }

    public int portalTimer() {
        return penguinScriptingObject.timeUntilPortal;
    }

    public int existed() {
        return penguinScriptingObject.ticksExisted;
    }

    public void disableItemDrops() {
        penguinScriptingObject.setDropItemsWhenDead(false);
    }

    public void kill() {
        penguinScriptingObject.setDead();
    }

    public String getUUID() {
        return penguinScriptingObject.getPersistentID().toString();
    }

    public String name() {
        return penguinScriptingObject.getName();
    }

    public PositionJS pos() {
        return WrapperRegistry.wrap(new BlockPos(penguinScriptingObject));
    }

    public WorldJS<?> world() {
        return WrapperRegistry.wrap(penguinScriptingObject.world);
    }

    public EnumFacing facing() {
        return EntityHelper.getFacingFromEntity(penguinScriptingObject).getOpposite();
    }

    public double x() {
        return penguinScriptingObject.posX;
    }

    public double y() {
        return penguinScriptingObject.posY;
    }

    public double z() {
        return penguinScriptingObject.posZ;
    }
}
