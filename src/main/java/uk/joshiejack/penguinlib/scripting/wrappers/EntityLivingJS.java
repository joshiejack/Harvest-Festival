package uk.joshiejack.penguinlib.scripting.wrappers;

import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class EntityLivingJS<E extends EntityLivingBase> extends EntityJS<E> {
    public EntityLivingJS(E entity) {
        super(entity);
    }

    public void heal(int amount) {
        penguinScriptingObject.heal(amount);
    }

    public ItemStackJS getHeldItem(EnumHand hand) {
        return WrapperRegistry.wrap(penguinScriptingObject.getHeldItem(hand));
    }

    public boolean isHolding(String name, EnumHand hand) {
        return penguinScriptingObject.getHeldItem(hand).getItem() == Item.REGISTRY.getObject(new ResourceLocation(name));
    }

    public boolean isWielding(String name) {
        EntityLivingBase object = penguinScriptingObject;
        return name.equals("sword") ? object.getHeldItemMainhand().getItem() instanceof ItemSword :
                object.getHeldItemMainhand().getItem().getToolClasses(object.getHeldItemMainhand()).contains(name);
    }

    public boolean hasEffect(String name) {
        return penguinScriptingObject.isPotionActive(Objects.requireNonNull(Potion.REGISTRY.getObject(new ResourceLocation(name))));
    }

    public void addEffect(String name, int time, int modifier) {
        penguinScriptingObject.addPotionEffect(new PotionEffect(Objects.requireNonNull(Potion.REGISTRY.getObject(new ResourceLocation(name))), time, modifier));
    }

    public void removeEffect(String name) {
        penguinScriptingObject.removePotionEffect(Objects.requireNonNull(Potion.REGISTRY.getObject(new ResourceLocation(name))));
    }

    public boolean isDamaged() {
        EntityLivingBase object = penguinScriptingObject;
        return object.getHealth() < object.getMaxHealth();
    }
}
