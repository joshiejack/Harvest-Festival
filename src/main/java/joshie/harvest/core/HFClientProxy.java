package joshie.harvest.core;

import joshie.harvest.core.render.FakeEntityRenderer.EntityItemRenderer;
import net.minecraft.item.Item;

import java.util.HashMap;

public class HFClientProxy extends HFCommonProxy {
    public static final HashMap<Item, EntityItemRenderer> RENDER_MAP = new HashMap<>();

    @Override
    public boolean isClient() {
        return true;
    }
}
