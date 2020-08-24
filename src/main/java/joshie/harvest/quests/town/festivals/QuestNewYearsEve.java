package joshie.harvest.quests.town.festivals;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.NPCEntity;
import joshie.harvest.api.npc.RelationStatus;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.api.npc.task.HFTask;
import joshie.harvest.api.npc.task.TaskElement;
import joshie.harvest.api.npc.task.TaskSpeech;
import joshie.harvest.api.npc.task.TaskWait;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.api.town.Town;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.data.CalendarServer;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.base.QuestFestival;
import joshie.harvest.quests.town.festivals.contest.cooking.TaskEat;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static joshie.harvest.api.HFApi.calendar;
import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFQuest("festival.new.years.eve")
public class QuestNewYearsEve extends QuestFestival {
    private static final Script scriptIntro = new Script(new ResourceLocation(MODID, "new_years_eve_intro"));
    private static final Script scriptCountdown5 = new Script(new ResourceLocation(MODID, "new_years_eve_5"));
    private static final Script scriptCountdown4 = new Script(new ResourceLocation(MODID, "new_years_eve_4"));
    private static final Script scriptCountdown3 = new Script(new ResourceLocation(MODID, "new_years_eve_3"));
    private static final Script scriptCountdown2 = new Script(new ResourceLocation(MODID, "new_years_eve_2"));
    private static final Script scriptCountdown1 = new Script(new ResourceLocation(MODID, "new_years_eve_1"));
    private static final Script scriptCountdown0 = new Script(new ResourceLocation(MODID, "new_years_eve_0"));
    private final Selection start = new Selection("harvestfestival.quest.festival.new.years.eve.question",
            "harvestfestival.quest.festival.new.years.eve.option1",
            "harvestfestival.quest.festival.new.years.eve.option2") {
        @Override
        public Result onSelected(EntityPlayer player, NPCEntity entity, @Nullable Quest quest, int option) {
            if (option == 1) {
                //Eat food before starting the new year countdown!
                Town town = entity.getTown();
                entity.setPath(new TaskEat(town, new BlockPos(13, 2, 16)), new TaskEat(town, new BlockPos(13, 2, 14)), new TaskEat(town, new BlockPos(13, 2, 12)), TaskSpeech.of(scriptIntro), TaskWait.of(1), TaskSpeech.of(scriptCountdown5), TaskWait.of(1), TaskSpeech.of(scriptCountdown4),
                        TaskWait.of(1), TaskSpeech.of(scriptCountdown3), TaskWait.of(1), TaskSpeech.of(scriptCountdown2), TaskWait.of(1),
                        TaskSpeech.of(scriptCountdown1), TaskWait.of(1), new TaskNewYear(), TaskSpeech.of(scriptCountdown0));
            }

            return Result.DENY;
        }
    };

    public QuestNewYearsEve() {
        setNPCs(HFNPCs.CARPENTER);
    }

    private long time;

    @Override
    public void onQuestSelectedForDisplay(EntityPlayer player, NPCEntity entity) {
        time = CalendarHelper.getTime(player.world);
    }

    private boolean isCorrectTime() {
        return time < 6000 || (time >= 18000L && time <= 24000L);
    }

    @Override
    @Nullable
    public Selection getSelection(EntityPlayer player, NPCEntity entity) {
        return isCorrectTime() ? start : null;
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, NPCEntity entity) {
        CalendarDate date = calendar.getDate(player.world);
        if (!isCorrectTime() || date.getSeason() != Season.WINTER) return null;
        return getLocalized("start");
    }

    @HFTask("new_year")
    public static class TaskNewYear extends TaskElement {
        @Override
        public void execute(NPCEntity entity) {
            EntityLiving npc = entity.getAsEntity();
            //Spawn the fireworks first
            startFireworksShow(npc, entity.getTown());

            //Set the world time to one tick before the new year!
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            CalendarServer calendar = HFTrackers.getCalendar(npc.world);
            CalendarHelper.setWorldTime(server, CalendarHelper.getTime(0, Season.SPRING, calendar.getDate().getYear() + 1) - 1);

            //Add 500RP to all the npcs for any players that are in range
            for (EntityPlayer player : EntityHelper.getEntities(EntityPlayer.class, npc.world, new BlockPos(npc), 64, 64)) {
                NPC.REGISTRY.values().stream().filter(aNPC -> HFApi.player.getRelationsForPlayer(player).isStatusMet(aNPC, RelationStatus.MET))
                        .forEach(aNPC -> HFApi.player.getRelationsForPlayer(player).affectRelationship(aNPC, 500));
            }

            super.execute(entity); //Mark this as satisfied
        }

        public static void startFireworksShow(EntityLiving npc, Town town) {
            int[] colors = {1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};
            BlockPos[] locations = new BlockPos[]{new BlockPos(8, 1, 11), new BlockPos(8, 1, 15), new BlockPos(8, 1, 19), new BlockPos(8, 1, 23)};

            summonFireworks(npc, town, locations[0], 45, 0, 0, 1, "2437522,4312372", "2437522");
            summonFireworks(npc, town, locations[1], 50, 1, 0, 1, "11743532,14602026", "11743532");
            summonFireworks(npc, town, locations[2], 50, 1, 0, 1, "11743532,14602026", "11743532");
            summonFireworks(npc, town, locations[3], 45, 0, 0, 1, "2437522,4312372", "2437522");
            if (stop(5)) {
                for (int color : colors) {
                    summonFireworks(npc, town, locations[0], 40, 1, 0, 1, String.valueOf(color), "");
                    summonFireworks(npc, town, locations[1], 40, 1, 0, 1, String.valueOf(color + "," + color), "");
                    summonFireworks(npc, town, locations[2], 40, 1, 0, 1, String.valueOf(color + "," + color), "");
                    summonFireworks(npc, town, locations[3], 40, 1, 0, 1, String.valueOf(color), "");
                }
            }
        }

        private static void summonFireworks(EntityLiving npc, Town town, BlockPos pos, int lifetime, int type, int flicker, int trail, String colors, String fadeColor) {
            pos = town.getCoordinatesFromOffset(HFBuildings.FESTIVAL_GROUNDS, pos);
            FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(npc, "/summon FireworksRocketEntity "  + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " {LifeTime:" + lifetime + ",FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:" + type + ",Flicker:" + flicker + ",Trail:" + trail + ",Colors:[" + colors + "],FadeColors:[" + fadeColor + "]}]}}}}");
        }

        static boolean stop(int seconds) {
            int timer = 20 * seconds;
            timer--;
            return timer == 0;
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            return tag;
        }
    }
}