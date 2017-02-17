package joshie.harvest.quests.town.festivals;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.RelationStatus;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.api.npc.schedule.ScheduleElement;
import joshie.harvest.api.npc.schedule.ScheduleSpeech;
import joshie.harvest.api.npc.schedule.ScheduleWait;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.api.quests.Selection;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.data.CalendarServer;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.StackHelper;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.npcs.entity.EntityNPCHuman;
import joshie.harvest.npcs.entity.ai.EntityAIPathing;
import joshie.harvest.quests.base.QuestFestival;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

import static joshie.harvest.api.HFApi.calendar;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static minetweaker.mc1102.MineTweakerMod.server;

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
        public boolean isSelected() {
            return false;
        }

        @Override
        public Result onSelected(EntityPlayer player, EntityLiving entity, NPC npc, @Nullable Quest quest, int option) {
            if (option == 1) {
                EntityAIPathing pathing = ((EntityNPCHuman)entity).getPathing();
                pathing.setPath(ScheduleSpeech.of(scriptIntro), ScheduleWait.of(1), ScheduleSpeech.of(scriptCountdown5), ScheduleWait.of(1), ScheduleSpeech.of(scriptCountdown4),
                                ScheduleWait.of(1), ScheduleSpeech.of(scriptCountdown3),  ScheduleWait.of(1), ScheduleSpeech.of(scriptCountdown2), ScheduleWait.of(1),
                                ScheduleSpeech.of(scriptCountdown1), ScheduleWait.of(1), new ScheduleNewYear(), ScheduleSpeech.of(scriptCountdown0));
            }

            return Result.DENY;
        }
    };

    public QuestNewYearsEve() {
        setNPCs(HFNPCs.CARPENTER);
    }

    private long time;

    @Override
    public void onQuestSelectedForDisplay(EntityPlayer player, EntityLiving entity, NPC npc) {
        this.time = CalendarHelper.getTime(player.worldObj);
    }

    private boolean isCorrectTime() {
        return time < 6000 || (time >= 18000L && time <= 24000L);
    }

    @Override
    @Nullable
    public Selection getSelection(EntityPlayer player, NPC npc) {
        return isCorrectTime() ? start : null;
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        CalendarDate date = calendar.getDate(player.worldObj);
        if (!isCorrectTime() || date.getSeason() != Season.WINTER) return null;
        return getLocalized("start");
    }

    private static class ScheduleNewYear extends ScheduleElement {
        private static final String tag = "{LifeTime:30,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:%s,Flicker:%s,Trail:%s,Colors:[%s],FadeColors:[%s]}]}}}}";
        @SuppressWarnings("ConstantConditions")
        private static ItemStack getRandomFireworks(Random rand) {
            ItemStack stack = new ItemStack(Items.FIREWORKS);
            stack.setTagCompound(StackHelper.getTag("{LifeTime:60,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:1,Flicker:0,Trail:1,Colors:[11743532,14602026],FadeColors:[]}]}}}}"));
            return stack;
        }

        @Override
        public void execute(EntityAgeable npc) {
            //Spawn the fireworks first
            for (int x = -10; x <= 10; x++) {
                for (int z = -10 ; z <= 10; z++) {
                    if (npc.worldObj.rand.nextBoolean()) {
                        npc.worldObj.spawnEntityInWorld(new EntityFireworkRocket(npc.worldObj, npc.posX + x, npc.posY + npc.worldObj.rand.nextInt(15), npc.posZ + z, getRandomFireworks(npc.worldObj.rand)));
                    }
                }
            }

            FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(npc, "/summon fireworks_rocket ~ ~ ~ {LifeTime:60,FireworksItem:{id:fireworks,Count:1,tag:{Fireworks:{Explosions:[{Type:1,Flicker:0,Trail:1,Colors:[11743532,14602026],FadeColors:[]}]}}}}");
            //Set the world time to one tick before the new year!
            CalendarServer calendar = HFTrackers.getCalendar(npc.worldObj);
            CalendarHelper.setWorldTime(server, CalendarHelper.getTime(0, Season.SPRING, calendar.getDate().getYear() + 1) - 1);

            //Add 500RP to all the npcs for any players that are in range
            for (EntityPlayer player: EntityHelper.getEntities(EntityPlayer.class, npc.worldObj, new BlockPos(npc), 64, 64)) {
                for (NPC aNPC: NPC.REGISTRY) {
                    if (HFApi.player.getRelationsForPlayer(player).isStatusMet(aNPC, RelationStatus.MET)) {
                        HFApi.player.getRelationsForPlayer(player).affectRelationship(aNPC, 500);
                    }
                }
            }

            super.execute(npc); //Mark this as satisfied
        }
    }
}
