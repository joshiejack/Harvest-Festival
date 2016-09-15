package joshie.harvest.api.quests;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.npc.INPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry.Impl;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Set;

public abstract class Quest extends Impl<Quest> {
    /** DO NOT MODIFY THE ENTRIES IN THE REGISTRY, ALWAYS MAKE A COPY OF THE QUESTS **/
    public static final IForgeRegistry<Quest> REGISTRY = new RegistryBuilder<Quest>().setName(new ResourceLocation("harvestfestival", "quests")).setType(Quest.class).setIDRange(0, 32000).create();
    public int quest_stage;
    private INPC[] npcs;

    public Quest() {}

    /** Returns a list of npcs that this quest lines uses */
    public final INPC[] getNPCs() {
        return npcs;
    }

    /** Called to check if this quest is able to be started, after all other checks are performed
     *
     * @param active    the quests the player currently has active
     * @param finished  the quests the player has completed currently
     * @return true if they can start this quest, false otherwise
     */
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return true;
    }

    /** Whether or not this quest is repeatable **/
    public boolean isRepeatable() {
        return false;
    }

    /** Quests that return true here, count towards the quest limit,
     *  You should return false for very simple quests, such as the default trader ones, or the blessing of tools
     *  Returning true also means a quest cannot be started if a npc is already being used in that quest line */
    public boolean isRealQuest() {
        return true;
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
     * Will only work when called on the SERVER side
     * Use this to increase the stage, best place to call it is in onChatClosed
     * @param player    the player we're increasing the stage for
     **/
    public final void increaseStage(EntityPlayer player) {
        HFApi.quests.increaseStage(this, player);
    }

    /** Try not to ever call this internally, ALWAYS use increaseStage
     *  @param quest_stage the stage of the quests  **/
    public final Quest setStage(int quest_stage) {
        this.quest_stage = quest_stage;
        return this;
    }

    /** Use this helper in conjunction with getLocalizedScript
     * @param quest short form name **/
    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public String getLocalized(String quest, Object... format) {
        if (format.length == 0) return I18n.translateToLocal(getRegistryName().getResourceDomain() + ".quest." + getRegistryName().getResourcePath() + "." + quest.replace("_", ""));
        else return I18n.translateToLocalFormatted(getRegistryName().getResourceDomain() + ".quest." + getRegistryName().getResourcePath() + "." + quest.replace("_", ""), format);
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
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, INPC npc) {
        return null;
    }

    /** This is used when you want the quest to have options to select from
     *  You can return this based on the stage */
    @Nullable
    public Selection getSelection(EntityPlayer player, INPC npc) {
        return null;
    }

    /** Called SERVER side only, when the stage changes
     *  You are free to manipulate data here, as it gets synced to the client
     * @param player        the player
     * @param previous      the previous stage
     * @param stage         the current stage */
    public void onStageChanged(EntityPlayer player, int previous, int stage) {}

    /** Called when chat opens
     *  @param player       the player
     *  @param entity       the npc entity
     *  @param npc          the npc instance**/
    public void onChatOpened(EntityPlayer player, EntityLiving entity, INPC npc) {}

    /** Called when chat closes
     *  @param player       the player
     *  @param entity       the npc entity
     *  @param npc          the npc instance**/
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc) {}

    /** Called when the quest is completed
     *  @param player       the player that completed the quest **/
    public void onQuestCompleted(EntityPlayer player) {}

    /** Call to complete a quest, only calls the serverside
     * @param player    the player to complete the quest for **/
    public final void complete(EntityPlayer player) {
        HFApi.quests.completeQuest(this, player);
    }

    /** Call this to reward the player with an item **/
    public final void takeHeldStack(EntityPlayer player, int amount) {
        player.inventory.decrStackSize(player.inventory.currentItem, amount);
    }

    /** Call this to reward the player with an item **/
    public final void rewardItem(EntityPlayer player, ItemStack stack) {
        HFApi.quests.rewardItem(this, player, stack);
    }

    /** Call this to reward the player with gold **/
    public final void rewardGold(EntityPlayer player, long gold) {
        HFApi.quests.rewardGold(player, gold);
    }

    /** Spawns an entity **/
    public final void rewardEntity(EntityPlayer player, String entity) {
        HFApi.quests.rewardEntity(this, player, entity);
    }

    /** Called to load data about this quest
     * @param nbt the nbt tag **/
    public void readFromNBT(NBTTagCompound nbt) {
        quest_stage = nbt.getShort("Stage");
    }

    /** Called to write data about this quest
     * @param nbt the nbt tag to write to
     * @return the nbt tag**/
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setShort("Stage", (short) quest_stage);
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
    //You need to return the events that get handled, so that they will get called
    public void onEntityInteract(EntityPlayer player, @Nullable ItemStack held, EnumHand hand, Entity target) {}
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
         *              return DEFAULT to do nothing */
        public abstract Result onSelected(EntityPlayer player, EntityLiving entity, INPC npc, @Nullable Q quest, int option);
    }
}