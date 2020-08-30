package joshie.harvest.core.advancements;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class EventTrigger implements ICriterionTrigger<EventTrigger.Instance>
{
    public static final EventTrigger INSTANCE = new EventTrigger();
    private static final ResourceLocation ID = new ResourceLocation("harvestfestival", "event");
    private final Map<PlayerAdvancements, EventTrigger.Listeners> listeners = Maps.newHashMap();

    @Override
    public ResourceLocation getId()
    {
        return ID;
    }

    @Override
    public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<EventTrigger.Instance> listener)
    {
        EventTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);

        if (listeners == null)
        {
            listeners = new EventTrigger.Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, listeners);
        }

        listeners.add(listener);
    }

    @Override
    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<EventTrigger.Instance> listener)
    {
        EventTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);

        if (listeners != null)
        {
            listeners.remove(listener);

            if (listeners.isEmpty())
            {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

    @Override
    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.listeners.remove(playerAdvancementsIn);
    }

    /**
     * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
     */
    @Override
    public EventTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        String event = json.get("name").getAsString();
        return new EventTrigger.Instance(event);
    }

    public void trigger(EntityPlayerMP player, String event)
    {
        EventTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());

        if (listeners != null)
        {
            listeners.trigger(player, event);
        }
    }

    public static class Instance extends AbstractCriterionInstance
    {
        private final String event;

        public Instance(String event)
        {
            super(EventTrigger.ID);
            this.event = event;
        }

        public boolean test(String event)
        {
            return this.event.equals(event);
        }
    }

    static class Listeners
    {
        private final PlayerAdvancements playerAdvancements;
        private final Set<ICriterionTrigger.Listener<EventTrigger.Instance>> listeners = Sets.<ICriterionTrigger.Listener<EventTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn)
        {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty()
        {
            return this.listeners.isEmpty();
        }

        public void add(ICriterionTrigger.Listener<EventTrigger.Instance> listener)
        {
            this.listeners.add(listener);
        }

        public void remove(ICriterionTrigger.Listener<EventTrigger.Instance> listener)
        {
            this.listeners.remove(listener);
        }

        public void trigger(EntityPlayerMP player, String event)
        {
            List<ICriterionTrigger.Listener<EventTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<EventTrigger.Instance> listener : this.listeners)
            {
                if (listener.getCriterionInstance().test(event))
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<EventTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<EventTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.playerAdvancements);
                }
            }
        }
    }
}
