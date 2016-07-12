package joshie.harvest.quests;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import joshie.harvest.npc.entity.AbstractEntityNPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.Set;

public abstract class Quest extends Impl<Quest> {
    /** DO NOT MODIFY THE ENTRIES IN THE REGISTRY, ALWAYS MAKE A COPY OF THE QUESTS **/
    public static final FMLControlledNamespacedRegistry<Quest> REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation("harvestfestival", "quests"), Quest.class, null, 0, 32000, true, null, null, null);
    protected int quest_stage;
    private Set<Quest> required;
    private INPC[] npcs;

    public Quest() {}

    /** Returns a set of quests that are required to start this one
     * @return set of required quests **/
    public final Set<Quest> getRequired() {
        return required;
    }

    /** Returns a list of npcs that this quest lines uses */
    public final INPC[] getNPCs() {
        return npcs;
    }

    /** Called to check if this quest is able to be started, after all other checks are performed
     *
     * @param player    the player we are checking for
     * @param active    the quests the player currently has active
     * @param finished  the quests the player has completed currently
     * @return true if they can start this quest, false otherwise
     */
    public boolean canStartQuest(EntityPlayer player, Set<Quest> active, Set<Quest> finished) {
        return true;
    }

    /** Call this to add quests as a requirement for your existing quests
     * @param quests    quests to add as a requirement**/
    public final Quest addRequirement(Quest... quests) {
        Collections.addAll(required, quests);
        return this;
    }

    /** Call this to set the npcs that handle this quest
     * @param npcs    the npcs **/
    public final Quest setNPCs(INPC... npcs) {
        this.npcs = npcs;
        return this;
    }

    public int getStage() {
        return quest_stage;
    }

    /**
     * ENSURE YOU ONLY EVER CALL THIS ON ONE SIDE
     * Use this to increase the stage
     * @param player    the player we're increasing the stage for
     **/
    protected final void increaseStage(EntityPlayer player) {
        int previous = this.quest_stage;
        this.quest_stage++;
        HFApi.player.syncQuest(this, player);
        onStageChanged(player, previous, quest_stage);
    }

    /** Try not to ever call this internally, ALWAYS use increaseStage
     *  @param quest_stage the stage of the quests  **/
    public final Quest setStage(int quest_stage) {
        this.quest_stage = quest_stage;
        return this;
    }

    public boolean isRepeatable() {
        return false;
    }

    /** Return the localised name of the result of getScript
     * @param quest short form name **/
    @SideOnly(Side.CLIENT)
    public String getLocalized(String quest) {
        return I18n.translateToLocal(getRegistryName().getResourceDomain() + ".quest." + getRegistryName().getResourcePath() + "." + quest.replace("_", ""));
    }

    /** Return the script, in a simple unlocalised form
     *  This will get run through getLocalized, this is
     *  so you can return shorter, simple strings by default
     * @param player    the player who is talking
     * @param entity    the entity for the npc they're interacting with
     * @param npc       the npc type they're interacting with
     * @return  the script*/
    @SideOnly(Side.CLIENT)
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        return null;
    }

    //Called Serverside, to claim the reward
    public void claim(EntityPlayer player) {
        return;
    }

    /** This is used when you want the quest to have options to select from
     *  You can return different values based on stage
     *  You can have a maximimum of 3 options**/
    public int getOptions() {
        return 0;
    }

    public void onSelected(EntityPlayer player, int option) {}

    /** Called to load data about this quest
     * @param nbt the nbt tag **/
    public void readFromNBT(NBTTagCompound nbt) {
        quest_stage = nbt.getShort("Completed");
    }

    /** Called to write data about this quest
     * @param nbt the nbt tag to write to
     * @return the nbt tag**/
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setShort("Completed", (short) quest_stage);
        return nbt;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Quest && getRegistryName() != null && getRegistryName().equals(((Quest) o).getRegistryName());
    }

    @Override
    public int hashCode() {
        return getRegistryName() == null? 0 : getRegistryName().hashCode();
    }

    /****
     * EVENTS - Called automatically from vanilla events or npc specific ones
     ****/
    //Called When a player interacts with an animal
    public void onEntityInteract(EntityPlayer player, Entity target) {}
    //Called serverside when you close the chat with an npc
    public void onClosedChat(EntityPlayer player, AbstractEntityNPC npc) { }
    public void onRightClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face) { }
    public void select(EntityPlayer player, AbstractEntityNPC npc, int option) {}
    public void confirm(EntityPlayer player, AbstractEntityNPC npc) {}
    public void cancel(EntityPlayer player, AbstractEntityNPC npc) {}
    public void onStageChanged(EntityPlayer player, int previous, int stage) { }
}