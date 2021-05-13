package uk.joshiejack.settlements.proxy;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import uk.joshiejack.settlements.client.gui.page.PageQuests;
import uk.joshiejack.settlements.client.gui.page.PageRelationships;
import uk.joshiejack.settlements.client.gui.page.PageTownManager;
import uk.joshiejack.settlements.client.renderer.entity.RenderNPC;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.penguinlib.client.gui.book.page.Page;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();

        //Register the gui pages
        RenderingRegistry.registerEntityRenderingHandler(EntityNPC.class, RenderNPC::new);
    }

    @Override
    public void postInit() {
        Page.REGISTRY.put("relationships", PageRelationships.INSTANCE);
        Page.REGISTRY.put("quests:all", PageQuests.INSTANCE);
        Page.REGISTRY.put("quests:player", PageQuests.PLAYER);
        Page.REGISTRY.put("quests:team", PageQuests.TEAM);
        Page.REGISTRY.put("quests:world", PageQuests.WORLD);
        Page.REGISTRY.put("town_manager", PageTownManager.INSTANCE);
    }
}
