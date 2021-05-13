package uk.joshiejack.settlements.data.database;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.client.gui.NPCButtons;
import uk.joshiejack.settlements.command.CommandNPCGenerator;
import uk.joshiejack.settlements.item.ItemRandomNPC;
import uk.joshiejack.settlements.npcs.Age;
import uk.joshiejack.settlements.npcs.NPC;
import uk.joshiejack.settlements.npcs.status.Status;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;

import java.util.Locale;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Settlements.MODID)
public class NPCLoader {
    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        try {
            event.table("npc_classes").rows().forEach(npc_class -> {
                String name = npc_class.name();
                Age age = npc_class.isTrue("is child") ? Age.CHILD : Age.ADULT;
                boolean has_small_arms = npc_class.isTrue("has small arms");
                float height = npc_class.getAsFloat("render height");
                float offset = npc_class.getAsFloat("render offset");
                boolean hideHearts = npc_class.isTrue("hide hearts");
                boolean invulnerable = npc_class.isTrue("is invulnerable");
                boolean immovable = npc_class.isTrue("is immovable");
                boolean underwater = npc_class.isTrue("can breathe underwater");
                boolean floats = npc_class.isTrue("floats out of water");
                boolean invitable = npc_class.isTrue("is invitable");
                int lifespan = npc_class.get("lifespan");
                NPCClass.REGISTRY.put(name, new NPCClass(age, has_small_arms, height, offset, invulnerable, immovable, underwater, floats, invitable, lifespan, hideHearts));
            });

            event.table("npcs").rows().forEach(npc -> {
                String name = npc.name();
                ResourceLocation registryName = name.contains(":") ? new ResourceLocation(name) : new ResourceLocation(Settlements.MODID, name.toLowerCase(Locale.ENGLISH));
                NPC theNPC = new NPC(registryName).setOccupation(npc.get("occupation")).setNPCClass(NPCClass.REGISTRY.get(npc.get("class").toString()));
                String skin = npc.get("skin");
                if (skin.isEmpty()) theNPC.setSkin(registryName);
                else if (skin.contains(":")) theNPC.setSkin(new ResourceLocation(skin));
                else theNPC.setSkin(skin); //If we aren't a mod skin then set it to load from the username
                if (!name.contains(":")) theNPC.setLocalizedName(name);
                int insideColor = npc.getColor("inside_color");
                int outsideColor = npc.getColor("outside_color");
                theNPC.setColors(insideColor, outsideColor);
                if (!npc.isEmpty("loot_table")) theNPC.setLootTable(new ResourceLocation(npc.get("loot_table")));
                if (!npc.isEmpty("script")) theNPC.setScript(npc.getScript());
                event.table("npc_data").where("npc_id="+ registryName)
                        .forEach(data -> theNPC.addData(data.get("name"), data.get("value")));
            });

            event.table("npc_status_reset").rows().forEach(row -> Status.register(row.get("name"), row.get("days")));
            event.table("npc_buttons").rows().forEach(row -> NPCButtons.register(row.getScript()));
            event.table("random_npcs").rows().forEach(row -> ItemRandomNPC.NAMES.add(row.name()));
            event.table("random_names").rows().forEach(row -> CommandNPCGenerator.NAMES.add(row.name()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static class NPCClass {
        public static final NPCClass NULL = new NPCClass(Age.ADULT, false, 1, 0, true, false, false, false, false, 0, false);
        public static final Map<String, NPCClass> REGISTRY = Maps.newHashMap();
        final Age age;
        final boolean smallArms;
        final float height;
        final float offset;
        final boolean invulnerable;
        final boolean immovable;
        final boolean underwater;
        final boolean floats;
        final boolean invitable;
        final int lifespan;
        final boolean hideHearts;

        NPCClass(Age age, boolean smallArms, float height, float offset, boolean invulnerable, boolean immovable, boolean underwater, boolean floats, boolean invitable, int lifespan, boolean hideHearts) {
            this.age = age;
            this.smallArms = smallArms;
            this.height = height;
            this.offset = offset;
            this.invulnerable = invulnerable;
            this.immovable = immovable;
            this.underwater = underwater;
            this.floats = floats;
            this.invitable = invitable;
            this.lifespan = lifespan;
            this.hideHearts = hideHearts;
        }

        public boolean hasSmallArms() {
            return smallArms;
        }

        public Age getAge() {
            return age;
        }

        public float getHeight() {
            return height;
        }

        public float getOffset() {
            return offset;
        }

        public boolean isInvulnerable() {
            return invulnerable;
        }

        public boolean isImmovable() {
            return immovable;
        }

        public boolean canBreatheUnderwater() {
            return underwater;
        }

        public boolean floats() {
            return floats;
        }

        public boolean canInvite() {
            return invitable;
        }

        public boolean hideHearts() {
            return hideHearts;
        }

        public int getLifespan() {
            return lifespan;
        }
    }
}
