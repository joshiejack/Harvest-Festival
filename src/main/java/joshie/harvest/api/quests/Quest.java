package joshie.harvest.api.quests;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.knowledge.Note;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.town.Town;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class Quest extends IForgeRegistryEntry.Impl<Quest> {
    /** DO NOT MODIFY THE ENTRIES IN THE OLD_REGISTRY, ALWAYS MAKE A COPY OF THE QUESTS **/
    public static final IForgeRegistry<Quest> REGISTRY = new RegistryBuilder<Quest>().setName(new ResourceLocation("harvestfestival", "quests")).setType(Quest.class).setIDRange(0, 32000).create();
    private Set<NPC> npcs = new HashSet<>();
    protected ItemStack primary;
    protected int quest_stage;
    private TargetType type;

    public Quest() {
        this.type = TargetType.PLAYER;
    }

    /** Called when this quest is registered initially **/
    public void onRegistered() {}

    /** Return the priority of the quest,
     *  Ideally you should leave this as normal
     *  Except for quests such as festivals.
     *  This way festivals will take priority. */
    public EventPriority getPriority() {
        return EventPriority.NORMAL;
    }

    /** Returns a list of npcs that this quest lines uses */
    protected Set<NPC> getNPCs() {
        return npcs;
    }

    public final TargetType getQuestType() {
        return type;
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

    /** Real quests are quests that take over a npcs chat,
     *  as in there is no chance of them ever saying anything else other than the quest,
     *  @return false for quests like greetings, recipes, traders etc where there is a condition for chatting */
    public boolean isRealQuest() {
        return true;
    }

    /** If this quest counts as a daily quest, and if it can currently be started
     * @param town  the town trying to start the quest in
     * @param world the world
     * @param pos   the position
     *  @return false for quests that can only be started via the quest board at a random chance **/
    public boolean canStartDailyQuest(Town town, World world, BlockPos pos) {
        return false;
    }

    /** This is only called for daily quests, if they are repeatable
     *  This is how many days need to have passed before this quest can be repeated **/
    public int getDaysBetween() {
        return 0;
    }

    /** Call this to set the npcs that handle this quest
     * @param npcs    the npcs **/
    public Quest setNPCs(NPC... npcs) {
        primary = HFApi.npc.getStackForNPC(npcs[0]);
        Collections.addAll(this.npcs, npcs);
        return this;
    }

    /** Called to check if this npc is used
     *  @param npc the npc **/
    public boolean isNPCUsed(EntityPlayer player, NPCEntity npc) {
        return getNPCs().contains(npc.getNPC());
    }

    /** Set this quest as a town based quest**/
    protected final Quest setTownQuest() {
        this.type = TargetType.TOWN;
        return this;
    }

    /** Returns the stage of this quest **/
    public int getStage() {
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

    /** Syncs the data to the player from server to client **/
    public final void syncData(EntityPlayer player) {
        HFApi.quests.syncData(this, player);
    }

    /** Try not to ever call this internally, ALWAYS use increaseStage
     *  @param quest_stage the stage of the quests  **/
    public final Quest setStage(int quest_stage) {
        this.quest_stage = quest_stage;
        return this;
    }

    protected String getPrefix() {
        return getRegistryName().getResourceDomain() + ".quest." + getRegistryName().getResourcePath();
    }

    /** The name of this quest line **/
    @SuppressWarnings("deprecation")
    public String getTitle() {
        return I18n.translateToLocal(getPrefix() + ".title");
    }

    /** A very short description, of what the player is supposed to be doing at this stage
     *  If the player is null, then we are looking for the description that appears on the notice board, instead of in the book
     * @param world the world
     * @param player the player**/
    @SideOnly(Side.CLIENT)
    public String getDescription(World world, @Nullable EntityPlayer player) {
        return null;
    }

    /** Which npc the player is supposed to be interacting with at this point **/
    @SideOnly(Side.CLIENT)
    public ItemStack getCurrentIcon(World world, EntityPlayer player) {
        return primary;
    }

    /** Use this helper in conjunction with getLocalizedScript
     * @param quest short form name **/
    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    public String getLocalized(String quest, Object... format) {
        if (format.length == 0) return I18n.translateToLocal(getPrefix() + "." + quest.replace("_", ""));
        else return I18n.translateToLocalFormatted(getPrefix() + "." + quest.replace("_", ""), format);
    }

    /** Called when this quest is selected to be displayed/used by an npc
     *  Use this method to setup context data for the quest that you need access to **/
    public void onQuestSelectedForDisplay(EntityPlayer player, NPCEntity npc) {}

    /** Return the script, in a simple unlocalised form
     *  This will get run through getLocalized, this is
     *  so you can return shorter, simple strings by default
     * @param player    the player who is talking
     * @param npc       the npc they're interacting with
     * @return  the script*/
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, NPCEntity npc) {
        return null;
    }

    /** This is used when you want the quest to have options to select from
     *  You can return this based on the stage */
    @Nullable
    public Selection getSelection(EntityPlayer player, NPCEntity npc) {
        return null;
    }

    /** Called when chat closes
     *  @param player       the player
     *  @param npc       the npc**/
    public void onChatClosed(EntityPlayer player, NPCEntity npc, boolean wasSneaking) {}

    /** Called when the quest is completed
     *  @param player       the player that completed the quest **/
    public void onQuestCompleted(EntityPlayer player) {}

    /** Called to gather the notes this quest fulfills, if it has been completed **/
    public Set<Note> getNotes() {
        return new HashSet<>();
    }

    /** Call to complete a quest, only calls the serverside
     * @param player    the player to complete the quest for **/
    public final void complete(EntityPlayer player) {
        HFApi.quests.completeQuest(this, player);
    }

    /** Call this to reward the player with an item **/
    protected final void takeHeldStack(EntityPlayer player, int amount) {
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
    protected final void rewardEntity(EntityPlayer player, String entity) {
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
    public boolean onEntityInteract(EntityPlayer player, @Nullable ItemStack held, EnumHand hand, Entity target) { return false; }
    public boolean onRightClickBlock(EntityPlayer player, BlockPos pos, EnumFacing face) { return false; }
}