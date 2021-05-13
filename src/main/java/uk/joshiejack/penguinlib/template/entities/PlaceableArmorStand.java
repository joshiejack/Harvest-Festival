package uk.joshiejack.penguinlib.template.entities;

import com.google.common.collect.Lists;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

@PenguinLoader("armor_stand")
public class PlaceableArmorStand extends PlaceableEntity {
    private float rotation;
    private List<ItemStack> hand;
    private List<ItemStack> armor;

    public PlaceableArmorStand() {}
    public PlaceableArmorStand(List<ItemStack> hand, List<ItemStack> armor, float rotation, BlockPos position) {
        super(position);
        this.hand = hand;
        this.armor = armor;
        this.rotation = rotation;
    }

    @Override
    public Entity getEntity(World world, BlockPos pos, Rotation rotation) {
        EntityArmorStand stand = new EntityArmorStand(world, getX(), getY(), getZ());

        for (EntityEquipmentSlot e: EntityEquipmentSlot.values()) {
            ItemStack in = e.getSlotType() == EntityEquipmentSlot.Type.HAND ? hand.get(e.getSlotIndex()) : armor.get(e.getSlotIndex());
            if (!in.isEmpty()) {
                stand.setItemStackToSlot(e, in);
            }
        }

        //TODO: Fix armor stand rotation? AND APPLY IT TO ALL ENTITIS!
        stand.setLocationAndAngles(getX() + 0.5, getY(), getZ() + 0.5, 0F, 0F);
        return stand;
    }

    @Override
    public Class<? extends Entity> getEntityClass() {
        return EntityArmorStand.class;
    }

    @Override
    public PlaceableArmorStand getCopyFromEntity(Entity ent, BlockPos position) {
        EntityArmorStand p = (EntityArmorStand) ent;
        List<ItemStack> armor = Lists.newArrayList();
        List<ItemStack> hand = Lists.newArrayList();
        for (EntityEquipmentSlot e: EntityEquipmentSlot.values()) {
            if (e.getSlotType() == EntityEquipmentSlot.Type.HAND) {
                hand.add(e.getSlotIndex(), p.getItemStackFromSlot(e));
            } else {
                armor.add(e.getSlotIndex(), p.getItemStackFromSlot(e));
            }
        }

        return new PlaceableArmorStand(hand, armor, p.rotationYaw, position);
    }
}