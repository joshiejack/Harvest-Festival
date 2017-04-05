package joshie.harvest.npcs.npc;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.npc.INPCHelper.Age;
import joshie.harvest.api.npc.INPCHelper.Gender;
import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.shops.Shop;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class NPCHolidayStore extends NPC {
    private final Map<Festival, Shop> shops = new HashMap<>();

    public NPCHolidayStore(ResourceLocation resource, Gender gender, Age age, CalendarDate birthday, int insideColor, int outsideColor) {
        super(resource, gender, age, birthday, insideColor, outsideColor);
    }

    @Override
    public Shop getShop(World world, BlockPos pos, @Nullable EntityPlayer player) {
        Festival festival = player != null ? TownHelper.getClosestTownToEntity(player, false).getFestival() : TownHelper.getClosestTownToBlockPos(world, pos, false).getFestival();
        if (shops.containsKey(festival)) return shops.get(festival);
        else return super.getShop(world, pos, player);
    }

    public NPCHolidayStore addHolidayShop(Festival festival, Shop shop) {
        shops.put(festival, shop);
        return this;
    }
}
