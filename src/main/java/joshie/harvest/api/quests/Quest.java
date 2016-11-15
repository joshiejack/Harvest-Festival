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
import net.minecraft.world.World;
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
    /** DO NOT MODIFY THE ENTRIES IN THE REGISTRY, ALWAYS MAKE A COPY OF THE QUESTS **/
    public static final IForgeRegistry<Quest> REGISTRY = new RegistryBuilder<Quest>().setName(new ResourceLocation("harvestfestival", "quests")).setType(Quest.class).setIDRange(0, 32000).create();
    private Set<INPC> npcs = new HashSet<>();
    private ItemStack primary;
    protected int quest_stage;
    private QuestType type;

    public Quest() {
        this.type = QuestType.PLAYER;
    }

    /** Called when this quest is registered initially **/
    public void onRegistered() {}

    /** Returns a list of npcs that this quest lines uses */
    public Set<INPC> getNPCs() {
        return npcs;
    }

    public final QuestType getQuestType() {
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

    /** Call this to set the npcs that handle this quest
     * @param npcs    the npcs **/
    public Quest setNPCs(INPC... npcs) {
        primary = HFApi.npc.getStackForNPC(npcs[0]);
        Collections.addAll(this.npcs, npcs);
        return this;
    }

    /** Set this quest as a town based quest**/
    protected final Quest setTownQuest() {
        this.type = QuestType.TOWN;
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
    public String getTitle() {
        return I18n.translateToLocal(getPrefix() + ".title");
    }

    /** A very short description, of what the player is supposed to be doing at this stage
     * @param world the world
     * @param player the player**/
    @SideOnly(Side.CLIENT)
    public String getDescription(World world, EntityPlayer player) {
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

    /** Called when chat closes
     *  @param player       the player
     *  @param entity       the npc entity
     *  @param npc          the npc instance**/
    @SuppressWarnings("deprecated")
    public void onChatClosed(EntityPlayer player, EntityLiving entity, INPC npc, boolean wasSneaking) {
        onChatClosed(player, entity, npc);
    }

    @Deprecated //TODO: Remove in 0.7+
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
}