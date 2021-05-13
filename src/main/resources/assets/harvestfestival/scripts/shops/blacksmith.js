var tier = ["copper", "silver", "gold", "mystril", "mythic"];
var type = ["axe", "hammer", "shovel", "hoe", "watering_can", "sickle", "fishing_rod", "sword"]

function onRightClickedNPC(player, npc) {
    if (npc.is("harvestfestival:daniel") && player.status().get("is_upgrading") == 1) {
        var today = calendar.elapsed(player.world());
        var then = player.status().get("upgrade_started");
        var difference = today - then;
        if (difference >= 3) {
            var tasks = npc.tasks(this_id, player);
            var tr = player.status().get("tool_tier");
            var ty = player.status().get("tool_type");
            var item = scripting.createUnbreakableItemStack("harvestfestival:" + tier[tr] + "_" + type[ty]);
            tasks.add("say", "got_your_tool", item.name());
            tasks.add("give_item", item);
            player.status().set("is_upgrading", 0); //We can upgrade again now that we have collected
            //Remove the letter we sent if they haven't read it
            mail.remove(player, "harvestfestival:blacksmith_reminder");
        } else {
            //Randomly remind the player his tool will be ready in X days
            //But no more frequently than every 1.5 hours
            var current_time = player.world().time();
            var last_time = player.status().get("last_upgrade_reminder");
            if (random(1, 4) == 3 && (last_time == 0 || current_time - last_time >= 1500)) {
                var days_left = (3 - difference)|0;
                var text = days_left == 1 ? "ready_in_singular" : "ready_in_plural";
                player.status().set("last_upgrade_reminder", current_time);
                npc.tasks(this_id, player).add("say", text, days_left);
            }
        }
    }
}

//Save which tool the player is upgrading =]
function onItemPurchased(player, purchased) {
    if (purchased.department() == "blacksmith_upgrades") {
        //Only allow for one upgrade at a time
        var id = purchased.id();
        player.status().set("is_upgrading", 1);
        for (var i = 0; i < tier.length; i++) {
            if (includes(id, tier[i])) {
                player.status().set("tool_tier", i);
                break;
            }
        }

        for (var j = 0; j < type.length; j++) {
            if (includes(id, type[j])) {
                player.status().set("tool_type", j);
                break;
            }
        }

        //Set when the day we started upgrading on
        player.status().set("upgrade_started", calendar.elapsed(player.world()));
        if (!player.world().isClient()) {
            //Find the nearest daniel
            var npc = settlements.getNearbyNPC(player, "harvestfestival:daniel");
            if (npc != null) {
                npc.tasks(this_id, player.add("say", "be_three_days"));
            }

            mail.send(player, "harvestfestival:blacksmith_reminder");
        }
    }
}