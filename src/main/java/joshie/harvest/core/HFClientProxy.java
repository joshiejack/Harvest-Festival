package joshie.harvest.core;

import com.cricketcraft.ctmlib.CTMRenderer;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class HFClientProxy extends HFCommonProxy {
	
    @Override
    public boolean isClient() {
        return true;
    }
}
