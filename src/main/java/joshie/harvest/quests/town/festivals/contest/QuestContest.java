package joshie.harvest.quests.town.festivals.contest;

import joshie.harvest.api.buildings.BuildingLocation;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.task.TaskMove;
import joshie.harvest.api.npc.task.TaskWait;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.api.town.Town;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.npcs.entity.EntityNPCHuman;
import joshie.harvest.quests.base.QuestFestival;
import joshie.harvest.quests.packet.PacketQuestUpdateNames;
import joshie.harvest.quests.town.festivals.Place;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public abstract class QuestContest<E extends ContestEntries> extends QuestFestival {
    protected static final int QUESTION = 0;
    protected static final int EXPLAIN = 1;
    protected static final int START = 2;
    protected static final int CONTINUE = 3;
    private final Selection selection;
    private final Selection entrySelection;
    private final Selection startSelection;
    protected final E entries = createEntries();
    protected long time;

    public QuestContest(NPC npc, String prefix) {
        this.selection = new ContestInfoMenu(prefix);
        this.entrySelection = new ContestEntrySelection<E>(prefix);
        this.startSelection = new ContestStartMenu(prefix);
        this.setNPCs(npc);
    }

    protected abstract E createEntries();

    @Override
    @SuppressWarnings("unchecked")
    public void onQuestSelectedForDisplay(EntityPlayer player, NPCEntity npc) {
        time = CalendarHelper.getTime(player.worldObj);
        if (!player.worldObj.isRemote) { //Sync up the animal names and stuff
            entries.setEntryNames(entries.getAvailableEntryNames(player));
            PacketHandler.sendToClient(new PacketQuestUpdateNames(getEntries().getNames()), player);
        }
    }

    protected TaskMove getMove(Town town, BuildingLocation location) {
        return TaskMove.of(town.getCoordinatesFor(location));
    }

    void targetEntries(EntityPlayer player, NPCEntity entity) {
        Town town = entity.getTown();
        List<EntityNPCHuman> npcs = EntityHelper.getEntities(EntityNPCHuman.class, player.worldObj, entity.getPos(), 64D, 10D);
        for (EntityNPCHuman theNPC: npcs) {
            BuildingLocation building = entries.getLocationFromNPC(theNPC.getNPC());
            if (building != null) {
                entity.setPath(TaskMove.of(town.getCoordinatesFor(building)), TaskWait.of(30));
            }
        }
    }

    public E getEntries() {
        return entries;
    }

    @SuppressWarnings("WeakerAccess")
    protected boolean isCorrectTime(long time) {
        return time >= 6000L && time <= 18000L;
    }

    @Override
    public Selection getSelection(EntityPlayer player, NPCEntity entity) {
        if (isCorrectTime(time)) {
            if (quest_stage == EXPLAIN || quest_stage == START || quest_stage == CONTINUE) return null;
            if (entries.isSelecting(player)) {
                if (entries.getNames().size() > 0) return entrySelection;
                else return null;
            }

            if (isCorrectTime(time) && !entries.isEntered(player)) return selection;
            if (entries.isEntered(player)) return startSelection;
        }

        return null;
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, NPCEntity entity) {
        if (quest_stage == START) return getLocalized("selected");
        if (quest_stage == CONTINUE) return getLocalized("judging");
        if (isCorrectTime(time)) {
            if (quest_stage == EXPLAIN) return getLocalized("explain");
            if (entries.isSelecting(player)) {
                return entries.getNames().size() > 0 ? getLocalized("select") : getLocalized("none");
            }

            if (!entries.isEntered(player)) return getLocalized("help");
            if (entries.isEntered(player)) return getLocalized("start");
        }

        return player.worldObj.rand.nextInt(4) == 0 ? getLocalized("wait") : null;
    }

    @Override
    public void onChatClosed(EntityPlayer player, NPCEntity entity, boolean wasSneaking) {
        if (quest_stage == EXPLAIN) quest_stage = QUESTION;
        if (quest_stage == START) increaseStage(player);
        if (entries.isSelecting(player) && entries.getNames().size() == 0) {
            entries.getSelecting().remove(EntityHelper.getPlayerUUID(player)); //Remove the player
        }
    }

    public void reward(World world, Place place) {
        entries.getEntry(place).reward(world, place, getEntries().getNPCs(), getReward(place));
    }

    public abstract ItemStack getReward(Place place);
    public abstract void execute(Town town, EntityPlayer player, NPCEntity entity);

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        entries.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        entries.writeToNBT(nbt);
        return nbt;
    }
}
