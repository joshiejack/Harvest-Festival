package joshie.harvest.player.stats;

import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class StatDataClient extends StatData {
    public void setGold(long gold) {
        this.gold = gold;
    }
}
