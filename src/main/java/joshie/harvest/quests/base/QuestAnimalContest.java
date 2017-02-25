package joshie.harvest.quests.base;

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
import joshie.harvest.quests.packet.PacketQuestUpdateAnimals;
import joshie.harvest.quests.town.festivals.Place;
import joshie.harvest.quests.town.festivals.contest.ContestAnimalSelection;
import joshie.harvest.quests.town.festivals.contest.ContestAnimalStartMenu;
import joshie.harvest.quests.town.festivals.contest.ContestEntries;
import joshie.harvest.quests.town.festivals.contest.ContestInfoMenu;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public abstract class QuestAnimalContest<E extends EntityAnimal> extends QuestFestival {
    protected final ContestEntries<E> entries;
    public static final int QUESTION = 0;
    public static final int EXPLAIN = 1;
    public static final int START = 2;
    public static final int CONTINUE = 3;
    private final Selection selection;
    private final Selection animalSelection;
    private final Selection startSelection;
    private long time;

    public QuestAnimalContest(NPC npc, ContestEntries<E> entries, String prefix) {
        this.setNPCs(npc);
        this.entries = entries;
        this.selection = new ContestInfoMenu(prefix);
        this.animalSelection = new ContestAnimalSelection<E>(prefix);
        this.startSelection = new ContestAnimalStartMenu(prefix);
    }

    public ContestEntries<E> getEntries() {
        return entries;
    }

    protected TaskMove getMove(Town town, BuildingLocation location) {
        return TaskMove.of(town.getCoordinatesFor(location));
    }

    public abstract ItemStack getReward(Place place);

    @Override
    public void onQuestSelectedForDisplay(EntityPlayer player, NPCEntity npc) {
        time = CalendarHelper.getTime(player.worldObj);
        if (!player.worldObj.isRemote) { //Sync up the animal names and stuff
            entries.setAnimalNames(PacketQuestUpdateAnimals.getNamesFromEntities(entries.getAvailableEntries(player)));
            PacketHandler.sendToClient(new PacketQuestUpdateAnimals(getEntries().getNames()), player);
        }
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
                if (entries.getNames().size() > 0) return animalSelection;
                else return null;
            }

            if (isCorrectTime(time) && !entries.isEntered(player)) return selection;
            if (entries.isEntered(player)) return startSelection;
        }

        return null;
    }

    public void targetEntries(EntityPlayer player, NPCEntity entity) {
        Town town = entity.getTown();
        List<EntityNPCHuman> npcs = EntityHelper.getEntities(EntityNPCHuman.class, player.worldObj, entity.getPos(), 64D, 10D);
        for (EntityNPCHuman theNPC: npcs) {
            BuildingLocation building = entries.getLocationFromNPC(theNPC.getNPC());
            if (building != null) {
                entity.setPath(TaskMove.of(town.getCoordinatesFor(building)), TaskWait.of(30));
            }
        }
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

    public abstract void execute(Town town, EntityPlayer player, NPCEntity pathing);

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
