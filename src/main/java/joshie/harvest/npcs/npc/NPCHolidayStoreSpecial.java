package joshie.harvest.npcs.npc;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.npc.INPCHelper.Age;
import joshie.harvest.api.npc.INPCHelper.Gender;
import joshie.harvest.api.shops.Shop;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class NPCHolidayStoreSpecial extends NPCSpecialSeller {
    private Shop shop;
    private Festival festival;
    public NPCHolidayStoreSpecial(ResourceLocation resource, Gender gender, Age age, CalendarDate birthday, int insideColor, int outsideColor) {
        super(resource, gender, age, birthday, insideColor, outsideColor);
    }

    @Override
    public Shop getShop(World world, BlockPos pos, @Nonnull EntityPlayer player) {
        Festival festival = HFApi.calendar.getFestival(world, pos);
        if (festival.equals(this.festival)) return shop;
        else return super.getShop(world, pos, player);
    }

    public NPCHolidayStoreSpecial setHolidayShop(Festival festival, Shop shop) {
        this.festival = festival;
        this.shop = shop;
        return this;
    }
}
