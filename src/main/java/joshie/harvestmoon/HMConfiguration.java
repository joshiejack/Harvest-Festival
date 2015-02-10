package joshie.harvestmoon;

import java.io.File;

import joshie.harvestmoon.config.Client;
import joshie.harvestmoon.config.Cooking;
import joshie.harvestmoon.config.Tools;
import net.minecraftforge.common.config.Configuration;

public class HMConfiguration {
    public static final int PACKET_DISTANCE = 172;

    public static void init() {
        Client.init(new Configuration(new File(HarvestMoon.root + File.separator + "client.cfg")));
        Cooking.init(new Configuration(new File(HarvestMoon.root + File.separator + "cooking.cfg")));
        Tools.init(new Configuration(new File(HarvestMoon.root + File.separator + "tools.cfg")));
    }
}
