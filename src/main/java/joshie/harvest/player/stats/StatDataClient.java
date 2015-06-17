package joshie.harvest.player.stats;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class StatDataClient extends StatData {
    public void setGold(long gold) {
        this.gold = gold;
    }
}
