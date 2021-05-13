package uk.joshiejack.settlements.scripting.wrappers;

import com.google.common.collect.Lists;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.AdventureDataLoader;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.scripting.wrappers.EntityLivingJS;
import uk.joshiejack.penguinlib.scripting.wrappers.ItemStackJS;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class EntityNPCJS extends EntityLivingJS<EntityNPC> {
    private final NPCTaskJS tasks;

    public EntityNPCJS(EntityNPC npcEntity) {
        super(npcEntity);
        this.tasks = new NPCTaskJS(Lists.newLinkedList(), Lists.newLinkedList(), this);
    }

    public String substring(String name) {
        return penguinScriptingObject.substring(name);
    }

    public void setHeldItem(EnumHand hand, ItemStackJS stack) {
        penguinScriptingObject.setHeldItem(hand, stack.penguinScriptingObject);
    }

    public void chat(ResourceLocation quest, PlayerJS playerW) {
        EntityNPC object = penguinScriptingObject;
        Scripting.IGNORE.add(quest); //Skip to another quest
        object.getMentalAI().updateTask(); //Clear out any leftover actions
        EntityPlayer player = playerW.penguinScriptingObject;
        object.processInteract(player, player.getActiveHand());
    }

    public boolean hasAction(String actionType) {
        return penguinScriptingObject.getPhysicalAI().all().stream().anyMatch(t -> t.getType().equals(actionType));
    }

    public boolean hasAction(String actionType, String... data) {
        return penguinScriptingObject.getPhysicalAI().all().stream().filter(t -> t.getType().equals(actionType)).anyMatch(a -> a.has(data));
    }

    public AbstractTownJS<?> town() {
        return WrapperRegistry.wrap(AdventureDataLoader.get(penguinScriptingObject.world).getTownByID(penguinScriptingObject.world.provider.getDimension(), penguinScriptingObject.getTown()));
    }

    @Override
    public boolean is(String string) {
        //boolean bool = string.contains(":") && string.toLowerCase(Locale.ENGLISH).equals(penguinScriptingObject.getCachedUniqueIdString().toLowerCase(Locale.ENGLISH));
        if (penguinScriptingObject.getBaseNPC().getRegistryName().equals(new ResourceLocation(Settlements.MODID, "custom"))) {
            return false;
        } else return string.contains(":") && string.toLowerCase(Locale.ENGLISH).equals(penguinScriptingObject.getBaseNPC().getRegistryName().toString().toLowerCase(Locale.ENGLISH));
    }

    public NPCStatusJS status() {
        return WrapperRegistry.wrap(penguinScriptingObject.getInfo().getRegistryName());
    }

    public NPCTaskJS tasks(ResourceLocation quest, PlayerJS player) {
        return tasks.start(quest, player);
    }

    public int data(String name) {
        return penguinScriptingObject.getInfo().getData(name);
    }
}
