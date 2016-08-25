package joshie.harvest.api.quests;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class Quest extends Impl<Quest> {
    /** DO NOT MODIFY THE ENTRIES IN THE REGISTRY, ALWAYS MAKE A COPY OF THE QUESTS **/
    public static final FMLControlledNamespacedRegistry<Quest> REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation("harvestfestival", "quests"), Quest.class, null, 0, 32000, true, null, null, null);
    private Set<Quest> required = new HashSet<>();
    protected int quest_stage;
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

    /** Whether or not this quest is repeatable **/
    public boolean isRepeatable() {
        return false;
    }

    /** Quests that return true here, count towards the quest limit,
     *  You should return false for very simple quests, such as the default trader ones, or the blessing of tools */
    public boolean isRealQuest() {
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

    /** Returns the stage of this quest **/
    public final int getStage() {
        return quest_stage;
    }

    /**
     * ENSURE YOU ONLY EVER CALL THIS ON ONE SIDE
     * Use this to increase the stage
     * @param player    the player we're increasing the stage for
     **/
    public final void increaseStage(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            int previous = this.quest_stage;
            this.quest_stage++;
            onStageChanged(player, previous, quest_stage);
        }

        HFApi.player.getQuestHelper().increaseStage(this, player);
    }

    /** Try not to ever call this internally, ALWAYS use increaseStage
     *  @param quest_stage the stage of the quests  **/
    public final Quest setStage(int quest_stage) {
        this.quest_stage = quest_stage;
        return this;
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
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        return null;
    }

    /** This is used when you want the quest to have options to select from
     *  You can return this based on the stage */
    @Nullable
    public Selection getSelection() {
        return null;
    }

    /** Called SERVER side only, when the stage changes
     *  You are free to manipulate data here, as it gets synced to the client
     * @param player        the player
     * @param previous      the previous stage
     * @param stage         the current stage */
    public void onStageChanged(EntityPlayer player, int previous, int stage) {}

    /** Call to complete a quest, only call this on one side
     * @param player    the player to complete the quest for **/
    public final void complete(EntityPlayer player) {
        HFApi.player.getQuestHelper().completeQuest(this, player);
    }

    /** Called on the serverside when a quest is completed **/
    public void claim(EntityPlayer player) {}

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
    public void onEntityInteract(EntityPlayer player, Entity target) {}
    public void onClosedChat(EntityPlayer player, EntityLiving entity, INPC npc) { }
    public void onRightClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face) {}

    /** Used for selection menus **/
    public static abstract class Selection<Q extends Quest> {
        private String[] lines;

        public Selection(String title, String line1, String line2) {
            this.lines = new String[3];
            this.lines[0] = title;
            this.lines[1] = line1;
            this.lines[2] = line2;
        }

        public Selection(String title, String line1, String line2, String line3) {
            this.lines = new String[4];
            this.lines[0] = title;
            this.lines[1] = line1;
            this.lines[2] = line2;
            this.lines[3] = line3;
        }

        /** Returns the unlocalised text **/
        public final String[] getText() {
            return lines;
        }

        /** Called when these options are selected
         * @param player        the player interacting
         * @param entity        the entity the player is interacting with
         * @param npc           the npc associated with the entity
         * @param option        which option they selected (1/2/3)
         * @param quest         the quest object associated with this
         * @return return what happens next,
         *              return DENY to close the options menu
         *              return ALLOW to open the npc chat window
         *              return DEFAULT to do nothing*/
        public abstract Result onSelected(EntityPlayer player, EntityLiving entity, INPC npc, Q quest, int option);
    }
}