package joshie.harvest.quests.player.schedule;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.quests.HFQuest;
import joshie.harvest.api.quests.Quest;
import joshie.harvest.buildings.HFBuildings;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.npcs.HFNPCs;
import joshie.harvest.quests.Quests;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.Set;

@HFQuest("schedule.katlin.baking")
public class QuestKatlinSundayBakingFreeCake extends Quest  {
    private final Meal[] meals;
    private CalendarDate date;

    public QuestKatlinSundayBakingFreeCake() {
        setNPCs(HFNPCs.CAFE_GRANNY);
        meals = new Meal[] { Meal.COOKIES, Meal.COOKIES_CHOCOLATE, Meal.PIE_APPLE, Meal.CAKE, Meal.CAKE_CHOCOLATE, Meal.DOUGHNUT, Meal.BUN_JAM };
    }

    @Override
    public boolean canStartQuest(Set<Quest> active, Set<Quest> finished) {
        return finished.contains(Quests.KATLIN_MEET) && finished.contains(Quests.TOMAS_MEET);
    }

    @Override
    public boolean isNPCUsed(EntityPlayer player, NPC npc) {
        if (npc != HFNPCs.CAFE_GRANNY || !TownHelper.getClosestTownToEntity(player, false).hasBuilding(HFBuildings.CHURCH)) return false;
        CalendarDate today = HFApi.calendar.getDate(player.worldObj);
        long daytime = CalendarHelper.getTime(player.worldObj);
        if (today.getWeekday() == Weekday.SUNDAY && daytime >= 7000L && daytime <= 17000L) {
            if (date == null || CalendarHelper.getDays(date, today) >= 7) {
                return true;
            }
        }

        return false;
    }

    @Nullable
    @SideOnly(Side.CLIENT)
    public String getLocalizedScript(EntityPlayer player, EntityLiving entity, NPC npc) {
        return getLocalized("cake");
    }

    private ItemStack getRandomBakedGoods(Random rand) {
        if (rand.nextInt(10) == 0) return new ItemStack(rand.nextBoolean() ? Items.COOKIE : Items.CAKE, 3);
        else return HFCooking.MEAL.getStackFromEnum(meals[rand.nextInt(meals.length)], 3);
    }

    @Override
    public void onChatClosed(EntityPlayer player, EntityLiving entity, NPC npc, boolean wasSneaking) {
        CalendarDate today = HFApi.calendar.getDate(player.worldObj);
        date = today.copy(); //Save the date we received this
        syncData(player); //Sync the new data
        rewardItem(player, getRandomBakedGoods(player.worldObj.rand));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("Date")) {
            date = CalendarDate.fromNBT(nbt.getCompoundTag("Date"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (date != null) {
            nbt.setTag("Date", date.toNBT());
        }

        return nbt;
    }
}
